package mutagen;

import java.util.ArrayList;
import java.util.List;

public class JavaSource extends ArrayList<String>
{
    public JavaSource() { super(); }
    public JavaSource(List list)
    {
        super(list);
    }

    public JavaSource copy()
    {
        JavaSource copied = new JavaSource();
        copied.addAll(this);
        return copied;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for(String l : this)
        {
            s.append(l);
            s.append('\n');
        }
        return s.toString();
    }

    /*
    public String getLine(int lineNumber)
    {
        String line = this.get(lineNumber - 1);
        return line;
    }
    */
}
