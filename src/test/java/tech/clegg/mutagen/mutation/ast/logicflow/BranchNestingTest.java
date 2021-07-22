package tech.clegg.mutagen.mutation.ast.logicflow;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTStrategyTest;

public class BranchNestingTest extends ASTStrategyTest
{
    @Test
    public void testSimpleCase()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "int a = 0; \n" +
                        "if (a == 0) { \n" +
                            "int b = 7;\n" +
                            "if (true) { \n" +
                                "a = 1; \n" +
                            "}\n" +
                            "else { \n" +
                                "a = 2; \n" +
                            "}\n" +
                            "b = 5;\n" +
                        "}\n" +
                        "String x = \"a\"; \n" +
                    "}\n" +
                "}"
        );

        BranchNesting branchNesting = new BranchNesting(targetSource);
        branchNesting.createAllMutants();
        Assert.assertEquals(10, branchNesting.getMutants().size());

        assertNodePatchesAllModified(branchNesting);
        assertSameNumberLines(branchNesting);

        // TODO Test for precise mutant behaviour
    }
}
