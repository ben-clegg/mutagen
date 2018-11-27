package mutagen.mutation.strategy;

import mutagen.TargetSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;

public class ComparatorConfusion extends MutationStrategy
{
    public ComparatorConfusion(TargetSource original)
    {
        super(original);
        setType("ComparatorConfusion");
    }

    @Override
    boolean isMutatable(String cleanedLine)
    {
        if(cleanedLine.contains(">=") || cleanedLine.contains("<="))
            return true;
        return false;
    }

    @Override
    List<Mutant> createLineMutants(int lineIndex)
    {
        List<Mutant> mutants = new ArrayList<Mutant>();
        String original = getOriginalLines().get(lineIndex);

        for (int i = 0; i < original.length()-2; i++)
        {
            if((original.charAt(i) == '<')||(original.charAt(i) == '>'))
            {
                if (original.charAt(i+1) == '=')
                {
                    mutants.add(createMutantAtCharIndex(i, original, lineIndex));
                }
            }
        }

        return mutants;
    }

    private Mutant createMutantAtCharIndex(int charIndex, String originalLine, int lineIndex)
    {
        String mutated = originalLine.substring(0, charIndex);
        mutated = mutated + '=';
        mutated = mutated + originalLine.charAt(charIndex);
        mutated = mutated + originalLine.substring(charIndex + 2);

        return createMutant(mutated, lineIndex);
    }

}
