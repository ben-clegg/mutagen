package mutagen.mutation.ast.identifiernaming;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;
import mutagen.mutation.ast.ASTVisitorMutationStrategy;
import mutagen.mutation.ast.ASTMutant;
import mutagen.mutation.ast.NodePatch;

import java.util.ArrayList;
import java.util.List;

public class IncorrectIdentifierStyle extends IdentiferNamingMutationOperation
{
    public IncorrectIdentifierStyle(TargetSource target)
    {
        super(target);
        setType("IncorrectIdentifierStyle");
    }

    @Override
    List<String> nameReplacements(String original)
    {
        return NameReformatter.generateMutants(original);
    }
}
