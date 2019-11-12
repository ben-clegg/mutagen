package mutagen;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class TargetSource
{
    private File fullyQualifiedFile;
    private File rootDir;
    private JavaSource lines;
    private String relativePath;
    private CompilationUnit compilationUnit;
    private String classpath;

    private void loadContents()
    {
        try
        {
            // Load contents of file
            lines = new JavaSource(
                        Files.readLines(fullyQualifiedFile, Charset.forName("UTF-8")));

            // Parse Abstract Syntax Tree with JavaParser
            compilationUnit = JavaParser.parse(fullyQualifiedFile);
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }

    public TargetSource(String dir, String relativeLocalPath, String targetClasspath)
    {
        rootDir = new File(dir);
        fullyQualifiedFile = new File(dir + File.separator + relativeLocalPath);
        relativePath = relativeLocalPath;
        classpath = targetClasspath;
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

    public String getRelativePath()
    {
        return relativePath;
    }

    public File getFullyQualifiedFile()
    {
        return fullyQualifiedFile;
    }

    public String getClasspath()
    {
        return classpath;
    }

    public File getRootDir()
    {
        return rootDir;
    }
}
