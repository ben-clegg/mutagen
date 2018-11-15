package mutagen.mutation.strategy;

import mutagen.JavaSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;

public class EqualityConfusion extends MutationStrategy
{
    public EqualityConfusion(JavaSource originalLines)
    {
        super(originalLines);
    }

    @Override
    boolean isMutatable(String line)
    {
        if(line.contains("="))
            return true;
        return false;
    }

    @Override
    List<Mutant> createLineMutants(int lineIndex)
    {
        List<Mutant> mutants = new ArrayList<Mutant>();
        String original = getOriginalLines().get(lineIndex);

        String mutated = original;

        // TODO does not consider each occurrence of possible mutant, only first - may need fixing
        if(mutated.contains("=="))
        {
            mutated = mutated.replace("==", "=");
        }
        else
        {
            mutated = mutated.replace("=", "==");
        }

        Mutant m = new Mutant(mutated, lineIndex);
        mutants.add(m);

        return mutants;
    }

}
