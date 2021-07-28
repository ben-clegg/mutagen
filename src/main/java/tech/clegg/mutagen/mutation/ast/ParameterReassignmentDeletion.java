package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ParameterReassignmentDeletion extends ASTVisitorMutationStrategy
{
    public ParameterReassignmentDeletion(TargetSource target)
    {
        super(target);
        setType(MutantType.PARAMETER_REASSIGNMENT_DELETION);
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
                generateMutants(n);
            }

        };
    }

    private void generateMutants(MethodDeclaration methodDeclaration)
    {
        for (Parameter p : methodDeclaration.getParameters())
            generateMutant(methodDeclaration, p);
    }

    private void generateMutant(MethodDeclaration methodDeclaration, Parameter parameter)
    {
        if (!methodDeclaration.getBody().isPresent())
            return;

        // Find variable declarations that are assigned to parameter's value
        List<VariableDeclarator> declaratorList = new ArrayList<>();
        methodDeclaration.getBody().get().walk(Node.TreeTraversal.BREADTHFIRST, n -> {
            if (n instanceof VariableDeclarator)
            {
                declaratorList.add((VariableDeclarator) n);
            }
        });

        if (declaratorList.isEmpty())
            return;

        for (VariableDeclarator vd : declaratorList)
        {
            Optional<Expression> init = vd.getInitializer();
            if (init.isPresent())
            {
                if (init.get() instanceof NameExpr && init.get().asNameExpr().getName().equals(parameter.getName()))
                    generateMutant(methodDeclaration, parameter, vd);
            }
        }
    }

    private void generateMutant(MethodDeclaration methodDeclaration, Parameter parameter, VariableDeclarator varDecl)
    {
        MethodDeclaration modifiedMethod = methodDeclaration.clone();

        // Remove declarator's expression
        modifiedMethod.findAll(VariableDeclarator.class).stream().filter(varDecl::equals).forEach(Node::remove);

        // Replace calls to varDecl's name with calls to parameter's name
        modifiedMethod.findAll(NameExpr.class).stream()
                .filter(n -> n.getName().equals(varDecl.getName()))
                .forEach(nameExpr -> nameExpr.setName(parameter.getName()));

        // Add mutant
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                methodDeclaration,
                modifiedMethod,
                this.getType()
        ));
    }
}
