package mutagen.mutation.strategy;

import mutagen.JavaSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;

public abstract class MutationStrategy
{
    private JavaSource originalLines;

    public MutationStrategy(JavaSource targetLines)
    {
        originalLines = targetLines;
    }

    public List<Integer> getMutatableIndexes()
    {
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < originalLines.size(); i++)
        {
            if(isMutatable(originalLines.get(i)))
                indexes.add(i);
        }
        return indexes;
    }

    public List<Mutant> createMutants()
    {
        List<Mutant> mutants = new ArrayList();
        for (int i : getMutatableIndexes())
        {
            mutants.add(createMutant(i));
        }
        return mutants;
    }

    public JavaSource getOriginalLines()
    {
        return originalLines;
    }

    abstract boolean isMutatable(String line);

    abstract Mutant createMutant(int lineIndex);
}
