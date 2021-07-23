package tech.clegg.mutagen.mutation.ast;

import org.junit.Test;
import tech.clegg.mutagen.TargetSource;

import static org.junit.Assert.assertEquals;

public class ParameterReassignmentDeletionTest extends ASTStrategyTest
{
    @Test
    public void testMultipleCasts()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                            "public static void main(String[] args) {\n" +
                                "int i = ex(1); \n" +
                            "}\n" +
                            "public static int ex(int param) {\n" +
                                "int a = param; \n" +
                                "a++; \n" +
                                "return a;\n" +
                            "}\n" +
                        "}"
        );

        ParameterReassignmentDeletion strategy = new ParameterReassignmentDeletion(targetSource);
        strategy.createAllMutants();
        assertEquals(1, strategy.getMutants().size());
        assertNodePatchesAllModified(strategy);

        // TODO specific tests on data
    }
}
