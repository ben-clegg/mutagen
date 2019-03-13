package mutagen.cli;

import mutagen.TargetSource;
import mutagen.output.FileOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configuration
{
    private List<TargetSource> targets;
    private FileOutput fileOutput;

    private static String majorLocation = File.separator + "usr" +
                                    File.separator + "share" +
                                    File.separator + "major" +
                                    File.separator + "bin" +
                                    File.separator + "javac";

    public Configuration(String targetSolutionPaths,
                         String targetSourceFilename,
                         String outputDir)
    {
        String[] targetLocs = (targetSolutionPaths.split(","));
        targets = createTargetList(targetLocs, targetSourceFilename);
        fileOutput = new FileOutput(outputDir, targetSourceFilename);
    }

    public Configuration(CLIReader cli)
    {
        try
        {
            String[] targetLocs = (cli.getInputValue(OptionNames.TARGET_DIR).split(","));
            String targetSourceFilename = cli.getInputValue(OptionNames.TARGET_SOURCEFILE);
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

    private List<TargetSource> createTargetList(String[] targetLocs, String sourceFilename)
    {
        List<TargetSource> targetList = new ArrayList<>();
        for (String l : targetLocs)
        {
            TargetSource t = new TargetSource(l + File.separatorChar + sourceFilename);
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

    public static String getMajorLocation()
    {
        return majorLocation;
    }
}
