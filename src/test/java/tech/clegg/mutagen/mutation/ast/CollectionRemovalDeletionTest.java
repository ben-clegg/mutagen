package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.expr.CastExpr;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectionRemovalDeletionTest extends ASTStrategyTest
{
    @Test
    public void testMultipleCasts()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                        "public static void main(String[] args) {\n" +
                            "ArrayList<String> a = new ArrayList<>(); \n" +
                            "a.add(\"val\");\n" +
                            "int[][] b = {{1,2},{3,4}}; \n" +
                            "a.remove(0);\n" +
                            "b[1][1] = null;\n" +
                            "}\n" +
                        "}"
        );

        CollectionRemovalDeletion strategy = new CollectionRemovalDeletion(targetSource);
        strategy.createAllMutants();
        assertEquals(2, strategy.getMutants().size());

        assertNodePatchesAllModified(strategy);
        assertNoClearEquivalents(strategy, targetSource);

        // TODO specific tests on data
    }

    @Test
    public void testArrayAccessInline()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                        "public static void main(String[] args) {\n" +
                        "ArrayList<String> a = new ArrayList<>(); \n" +
                        "a.add(\"val\");\n" +
                        "int[][] b = {{1,2},{3,4}}; \n" +
                        "a.remove(0);\n" +
                        "if (true) \n" +
                        "\tb[1][1] = null;\n" +
                        "}\n" +
                        "}"
        );

        CollectionRemovalDeletion strategy = new CollectionRemovalDeletion(targetSource);
        strategy.createAllMutants();
        assertEquals(2, strategy.getMutants().size());

        assertNodePatchesAllModified(strategy);
        assertNoClearEquivalents(strategy, targetSource);
    }

    @Test
    public void testArrayAccessInlineAlt()
    {
        TargetSource targetSource = new TargetSource(
                "package blah;\n" +
                    "import blahblah;\n" +
                "public class TestClass {\n" +
                        "public void method(int param, int alt) {\n" +
                        "// Pointless comment \n" +
                        "if (true) \n" +
                        "\tb[1][1] = null;\n" +
                        "int a = 0;\n" +
                        "int c = 0;\n" +
                        "}\n" +
                        "}"
        );

        CollectionRemovalDeletion strategy = new CollectionRemovalDeletion(targetSource);
        strategy.createAllMutants();
        assertEquals(1, strategy.getMutants().size());

        assertNodePatchesAllModified(strategy);
        assertNoClearEquivalents(strategy, targetSource);
    }

}
