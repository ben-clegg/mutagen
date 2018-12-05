package mutagen.mutation;

import mutagen.JavaSource;

import java.io.File;

public abstract class Mutant
{
    protected int id;
    protected File location;
    protected JavaSource modified;
    private String type;

    public Mutant(String mutantType)
    {
        type = mutantType;
    }

    protected abstract void setupMutatedJavaSource();


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

    protected String getType()
    {
        return type;
    }

    public JavaSource getModifiedLines()
    {
        return modified;
    }
}
