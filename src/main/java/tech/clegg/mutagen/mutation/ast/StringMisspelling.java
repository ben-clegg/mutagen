package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class StringMisspelling extends ASTVisitorMutationStrategy
{
    private static final int BUDGET = 1; // How many to generate

    public StringMisspelling(TargetSource target)
    {
        super(target);
        setType(MutantType.STRING_MISSPELLING);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
        addFlag(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY);
        addFlag(MutantFlag.MUTAGEN_THESIS_FINAL);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter<Void>()
        {
            @Override
            public void visit(StringLiteralExpr stringLiteralExpr, Void v)
            {
                super.visit(stringLiteralExpr, v);
                generateMutants(stringLiteralExpr);
            }
        };
    }

    private void generateMutants(StringLiteralExpr strExpr)
    {
        for (String newContents : replacementStrings(strExpr.asString()))
        {
            // Make node patch
            StringLiteralExpr mutated = strExpr.clone();
            mutated.setString(newContents);
            NodePatch nodePatch = new NodePatch(strExpr, mutated);

            // Add full mutant
            ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), strExpr, mutated, type);
            m.setPreMutation(strExpr.asString());
            m.setPostMutation(mutated.asString());
            addMutant(m);
        }
    }

    private List<String> selectMutantsToUse(List<String> replacements)
    {
        // Use all
        if (BUDGET <= 0 || replacements.isEmpty())
            return replacements;

        // Randomly select
        List<String> shuffled = new ArrayList<>(replacements);
        Collections.shuffle(shuffled);

        int limit = replacements.size();
        if (BUDGET < limit)
            limit = BUDGET;

        return shuffled.subList(0, limit);
    }

    private List<String> replacementStrings(String original)
    {
        List<String> replacements = new ArrayList<>();

        replacements.addAll(characterDeletions(original));
        replacements.addAll(characterAdditions(original));
        replacements.addAll(characterReplacements(original));
        // TODO other replacement types


        // Filter to maintain budget
        return selectMutantsToUse(replacements);
    }

    private List<String> characterDeletions(String original)
    {
        List<String> replacements = new ArrayList<>();
        for (int i = 0; i < original.length(); i++)
        {
            StringBuilder sb = new StringBuilder(original);
            replacements.add(sb.deleteCharAt(i).toString());
        }
        return replacements;
    }


    private List<String> characterReplacements(String original)
    {
        List<String> replacements = new ArrayList<>();
        for (int i = 0; i < original.length(); i++)
        {
            StringBuilder sb = new StringBuilder(original);
            String modified = sb.replace(i, i+1, randomCharacter()).toString();
            // ensure non-equivalence
            if(!modified.equals(original))
                replacements.add(modified);
        }
        return replacements;
    }


    private List<String> characterAdditions(String original)
    {
        List<String> replacements = new ArrayList<>();
        for (int i = 0; i < original.length(); i++)
        {
            StringBuilder sb = new StringBuilder(original);
            replacements.add(sb.insert(i, randomCharacter()).toString());
        }
        return replacements;
    }

    private String randomCharacter()
    {
        return (Character.toString((char)(int)(Math.floor(Math.random() * 26) + 97)));
    }

}
