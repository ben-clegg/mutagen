package mutagen.mutation.strategy;

import mutagen.JavaSource;
import mutagen.mutation.Mutant;

public class EqualityConfusion extends MutationStrategy
{
    public EqualityConfusion(JavaSource originalLines)
    {
        super(originalLines);
    }

    boolean isMutatable(String line)
    {
        if(line.contains("="))
            return true;
        return false;
    }

    Mutant createMutant(int lineIndex)
    {
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
        return m;
    }
}
