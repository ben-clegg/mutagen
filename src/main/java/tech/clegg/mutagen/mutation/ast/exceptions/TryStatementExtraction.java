package tech.clegg.mutagen.mutation.ast.exceptions;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TryStatementExtraction extends ASTVisitorMutationStrategy
{
    public TryStatementExtraction(TargetSource target)
    {
        super(target);
        setType(MutantType.TRY_STATEMENT_EXTRACTION);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
        addFlag(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY);
        addFlag(MutantFlag.MUTAGEN_THESIS_FINAL);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter<Void>()
        {
            @Override
            public void visit(TryStmt n, Void arg)
            {
                super.visit(n, arg);
                generateFullTryStatementExtractionMutation(n);
            }
        };
    }

    private void generateFullTryStatementExtractionMutation(TryStmt tryStmt)
    {
        // Make copy of statement to modify
        TryStmt modifiedTryStmt = tryStmt.clone();

        // Remove catch clauses
        modifiedTryStmt.setCatchClauses(new NodeList<>());

        // Generate a block statement to move contents of try to
        BlockStmt tryReplacement = new BlockStmt();

        // Identify contents of try block, move to beginning of replacement
        for (Statement s : modifiedTryStmt.getTryBlock().getStatements())
            tryReplacement.addStatement(s);

        // (If exists) move contents of finally block immediately to parent, immediately after this
        Optional<BlockStmt> optFinally = modifiedTryStmt.getFinallyBlock();
        optFinally.ifPresent(finallyBlock -> finallyBlock.getStatements().forEach(tryReplacement::addStatement));

        // Generate mutant that replaces try with new block
        addMutant(new ASTMutant(this.getOriginal().getCompilationUnit(), tryStmt, tryReplacement, this.getType()));
    }
}
