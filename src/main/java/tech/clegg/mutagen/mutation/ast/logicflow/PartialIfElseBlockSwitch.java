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

import java.util.ArrayList;
import java.util.List;

public class PartialIfElseBlockSwitch extends ASTVisitorMutationStrategy
{
    public PartialIfElseBlockSwitch(TargetSource target)
    {
        super(target);
        setType(MutantType.PARTIAL_IF_ELSE_BLOCK_SWITCH);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
        addFlag(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter<Void>()
        {
            @Override
            public void visit(IfStmt n, Void arg)
            {
                super.visit(n, arg);
                generateMutants(n);
            }
        };
    }

    private void generateMutants(IfStmt ifStmt)
    {
        // Must have "then" block
        if (ifStmt.getThenStmt() == null || ifStmt.getThenStmt().isEmptyStmt())
            return;
        // Must have else block
        if (!ifStmt.hasElseBlock())
            return;

        // Only works for block statements
        if (!ifStmt.getThenStmt().isBlockStmt() || !ifStmt.getElseStmt().get().isBlockStmt())
            return;
        BlockStmt originalThen = (BlockStmt) ifStmt.getThenStmt();
        BlockStmt originalElse = (BlockStmt) ifStmt.getElseStmt().get();

        generateOneWayReplacements(ifStmt, originalThen, originalElse);
        generateOneWayReplacements(ifStmt, originalElse, originalThen);

        generateBothWayReplacements(ifStmt, originalThen, originalElse);
    }

    private void generateOneWayReplacements(IfStmt ifStmt, BlockStmt stmtA, BlockStmt stmtB)
    {
        // Skip if empty
        if (stmtA.getChildNodes().isEmpty())
            return;

        // Identify how many child nodes exist for stmtA
        int childNodeCount = stmtA.getChildNodes().size();

        // Generate mutant for each in limit
        for (int i = 1; i <= childNodeCount; i++)
            generateSingleOneWayReplacement(ifStmt, stmtA, stmtB, i);
    }

    private List<Statement> identifyStatementsToExtract(BlockStmt target, int nodeCount)
    {
        List<Statement> toExtract = new ArrayList<>();
        int startIndex = target.getChildNodes().size() - nodeCount;
        for (int i = startIndex; i < target.getChildNodes().size(); i++)
            toExtract.add(target.getStatement(i));

        return toExtract;
    }

    private void modifyIfAndGenerateMutant(IfStmt ifStmtOriginal,
                                           IfStmt ifModified,
                                           BlockStmt stmtA,
                                           BlockStmt mutatedA,
                                           BlockStmt mutatedB
    )
    {
        if (ifModified.getThenStmt().getChildNodes().equals(stmtA.getChildNodes()))
        {
            // - A is then statement
            ifModified.setThenStmt(mutatedA);
            ifModified.setElseStmt(mutatedB);
        }
        else
        {
            // - A is else statement
            ifModified.setThenStmt(mutatedB);
            ifModified.setElseStmt(mutatedA);
        }
        addMutant(new ASTMutant(this.getOriginal().getCompilationUnit(), ifStmtOriginal, ifModified, this.getType()));

    }

    private void generateSingleOneWayReplacement(IfStmt ifStmt, BlockStmt stmtA, BlockStmt stmtB, int nodeCount)
    {
        IfStmt ifModified = ifStmt.clone();
        BlockStmt mutatedA = stmtA.clone();
        BlockStmt mutatedB = stmtB.clone();

        // Identify nodes to remove
        List<Statement> toExtract = identifyStatementsToExtract(mutatedA, nodeCount);

        // Remove from a
        for (Statement s : toExtract)
        {
            mutatedA.findAll(Statement.class, n -> n.equals(s) && treesEqual(n, s)).forEach(Node::remove);
        }

        // Add to b
        for (Statement s : toExtract)
            mutatedB.addStatement(s);

        // Modify if statement
        modifyIfAndGenerateMutant(ifStmt, ifModified, stmtA, mutatedA, mutatedB);
    }

    private void generateBothWayReplacements(IfStmt ifStmt, BlockStmt stmtA, BlockStmt stmtB)
    {
        // Skip if empty
        if (stmtA.getChildNodes().isEmpty() || stmtB.getChildNodes().isEmpty())
            return;

        // Get smallest number of child nodes
        int childNodeCount = Integer.min(stmtA.getChildNodes().size(), stmtB.getChildNodes().size());

        // Generate mutant for each in limit
        for (int i = 1; i <= childNodeCount; i++)
            generateSingleBothWayReplacement(ifStmt, stmtA, stmtB, i);

    }

    private void generateSingleBothWayReplacement(IfStmt ifStmt, BlockStmt stmtA, BlockStmt stmtB, int nodeCount)
    {
        IfStmt ifModified = ifStmt.clone();
        BlockStmt mutatedA = stmtA.clone();
        BlockStmt mutatedB = stmtB.clone();

        // Identify nodes to remove
        List<Statement> toExtractA = identifyStatementsToExtract(mutatedA, nodeCount);
        List<Statement> toExtractB = identifyStatementsToExtract(mutatedB, nodeCount);

        // Remove
        for (Statement s : toExtractA)
            mutatedA.findAll(Statement.class, n -> n.equals(s) && treesEqual(n, s)).forEach(Node::remove);
        for (Statement s : toExtractB)
            mutatedB.findAll(Statement.class, n -> n.equals(s) && treesEqual(n, s)).forEach(Node::remove);

        // Add
        for (Statement s : toExtractA)
            mutatedB.addStatement(s);
        for (Statement s : toExtractB)
            mutatedA.addStatement(s);

        // Modify if statement

        // Modify if statement
        modifyIfAndGenerateMutant(ifStmt, ifModified, stmtA, mutatedA, mutatedB);
    }
}
