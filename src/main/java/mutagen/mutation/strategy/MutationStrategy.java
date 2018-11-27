package mutagen.mutation.strategy;

import mutagen.JavaSource;
import mutagen.TargetSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MutationStrategy
{
    private TargetSource original;
    protected String type;
    private final int TYPE_LENGTH = 22;

    public MutationStrategy(TargetSource target)
    {
        original = target;
        setType("UnspecifiedType");
    }

    protected void setType(String typeName)
    {
        StringBuffer str = new StringBuffer(TYPE_LENGTH);
        str.append(typeName);
        while(str.length() < TYPE_LENGTH) {
            str.append(" ");
        }

        type = str.toString();
    }

    public List<Integer> getMutatableIndexes()
    {
        List<Integer> indexes = new ArrayList<Integer>();
        JavaSource originalLines = original.getLines();
        for (int i = 0; i < originalLines.size(); i++)
        {
            if(isMutatable(cleanLine(originalLines.get(i))))
                indexes.add(i);
        }
        return indexes;
    }

    private String cleanLine(String original)
    {
        List<String> split = Arrays.asList(original.split(" "));

        for (int i = 0; i < split.size(); i++)
        {
            // remove comments
            if (split.get(i).contains("//"))
            {
                split = split.subList(0, i);
                break;
            }
            // TODO remove strings
        }

        StringBuilder sb = new StringBuilder();
        for (String s : split)
        {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    public List<Mutant> createAllMutants()
    {
        List<Mutant> mutants = new ArrayList();
        for (int i : getMutatableIndexes())
        {
            mutants.addAll(createLineMutants(i));
        }
        return mutants;
    }

    public JavaSource getOriginalLines()
    {
        return original.getLines();
    }

    /**
     * Check if a line of the program can be mutated
     * @param cleanedLine - the line, with comments and strings removed
     * @return true if the line is mutatable
     */
    abstract boolean isMutatable(String cleanedLine);

    abstract List<Mutant> createLineMutants(int lineIndex);

    protected Mutant createMutant(String mutatedLine, int lineIndex)
    {
        Mutant m = new Mutant(mutatedLine,
                                lineIndex,
                                type,
                                original);
        return m;
    }
}
