package mutagen.mutation;

import mutagen.TargetSource;
import mutagen.mutation.ast.*;
import mutagen.mutation.ast.identifiernaming.IncorrectIdentifierStyle;
import mutagen.mutation.ast.identifiernaming.PoorIdentifierNaming;
import mutagen.mutation.ast.logicflow.ExtractedContentsIfStatement;
import mutagen.mutation.major.MajorStrategy;
import mutagen.mutation.simple.*;
import mutagen.mutation.simple.poorindentation.AdditionalIndentation;
import mutagen.mutation.simple.poorindentation.RemovedIndentation;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
            strategies.add(new ExtractedContentsIfStatement(t));
            // Major mutants
            strategies.add(new MajorStrategy(t,"AOR", MutantType.INCORRECT_CALCULATION));
            strategies.add(new MajorStrategy(t,"EVR,LVR",MutantType.INCORRECT_VALUES));
            strategies.add(new MajorStrategy(t,"STD",MutantType.INCOMPLETE_IMPLEMENTATION));
            strategies.add(new MajorStrategy(t,"LOR", MutantType.LOGICAL_OPERATOR_REPLACEMENT));
            strategies.add(new MajorStrategy(t,"COR", MutantType.CONDITIONAL_OPERATOR_REPLACEMENT));
            strategies.add(new MajorStrategy(t,"ROR", MutantType.RELATIONAL_OPERATOR_REPLACEMENT));
            strategies.add(new MajorStrategy(t,"SOR", MutantType.SHIFT_OPERATOR_REPLACEMENT));
            // Poor Indentation
            strategies.add(new AdditionalIndentation(t,2));
            strategies.add(new RemovedIndentation(t,2));
            // Incorrect Identifier Style
            strategies.add(new IncorrectIdentifierStyle(t));
            strategies.add(new PoorIdentifierNaming(t));

            strategies.add(new LiteralValueRepetition(t));
            strategies.add(new StringMisspelling(t));

            strategies.add(new EarlyExit(t));
        }
    }

    public void applyFilterToStrategies(Predicate<? super MutationStrategy> predicate)
    {
        strategies = strategies.stream().filter(predicate).collect(Collectors.toList());
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
