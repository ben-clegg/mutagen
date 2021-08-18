package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.concurrent.atomic.AtomicReference;

public class CollectionRemovalDeletion extends ASTVisitorMutationStrategy
{
    public CollectionRemovalDeletion(TargetSource target)
    {
        super(target);
        setType(MutantType.COLLECTION_REMOVAL_DELETION);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
        addFlag(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY);
        addFlag(MutantFlag.MUTAGEN_THESIS_FINAL);
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
                generateMutantCollection(n);
            }

            @Override
            public void visit(AssignExpr n, Object arg)
            {
                super.visit(n, arg);
                generateMutantArray(n);
            }
        };
    }

    private void generateMutantCollection(MethodCallExpr methodCallExpr)
    {
        // Must be <var>.remove()
        if (!methodCallExpr.getName().toString().equals("remove"))
            return;

        // Generate a mutant that removes the ExpressionStmt that this belongs to
        generateMutantRemovingExpressionsStmt(methodCallExpr);
    }

    private void generateMutantArray(AssignExpr assignExpr)
    {
        // Must be <array> = null
        if (!(assignExpr.getTarget() instanceof ArrayAccessExpr))
            return;
        if (!(assignExpr.getValue() instanceof NullLiteralExpr))
            return;

        // Generate a mutant that removes the ExpressionStmt that this belongs to
        generateMutantRemovingExpressionsStmt(assignExpr);
    }

    private void generateMutantRemovingExpressionsStmt(Expression expression)
    {
        // Find statement to remove
        AtomicReference<Node> statementRef = new AtomicReference<>();
        expression.walk(Node.TreeTraversal.PARENTS, n -> matchesType(n, statementRef, ExpressionStmt.class));
        if (statementRef.get() == null)
            return;
        ExpressionStmt statement = (ExpressionStmt) statementRef.get();

        // Get parent
        Node parent = statement.getParentNode().get();

        // Special case for inline / single line if stmt - remove whole if statement instead
        if (parent instanceof IfStmt)
        {
            IfStmt ifStmt = ((IfStmt) parent);
            if (!(ifStmt.getThenStmt() instanceof BlockStmt))
            {
                // If stmt does not have blockStmt for thenStmt
                // - Perform special case
                simpleIfStmtSpecialCase(ifStmt);
                // - Do not evaluate normal case
                return;
            }
        }

        // Remove the statement from its parent
        Node modifiedParent = parent.clone();
        modifiedParent.findAll(ExpressionStmt.class)
                .stream().filter(statement::equals)
                .forEach(Node::remove);

        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                parent,
                modifiedParent,
                this.getType()
        ));
    }

    private void simpleIfStmtSpecialCase(IfStmt toRemove)
    {
        if (!toRemove.getParentNode().isPresent())
            return;

        Node parent = toRemove.getParentNode().get();
        Node modifiedParent = parent.clone();

        // Remove ifStmt from modifiedParent
        modifiedParent.findAll(IfStmt.class)
                .stream().filter(toRemove::equals)
                .forEach(Node::remove);

        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                parent,
                modifiedParent,
                this.getType()
        ));
    }

}
