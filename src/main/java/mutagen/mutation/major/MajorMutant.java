package mutagen.mutation.major;

import mutagen.JavaSource;
import mutagen.mutation.Mutant;

import java.io.File;

public class MajorMutant extends Mutant
{
    private File mutantLoc;

    public MajorMutant(String mutantType, File mutantLocation)
    {
        super(mutantType);
        mutantLoc = mutantLocation;
        setupMutatedJavaSource();

        setPreMutation("Unknown (Major)");
        setPostMutation("Unknown (Major)");
    }

    @Override
    public void setupMutatedJavaSource()
    {
        // load lines from mutated file location
        modified = new JavaSource(mutantLoc);
    }

    @Override
    public String toString()
    {
        return getType() +
                "[" + getIdString() + "] (Major Mutant - Change not directly known) " + hashCode();
    }
}
