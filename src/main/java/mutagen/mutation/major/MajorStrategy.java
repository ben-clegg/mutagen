package mutagen.mutation.major;

import mutagen.TargetSource;
import mutagen.conf.Configuration;
import mutagen.conf.Paths;
import mutagen.mutation.MutationStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MajorStrategy extends MutationStrategy
{
    protected String operators; // Operators to use (e.g. AOR,LOR)
    File mutantsDir;

    public MajorStrategy(TargetSource targetSource, String operatorSet, String mType)
    {
        super(targetSource);
        operators = operatorSet;
        mutantsDir = new File(getOriginal().getLocation().getParent() +
                File.separator + "_mutants" + File.separator + operators);
        setType(mType);
    }

    private void runMajor()
    {
        ProcessBuilder pb = new ProcessBuilder(constructMajorCommand());

        try
        {
            Process p = pb.start();
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

    private String[] constructMajorCommand()
    {
        List<String> c = new ArrayList<>();
        c.add(Paths.getMajorLocation());                              // Major javac binary
        c.add(getOriginal().getLocation().getAbsolutePath());                              // Location of target file
        c.add("-XMutator:" + operators);                                 // Set operators
        c.add("-J-Dmajor.export.mutants=true");                               // Export source file
        c.add("-J-Dmajor.export.directory=" + mutantsDir.getAbsolutePath());   // Set export directory

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
                File src = new File(f + File.separator + getOriginal().getFilename());
                mutants.add(new MajorMutant(type, src));
            }
        }
    }
}
