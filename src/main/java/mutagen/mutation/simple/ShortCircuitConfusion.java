package mutagen.mutation.simple;

import mutagen.TargetSource;

import java.util.ArrayList;
import java.util.List;

public class ShortCircuitConfusion extends SimpleMutationStrategy
{
    public ShortCircuitConfusion(TargetSource original)
    {
        super(original);
        setType("ShortCircuitConfusion");
    }

    @Override
    boolean isMutatable(String cleanedLine)
    {
        if(cleanedLine.contains("|") || cleanedLine.contains("&"))
            return true;
        return false;
    }

    @Override
    List<SimpleMutant> createLineMutants(int lineIndex)
    {
        List<SimpleMutant> simpleMutants = new ArrayList<SimpleMutant>();
        String original = getOriginalLines().get(lineIndex);

        for (int i = 0; i < original.length(); i++)
        {
            if ((original.charAt(i) == '|')||(original.charAt(i) == '&'))
            {
                // check if preceding char is also same - skip if so
                if ((i > 0) && (original.charAt(i) == original.charAt(i-1)))
                    break;

                if(i < original.length() - 1)
                {
                    // Can either be duplicated or reduced
                    if(original.charAt(i) == original.charAt(i+1))
                    {
                        // Reduce
                        simpleMutants.add(createMutant(reduceOperator(original, i), lineIndex));
                    }
                    else
                    {
                        // Duplicate
                        simpleMutants.add(createMutant(duplicateOperator(original, i), lineIndex));
                    }
                }
                else
                {
                    // Can only be duplicated
                    simpleMutants.add(createMutant(duplicateOperator(original, i), lineIndex));
                }
            }
        }

        return simpleMutants;
    }

    private String duplicateOperator(String original, int index)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(original.substring(0,index));
        sb.append(original.charAt(index));
        sb.append(original.substring(index));
        return sb.toString();
    }

    private String reduceOperator(String original, int index)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(original.substring(0, index));
        if(index < original.length() - 1)
            sb.append(original.substring(index + 1));
        return sb.toString();
    }

}
