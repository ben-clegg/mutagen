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
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class IncorrectIdentifierStyle extends IdentiferNamingMutationOperation
{
    public IncorrectIdentifierStyle(TargetSource target)
    {
        super(target);
        setType(MutantType.INCORRECT_IDENTIFIER_STYLE);
        addFlag(MutantFlag.STYLE);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
    }

    @Override
    List<String> nameReplacements(String original)
    {
        return NameReformatter.generateMutants(original);
    }
}
