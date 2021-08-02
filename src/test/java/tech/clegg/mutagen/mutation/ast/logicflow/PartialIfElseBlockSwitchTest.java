package tech.clegg.mutagen.mutation.ast.logicflow;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTStrategyTest;

public class PartialIfElseBlockSwitchTest extends ASTStrategyTest
{
    @Test
    public void testSimpleCase()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "int a = 1; \n" +
                        "int b = 20; \n" +
                        "if (a >= b) { \n" +
                            "a = 999; \n" +
                            "b = 999; \n" +
                            "a = a + b; \n" +
                        "}\n" +
                        "else { \n" +
                            "a = 0; \n" +
                            "b = 0; \n" +
                        "}\n" +
                    "}\n" +
                "}"
        );

        PartialIfElseBlockSwitch strategy =
                new PartialIfElseBlockSwitch(targetSource);
        strategy.createAllMutants();
        Assert.assertEquals(7, strategy.getMutants().size());
        Assert.assertFalse(strategy.getMutants().isEmpty());

        assertNodePatchesAllModified(strategy);
        assertAllMutationsDifferent(strategy);

        // TODO Test for precise mutant behaviour
    }
}
