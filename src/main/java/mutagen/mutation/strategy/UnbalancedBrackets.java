package mutagen.mutation.strategy;

import mutagen.JavaSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class UnbalancedBrackets extends MutationStrategy
{
    private static final String BRACKET_MATCH = "(.*)([\\{\\[\\(\\)\\]\\}]+)(.*)";
    //private static final Character[] POSSIBLE_BRACKETS = {'{', '[', '(', ')', ']', '}'};
    private static final String POSSIBLE_BRACKETS = "{[()]}";

    public UnbalancedBrackets(JavaSource originalLines)
    {
        super(originalLines);
    }

    @Override
    boolean isMutatable(String line)
    {
        if (line.matches(BRACKET_MATCH))
        {
            System.out.println(line);
            return true;
        }
        return false;
    }

    @Override
    List<Mutant> createLineMutants(int lineIndex)
    {
        ArrayList<Mutant> mutants = new ArrayList<Mutant>();

        String original = getOriginalLines().get(lineIndex);

        List<Integer> bracketIndexes = new ArrayList<Integer>();

        // Obtain position of every bracket
        if (original.length() > 1)
        {
            int i = 0;
            do
            {
                CharSequence c = original.subSequence(i, i+1);
                if (Pattern.matches(BRACKET_MATCH, c))
                    bracketIndexes.add(i);
                i ++;
            } while (i < original.length() - 1);
        }
        else
        {
            bracketIndexes.add(0);
        }

        for (int bIndex : bracketIndexes)
        {
            // Bracket deletion
            Mutant deletionMutant = new Mutant(deleteBracket(original, bIndex), lineIndex);
            mutants.add(deletionMutant);

            // Bracket replacement
            for (String replaced : replaceBrackets(original, bIndex))
            {
                Mutant replacementMutant = new Mutant(replaced, lineIndex);
                mutants.add(replacementMutant);
            }
        }

        return mutants;
    }

    private String deleteBracket(String originalLine, int charIndex)
    {
        if (originalLine.length() > 1)
            return originalLine.substring(0, charIndex) +
                            originalLine.substring(charIndex + 1, originalLine.length());
        else
            return "";
    }

    private List<String> replaceBrackets(String originalLine, int charIndex)
    {
        List<String> replacedLines = new ArrayList<String>();

        char existingBracket = originalLine.charAt(charIndex);
        String replacements = POSSIBLE_BRACKETS.replace(String.valueOf(existingBracket), "");

        for (char replace : replacements.toCharArray())
        {
            if (originalLine.length() > 1)
                replacedLines.add(originalLine.substring(0, charIndex) + replace +
                        originalLine.substring(charIndex + 1, originalLine.length()));
            else
                replacedLines.add(String.valueOf(replace));
        }

        return replacedLines;
    }
}
