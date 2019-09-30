package mutagen.mutation.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;
import mutagen.mutation.ast.identifiernaming.NameReformatter;
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LiteralValueRepetition extends ASTVisitorMutationStrategy
{
    final Class[] NODE_TYPES_TO_REPLACE = new Class[]{
            com.github.javaparser.ast.expr.NameExpr.class
    };

    public LiteralValueRepetition(TargetSource target)
    {
        super(target);
        setType(MutantType.LITERAL_REPETITION);
        addFlag(MutantFlag.QUALITY);
        addFlag(MutantFlag.USES_AST);
    }


    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter<Void>()
        {
            @Override
            public void visit(FieldDeclaration n, Void v)
            {
                super.visit(n, v);
                generateConstantReplacementMutant(n, NODE_TYPES_TO_REPLACE);
            }


            @Override
            public void visit(VariableDeclarationExpr n, Void v)
            {
                super.visit(n, v);
                generateConstantReplacementMutant(n, NODE_TYPES_TO_REPLACE);
            }

            /*
            @Override
            public void visit(IntegerLiteralExpr n, Void arg)
            {
                super.visit(n, arg);
                System.err.println(n.toString());
            }
            */

        };
    }

    private void generateConstantReplacementMutant(NodeWithVariables declaration, Class[] usageNodeTypes)
    {

        //System.err.println(declaration.toString());

        // Extract value
        //LiteralExpr literal = declaration.getValue().asLiteralExpr();

        // Iterate over declarators associated with assignment
        //for (VariableDeclarator varDec : declaration.getVariables())
        for (Object variableDeclarator : declaration.getVariables())
        {
            VariableDeclarator varDec = (VariableDeclarator)variableDeclarator;

            // Skip if declarator is not a constant
            // TODO check for final keyword instaead
            if (!NameReformatter.isConstant(varDec.getNameAsString())) {
                break;
            }

            // extract value by searching children for literals
            Iterator<Node> nodeIterator = varDec.getChildNodes().stream()
                    .filter(n -> (n instanceof LiteralExpr))
                    .iterator();

            while(nodeIterator.hasNext())
            {
                Node n = nodeIterator.next();

                // Already filtered; Contains value
                LiteralExpr literal = (LiteralExpr) n;

                // Find usages of the identifier, and make patches
                List<NodePatch> nodePatches = generateIdentifierValuePatches(varDec.getName(), literal, usageNodeTypes);

                // Add full mutant
                ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), nodePatches, type);
                m.setPreMutation(varDec.getNameAsString());
                m.setPostMutation(literal.toString());
                addMutant(m);
            }




        }

    }

}
