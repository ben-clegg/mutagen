package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.util.Optional;

public class StatementCreationIteratorNext extends ASTVisitorMutationStrategy
{
    public StatementCreationIteratorNext(TargetSource target)
    {
        super(target);
        setType(MutantType.STATEMENT_CREATION_ITERATOR_NEXT);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
        addFlag(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter()
        {
            @Override
            public void visit(MethodCallExpr n, Object arg)
            {
                super.visit(n, arg);
                generateMutant(n);
            }

        };
    }

    private void generateMutant(MethodCallExpr callExpr)
    {
        // Must be a next() call
        if(!callExpr.getName().toString().equals("next"))
            return;


        try
        {
            Node parent = callExpr.getParentNode().get();

            if (parent instanceof VariableDeclarator)
            {
                // Handle Variable declaration case
                generateMutantWithParent(callExpr, (VariableDeclarator) parent);
            }
            else if (parent instanceof ExpressionStmt)
            {
                // Handle simple case - duplicate the expression statement
                generateMutantWithParent((ExpressionStmt) parent);
            }
            else
            {
                // Unsupported case
                System.err.println("Unsupported case for iterator.next() duplication.");
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println("Could not generate iterator.next() duplication mutant for " + callExpr);
        }
    }

    /**
     *
     * @param callExpr the call expression
     * @param parent the call expression's parent (a variable declaration)
     */
    private void generateMutantWithParent(MethodCallExpr callExpr, VariableDeclarator parent)
    {
        // Recursively move up tree until the block statement is reached, recording the nearest ancestor
        Node ancestor = locateNearestAncestorToBlockStmt(parent);

        if (ancestor == null)
            return; // No valid ancestor

        // Create a variable modification with iterator.next()
        ExpressionStmt expressionStmt = new ExpressionStmt();
        expressionStmt.setExpression(parent.getName() + " = " + callExpr.asMethodCallExpr());

        // Inject variable modification immediately after ancestor
        BlockStmt blockStmt = (BlockStmt) ancestor.getParentNode().get();
        BlockStmt modifiedBlockStmt = blockStmt.clone();
        int indexAncestor = modifiedBlockStmt.getStatements().indexOf(ancestor);
        modifiedBlockStmt.addStatement(indexAncestor + 1, expressionStmt);

        // Create Mutant
        addMutant(new ASTMutant(this.getOriginal().getCompilationUnit(),
                blockStmt, modifiedBlockStmt, this.getType()));
    }

    /**
     * Recursively walk up tree until a child of a BlockStmt is found
     * @param node the node to check the parent of
     * @return the child of a BlockStmt (or null if none exists(
     */
    private Node locateNearestAncestorToBlockStmt(Node node)
    {
        if (node.getParentNode().isPresent())
        {
            if (node.getParentNode().get() instanceof BlockStmt)
                return node;
            else
                return locateNearestAncestorToBlockStmt(node.getParentNode().get());
        }
        return null;
    }

    /**
     * Duplicate parent (simple expression) within its parent
     * @param parent the call expression's parent
     */
    private void generateMutantWithParent(ExpressionStmt parent)
    {
        BlockStmt grandParent = (BlockStmt) parent.getParentNode().get();
        BlockStmt modifiedGrandParent = grandParent.clone();
        int indexParent = modifiedGrandParent.getStatements().indexOf(parent);
        modifiedGrandParent.addStatement(indexParent, parent.clone());

        addMutant(new ASTMutant(this.getOriginal().getCompilationUnit(),
                grandParent, modifiedGrandParent, this.getType()));
    }
}
