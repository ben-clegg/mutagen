package mutagen.mutation.major;

import mutagen.TargetSource;
import mutagen.mutation.MutationStrategy;

import java.io.File;

public abstract class MajorStrategy extends MutationStrategy
{
    protected String operators; // Operators to use (e.g. :AOR:LOR)

    public MajorStrategy(TargetSource targetSource, String operatorSet)
    {
        super(targetSource);
        operators = operatorSet;
    }

    private void runMajor()
    {
        // TODO implement
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
