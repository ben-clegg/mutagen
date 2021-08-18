package tech.clegg.mutagen.mutation.ast;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import tech.clegg.mutagen.JavaSource;
import tech.clegg.mutagen.mutation.Mutant;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.Arrays;
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

        // Update preview
        DiffRowGenerator diffRowGenerator = DiffRowGenerator.create().build();
        List<DiffRow> diffRows = diffRowGenerator.generateDiffRows(
                Arrays.asList(originalNode.toString().split("\n")),
                Arrays.asList(mutatedNode.toString().split("\n"))
        );

        for (DiffRow diffRow : diffRows)
        {
            if (!diffRow.getOldLine().equals(diffRow.getNewLine()))
            {
                setPreMutation(preMutation + "\n" + "- " + diffRow.getOldLine());
                setPostMutation(postMutation + "\n" + "+ " + diffRow.getNewLine());
            }
        }

    }

    @Override
    protected void setupMutatedJavaSource()
    {
        // Clone the AST to begin modifying it
        CompilationUnit modifiedCU = originalCU.clone();

        // Apply every node patch to the cloned AST
        // TODO Fix : sometimes valid patches are not applied. It is not clear why.
        for (NodePatch nodePatch : nodePatches)
        {
            //modifiedCU
            modifiedCU.findAll(nodePatch.getOriginal().getClass()).stream()
                    .filter(f -> f.equals(nodePatch.getOriginal()))
                    .forEach(n -> n.replace(nodePatch.getMutated()));
        }

        if (modifiedCU.equals(originalCU))
        {
            System.err.println("Could not apply mutation for " + this.getIdString() + "_" + this.getType());
            for (NodePatch nodePatch : nodePatches)
            {
                modifiedCU.findAll(nodePatch.getOriginal().getClass()).stream()
                        .filter(n -> n.equals(nodePatch.getOriginal()))
                        .forEach(n -> System.out.println(n));
            }
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

    public List<NodePatch> getNodePatches()
    {
        return nodePatches;
    }
}
