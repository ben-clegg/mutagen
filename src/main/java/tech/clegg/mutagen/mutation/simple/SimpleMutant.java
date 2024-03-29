package tech.clegg.mutagen.mutation.simple;

import tech.clegg.mutagen.JavaSource;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.Mutant;
import tech.clegg.mutagen.properties.MutantType;

import java.io.File;

public class SimpleMutant extends Mutant
{
    private String patched;
    private int index;
    private TargetSource original;

    public SimpleMutant(String mutatedLine,
                        int lineIndex,
                        MutantType mutantType,
                        TargetSource originalSrc)
    {
        super(mutantType);
        patched = mutatedLine;
        index = lineIndex;
        original = originalSrc;
        setupMutatedJavaSource();

        setPreMutation(originalSrc.getLines().get(index).trim());
        setPostMutation(patched.trim());
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
                " : " + getChange();
    }

    public String getReplacement()
    {
        return patched;
    }
}