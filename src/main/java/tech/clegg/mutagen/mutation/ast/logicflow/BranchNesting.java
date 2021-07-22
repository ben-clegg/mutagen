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
import java.util.concurrent.atomic.AtomicReference;

public class BranchNesting extends ASTVisitorMutationStrategy
{
    public BranchNesting(TargetSource target)
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
            public void visit(BlockStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutants(n);
            }
        };
    }

    private void generateMutants(BlockStmt blockStmt)
    {
        // Identify nodes that are BlockStmts to move contents to
        List<BlockStmt> targets = new ArrayList<>();
        blockStmt.walk(Node.TreeTraversal.BREADTHFIRST, n -> loadTargets(n, targets));
        if (targets.isEmpty())
            return;

        // Generate mutants
        for (BlockStmt t : targets)
        {
            generateMutant(blockStmt, t);
        }
    }

    private void generateMutant(BlockStmt blockStmt, BlockStmt target)
    {
        BlockStmt modified = blockStmt.clone();
        // Find child of original block that includes target in its subtree
        Optional<Statement> ancestorOfTarget = modified.getStatements().stream()
                .filter(n -> leadsToSearched(n, target))
                .findFirst();
        if (!ancestorOfTarget.isPresent())
            return;

        generateMutantMergeBeforeAncestor(blockStmt, target, ancestorOfTarget.get());
        generateMutantMergeAfterAncestor(blockStmt, target, ancestorOfTarget.get());
    }

    private void generateMutantMergeBeforeAncestor(BlockStmt original, BlockStmt target, Statement ancestorOfTarget)
    {
        BlockStmt modified = original.clone();

        // Identify statements before target ancestor
        int index = modified.getStatements().indexOf(ancestorOfTarget);

        if (index <= 0)
            return;

        List<Statement> statementsToMove = new ArrayList<>();
        for (int i = 0; i < index; i++)
        {
            statementsToMove.add(modified.getStatement(i));
        }

        // Remove identified statements
        modified.findAll(Statement.class, statementsToMove::contains).forEach(Node::remove);

        // Add to target
        BlockStmt modifiedTarget = target.clone();
        int i = 0;
        for (Statement s : statementsToMove)
        {
            modifiedTarget.addStatement(i, s);
            i++;
        }

        // Update
        modified.findAll(BlockStmt.class, target::equals).forEach(n -> n.replace(modifiedTarget));

        // Mutate
        storeMutantShared(original, modified);
    }

    private void generateMutantMergeAfterAncestor(BlockStmt original, BlockStmt target, Statement ancestorOfTarget)
    {
        BlockStmt modified = original.clone();

        // Identify statements before target ancestor
        int index = modified.getStatements().indexOf(ancestorOfTarget);

        if (index >= modified.getStatements().size() - 1)
            return;

        List<Statement> statementsToMove = new ArrayList<>();
        for (int i = index + 1; i < modified.getStatements().size(); i++)
        {
            statementsToMove.add(modified.getStatement(i));
        }

        // Remove identified statements
        modified.findAll(Statement.class, statementsToMove::contains).forEach(Node::remove);

        // Add to target
        BlockStmt modifiedTarget = target.clone();
        for (Statement s : statementsToMove)
        {
            modifiedTarget.addStatement(s);
        }

        // Update
        modified.findAll(BlockStmt.class, target::equals).forEach(n -> n.replace(modifiedTarget));

        // Mutate
        storeMutantShared(original, modified);

    }

    private void storeMutantShared(BlockStmt original, BlockStmt modified)
    {
        // Skip if no modification
        if (modified.equals(original))
            return;

        // Add Mutant
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                original,
                modified,
                this.getType()
        ));

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
