package tech.clegg.mutagen.mutation.ast.logicflow;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTStrategyTest;

public class TargetedNestingBreakContinueTest extends ASTStrategyTest
{
    @Test
    public void testSimpleCase()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "int a = 0; \n" +
                        "while (a == 0) { \n" +
                            "int b = 7;\n" +
                            "if (false) { \n" +
                                "a = 1; \n" +
                            "}\n" +
                            "else { \n" +
                                "b = 2; \n" +
                            "}\n" +
                            "b = 5;\n" +
                            "break;\n" +
                        "}\n" +
                    "}\n" +
                "}"
        );

        TargetedNestingBreakContinue strategy = new TargetedNestingBreakContinue(targetSource);
        strategy.createAllMutants();
        Assert.assertEquals(2, strategy.getMutants().size());

        assertNodePatchesAllModified(strategy);
        assertSameNumberLines(strategy);

        // TODO Test for precise mutant behaviour
    }
}
