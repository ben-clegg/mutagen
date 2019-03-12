package mutagen.mutation.major;

import mutagen.TargetSource;
import mutagen.mutation.MutationStrategy;

import java.io.File;

public abstract class MajorStrategy extends MutationStrategy
{
    protected String operators; // Operators to use (e.g. :AOR:LOR)

    public MajorStrategy(TargetSource targetSource)
    {
        super(targetSource);
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
        for (File f : mutantsDir.listFiles())
        {
            // TODO add each mutant source as a new MajorMutant
        }
    }
}
