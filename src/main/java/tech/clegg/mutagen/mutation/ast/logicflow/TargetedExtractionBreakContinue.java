package tech.clegg.mutagen.mutation.ast.logicflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.mutation.ast.NodePatch;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TargetedExtractionBreakContinue extends ASTVisitorMutationStrategy
{
    public TargetedExtractionBreakContinue(TargetSource target)
    {
        super(target);
        setType(MutantType.TARGETED_EXTRACTION_BREAK_CONTINUE);
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
                generateMutant(n);
            }

            @Override
            public void visit(ContinueStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutant(n);
            }
        };
    }

    private void generateMutant(Statement statement)
    {

        // Find closest block statement (i.e. block statement to remove from)
        AtomicReference<BlockStmt> firstBlockStmt = new AtomicReference<>();
        statement.walk(Node.TreeTraversal.PARENTS, n -> blockStatementChecker(n, firstBlockStmt));

        if (firstBlockStmt.get() == null)
            return; // Invalid

        // Find next closest block statement (i.e. block statement to move to)
        AtomicReference<BlockStmt> ancestorBlockStmt = new AtomicReference<>();
        firstBlockStmt.get().walk(Node.TreeTraversal.PARENTS, n -> blockStatementChecker(n, ancestorBlockStmt));

        if (ancestorBlockStmt.get() == null)
            return; // Invalid

        // Generate mutant - before variant
        generateMutantForAncestor(ancestorBlockStmt.get(), statement, 0);
        // Generate mutant - after variant
        generateMutantForAncestor(ancestorBlockStmt.get(), statement, 1);

    }

    private void generateMutantForAncestor(BlockStmt ancestor, Statement original, int positionOffset)
    {
        BlockStmt modifiedAncestor = ancestor.clone();
        // Determine which statement leads to original
        Optional<Statement> nextAncestorOfOriginal = modifiedAncestor.getStatements().stream()
                .filter(n -> leadsToSearched(n, original))
                .findFirst();
        if (!nextAncestorOfOriginal.isPresent())
            return;
        int indexOfAncestor = modifiedAncestor.getStatements().indexOf(nextAncestorOfOriginal.get());

        // Remove original statement
        modifiedAncestor.findAll(Statement.class, n -> n.equals(original)).forEach(Node::remove);
        // Add to ancestor
        modifiedAncestor.addStatement(indexOfAncestor + positionOffset, original);

        // Add
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                ancestor,
                modifiedAncestor,
                this.getType()
        ));

    }

    /**
     * Check if node is block statement
     * @param node the node to check
     * @param referenceToSet set the AtomicReference to the node if it is a BlockStmt
     */
    private void blockStatementChecker(Node node, AtomicReference<BlockStmt> referenceToSet)
    {
        // Skip if already set
        if (referenceToSet.get() != null)
            return;

        // Set if BlockStmt
        if (node instanceof BlockStmt)
        {
            referenceToSet.set((BlockStmt) node);
        }
    }

    private boolean leadsToSearched(Node node, Node searched)
    {
        if (node.stream(Node.TreeTraversal.BREADTHFIRST).anyMatch(n -> n.equals(searched)))
            return true;

        return false;
    }

}
