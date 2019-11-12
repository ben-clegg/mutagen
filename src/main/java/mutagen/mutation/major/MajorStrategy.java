package mutagen.mutation.major;

import com.sun.istack.internal.NotNull;
import mutagen.TargetSource;
import mutagen.conf.Paths;
import mutagen.mutation.MutationStrategy;
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MajorStrategy extends MutationStrategy
{
    protected String operators; // Operators to use (e.g. AOR,LOR)
    private File mutantsDir;

    public MajorStrategy(@NotNull TargetSource targetSource,
                         @NotNull String operatorSet,
                         @NotNull MutantType mutantType)
    {
        super(targetSource);
        operators = operatorSet;
        mutantsDir = new File(getOriginal().getFullyQualifiedFile().getParent() +
                File.separator + "_mutants" + File.separator + operators);
        setType(mutantType);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_MAJOR);
    }

    private void runMajor()
    {
        ProcessBuilder pb = new ProcessBuilder(constructMajorCommand());
        try
        {
            System.out.println(pb.command());
            Process p = pb.start();
            logStream(p.getErrorStream());
            logStream(p.getInputStream());
            p.waitFor(100, TimeUnit.SECONDS);
            p.destroy();

        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
            System.err.println("Failed to generate major mutants for " + operators);
        }
        catch (InterruptedException intEx)
        {
            intEx.printStackTrace();
        }
        // TODO implement
    }

    private void logStream(InputStream inStream) throws IOException
    {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inStream));
        String l = null;

        while ((l = in.readLine()) != null)
        {
            System.out.println(l);
        }
    }

    private String[] constructMajorCommand()
    {
        List<String> c = new ArrayList<>();
        c.add(Paths.getMajorLocation());                              // Major javac binary
        //c.add(getOriginal().getFullyQualifiedFile().getAbsolutePath());
        if(getOriginal().getClasspath() != null)
        {
            c.add("-classpath");
            c.add(getOriginal().getClasspath());
            //c.add("-classpath " + getOriginal().getClasspath());
        }
        c.add("-XMutator:" + operators);                                 // Set operators
        c.add("-J-Dmajor.export.mutants=true");                               // Export source file
        c.add("-J-Dmajor.export.directory=" + mutantsDir.getAbsolutePath());   // Set export directory
        // Location of target file
        c.add(getOriginal().getFullyQualifiedFile().toString());

        String[] finalCommand = new String[c.size()];
        return c.toArray(finalCommand);
    }

    @Override
    public void createAllMutants()
    {
        runMajor();

        if(mutantsDir.exists() && mutantsDir.listFiles(File::isDirectory) != null)
        {
            for (File f : mutantsDir.listFiles(File::isDirectory))
            {
                // TODO add each mutant source as a new MajorMutant
                File src = new File(f + File.separator + getOriginal().getRelativePath());
                mutants.add(new MajorMutant(type, src, operators));
            }
        }
    }
}
