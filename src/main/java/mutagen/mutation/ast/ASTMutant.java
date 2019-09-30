package mutagen.mutation.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import mutagen.JavaSource;
import mutagen.mutation.Mutant;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class ASTMutant extends Mutant
{
    private CompilationUnit originalCU;

    private List<NodePatch> nodePatches;

    private ASTMutant(CompilationUnit originalCompilationUnit, MutantType mutantType)
    {
        super(mutantType);
        originalCU = originalCompilationUnit;
    }

    public ASTMutant(CompilationUnit originalCompilationUnit,
                        List<NodePatch> nodePatchesToApply,
                        MutantType mutantType)
    {
        this(originalCompilationUnit, mutantType);

        nodePatches = nodePatchesToApply;
        setupMutatedJavaSource();
    }

    public ASTMutant(CompilationUnit originalCompilationUnit,
                        Node originalNode,
                        Node mutatedNode,
                        MutantType mutantType)
    {
        this(originalCompilationUnit, mutantType);

        nodePatches = new ArrayList<>();
        NodePatch patch = new NodePatch(originalNode, mutatedNode);
        nodePatches.add(patch);
        setupMutatedJavaSource();
    }

    @Override
    protected void setupMutatedJavaSource()
    {
        // Clone the AST to begin modifying it
        CompilationUnit modifiedCU = originalCU.clone();

        // Apply every node patch to the cloned AST
        for (NodePatch nodePatch : nodePatches)
        {
            //modifiedCU
            modifiedCU.findAll(nodePatch.getNodeType()).stream()
                    .filter(f -> f.equals(nodePatch.getOriginal()))
                    .forEach(n -> n.replace(nodePatch.getMutated()));
        }

        // Prepare a PrettyPrinterConfiguration to use for sourcecode output
        PrettyPrinterConfiguration p = new PrettyPrinterConfiguration();
        p.setIndentType(PrettyPrinterConfiguration.IndentType.SPACES);
        p.setIndentSize(2);
        // Convert the cloned AST to a JavaSource
        modified = new JavaSource(modifiedCU.toString(p));
    }

    @Override
    public String toString()
    {
        return getType() +
                "[" + getIdString() + "] (AST Mutant): " +
                getChange();
    }

}
