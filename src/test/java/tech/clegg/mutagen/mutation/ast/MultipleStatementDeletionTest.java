package tech.clegg.mutagen.mutation.ast;

import org.junit.Test;
import tech.clegg.mutagen.TargetSource;

import static org.junit.Assert.assertEquals;

public class MultipleStatementDeletionTest extends ASTStrategyTest
{
    @Test
    public void testSimple()
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

        MultipleStatementDeletion strategy = new MultipleStatementDeletion(targetSource);
        strategy.createAllMutants();

        assertNodePatchesAllModified(strategy);
        assertNoClearEquivalents(strategy, targetSource);
    }
}
