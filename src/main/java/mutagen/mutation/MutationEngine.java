package mutagen.mutation;

import mutagen.TargetSource;
import mutagen.mutation.ast.ClassnameReplacement;
import mutagen.mutation.ast.ForSeparatorConfusion;
import mutagen.mutation.ast.LiteralValueRepetition;
import mutagen.mutation.ast.StringMisspelling;
import mutagen.mutation.ast.identifiernaming.IncorrectIdentifierStyle;
import mutagen.mutation.ast.identifiernaming.PoorIdentifierNaming;
import mutagen.mutation.major.MajorStrategy;
import mutagen.mutation.simple.*;
import mutagen.mutation.simple.poorindentation.AdditionalIndentation;
import mutagen.mutation.simple.poorindentation.RemovedIndentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Oversees the creation of every possible mutant
 */
public class MutationEngine
{
    private List<Mutant> mutants;
    private List<MutationStrategy> strategies;
    private List<TargetSource> targets;

    public MutationEngine(List<TargetSource> targetSource)
    {
        targets = targetSource;
        mutants = new ArrayList<>();
        strategies = new ArrayList<>();
        initialiseStrategies();
    }

    protected void initialiseStrategies()
    {
        for (TargetSource t : targets)
        {
            strategies.add(new EqualityConfusion(t));
            strategies.add(new ComparatorConfusion(t));
            strategies.add(new ShortCircuitConfusion(t));
            strategies.add(new ForSeparatorConfusion(t));
            strategies.add(new UnbalancedBrackets(t));
            strategies.add(new ConstantToVariable(t));
            strategies.add(new ClassnameReplacement(t));
            // Major mutants
            strategies.add(new MajorStrategy(t,"AOR","IncorrectCalculation"));
            strategies.add(new MajorStrategy(t,"EVR,LVR","IncorrectValues"));
            strategies.add(new MajorStrategy(t,"STD","IncompleteImplementation"));
            strategies.add(new MajorStrategy(t, "LOR", "LogicalOperatorReplacement"));
            strategies.add(new MajorStrategy(t, "COR", "ConditionalOperatorReplacement"));
            strategies.add(new MajorStrategy(t, "ROR", "RelationalOperatorReplacement"));
            strategies.add(new MajorStrategy(t, "SOR", "ShiftOperatorReplacement"));
            // Poor Indentation
            strategies.add(new AdditionalIndentation(t, 2));
            strategies.add(new RemovedIndentation(t, 2));
            // Incorrect Identifier Style
            strategies.add(new IncorrectIdentifierStyle(t));
            strategies.add(new PoorIdentifierNaming(t));

            strategies.add(new LiteralValueRepetition(t));
            strategies.add(new StringMisspelling(t));
        }
    }

    public void generateMutants()
    {
        // Create mutants from every simple
        for (MutationStrategy s : strategies)
        {
            s.createAllMutants();
            mutants.addAll(s.getMutants());
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

    public List<TargetSource> getTargets()
    {
        return targets;
    }

    public List<Mutant> getMutants()
    {
        return mutants;
    }

    public List<MutationStrategy> getStrategies()
    {
        return strategies;
    }
}
