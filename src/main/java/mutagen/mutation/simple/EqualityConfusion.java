package mutagen.mutation.simple;

import mutagen.TargetSource;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class EqualityConfusion extends SimpleMutationStrategy
{
    public EqualityConfusion(TargetSource original)
    {
        super(original);
        setType(MutantType.EQUALITY_CONFUSION);
    }

    @Override
    protected boolean isMutatable(String cleanedLine)
    {
        return cleanedLine.contains("=");
    }

    @Override
    protected List<SimpleMutant> createLineMutants(int lineIndex)
    {
        List<SimpleMutant> simpleMutants = new ArrayList<SimpleMutant>();
        String original = getOriginalLines().get(lineIndex);

        String mutated = original;

        // TODO does not consider each occurrence of possible mutant, only first - may need fixing
        if(mutated.contains("=="))
        {
            mutated = mutated.replaceFirst("==", "=");
        }
        else
        {
            mutated = mutated.replaceFirst("=", "==");
        }

        simpleMutants.add(createMutant(mutated, lineIndex));

        return simpleMutants;
    }

}
