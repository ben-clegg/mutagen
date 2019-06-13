package mutagen.mutation.ast.identifiernaming;

import java.util.*;

public class NameReformatter
{
    /**
     * Convert either upper or lower camel case to individual words
     * @return a list of individual lowercase words
     */
    public static List<String> camelCaseToWords(String camelCaseIdentifer)
    {
        String[] wordsArr = camelCaseIdentifer.split("(?=\\p{Upper})");
        return cleanWords(Arrays.asList(wordsArr));
    }

    /**
     * Convert a constant-name to individual words
     * @return a list of individual lowercase words
     */
    public static List<String> constantToWords(String constantIdentifer)
    {
        String[] wordsArr = constantIdentifer.toLowerCase().split("_");
        return cleanWords(Arrays.asList(wordsArr));
    }

    /**
     * Convert a list of individual words to lowerCamelCase
     * @param words individual words
     * @return the words in lowerCamelCase form
     */
    public static String wordsToLowerCamelCase(List<String> words)
    {
        // Covert to upper camel case
        String combined = wordsToUpperCamelCase(words);
        // Make first letter of the result lower case
        return combined.substring(0,1).toLowerCase() + combined.substring(1);
    }

    /**
     * Convert a list of individual words to UpperCamelCase
     * @param words individual words
     * @return the words in UpperCamelCase form
     */
    public static String wordsToUpperCamelCase(List<String> words)
    {
        StringBuilder sb = new StringBuilder();

        for (String word : words)
        {
            if(word.length() > 1)
            {
                sb.append(word.substring(0,1).toUpperCase());
                sb.append(word.substring(1).toLowerCase());
            }
            else if(word.length() > 0)
            {
                sb.append(word.toUpperCase());
            }
        }

        return sb.toString();
    }

    /**
     * Convert a list of individual words to constant naming
     * @param words individual words
     * @return the words in constant-naming form
     */
    public static String wordsToConstant(List<String> words)
    {
        StringBuilder sb = new StringBuilder();

        Iterator<String> i = words.iterator();
        while (i.hasNext())
        {
            String word = i.next();
            if (word.length() > 0)
            {
                sb.append(word.toUpperCase());
                if(i.hasNext())
                    sb.append("_");
            }
        }

        return sb.toString();
    }

    private static List<String> cleanWords(List<String> words)
    {
        ArrayList<String> wordsArrayList = new ArrayList<>(words);
        wordsArrayList.removeIf(Objects::isNull);
        wordsArrayList.removeIf(String::isEmpty);
        wordsArrayList.replaceAll(String::toLowerCase);
        return wordsArrayList;
    }
}
