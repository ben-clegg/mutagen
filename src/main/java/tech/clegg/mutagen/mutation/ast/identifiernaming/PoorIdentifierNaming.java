package tech.clegg.mutagen.mutation.ast.identifiernaming;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.mutation.ast.NodePatch;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class PoorIdentifierNaming extends IdentiferNamingMutationOperation
{
    public PoorIdentifierNaming(TargetSource target)
    {
        super(target);
        setType(MutantType.POOR_IDENTIFIER_NAMING);
        //TODO Preserve identifier style
        addFlag(MutantFlag.QUALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
    }

    @Override
    List<String> nameReplacements(String original)
    {
        // TODO implement
        List<String> replacements = new ArrayList<>();

        // Vowel deletion
        String noVowels = original.replaceAll("[aeiou]", "");
        if (noVowels.length() > 1)
            replacements.add(noVowels);

        // Replacement with uninformative word
        replacements.add("var");
        replacements.add("variable");
        replacements.add("integer");
        replacements.add("string");
        replacements.add("a");
        replacements.add("b");

        // Keep only first character
        if (original.length() > 1)
            replacements.add(original.substring(0,1));



        return replacements;
    }
}
