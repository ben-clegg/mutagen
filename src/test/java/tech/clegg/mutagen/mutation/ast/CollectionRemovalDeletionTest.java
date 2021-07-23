package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.expr.CastExpr;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectionRemovalDeletionTest extends ASTStrategyTest
{
    @Test
    public void testMultipleCasts()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                        "public static void main(String[] args) {\n" +
                            "ArrayList<String> a = new ArrayList<>(); \n" +
                            "a.add(\"val\");\n" +
                            "int[][] b = {{1,2},{3,4}}; \n" +
                            "a.remove(0);\n" +
                            "b[1][1] = null;\n" +
                            "}\n" +
                        "}"
        );

        CollectionRemovalDeletion strategy = new CollectionRemovalDeletion(targetSource);
        strategy.createAllMutants();
        assertEquals(2, strategy.getMutants().size());

        assertNodePatchesAllModified(strategy);

        // TODO specific tests on data
    }
}
