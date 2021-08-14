package tech.clegg.mutagen.mutation.ast.exceptions;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

public class TargetedStatementDeletionThrowStatement extends ASTVisitorMutationStrategy
{
    public TargetedStatementDeletionThrowStatement(TargetSource target)
    {
        super(target);
        setType(MutantType.TARGETED_STATEMENT_DELETION_THROWS);
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
            public void visit(ThrowStmt n, Object arg)
            {
                super.visit(n, arg);
                generateRemoveStatementMutant(n);
            }
        };
    }

    private void generateRemoveStatementMutant(Statement statement)
    {
        if(!statement.getParentNode().isPresent())
            return;

        // Get parent
        Node parent = statement.getParentNode().get();

        // Copy parent
        Node modifiedParent = parent.clone();

        // Remove from copied parent
        for (Node c : modifiedParent.getChildNodes())
        {
            if (c.equals(statement))
            {
                modifiedParent.remove(c);
                break;
            }
        }

        // Skip if identical
        if(parent.getChildNodes().equals(modifiedParent.getChildNodes()))
            return;

        // Generate mutant
        ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), parent, modifiedParent, getType());
        addMutant(m);
    }
}
