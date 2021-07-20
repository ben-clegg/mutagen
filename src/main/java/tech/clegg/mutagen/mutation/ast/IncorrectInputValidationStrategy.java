package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

public class IncorrectInputValidationStrategy extends ASTVisitorMutationStrategy
{
    public IncorrectInputValidationStrategy(TargetSource target)
    {
        super(target);
        setType(MutantType.INCORRECT_INPUT_VALIDATION);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter()
        {
            // TODO may need to change node type
            @Override
            public void visit(VariableDeclarationExpr n, Object arg)
            {
                super.visit(n, arg);
                for (VariableDeclarator varDec : n.getVariables())
                    generateIncorrectInputValidationMutant(n, varDec);
            }
        };
    }

    private void generateIncorrectInputValidationMutant(Node n, VariableDeclarator varDec)
    {
        // TODO determine conditions and variables to evaluate
    }
}
