package mutagen.mutation.major;

import mutagen.TargetSource;
import mutagen.cli.Configuration;
import mutagen.mutation.MutationStrategy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class MajorStrategy extends MutationStrategy
{
    protected String operators; // Operators to use (e.g. AOR,LOR)

    public MajorStrategy(TargetSource targetSource, String operatorSet)
    {
        super(targetSource);
        operators = operatorSet;
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

    private String constructMajorCommand()
    {
        String c = Configuration.getMajorLocation() + " ";  // Major javac binary
        c = c + getOriginal().getLocation() + " ";          // Location of target file
        c = c + "-XMutator:" + operators;                   // Set operators
        c = c + "-J-Dmajor.export.mutants=true ";           // Export source file
        c = c + "-J-Dmajor.export.directory= ";             // TODO Set export directory

        return c;
    }

    @Override
    public void createAllMutants()
    {
        runMajor();

        File mutantsDir = new File(getOriginal().getLocation().getParent() +
                File.separator + "_mutants" + operators);

        for (File f : mutantsDir.listFiles(File::isDirectory))
        {
            // TODO add each mutant source as a new MajorMutant
            File src = new File(f + File.separator + getOriginal().getFilename());
            mutants.add(new MajorMutant(operators, src));
        }
    }
}
