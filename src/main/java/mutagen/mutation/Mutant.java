package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.properties.MutantFlag;
import mutagen.properties.MutantType;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class Mutant
{
    private Set<MutantFlag> flags = Collections.synchronizedSet(EnumSet.noneOf(MutantFlag.class));
    private MutantType type;

    protected int id;
    protected File location;
    private File directory;
    protected JavaSource modified;
    //private String type;

    protected String preMutation = null;
    protected String postMutation = null;

    public Mutant(MutantType mutantType)
    {
        type = mutantType;
    }

    protected void addFlag(MutantFlag mutantFlag)
    {
        flags.add(mutantFlag);
    }

    protected void setFlags(Collection<MutantFlag> mutantFlags)
    {
        flags.clear();
        flags.addAll(mutantFlags);
    }

    protected abstract void setupMutatedJavaSource();

    public void setPreMutation(String preMutation)
    {
        this.preMutation = preMutation;
    }

    public void setPostMutation(String postMutation)
    {
        this.postMutation = postMutation;
    }

    public String getChange()
    {
        if(postMutation == null)
        {
            return "";
        }
        else
        {
            if(preMutation == null)
            {
                return postMutation;
            }
            else
            {
                return preMutation + " -> " + postMutation;
            }
        }
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

    public void setLocation(File dir, String relativeLocation)
    {
        directory = dir;
        this.location = new File(dir + File.separator + relativeLocation);
    }

    public File getDirectory()
    {
        return directory;
    }

    public MutantType getType()
    {
        return type;
    }

    public JavaSource getModifiedLines()
    {
        return modified;
    }
}
