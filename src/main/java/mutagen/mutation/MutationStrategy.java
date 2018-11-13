package mutagen.mutation;

import mutagen.JavaSource;

import java.util.ArrayList;

public abstract class MutationStrategy
{
    private JavaSource original;

    public MutationStrategy(JavaSource originalLines)
    {
        original = originalLines;
    }

    abstract int[] getMutatableIndexes();


}
