package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class RemoveCasts extends ASTVisitorMutationStrategy
{
    public RemoveCasts(TargetSource target)
    {
        super(target);
        setType(MutantType.REMOVE_CASTS);
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
            public void visit(ExpressionStmt n, Object arg)
            {
                super.visit(n, arg);
                generateMutant(n);
            }

        };
    }

    private void generateMutant(ExpressionStmt expressionStmt)
    {
        ExpressionStmt modifiedStmt = expressionStmt.clone();
        List<CastExpr> castExprs = findCastExprs(modifiedStmt);

        if (castExprs.isEmpty())
            return;

        for (CastExpr c : castExprs)
        {
            c.replace(c.getExpression());
        }


        addMutant(new ASTMutant(this.getOriginal().getCompilationUnit(), expressionStmt, modifiedStmt, this.getType()));
    }

    private List<CastExpr> findCastExprs(ExpressionStmt expressionStmt)
    {
        return expressionStmt.findAll(CastExpr.class);
    }

}
