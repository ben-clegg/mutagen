package tech.clegg.mutagen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaSource extends ArrayList<String>
{
    public JavaSource() { super(); }
    public JavaSource(List list)
    {
        super(list);
    }

    public JavaSource(File location)
    {
        try
        {
            Files.lines(location.toPath()).forEach((s) -> this.add(s));
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
            System.err.println("Failed to read JavaSource " + hashCode() + " from "+ location);
        }
    }

    public JavaSource(String full)
    {
        String[] lines = full.split("\n");
        this.addAll(Arrays.asList(lines));
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
