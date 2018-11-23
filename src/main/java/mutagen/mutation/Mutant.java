package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.TargetSource;

import java.io.File;

public class Mutant
{
    private String patched;
    private int index;
    private String type;
    private int id;
    private File location;
    private TargetSource original;

    public Mutant(String mutatedLine,
                  int lineIndex,
                  String mutantType,
                  TargetSource originalFile)
    {
        type = mutantType;
        patched = mutatedLine;
        index = lineIndex;
        original = originalFile;
    }

    public JavaSource getModifiedLines()
    {
        JavaSource modified = original.getLines().copy();
        modified.remove(index);
        modified.add(index, patched);
        return modified;
    }

    public int getLineNumber()
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

    public File getLocation()
    {
        return location;
    }

    public void setLocation(File location)
    {
        this.location = location;
    }

    public String getReplacement()
    {
        return patched;
    }
}