package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class NewParameterCreation extends ASTVisitorMutationStrategy
{
    public NewParameterCreation(TargetSource target)
    {
        super(target);
        setType(MutantType.NEW_PARAMETER_CREATION);
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
            public void visit(ConstructorDeclaration n, Object arg)
            {
                super.visit(n, arg);
                generateParameterCreationMutant(n);
            }

            @Override
            public void visit(MethodDeclaration n, Object arg)
            {
                super.visit(n, arg);
                generateParameterCreationMutant(n);
            }
        };
    }

    private void generateParameterCreationMutant(CallableDeclaration<?> originalMethodDefinition)
    {
        List<NodePatch> nodePatches = new ArrayList<>();
        TypeExpr intTypeExpr = new TypeExpr();
        intTypeExpr.setType("int");
        Parameter parameter = new Parameter(intTypeExpr.getType(), "mutatedParam");
        CallableDeclaration<?> mutatedMethodDefinition = originalMethodDefinition.clone();
        mutatedMethodDefinition.addParameter(parameter);

        // Create patch with version including new parameters
        nodePatches.add(new NodePatch(originalMethodDefinition, mutatedMethodDefinition));

        // Find calls to original method across whole tree (with same name and original parameters)
        Collection<MethodCallExpr> callsToMethod = new HashSet<>();
        VoidVisitorAdapter methodCallVisitor = new VoidVisitorAdapter()
        {
            @Override
            public void visit(MethodCallExpr n, Object arg)
            {
                super.visit(n, arg);
                if(n.getNameAsExpression().equals(mutatedMethodDefinition.getNameAsExpression()))
                    callsToMethod.add(n);
            }
        };
        methodCallVisitor.visit(this.getOriginal().getCompilationUnit(), null);

        // Create patches to these calls
        for (MethodCallExpr callExpr : callsToMethod)
        {
            MethodCallExpr modifiedCallExpr = callExpr.clone();
            modifiedCallExpr.addArgument(new IntegerLiteralExpr(1));
            nodePatches.add(new NodePatch(callExpr, modifiedCallExpr));
        }

        // Create the mutant with the patches
        addMutant(new ASTMutant(this.getOriginal().getCompilationUnit(), nodePatches, this.getType()));
    }
}
