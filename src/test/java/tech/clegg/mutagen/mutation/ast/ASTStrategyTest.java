package tech.clegg.mutagen.mutation.ast;

import org.junit.Assert;
import tech.clegg.mutagen.ClearEquivalentMutantsChecker;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

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

    protected void assertAllMutationsDifferent(ASTVisitorMutationStrategy astVisitorMutationStrategy)
    {
        for (int i = 0; i < astVisitorMutationStrategy.getMutants().size() - 1; i++)
        {
            for (int j = i + 1; j < astVisitorMutationStrategy.getMutants().size(); j++)
            {
                ASTMutant mutantA = (ASTMutant) astVisitorMutationStrategy.getMutants().get(i);
                ASTMutant mutantB = (ASTMutant) astVisitorMutationStrategy.getMutants().get(j);

                if (mutantA.equals(mutantB))
                    fail("Mutant " + mutantA + " is equal to " + mutantB);

                if (mutantA.getNodePatches().size() == mutantB.getNodePatches().size())
                {
                    int equivalentPatches = 0;

                    for (NodePatch nodePatchA : mutantA.getNodePatches())
                    {
                        NodePatch nodePatchB =
                                mutantB.getNodePatches().get(mutantA.getNodePatches().indexOf(nodePatchA));

                        if (nodePatchA.equals(nodePatchB))
                        {
                            equivalentPatches++;
                            continue;
                        }
                        if (nodePatchA.getMutated().equals(nodePatchB.getMutated()))
                            if (nodePatchA.getOriginal().equals(nodePatchB.getOriginal()))
                                equivalentPatches++;

                        if (equivalentPatches == mutantA.getNodePatches().size())
                            fail("Mutant " + mutantA + " is equal to " + mutantB);
                    }
                }
            }
        }
    }

    protected void assertNoClearEquivalents(ASTVisitorMutationStrategy astVisitorMutationStrategy,
                                            TargetSource targetSource)
    {
        Collection<TargetSource> targetSources = new ArrayList<>();
        targetSources.add(targetSource);
        ClearEquivalentMutantsChecker clearEquivalentsChecker = new ClearEquivalentMutantsChecker(targetSources);

        assertTrue(
                clearEquivalentsChecker.getClearEquivalentMutants(astVisitorMutationStrategy.getMutants())
                .isEmpty()
        );
    }



}
