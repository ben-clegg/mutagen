package tech.clegg.mutagen.mutation.ast.logicflow;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTStrategyTest;

public class StatementTransposeTest extends ASTStrategyTest
{
    @Test
    public void testSimpleCase()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "int a = 1; \n" +
                        "a++; \n" +
                        "int b = 20 + a; \n" +
                        "a = 30; \n" +
                    "}\n" +
                "}"
        );

        StatementTranspose statementTranspose = new StatementTranspose(targetSource);
        statementTranspose.createAllMutants();
        Assert.assertEquals(3, statementTranspose.getMutants().size());

        assertNodePatchesAllModified(statementTranspose);

        // TODO Test for precise mutant behaviour
    }
}
