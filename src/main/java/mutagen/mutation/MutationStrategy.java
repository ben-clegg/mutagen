package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.TargetSource;

import java.util.ArrayList;
import java.util.List;

public abstract class MutationStrategy
{
    private final int TYPE_LENGTH = 22;

    private TargetSource original;
    protected String type;
    protected List<Mutant> mutants;

    public MutationStrategy(TargetSource target)
    {
        original = target;
        setType("UnspecifiedType");
        mutants = new ArrayList<Mutant>();
    }

    public abstract void createAllMutants();

    protected void addMutant(Mutant mutant)
    {
        mutants.add(mutant);
    }

    public List<Mutant> getMutants()
    {
        return mutants;
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

    protected TargetSource getOriginal()
    {
        return original;
    }

    public JavaSource getOriginalLines()
    {
        return original.getLines();
    }

    public String getType()
    {
        return type;
    }
}
