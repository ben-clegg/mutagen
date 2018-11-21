package mutagen.mutation.strategy;

import mutagen.JavaSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;

public abstract class MutationStrategy
{
    private JavaSource originalLines;
    protected String type;
    private final int TYPE_LENGTH = 20;

    public MutationStrategy(JavaSource targetLines)
    {
        originalLines = targetLines;
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
        for (int i = 0; i < originalLines.size(); i++)
        {
            if(isMutatable(originalLines.get(i)))
                indexes.add(i);
        }
        return indexes;
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
        return originalLines;
    }

    abstract boolean isMutatable(String line);

    abstract List<Mutant> createLineMutants(int lineIndex);
}
