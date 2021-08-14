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
            public void visit(IfStmt n, Void arg)
            {
                super.visit(n, arg);
                generateModifiedIfContentsMutant(n);
            }

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

    private void generateModifiedIfContentsMutant(IfStmt ifStmt)
    {
        List<Node> ifStmtChildren = ifStmt.getChildNodes();
        // The contents of the BlockStmt are the lines to be extracted, minus any extra braces


        BlockStmt blockStmt = null;
        for (Node child : ifStmtChildren)
        {
            if(child.getClass().equals(BlockStmt.class))
            {
                blockStmt = (BlockStmt) child;
                break;
            }
        }
        generateMutantExtractBlockFromIfStmt(blockStmt, ifStmt);

        // Perform for else statement
        if(ifStmt.hasElseBlock())
        {
            generateMutantExtractBlockFromIfStmt(ifStmt.getElseStmt().get().asBlockStmt(), ifStmt);
        }

    }

    private void generateMutantExtractBlockFromIfStmt(BlockStmt toExtract, IfStmt originalIfStmt)
    {
        if(toExtract == null)
            return;

        // Children of BlockStmt are loaded
        List<Node> blockStmtChildren = toExtract.getChildNodes();

        // Generate mutant
        CompilationUnit mutationCU = getOriginal().getCompilationUnit().clone();
        IfStmt modifiedIfStmt = mutationCU.findAll(originalIfStmt.getClass())
                .stream()
                .filter(n -> n.equals(originalIfStmt)).findFirst().get();


        // Remove children from BlockStmt in IfStmt
        for (Node n : modifiedIfStmt.getChildNodes())
        {
            if (n.getClass().equals(BlockStmt.class))
            {
                modifiedIfStmt.replace(n, new BlockStmt());
                break;
            }
        }

        // parent should be a block statement
        Node parent = modifiedIfStmt.getParentNode().get();

        //NodeList siblings = (NodeList) parent.getChildNodes();
        LinkedList<Node> siblings = new LinkedList<>(parent.getChildNodes());

        // Copy children of BlockStmt to before if statement in separate list
        for (Node n : siblings)
        {
            if (n.equals(modifiedIfStmt))
            {
                siblings.addAll(siblings.indexOf(n),
                        blockStmtChildren);
                break;
            }
        }

        // Construct new siblings with changes
        //NodeList<Statement> parentContents = new NodeList<>();
        BlockStmt block = new BlockStmt(new NodeList<>());
        for (Node n : siblings)
        {
            if(n instanceof Statement)
            {
                Statement s = (Statement) n;
                block.addStatement(s);
            }
            else if (n instanceof Expression)
            {
                Expression e = (Expression) n;
                block.addStatement(e);
            }
            //parentContents.add(s);
        }
        //BlockStmt b = new BlockStmt(parentContents);

        try
        {
            addMutant(new ASTMutant(getOriginal().getCompilationUnit(),
                    originalIfStmt.getParentNode().get(),
                    block,
                    this.getType()));
        }
        catch (ClassCastException classCastException)
        {
            System.err.println("Could not generate BranchExtraction mutant on if statement with condition: " +
                    originalIfStmt.getCondition().toString());
        }

    }
}
