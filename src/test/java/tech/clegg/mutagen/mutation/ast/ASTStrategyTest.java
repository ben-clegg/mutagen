package tech.clegg.mutagen.mutation.ast;

import org.junit.Assert;
import tech.clegg.mutagen.mutation.Mutant;

import static org.junit.Assert.assertNotEquals;

public class ASTStrategyTest
{
    protected void assertNodePatchesAllModified(ASTVisitorMutationStrategy astVisitorMutationStrategy)
    {
        for (Mutant m : astVisitorMutationStrategy.getMutants())
        {
            ASTMutant astMutant = (ASTMutant) m;
            for (NodePatch n : astMutant.getNodePatches())
                assertNotEquals(n.getOriginal(), n.getMutated());
        }
    }

    protected void assertSameNumberLines(ASTVisitorMutationStrategy astVisitorMutationStrategy)
    {
        int originalCount = astVisitorMutationStrategy.getOriginalLines().size();

        for (Mutant m : astVisitorMutationStrategy.getMutants())
        {
            Assert.assertEquals(originalCount, m.getModifiedLines().size());
        }
    }


}
