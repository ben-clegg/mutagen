package mutagen.mutation;

import mutagen.JavaSource;

import java.util.ArrayList;
import java.util.List;

public abstract class MutationStrategy
{
    private JavaSource original;

    public MutationStrategy(JavaSource originalLines)
    {
        original = originalLines;
    }

    public List<Integer> getMutatableIndexes()
    {
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < original.size(); i++)
        {
            if(isMutatable(original.get(i)))
                indexes.add(i);
        }
        return indexes;
    }

    abstract boolean isMutatable(String line);

    abstract ArrayList<Mutant> createMutants();
}
