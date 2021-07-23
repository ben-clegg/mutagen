package tech.clegg.mutagen.mutation.ast;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.logicflow.TargetedNestingBreakContinue;

public class VariableReinitialisationTest extends ASTStrategyTest
{
    @Test
    public void testSimpleCase()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "int a = 0; \n" +
                        "int b = 7;\n" +
                        "b = 4; \n" +
                        "if (a == 0) { \n" +
                            "b++;\n " +
                            "if (b == 5) { \n" +
                                "a = 1; \n" +
                            "}\n" +
                        "}\n" +
                    "}\n" +
                "}"
        );

        VariableReinitialisation strategy = new VariableReinitialisation(targetSource);
        strategy.createAllMutants();
        Assert.assertEquals(3, strategy.getMutants().size());

        assertNodePatchesAllModified(strategy);

        // TODO Test for precise mutant behaviour
    }
}
