package tech.clegg.mutagen;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import tech.clegg.mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClearEquivalentMutantsChecker
{
    private Collection<TargetSource> targetSources;

    public ClearEquivalentMutantsChecker(Collection<TargetSource> targetSources)
    {
        this.targetSources = targetSources;
    }

    public void printMutantsAsEquivalents(Collection<Mutant> mutants)
    {
        System.out.println("Clear equivalent mutants (identical to original):");

        if (mutants.isEmpty())
            System.err.println("No clear equivalent mutants!");

        for (Mutant m : mutants)
        {
            System.err.println(m.getIdString() + "_" + m.getType());
        }
    }

    public Collection<Mutant> getClearEquivalentMutants(Collection<Mutant> mutants)
    {
        Collection<Mutant> clearEquivalents = new ArrayList<>();

        for (Mutant m : mutants)
        {
            if (isClearEquivalent(m))
                clearEquivalents.add(m);
        }

        return clearEquivalents;
    }

    private CompilationUnit getMutatedCU(Mutant mutant)
    {
        return JavaParser.parse(mutant.getModifiedLines().toString());
    }

    private boolean compilationUnitsEqual(CompilationUnit a, CompilationUnit b)
    {
        // TODO implement

        //List<Node> aNodes = new ArrayList<>();
        //List<Node> bNodes = new ArrayList<>();


        return a.equals(b);

        //return false;
    }

    private boolean isClearEquivalent(Mutant mutant)
    {
        CompilationUnit mutantCU;
        try
        {
            mutantCU = getMutatedCU(mutant);
        }
        catch (ParseProblemException parseProblemException)
        {
            System.err.println("Could not parse mutant " + mutant.getIdString() + "_" + mutant.getType());
            return false;
        }

        for (TargetSource s : targetSources)
        {
            if (compilationUnitsEqual(s.getCompilationUnit(), mutantCU))
                return true;
        }
        return false;
    }

}
