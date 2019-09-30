package mutagen.mutation.ast.logicflow;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;
import mutagen.mutation.ast.ASTMutant;
import mutagen.mutation.ast.ASTVisitorMutationStrategy;
import mutagen.mutation.ast.NodePatch;
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.util.LinkedList;
import java.util.List;

public class ExtractedContentsIfStatement extends ASTVisitorMutationStrategy
{
    public ExtractedContentsIfStatement(TargetSource target)
    {
        super(target);
        setType(MutantType.LOGIC_FLOW_ERROR);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
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
        // TODO perform for else statement
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

        if(blockStmt == null)
            return;

        // Children of BlockStmt are loaded
        List<Node> blockStmtChildren = blockStmt.getChildNodes();

        // Generate mutant
        CompilationUnit mutationCU = getOriginal().getCompilationUnit().clone();
        IfStmt modifiedIfStmt = mutationCU.findAll(ifStmt.getClass())
                .stream()
                .filter(n -> n.equals(ifStmt)).findFirst().get();


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

        NodeList<Statement> parentContents = new NodeList<>();
        for (Node n : siblings)
        {
            Statement s = (Statement) n;
            parentContents.add(s);
        }
        BlockStmt b = new BlockStmt(parentContents);

        addMutant(new ASTMutant(getOriginal().getCompilationUnit(),
                                ifStmt.getParentNode().get(),
                                b,
                                this.getType()));

    }
}
