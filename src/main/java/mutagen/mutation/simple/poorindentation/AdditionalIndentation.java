package mutagen.mutation.simple.poorindentation;

import mutagen.TargetSource;
import mutagen.mutation.simple.SimpleMutant;
import mutagen.mutation.simple.SimpleMutationStrategy;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class AdditionalIndentation extends SimpleMutationStrategy
{
    private String spaces;

    public AdditionalIndentation(TargetSource original, int spacesInIndent)
    {
        super(original);
        setType(MutantType.POOR_INDENTATION);

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
        // Every line can have additional poorindentation added
        return true;
    }

    @Override
    protected List<SimpleMutant> createLineMutants(int lineIndex)
    {
        List<SimpleMutant> simpleMutants = new ArrayList<SimpleMutant>();
        String original = getOriginalLines().get(lineIndex);

        // Add spaces before the original line
        simpleMutants.add(createMutant(spaces + original, lineIndex));

        return simpleMutants;
    }
}
