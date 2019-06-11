package mutagen.mutation.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;
import mutagen.mutation.MutationStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class ASTVisitorMutationStrategy extends MutationStrategy
{
    protected VoidVisitorAdapter visitor;

    public ASTVisitorMutationStrategy(TargetSource target)
    {
        super(target);
        visitorSetup();
    }

    @Override
    public void createAllMutants()
    {
        visitor.visit(getOriginal().getCompilationUnit(), null);
    }

    protected abstract void visitorSetup();
}
