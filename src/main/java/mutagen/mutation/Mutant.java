package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.TargetSource;

public class Mutant
{
    private String patched;
    private int index;
    private String type;
    private int id;

    public Mutant(String mutantType, String mutatedLine, int lineIndex)
    {
        type = mutantType;
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
        return  type +
                "[" + getIdString() + "] @ Line " +
                String.format("%04d", getLineNumber()) +
                " : " + patched;
    }

    public int getId()
    {
        return id;
    }
    public String getIdString()
    {
        return String.format("%06d", getId());
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
