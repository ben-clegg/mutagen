package tech.clegg.mutagen.mutation.ast;

import org.junit.Test;
import tech.clegg.mutagen.TargetSource;

import static org.junit.Assert.assertEquals;

public class TransformCopyToReferenceTest extends ASTStrategyTest
{
    @Test
    public void test()
    {
        TargetSource targetSource = new TargetSource("" +
                "public class TestClass {\n" +
                    "public static void main(String[] args) {\n" +
                        "Object a; \n " +
                        "Object b = a.copy(); \n " +
                        "b.ignore(); \n" +
                        "Object c = new AnObject(a); \n " +
                    "}\n" +
                "}"
        );

        TransformCopyToReference strategy = new TransformCopyToReference(targetSource);
        strategy.createAllMutants();
        assertEquals(2, strategy.getMutants().size());
        assertNodePatchesAllModified(strategy);

        // TODO specific tests on data
    }
}
