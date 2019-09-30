package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.TargetSource;
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.util.*;

public abstract class MutationStrategy
{
    private final int TYPE_LENGTH = 22;

    private Set<MutantFlag> flags = Collections.synchronizedSet(EnumSet.noneOf(MutantFlag.class));

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
        mutant.setFlags(flags);
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

    protected void addFlag(MutantFlag mutantFlag)
    {
        flags.add(mutantFlag);
    }

    protected void setFlags(Collection<MutantFlag> mutantFlags)
    {
        flags.clear();
        flags.addAll(mutantFlags);
    }

    public Set<MutantFlag> getFlags()
    {
        return flags;
    }
}
