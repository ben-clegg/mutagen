package mutagen.mutation;

import mutagen.JavaSource;
import mutagen.TargetSource;

public class Mutant
{
    private int index;
    private String patched;

    public Mutant()
    {

    }

    public JavaSource getModifiedLines(TargetSource original)
    {
        JavaSource modified = original.getLines().copy();
        modified.remove(index);
        modified.add(index, patched);
        return modified;
    }

}
