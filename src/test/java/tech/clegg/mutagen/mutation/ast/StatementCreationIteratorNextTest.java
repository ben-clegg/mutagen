package tech.clegg.mutagen.mutation.ast;

import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StatementCreationIteratorNextTest extends ASTStrategyTest
{
    @Test
    public void testIteratorSimpleCallCase()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                        "public static void main(String[] args) {\n" +
                        "Collection<Integer> a = new ArrayList<>();\n" +
                        "Iterator<Integer> iter = a.iterator();\n" +
                        "iter.next(); \n" +
                        "}\n" +
                        "}"
        );

        StatementCreationIteratorNext statementCreationIteratorNext = new StatementCreationIteratorNext(targetSource);
        statementCreationIteratorNext.createAllMutants();
        assertEquals(1, statementCreationIteratorNext.getMutants().size());

        assertNodePatchesAllModified(statementCreationIteratorNext);

        // TODO Test for precise mutant behaviour
    }

    @Test
    public void testIteratorAssignmentCase()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                        "public static void main(String[] args) {\n" +
                        "Collection<Integer> a = new ArrayList<>();\n" +
                        "Iterator<Integer> iter = a.iterator();\n" +
                        "while (iter.hasNext()) { \n" +
                        "int ignore = 0; \n" +
                        "int res = iter.next(); \n" +
                        "}\n" +
                        "}\n" +
                        "}"
        );

        StatementCreationIteratorNext statementCreationIteratorNext = new StatementCreationIteratorNext(targetSource);
        statementCreationIteratorNext.createAllMutants();
        assertEquals(1, statementCreationIteratorNext.getMutants().size());

        assertNodePatchesAllModified(statementCreationIteratorNext);

        // TODO Test for precise mutant behaviour
    }
    @Test
    public void testIteratorBothCases()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                        "public static void main(String[] args) {\n" +
                        "Collection<Integer> a = new ArrayList<>();\n" +
                        "Iterator<Integer> iter = a.iterator();\n" +
                        "iter.next(); \n" +
                        "while (iter.hasNext()) { \n" +
                        "int ignore = 0; \n" +
                        "int res = iter.next(); \n" +
                        "}\n" +
                        "}\n" +
                        "}"
        );

        StatementCreationIteratorNext statementCreationIteratorNext = new StatementCreationIteratorNext(targetSource);
        statementCreationIteratorNext.createAllMutants();
        assertEquals(2, statementCreationIteratorNext.getMutants().size());

        assertNodePatchesAllModified(statementCreationIteratorNext);

        // TODO Test for precise mutant behaviour
    }
}
