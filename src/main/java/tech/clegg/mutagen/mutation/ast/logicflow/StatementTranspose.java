package tech.clegg.mutagen.mutation.ast.logicflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

public class StatementTranspose extends ASTVisitorMutationStrategy
{
    public StatementTranspose(TargetSource target)
    {
        super(target);
        setType(MutantType.STATEMENT_TRANSPOSE);
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
            public void visit(BlockStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutants(n);
            }
        };
    }

    private void generateMutants(BlockStmt blockStmt)
    {
        for (Statement s : blockStmt.getStatements())
            generateMutant(s, blockStmt);
    }

    private void generateMutant(Statement statement, BlockStmt blockStmt)
    {
        BlockStmt modifiedBlockStmt = blockStmt.clone();

        int index = modifiedBlockStmt.getStatements().indexOf(statement);
        // Skip last
        if (index >= blockStmt.getStatements().size() - 1)
            return;

        // Remove original
        modifiedBlockStmt.findAll(Statement.class).stream().filter(s -> s.equals(statement)).forEach(Node::remove);
        // Re-insert after the statement that followed the original
        modifiedBlockStmt.addStatement(index + 1, statement);

        // Add
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                blockStmt,
                modifiedBlockStmt,
                this.getType()
        ));
    }
}
