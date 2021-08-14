package tech.clegg.mutagen.mutation.ast.logicflow;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.EqualsVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

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
        addFlag(MutantFlag.MUTAGEN_THESIS_FINAL);
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
        AtomicReference<Node> firstBlockStmt = new AtomicReference<>();
        statement.walk(Node.TreeTraversal.PARENTS, n -> matchesType(n, firstBlockStmt, BlockStmt.class));

        if (firstBlockStmt.get() == null)
            return; // Invalid

        // Find next closest block statement (i.e. block statement to move to)
        AtomicReference<Node> ancestorBlockStmt = new AtomicReference<>();
        firstBlockStmt.get().walk(Node.TreeTraversal.PARENTS, n -> matchesType(n, ancestorBlockStmt, BlockStmt.class));

        if (ancestorBlockStmt.get() == null)
            return; // Invalid

        // Generate mutant - before variant
        generateMutantForAncestor((BlockStmt) ancestorBlockStmt.get(),
                (BlockStmt) firstBlockStmt.get(), statement, 0);
        // Generate mutant - after variant
        generateMutantForAncestor((BlockStmt) ancestorBlockStmt.get(),
                (BlockStmt) firstBlockStmt.get(), statement, 1);

    }

    private void generateMutantForAncestor(BlockStmt ancestor,
                                           BlockStmt closestBlockStmt,
                                           Statement original,
                                           int positionOffset)
    {
        BlockStmt modifiedAncestor = ancestor.clone();
        // Determine which statement leads to original
        Optional<Statement> nextAncestorOfOriginal = modifiedAncestor.getStatements().stream()
                .filter(n -> leadsToSearched(n, original))
                .filter(n -> leadsToSearched(n, closestBlockStmt.getParentNode().get()))
                .findFirst();
        if (!nextAncestorOfOriginal.isPresent())
            return;
        int indexOfAncestor = modifiedAncestor.getStatements().indexOf(nextAncestorOfOriginal.get());

        // Remove original statement
        modifiedAncestor
                .findAll(Statement.class, s -> s.equals(original))
                .stream().filter(s -> {
                    AtomicReference<Node> firstBlockStmt = new AtomicReference<>();
                    s.walk(Node.TreeTraversal.PARENTS, n -> matchesType(n, firstBlockStmt, BlockStmt.class));
                    return (firstBlockStmt.get().equals(closestBlockStmt) &&
                            firstBlockStmt.get().getParentNode().equals(closestBlockStmt.getParentNode()));
                })
                .forEach(Node::remove);
        // Add to ancestor
        modifiedAncestor.addStatement(indexOfAncestor + positionOffset, original.clone());

        // Add
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                ancestor,
                modifiedAncestor,
                this.getType()
        ));

    }

}
