package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.EnumSet;

public class RemovePublicAccessModifier extends ASTVisitorMutationStrategy
{
    public RemovePublicAccessModifier(TargetSource target)
    {
        super(target);
        setType(MutantType.REMOVE_PUBLIC_ACCESS_MODIFIER);
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
            public void visit(MethodDeclaration n, Object arg)
            {
                super.visit(n, arg);
                generatePublicModifierRemovalMutant(n);
            }

            @Override
            public void visit(ConstructorDeclaration n, Object arg)
            {
                super.visit(n, arg);
                generatePublicModifierRemovalMutant(n);
            }
        };
    }

    private void generatePublicModifierRemovalMutant(CallableDeclaration<?> declaration)
    {
        if(declaration.getModifiers().contains(Modifier.PUBLIC))
        {
            CallableDeclaration<?> modified = declaration.clone();
            modified.removeModifier(Modifier.PUBLIC);

            ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), declaration, modified, getType());
            addMutant(m);
        }
    }
}
