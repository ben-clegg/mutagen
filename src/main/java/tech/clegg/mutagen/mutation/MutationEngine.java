package tech.clegg.mutagen.mutation;

import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.*;
import tech.clegg.mutagen.mutation.ast.exceptions.TargetedStatementDeletionThrowStatement;
import tech.clegg.mutagen.mutation.ast.exceptions.TryStatementExtraction;
import tech.clegg.mutagen.mutation.ast.identifiernaming.IncorrectIdentifierStyle;
import tech.clegg.mutagen.mutation.ast.identifiernaming.PoorIdentifierNaming;
import tech.clegg.mutagen.mutation.ast.logicflow.BranchExtraction;
import tech.clegg.mutagen.mutation.ast.logicflow.BreakContinueDeletion;
import tech.clegg.mutagen.mutation.major.MajorStrategy;
import tech.clegg.mutagen.mutation.simple.*;
import tech.clegg.mutagen.mutation.simple.poorindentation.AdditionalIndentation;
import tech.clegg.mutagen.mutation.simple.poorindentation.RemovedIndentation;
import tech.clegg.mutagen.properties.MutantType;

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
            //strategies.add(new ComparatorConfusion(t));
            //strategies.add(new ShortCircuitConfusion(t));
            strategies.add(new ForSeparatorConfusion(t));
            strategies.add(new UnbalancedBrackets(t));
            strategies.add(new ConstantToVariable(t));
            strategies.add(new ClassnameReplacement(t));
            strategies.add(new BranchExtraction(t));
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

            // Updated mutant set
            strategies.add(new EarlyExit(t));
            strategies.add(new RemovePublicAccessModifier(t));
            strategies.add(new StaticModifierIntroduction(t));
            strategies.add(new BreakContinueDeletion(t));
            strategies.add(new NewParameterCreation(t));
            strategies.add(new TargetedStatementDeletionThrowStatement(t));
            strategies.add(new TryStatementExtraction(t));
            strategies.add(new RemoveVariableInitialValue(t));
        }
    }

    public void printEnabledStrategies()
    {
        System.out.println("Enabled mutation strategies:");
        for (MutationStrategy s : strategies)
        {
            System.out.println(s.getType().name());
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
