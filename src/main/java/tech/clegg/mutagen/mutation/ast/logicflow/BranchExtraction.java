package tech.clegg.mutagen.mutation.ast.logicflow;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.mutation.ast.NodePatch;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.LinkedList;
import java.util.List;

public class BranchExtraction extends ASTVisitorMutationStrategy
{
    public BranchExtraction(TargetSource target)
    {
        super(target);
        setType(MutantType.BRANCH_EXTRACTION);
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
                generateModifiedIfContentsMutant(n);
            }
        };
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
