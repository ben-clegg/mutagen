package mutagen;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TargetSource
{
    private File location;
    private JavaSource lines;

    private void loadContents()
    {
        try
        {
            lines = new JavaSource(
                        Files.readLines(location, Charset.forName("UTF-8")));
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }

    public TargetSource(String path)
    {
        location = new File(path);
        loadContents();
    }

    public JavaSource getLines()
    {
        return lines;
    }
}
