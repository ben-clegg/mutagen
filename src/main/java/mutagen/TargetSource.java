package mutagen;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TargetSource
{
    private File location;
    private JavaSource lines;
    private String filename;
    private CompilationUnit compilationUnit;

    private void loadContents()
    {
        try
        {
            // Load contents of file
            lines = new JavaSource(
                        Files.readLines(location, Charset.forName("UTF-8")));

            // Parse Abstract Syntax Tree with JavaParser
            compilationUnit = JavaParser.parse(location);
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }

    public TargetSource(String path)
    {
        location = new File(path);
        filename = location.getName();
        loadContents();
    }

    public JavaSource getLines()
    {
        return lines;
    }

    public CompilationUnit getCompilationUnit()
    {
        return compilationUnit;
    }

    public String getFilename()
    {
        return filename;
    }

    public File getLocation()
    {
        return location;
    }
}
