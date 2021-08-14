package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

public class RemoveVariableInitialValue extends ASTVisitorMutationStrategy
{
    public RemoveVariableInitialValue(TargetSource target)
    {
        super(target);
        setType(MutantType.REMOVE_VARIABLE_INITIAL_VALUE);
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
            public void visit(VariableDeclarator n, Object arg)
            {
                super.visit(n, arg);
                generateRemoveInitialValueMutant(n);
            }
            /*
            @Override
            public void visit(FieldDeclaration n, Object arg)
            {
                super.visit(n, arg);
            }

            @Override
            public void visit(VariableDeclarator n, Object arg)
            {
                super.visit(n, arg);
            }
            */
        };
    }


    private void generateRemoveInitialValueMutant(VariableDeclarator originalDeclarator)
    {
        VariableDeclarator modifiedDeclarator = originalDeclarator.clone();

        modifiedDeclarator.removeInitializer();

        ASTMutant m = new ASTMutant(
                getOriginal().getCompilationUnit(), originalDeclarator, modifiedDeclarator, getType()
        );
        addMutant(m);
    }
}
