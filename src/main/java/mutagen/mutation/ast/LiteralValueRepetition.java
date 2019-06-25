package mutagen.mutation.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;
import mutagen.mutation.ast.identifiernaming.NameReformatter;

import java.util.ArrayList;
import java.util.List;

public class LiteralValueRepetition extends ASTVisitorMutationStrategy
{
    public LiteralValueRepetition(TargetSource target)
    {
        super(target);
        setType("LiteralValueRepetition");
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter<Void>()
        {
            @Override
            public void visit(FieldDeclaration declaration, Void v)
            {

                super.visit(declaration, v);
                generateConstantReplacementMutant(declaration, new Class[]{
                        com.github.javaparser.ast.body.Parameter.class
                });
            }


            @Override
            public void visit(VariableDeclarationExpr n, Void arg)
            {
                super.visit(n, arg);
                System.err.println(n.toString());
                //TODO also implement for var decl
            }

            /*
            @Override
            public void visit(FieldDeclaration n, Void arg)
            {
                super.visit(n, arg);
                System.err.println(n.toString());
            }
            */

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

    private void generateConstantReplacementMutant(FieldDeclaration declaration, Class[] usageNodeTypes)
    {
        // TODO extract value by searching children for literals

        System.err.println(varAssignment.toString());

        // Skip if not a literal
        if (!varAssignment.isLiteralExpr())
            return;

        // Extract value
        LiteralExpr literal = varAssignment.getValue().asLiteralExpr();

        // Iterate over declarators associated with assignment
        for (VariableDeclarator declarator : varAssignment.getTarget().asVariableDeclarationExpr().getVariables())
        {
            // Skip if declarator is not a constant
            if (!NameReformatter.isConstant(declarator.getNameAsString()))
                break;

            // Find usages of the identifier, and make patches
            List<NodePatch> nodePatches = generateIdentifierValuePatches(declarator.getName(), literal, usageNodeTypes);

            // Add full mutant
            ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), nodePatches, type);
            m.setPreMutation(declarator.getNameAsString());
            m.setPostMutation(literal.toString());
            addMutant(m);
        }

    }

}
