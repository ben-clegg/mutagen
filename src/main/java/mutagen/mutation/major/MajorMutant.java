package mutagen.mutation.major;

import mutagen.JavaSource;
import mutagen.mutation.Mutant;
import mutagen.properties.MutantType;

import java.io.File;

public class MajorMutant extends Mutant
{
    private File mutantLoc;
    private String operators;

    public MajorMutant(MutantType mutantType, File mutantLocation, String operatorGroup)
    {
        super(mutantType);
        mutantLoc = mutantLocation;
        operators = operatorGroup;
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
                "[" + getIdString() + "] (Major Mutant {" + operators +
                    "}- Change not directly known) " + hashCode();
    }
}
