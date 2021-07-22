package tech.clegg.mutagen.mutation.ast.logicflow;

import org.junit.Assert;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTStrategyTest;

public class ElseRelocationTest extends ASTStrategyTest
{
    @Test
    public void testSimpleCase()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "int a = 0; \n" +
                        "if (a == 0) { \n" +
                            "if (true) { \n" +
                                "a = 1; \n" +
                            "}\n" +
                            "else { \n" +
                                "a = 2; \n" +
                            "}\n" +
                        "}\n" +
                    "}\n" +
                "}"
        );

        ElseRelocation elseRelocation = new ElseRelocation(targetSource);
        elseRelocation.createAllMutants();
        Assert.assertEquals(1, elseRelocation.getMutants().size());

        assertNodePatchesAllModified(elseRelocation);
    }
}
