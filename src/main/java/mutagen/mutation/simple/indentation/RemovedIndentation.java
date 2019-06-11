package mutagen.mutation.simple.indentation;

import mutagen.TargetSource;
import mutagen.mutation.simple.SimpleMutant;
import mutagen.mutation.simple.SimpleMutationStrategy;

import java.util.ArrayList;
import java.util.List;

public class RemovedIndentation extends SimpleMutationStrategy
{
    private String spaces;

    public RemovedIndentation(TargetSource original, int spacesInIndent)
    {
        super(original);
        setType("RemovedIndentation");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spacesInIndent; i++)
        {
            sb.append(" ");
        }
        spaces = sb.toString();
    }

    @Override
    protected boolean isMutatable(String cleanedLine)
    {
        // Every line beginning with indentation can have it removed
        return cleanedLine.startsWith(spaces);
    }

    @Override
    protected List<SimpleMutant> createLineMutants(int lineIndex)
    {
        List<SimpleMutant> simpleMutants = new ArrayList<SimpleMutant>();
        String original = getOriginalLines().get(lineIndex);

        // Remove spaces at beginning of original line
        simpleMutants.add(createMutant(original.replaceFirst(spaces, ""), lineIndex));

        return simpleMutants;
    }
}
