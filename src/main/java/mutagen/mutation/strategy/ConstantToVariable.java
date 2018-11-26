package mutagen.mutation.strategy;

import mutagen.TargetSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConstantToVariable extends MutationStrategy
{
    public ConstantToVariable(TargetSource original)
    {
        super(original);
        setType("ConstantToVariable");
    }

    @Override
    boolean isMutatable(String line)
    {
        String[] elems = line.split(" ");
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
    List<Mutant> createLineMutants(int lineIndex)
    {
        ArrayList<Mutant> mutants = new ArrayList<Mutant>();
        String original = getOriginalLines().get(lineIndex);

        mutants.add(createMutant(original.replaceFirst("final ", ""), lineIndex));

        return mutants;
    }

}
