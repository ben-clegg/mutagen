package tech.clegg.mutagen.mutation.ast.logicflow;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.Mutant;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.NodePatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TargetedExtractionBreakContinueTest
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

        for (Mutant m : targetedExtractionBreakContinue.getMutants())
        {
            ASTMutant astMutant = (ASTMutant) m;
            for (NodePatch n : astMutant.getNodePatches())
                assertNotEquals(n.getOriginal(), n.getMutated());
        }
    }
}
