package tech.clegg.mutagen.mutation.simple;

import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class ComparatorConfusion extends SimpleMutationStrategy
{
    public ComparatorConfusion(TargetSource original)
    {
        super(original);
        setType(MutantType.COMPARATOR_CONFUSION);
        addFlag(MutantFlag.COMPILABILITY); //TODO check
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_STRING_MANIPULATION);
    }

    @Override
    protected boolean isMutatable(String cleanedLine)
    {
        if(cleanedLine.contains(">=") || cleanedLine.contains("<="))
            return true;
        return false;
    }

    @Override
    protected List<SimpleMutant> createLineMutants(int lineIndex)
    {
        List<SimpleMutant> simpleMutants = new ArrayList<SimpleMutant>();
        String original = getOriginalLines().get(lineIndex);

        for (int i = 0; i < original.length()-2; i++)
        {
            if((original.charAt(i) == '<')||(original.charAt(i) == '>'))
            {
                if (original.charAt(i+1) == '=')
                {
                    simpleMutants.add(createMutantAtCharIndex(i, original, lineIndex));
                }
            }
        }

        return simpleMutants;
    }

    private SimpleMutant createMutantAtCharIndex(int charIndex, String originalLine, int lineIndex)
    {
        String mutated = originalLine.substring(0, charIndex);
        mutated = mutated + '=';
        mutated = mutated + originalLine.charAt(charIndex);
        mutated = mutated + originalLine.substring(charIndex + 2);

        return createMutant(mutated, lineIndex);
    }

}
