package mutagen.mutation.simple;

import mutagen.JavaSource;
import mutagen.TargetSource;
import mutagen.mutation.Mutant;

import java.io.File;

public class SimpleMutant extends Mutant
{
    private String patched;
    private int index;
    private TargetSource original;

    public SimpleMutant(String mutatedLine,
                        int lineIndex,
                        String mutantType,
                        TargetSource originalSrc)
    {
        super(mutantType);
        patched = mutatedLine;
        index = lineIndex;
        original = originalSrc;
        setupMutatedJavaSource();
    }

    @Override
    protected void setupMutatedJavaSource()
    {
        modified = original.getLines().copy();
        modified.remove(index);
        modified.add(index, patched);
    }

    public int getLineNumber()
    {
        return index + 1;
    }

    @Override
    public String toString()
    {
        return  getType() +
                "[" + getIdString() + "] @ Line " +
                String.format("%04d", getLineNumber()) +
                " : " + patched;
    }

    public String getReplacement()
    {
        return patched;
    }
}