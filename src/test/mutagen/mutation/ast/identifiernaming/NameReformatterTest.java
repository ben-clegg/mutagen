package mutagen.mutation.ast.identifiernaming;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NameReformatterTest
{
    @Test
    public void testLowerCamelCaseToWords()
    {
        String t = "lowerCamelCase";

        List<String> expected = new ArrayList<>();
        expected.add("lower");
        expected.add("camel");
        expected.add("case");

        List<String> result = NameReformatter.camelCaseToWords(t);

        assertEquals(expected, result);
    }

    @Test
    public void testUpperCamelCaseToWords()
    {
        String t = "UpperCamelCase";

        List<String> expected = new ArrayList<>();
        expected.add("upper");
        expected.add("camel");
        expected.add("case");

        List<String> result = NameReformatter.camelCaseToWords(t);

        assertEquals(expected, result);
    }

    @Test
    public void testConstantToWords()
    {
        String t = "CONSTANT_IDENTIFIER";

        List<String> expected = new ArrayList<>();
        expected.add("constant");
        expected.add("identifier");

        List<String> result = NameReformatter.constantToWords(t);

        assertEquals(expected, result);
    }

    @Test
    public void testBadConstantToWords()
    {
        String t = "CONSTANT__IDENTIFIER";

        List<String> expected = new ArrayList<>();
        expected.add("constant");
        expected.add("identifier");

        List<String> result = NameReformatter.constantToWords(t);

        assertEquals(expected, result);
    }

    @Test
    public void testWordsToLowerCamelCase()
    {
        List<String> t = new ArrayList<>();
        t.add("lower");
        t.add("camel");
        t.add("case");

        String expected = "lowerCamelCase";

        String result = NameReformatter.wordsToLowerCamelCase(t);

        assertEquals(expected, result);
    }

    @Test
    public void testWordsToUpperCamelCase()
    {
        List<String> t = new ArrayList<>();
        t.add("upper");
        t.add("camel");
        t.add("case");

        String expected = "UpperCamelCase";

        String result = NameReformatter.wordsToUpperCamelCase(t);

        assertEquals(expected, result);
    }

    @Test
    public void testWordsToConstant()
    {
        List<String> t = new ArrayList<>();
        t.add("constant");
        t.add("identifier");

        String expected = "CONSTANT_IDENTIFIER";

        String result = NameReformatter.wordsToConstant(t);

        assertEquals(expected, result);
    }

}
