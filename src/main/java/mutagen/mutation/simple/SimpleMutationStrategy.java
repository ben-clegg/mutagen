package mutagen.mutation.simple;

import mutagen.JavaSource;
import mutagen.TargetSource;
import mutagen.mutation.MutationStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SimpleMutationStrategy extends MutationStrategy
{

    public SimpleMutationStrategy(TargetSource target)
    {
        super(target);
    }

    public List<Integer> getMutatableIndexes()
    {
        List<Integer> indexes = new ArrayList<Integer>();
        JavaSource originalLines = getOriginal().getLines();
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

    @Override
    public void createAllMutants()
    {
        for (int i : getMutatableIndexes())
        {
            mutants.addAll(createLineMutants(i));
        }
    }

    /**
     * Check if a line of the program can be mutated
     * @param cleanedLine - the line, with comments and strings removed
     * @return true if the line is mutatable
     */
    abstract boolean isMutatable(String cleanedLine);

    abstract List<SimpleMutant> createLineMutants(int lineIndex);

    protected SimpleMutant createMutant(String mutatedLine, int lineIndex)
    {
        SimpleMutant m = new SimpleMutant(mutatedLine,
                                lineIndex,
                                type,
                                getOriginal());
        return m;
    }
}
