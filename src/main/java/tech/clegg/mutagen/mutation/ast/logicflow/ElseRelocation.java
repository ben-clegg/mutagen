package tech.clegg.mutagen.mutation.ast.logicflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.concurrent.atomic.AtomicReference;

public class ElseRelocation extends ASTVisitorMutationStrategy
{
    public ElseRelocation(TargetSource target)
    {
        super(target);
        setType(MutantType.STATEMENT_TRANSPOSE);
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
            public void visit(IfStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutant(n);
            }
        };
    }

    private void generateMutant(IfStmt ifStmt)
    {
        // Must have an else block
        if (!ifStmt.hasElseBlock() || !ifStmt.getElseStmt().isPresent())
            return;

        Statement elseStmt = ifStmt.getElseStmt().get();

        // Find closest IfStmt ancestor without else statement to mutate
        AtomicReference<Node> ancestor = new AtomicReference<>();
        ifStmt.walk(Node.TreeTraversal.PARENTS, n -> {
            matchesType(n, ancestor, IfStmt.class);
        });
        if (ancestor == null)
            return; // Invalid
        IfStmt modifiedAncestor = (IfStmt) ancestor.get().clone();

        // Remove else from modified tree
        modifiedAncestor.findAll(Statement.class, s -> s.equals(elseStmt)).forEach(Node::remove);

        // Add else to modified ancestor
        modifiedAncestor.setElseStmt(elseStmt.clone());

        // Generate mutant
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                ancestor.get(),
                modifiedAncestor,
                this.getType()
        ));

    }

}
