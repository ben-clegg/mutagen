package mutagen.mutation.strategy;

import mutagen.TargetSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;

public class EqualityConfusion extends MutationStrategy
{
    public EqualityConfusion(TargetSource original)
    {
        super(original);
        setType("EqualityConfusion");
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

        mutants.add(createMutant(mutated, lineIndex));

        return mutants;
    }

}
