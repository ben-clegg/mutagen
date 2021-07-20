package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LiteralExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.MutationStrategy;

import java.util.ArrayList;
import java.util.List;

public class ASTVisitorMutationStrategy extends MutationStrategy
{
    protected VoidVisitorAdapter visitor;

    public ASTVisitorMutationStrategy(TargetSource target)
    {
        super(target);
        visitorSetup();
    }

    @Override
    public void createAllMutants()
    {
        visitor.visit(getOriginal().getCompilationUnit(), null);
    }

    protected List<NodePatch> generateIdentifierUsagePatches(SimpleName originalName, String mutatedName, Class[] targetReplacementNodeTypes)
    {
        List<NodePatch> patches = new ArrayList<>();

        // Find all identifiers of the given node types that match the identifier being modified
        // TODO tighten scope to just the nodes adjacent to our declarators parent?
        CompilationUnit mutationCU = getOriginal().getCompilationUnit().clone();

        for (Class<Node> nodeType : targetReplacementNodeTypes)
        {
            mutationCU.findAll(nodeType).stream()
                    .filter(n -> ((NodeWithSimpleName) n).getName().equals(originalName))
                    .forEach(nameExpr -> {
                        NodeWithSimpleName mut = (NodeWithSimpleName) nameExpr.clone();
                        mut.setName(mutatedName);
                        patches.add(new NodePatch(nameExpr, (Node) mut));
                    });
        }

        return patches;
    }
    protected List<NodePatch> generateIdentifierValuePatches(SimpleName originalName, LiteralExpr literalExpr, Class[] targetReplacementNodeTypes)
    {
        List<NodePatch> patches = new ArrayList<>();

        // Find all identifiers of the given node types that match the identifier being modified
        // TODO tighten scope to just the nodes adjacent to our declarators parent?
        CompilationUnit mutationCU = getOriginal().getCompilationUnit().clone();

        for (Class<Node> nodeType : targetReplacementNodeTypes)
        {
            mutationCU.findAll(nodeType).stream()
                    .filter(n -> ((NodeWithSimpleName) n).getName().equals(originalName))
                    .forEach(nameExpr -> {
                        patches.add(new NodePatch(nameExpr, literalExpr));
                    });
        }

        return patches;
    }

    protected void visitorSetup(){}
}
