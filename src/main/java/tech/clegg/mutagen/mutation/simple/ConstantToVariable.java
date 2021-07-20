package tech.clegg.mutagen.mutation.simple;

import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class ConstantToVariable extends SimpleMutationStrategy
{
    public ConstantToVariable(TargetSource original)
    {
        super(original);
        setType(MutantType.CONSTANT_TO_VARIABLE);
        addFlag(MutantFlag.QUALITY);
        addFlag(MutantFlag.USES_STRING_MANIPULATION);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
    }

    @Override
    protected boolean isMutatable(String cleanedLine)
    {
        String[] elems = cleanedLine.split(" ");
        for (String e : elems)
        {
            if(e.equals("final"))
            {
                return true;
            }
            // Check that final is not preceded by anything other than the following keywords
            // This prevents "final" appearing in another context (such as a comment) from being considered
            if(!e.equals("") &&
                    (!e.equals("public")) &&
                    (!e.equals("private")) &&
                    (!e.equals("protected")) &&
                    (!e.equals("static")))
            {
                return false;
            }
        }
        return false;
    }

    @Override
    protected List<SimpleMutant> createLineMutants(int lineIndex)
    {
        List<SimpleMutant> simpleMutants = new ArrayList<SimpleMutant>();
        String original = getOriginalLines().get(lineIndex);

        simpleMutants.add(createMutant(original.replaceFirst("final ", ""), lineIndex));

        return simpleMutants;
    }

}
