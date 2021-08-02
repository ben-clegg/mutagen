package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public class TransformCopyToReference extends ASTVisitorMutationStrategy
{
    public TransformCopyToReference(TargetSource target)
    {
        super(target);
        setType(MutantType.TRANSFORM_COPY_TO_REFERENCE);
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
            public void visit(MethodCallExpr n, Object arg)
            {
                super.visit(n, arg);
                generateMutant(n);
            }

            @Override
            public void visit(ObjectCreationExpr n, Object arg)
            {
                super.visit(n, arg);
                generateMutant(n);
            }
        };
    }

    private void generateMutant(MethodCallExpr methodCallExpr)
    {
        // Type a = b.copy();
        // or duplicate(), clone()
        switch (methodCallExpr.getNameAsString())
        {
            case "clone":
            case "copy":
            case "duplicate":
                break;
            default:
                return; // Skip others
        }

        // Call's scope must be a NameExpr
        Optional<Expression> scope = methodCallExpr.getScope();
        if (!scope.isPresent())
            return;
        if (!(scope.get() instanceof NameExpr))
            return;
        NameExpr copied = scope.get().asNameExpr();

        // Replace method call with scope
        Node parent = methodCallExpr.getParentNode().get();
        Node modifiedParent = parent.clone();
        modifiedParent.findAll(MethodCallExpr.class).stream()
                .filter(methodCallExpr::equals)
                .forEach(n -> n.replace(copied.clone()));

        // Mutate
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                parent,
                modifiedParent,
                this.getType()
        ));
    }

    private void generateMutant(ObjectCreationExpr objectCreationExpr)
    {
        // Type a = new Type(b);
        System.out.println(objectCreationExpr);

        // Must have only one arg
        if (objectCreationExpr.getArguments().size() != 1)
            return;

        // Get arg
        Expression arg = objectCreationExpr.getArguments().get(0);

        // Replace call with arg
        Node parent = objectCreationExpr.getParentNode().get();
        Node modifiedParent = parent.clone();
        modifiedParent.findAll(ObjectCreationExpr.class).stream()
                .filter(objectCreationExpr::equals)
                .forEach(n -> n.replace(arg.clone()));

        // Mutate
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                parent,
                modifiedParent,
                this.getType()
        ));
    }

}
