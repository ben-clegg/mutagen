package tech.clegg.mutagen.mutation.ast.logicflow;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.Mutant;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTStrategyTest;
import tech.clegg.mutagen.mutation.ast.NodePatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TargetedExtractionBreakContinueTest extends ASTStrategyTest
{
    @Test
    public void testSimpleCase()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "int a = 1; \n" +
                        "int b = 20; \n" +
                        "while (true) {\n" +
                            "a++; \n" +
                            "if (a >= b) { \n" +
                                "break; \n" +
                            "}\n" +
                        "}\n" +
                    "}\n" +
                "}"
        );

        TargetedExtractionBreakContinue targetedExtractionBreakContinue =
                new TargetedExtractionBreakContinue(targetSource);
        targetedExtractionBreakContinue.createAllMutants();
        Assert.assertEquals(2, targetedExtractionBreakContinue.getMutants().size());

        assertNodePatchesAllModified(targetedExtractionBreakContinue);
    }
}
