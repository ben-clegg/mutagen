package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.TargetSource;

public class Mutant
{
    private String patched;
    private int index;

    public Mutant(String mutatedLine, int lineIndex)
    {
        patched = mutatedLine;
        index = lineIndex;
    }

    public JavaSource getModifiedLines(TargetSource original)
    {
        JavaSource modified = original.getLines().copy();
        modified.remove(index);
        modified.add(index, patched);
        return modified;
    }

    private int getLineNumber()
    {
        return index + 1;
    }

    @Override
    public String toString()
    {
        return "Mutant @ Line " +
                String.format("%04d", getLineNumber()) +
                " : " + patched;
    }
}
