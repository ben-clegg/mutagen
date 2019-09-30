package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.TargetSource;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public abstract class MutationStrategy
{
    private final int TYPE_LENGTH = 22;

    private TargetSource original;
    protected MutantType type;
    protected List<Mutant> mutants;

    public MutationStrategy(TargetSource target)
    {
        original = target;
        setType(MutantType.UNKNOWN_MUTANT);
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

    protected void setType(MutantType mutantType)
    {
        type = mutantType;
    }

    protected TargetSource getOriginal()
    {
        return original;
    }

    public JavaSource getOriginalLines()
    {
        return original.getLines();
    }

    public MutantType getType()
    {
        return type;
    }
}
