package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LiteralExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.MutationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    /**
     * Check if node matches correct Node type.
     * @param node the node to check.
     * @param referenceToSet set the AtomicReference to the node if it matches the type.
     * @param typeToMatch the Node type to match.
     */
    protected void matchesType(Node node, AtomicReference<Node> referenceToSet, Class<? extends Node> typeToMatch)
    {
        // Skip if already set
        if (referenceToSet.get() != null)
            return;

        // Set if class is typeToMatch
        if (node.getClass().equals(typeToMatch))
        {
            referenceToSet.set(node);
        }
    }

    /**
     * Check if a Node resides in another Node's subtree.
     * @param subtreeRoot the Node to start searching from.
     * @param toFind the Node to check exists within the subtree.
     * @return true if toFind is in subtree; false otherwise.
     */
    protected boolean leadsToSearched(Node subtreeRoot, Node toFind)
    {
        if (
                subtreeRoot.stream(Node.TreeTraversal.BREADTHFIRST)
                        .filter(n -> n.getParentNode().equals(toFind.getParentNode()))
                        .anyMatch(n -> n.equals(toFind))

        )
            return true;

        return false;
    }

    /**
     * Get the closest BlockStmt ancestor to a Node.
     * @param start the Node to search from.
     * @return the closest ancestor BlockStmt, or null if none exists.
     */
    protected BlockStmt getClosestBlockStatementAbove(Node start)
    {
        AtomicReference<Node> result = new AtomicReference<>();
        start.walk(Node.TreeTraversal.PARENTS, n -> matchesType(n, result, BlockStmt.class));

        if (result.get() == null)
            return null;

        return (BlockStmt) result.get();
    }
}
