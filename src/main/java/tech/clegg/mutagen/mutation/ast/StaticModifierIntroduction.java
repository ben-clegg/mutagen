package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.EnumSet;

public class StaticModifierIntroduction extends ASTVisitorMutationStrategy
{
    public StaticModifierIntroduction(TargetSource target)
    {
        super(target);
        setType(MutantType.STATIC_MODIFIER_INTRODUCTION);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
        addFlag(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY);
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
            public void visit(FieldDeclaration n, Object arg)
            {
                super.visit(n, arg);
                generatePublicModifierRemovalMutant(n);
            }
        };
    }

    private void generatePublicModifierRemovalMutant(MethodDeclaration declaration)
    {
        if(!declaration.getModifiers().contains(Modifier.STATIC))
        {
            MethodDeclaration modified = declaration.clone();
            modified.addModifier(Modifier.STATIC);

            ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), declaration, modified, getType());
            addMutant(m);
        }
    }

    // TODO Generify; though it's unclear how considering common type
    //  is only BodyDeclaration with "NodeWithStaticModifier"
    private void generatePublicModifierRemovalMutant(FieldDeclaration declaration)
    {
        if(!declaration.getModifiers().contains(Modifier.STATIC))
        {
            FieldDeclaration modified = declaration.clone();
            modified.addModifier(Modifier.STATIC);

            ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), declaration, modified, getType());
            addMutant(m);
        }
    }
}
