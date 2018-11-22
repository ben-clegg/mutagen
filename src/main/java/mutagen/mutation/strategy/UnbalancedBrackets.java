package mutagen.mutation.strategy;

import mutagen.TargetSource;
import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UnbalancedBrackets extends MutationStrategy
{
    private static final String BRACKET_MATCH = "(.*)([\\{\\[\\(\\)\\]\\}]+)(.*)";
    //private static final Character[] POSSIBLE_BRACKETS = {'{', '[', '(', ')', ']', '}'};
    private static final String POSSIBLE_BRACKETS = "{[()]}";
    private static final String OPENING_BRACKETS = "{[(";
    private static final String CLOSING_BRACKETS = ")]}";

    public UnbalancedBrackets(TargetSource original)
    {
        super(original);
        setType("UnbalancedBrackets");
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
            mutants.add(createMutant(deleteBracket(original, bIndex), lineIndex));

            // Bracket replacement
            for (String replaced : replaceBrackets(original, bIndex))
            {
                mutants.add(createMutant(replaced, lineIndex));
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

        String existingBracket = String.valueOf(originalLine.charAt(charIndex));

        String replacements = "";

        if (OPENING_BRACKETS.contains(existingBracket))
            replacements = OPENING_BRACKETS.replace(existingBracket, "");
        else if (CLOSING_BRACKETS.contains(existingBracket))
            replacements = CLOSING_BRACKETS.replace(existingBracket, "");

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
