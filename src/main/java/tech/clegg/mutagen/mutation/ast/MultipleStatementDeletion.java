package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultipleStatementDeletion extends ASTVisitorMutationStrategy
{

    public MultipleStatementDeletion(TargetSource target)
    {
        super(target);
        setType(MutantType.MULTIPLE_STATEMENT_DELETION);
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
            public void visit(ExpressionStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutants(n);
            }
        };
    }

    private void generateMutants(ExpressionStmt stmt)
    {
        // Halt if no parent
        if(!stmt.getParentNode().isPresent())
            return;

        // generate and add node patches for this level
        Node originalParent = stmt.getParentNode().get();
        Node mutatedParent = originalParent.clone();

        // Get indexes
        int index = mutatedParent.getChildNodes().indexOf(stmt);
        int limit = mutatedParent.getChildNodes().size() - 1;

        // Skip if not enough statements can be removed (must be 2 minimum)
        if (index == limit)
            return;

        // Possible nodes to remove
        List<Node> removalPool = new ArrayList<>();
        for (int i = index; i <= limit; i++)
            removalPool.add(mutatedParent.getChildNodes().get(i));
        // Make a mutant for each combination of limit
        while (removalPool.size() >= 2)
        {
            // TODO Generate mutant for available nodes in the pool
            generateMutantWithRemovals(originalParent, removalPool);
            // Remove last for next iteration
            removalPool.remove(removalPool.size() - 1);
        }
    }

    private void generateMutantWithRemovals(Node originalParent, List<Node> removalPool)
    {
        Node modifiedParent = originalParent.clone();

        // Remove every element that's in the pool
        for (Node r : removalPool)
        {
            modifiedParent.findAll(r.getClass()).stream()
                    .filter(r::equals)
                    .forEach(Node::remove);
        }

        // Create mutant
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                originalParent,
                modifiedParent,
                this.getType()
        ));
    }

}
