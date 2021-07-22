package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.CastExpr;
import org.junit.Test;
import tech.clegg.mutagen.TargetSource;

import static org.junit.Assert.*;

public class RemoveCastsTest extends ASTStrategyTest
{
    @Test
    public void testMultipleCasts()
    {
        TargetSource targetSource = new TargetSource(
                "public class TestClass {\n" +
                        "public static void main(String[] args) {\n" +
                        "int a = 3;\n" +
                        "int b = 2;\n" +
                        "double res = (double) a / (double) b;\n" +
                        "}\n" +
                        "}"
        );

        RemoveCasts removeCasts = new RemoveCasts(targetSource);
        removeCasts.createAllMutants();
        assertEquals(1, removeCasts.getMutants().size());

        assertNodePatchesAllModified(removeCasts);

        // Test that no cast calls are present
        TargetSource modified = new TargetSource(removeCasts.getMutants().get(0).getModifiedLines());
        assertTrue(modified.getCompilationUnit().findAll(CastExpr.class).isEmpty());
    }
}
