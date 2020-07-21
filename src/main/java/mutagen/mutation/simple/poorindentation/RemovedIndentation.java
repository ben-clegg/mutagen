package mutagen.mutation.simple.poorindentation;

import mutagen.TargetSource;
import mutagen.mutation.simple.SimpleMutant;
import mutagen.mutation.simple.SimpleMutationStrategy;
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class RemovedIndentation extends SimpleMutationStrategy
{
    private String spaces;

    public RemovedIndentation(TargetSource original, int spacesInIndent)
    {
        super(original);
        setType(MutantType.POOR_INDENTATION);
        addFlag(MutantFlag.STYLE);
        addFlag(MutantFlag.QUALITY);
        addFlag(MutantFlag.USES_STRING_MANIPULATION);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);

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
        // Every line beginning with poorindentation can have it removed
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
