package mutagen.mutation.major;

import mutagen.mutation.Mutant;

import java.io.File;

public class MajorMutant extends Mutant
{
    private File mutantLoc;

    public MajorMutant(String mutantType, File mutantLocation)
    {
        super(mutantType);
        setPreMutation("Unknown (Major)");
        setPostMutation("Unknown (Major)");
        mutantLoc = mutantLocation;
    }

    @Override
    public void setupMutatedJavaSource()
    {
        // TODO implement - load from mutated file location - read the source
    }
}
