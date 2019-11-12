package mutagen.conf;

import com.sun.istack.internal.NotNull;
import mutagen.TargetSource;
import mutagen.output.FileOutput;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

public class Configuration
{
    private List<TargetSource> targets;
    private FileOutput fileOutput;
    private String classpath;

    public Configuration(@NotNull String targetSolutionDirPaths,
                         @NotNull String targetRelativePath,
                         @NotNull String outputDir,
                         String targetClasspath)
    {
        String[] targetLocs = (targetSolutionDirPaths.split(","));
        targets = createTargetList(targetLocs, targetRelativePath);
        fileOutput = new FileOutput(outputDir, targetRelativePath);
        classpath = targetClasspath;
    }

    public Configuration(CLIReader cli)
    {

        try
        {
            classpath = cli.getInputValue(OptionNames.CLASSPATH);
        }
        catch (OptionNotSetException optEx)
        {
            optEx.printStackTrace();
            System.out.println("Proceeding without classpath for target sourcefile...");
        }

        try
        {
            String[] targetLocs = (cli.getInputValue(OptionNames.TARGET_DIR).split(","));
            String targetSourceFilename = cli.getInputValue(OptionNames.TARGET_RELATIVE_PATH);
            targets = createTargetList(targetLocs, targetSourceFilename);
            fileOutput = new FileOutput(
                    cli.getInputValue(OptionNames.OUTPUT_DIR),
                    targetSourceFilename);
        }
        catch (OptionNotSetException optEx)
        {
            optEx.printStackTrace();
            System.exit(ErrorCodes.NO_TARGET.ordinal());
        }
    }

    private List<TargetSource> createTargetList(String[] targetLocs, String localPath)
    {
        List<TargetSource> targetList = new ArrayList<>();
        for (String l : targetLocs)
        {
            TargetSource t = new TargetSource(l, localPath, classpath);
            targetList.add(t);
        }
        return targetList;
    }

    public List<TargetSource> getTargets()
    {
        return targets;
    }

    public FileOutput getFileOutput()
    {
        return fileOutput;
    }

    public String getClasspath()
    {
        return classpath;
    }
}
