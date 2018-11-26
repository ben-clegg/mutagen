package mutagen.mutation;

import mutagen.TargetSource;
import mutagen.mutation.strategy.ConstantToVariable;
import mutagen.mutation.strategy.EqualityConfusion;
import mutagen.mutation.strategy.MutationStrategy;
import mutagen.mutation.strategy.UnbalancedBrackets;

import java.util.ArrayList;
import java.util.List;

/**
 * Oversees the creation of every possible mutant
 */
public class MutationEngine
{
    private List<Mutant> mutants;
    private List<MutationStrategy> strategies;
    private TargetSource target;

    public MutationEngine(TargetSource targetSource)
    {
        target = targetSource;
        mutants = new ArrayList<Mutant>();
        initialiseStrategies();
    }

    private void initialiseStrategies()
    {
        strategies = new ArrayList<MutationStrategy>();
        strategies.add(new EqualityConfusion(target));
        strategies.add(new UnbalancedBrackets(target));
        strategies.add(new ConstantToVariable(target));
    }

    public void generateMutants()
    {
        // Create mutants from every strategy
        for (MutationStrategy s : strategies)
        {
            mutants.addAll(s.createAllMutants());
        }

        // Generate an ID for each mutant
        int i = 0;
        for (Mutant m : mutants)
        {
            i++;
            m.setId(i);
        }
    }

    public void printAllMutants()
    {
        for (Mutant m : mutants)
        {
            System.out.println(m.toString());
        }
    }

    public List<Mutant> getMutants()
    {
        return mutants;
    }
}
