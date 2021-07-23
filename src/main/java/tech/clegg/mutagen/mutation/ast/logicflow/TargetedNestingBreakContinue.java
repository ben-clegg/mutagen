package tech.clegg.mutagen.mutation.ast.logicflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TargetedNestingBreakContinue extends ASTVisitorMutationStrategy
{
    public TargetedNestingBreakContinue(TargetSource target)
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
            public void visit(BreakStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutants(n);
            }

            @Override
            public void visit(ContinueStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutants(n);
            }
        };
    }

    private void generateMutants(Statement statement)
    {
        // Identify nodes that are BlockStmts to move contents to
        List<BlockStmt> targets = new ArrayList<>();
        for (Node sibling : statement.getParentNode().get().getChildNodes())
            sibling.walk(Node.TreeTraversal.BREADTHFIRST, n -> loadTargets(n, targets));

        if (targets.isEmpty())
            return;

        // Generate mutants
        for (BlockStmt t : targets)
        {
            generateMutant(statement, t);
        }
    }

    private void generateMutant(Statement statement, BlockStmt target)
    {
        Node parent = statement.getParentNode().get();
        Node modifiedParent = parent.clone();
        BlockStmt modifiedTarget = target.clone();

        // Move break / continue to block
        modifiedTarget.addStatement(statement.clone());
        modifiedParent.findAll(BlockStmt.class).stream().filter(target::equals).forEach(n -> n.replace(modifiedTarget));

        // Remove break / continue from parent
        modifiedParent.walk(Node.TreeTraversal.DIRECT_CHILDREN, n -> {
            if (n.equals(statement))
                n.remove();
        });

        // Skip if no modification
        if (modifiedParent.equals(parent))
            return;

        // Add Mutant
        addMutant(new ASTMutant(this.getOriginal().getCompilationUnit(), parent, modifiedParent, this.getType()));
    }

    private void loadTargets(Node n, List<BlockStmt> targets)
    {
        if (n instanceof IfStmt || n instanceof ForStmt || n instanceof WhileStmt)
        {
            n.getChildNodes().forEach(c -> loadPotentialTarget(c, targets));
        }
    }

    private void loadPotentialTarget(Node potentialTarget, List<BlockStmt> targets)
    {
        if (potentialTarget instanceof BlockStmt)
            targets.add((BlockStmt) potentialTarget);
    }


}
