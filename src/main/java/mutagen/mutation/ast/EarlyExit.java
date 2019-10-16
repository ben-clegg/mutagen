package mutagen.mutation.ast;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EarlyExit extends ASTVisitorMutationStrategy
{

    public EarlyExit(TargetSource target)
    {
        super(target);
        setType(MutantType.EARLY_EXIT);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter()
        {
            @Override
            public void visit(ExpressionStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutants(n);
            }
        };
    }

    private void generateMutants(ExpressionStmt stmt)
    {
        List<NodePatch> nodePatches = truncateAfterNode(stmt, new ArrayList<>());
        ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), nodePatches, getType());
        m.setPreMutation(stmt.toString());
        m.setPostMutation(stmt.getBegin().toString());
        addMutant(m);
    }

    /**
     * Recursively truncate every node after current node for current "level",
     * recurses by calling this method on the parent node.
     * @param node
     * @param nodePatches
     * @return
     */
    private List<NodePatch> truncateAfterNode(Node node, List<NodePatch> nodePatches)
    {
        // Halt if method or class declaration
        if(node.getClass().equals(MethodDeclaration.class))
            return nodePatches;
        if(node.getClass().equals(ClassOrInterfaceDeclaration.class))
            return nodePatches;

        // Halt if no parent
        if(!node.getParentNode().isPresent())
            return nodePatches;

        // generate and add node patches for this level
        Node mutatedParent = node.getParentNode().get().clone();
        // Find nodes to remove from parent
        List<Node> siblingsToRemove = new ArrayList<>();
        boolean selfFound = false;
        for (Node sibling : node.getParentNode().get().getChildNodes())
        {
            if(selfFound)
            {
                siblingsToRemove.add(sibling);
                continue;
            }

            if(sibling.equals(node))
                selfFound = true;
        }

        LinkedList<Node> siblings = new LinkedList<>();
        siblings.addAll(node.getParentNode().get().getChildNodes());


        for (Node r : siblingsToRemove)
            siblings.remove(r);

        List<Node> clearing = new ArrayList<>(mutatedParent.getChildNodes());
        for (Node c : clearing)
        {
            mutatedParent.remove(c);
        }


        // Generate node patch

        NodePatch np = new NodePatch(node.getParentNode().get(), mutatedParent);
        nodePatches.add(np);


        // Recurse on parent node
        return truncateAfterNode(node.getParentNode().get(), nodePatches);
    }
}
