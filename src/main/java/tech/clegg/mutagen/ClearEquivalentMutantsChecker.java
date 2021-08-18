package tech.clegg.mutagen;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import tech.clegg.mutagen.mutation.Mutant;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.NodePatch;

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
            if (m instanceof ASTMutant)
            {
                ASTMutant astMutant = (ASTMutant) m;
                for (NodePatch np : astMutant.getNodePatches())
                {
                    System.out.println("- Original (" + astMutant.getName() + "):");
                    System.out.println(np.getOriginal());
                    System.out.println("- Mutated (" + astMutant.getName() + "):");
                    System.out.println(np.getMutated());
                }
            }
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
        // Simple object check
        if (a.equals(b))
            return true;

        // Printout check
        return toSourceCode(a).equals(toSourceCode(b));
    }

    private JavaSource toSourceCode(CompilationUnit compilationUnit)
    {
        // Prepare a PrettyPrinterConfiguration to use for sourcecode output
        PrettyPrinterConfiguration p = new PrettyPrinterConfiguration();
        p.setIndentType(PrettyPrinterConfiguration.IndentType.SPACES);
        p.setIndentSize(2);
        // Convert the cloned AST to a JavaSource
        return new JavaSource(compilationUnit.toString(p));
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
            return true;
        }
        catch (NullPointerException nullPointerException)
        {
            System.err.println("Null lines for mutant " + mutant.getIdString() + "_" + mutant.getType() +
                    "; likely equal to original");
            return true;
        }

        for (TargetSource s : targetSources)
        {
            if (compilationUnitsEqual(s.getCompilationUnit(), mutantCU))
                return true;
        }
        return false;
    }

}
