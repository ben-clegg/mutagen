package mutagen.conf;

import com.sun.istack.internal.NotNull;
import mutagen.MutaGen;
import mutagen.TargetSource;
import mutagen.output.FileOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration
{
    private List<TargetSource> targets;
    private FileOutput fileOutput;
    private String classpath;
    private Map<ConfigFlag, Boolean> configFlagsMap = new HashMap<>();

    private void initConfigFlags()
    {
        for (ConfigFlag f : ConfigFlag.values())
            configFlagsMap.put(f, false);
    }

    public static void main(String[] args)
    {
        Configuration configuration = new Configuration(
                "/home/cleggy/Documents/phd-research/data/2018-19/assignment4_wine/model/modelSolution_mutation",
                "assignment2019/WineSampleCellar.java",
                "/home/cleggy/Documents/phd-research/data/2018-19/assignment4_wine/mutants",
                "/home/cleggy/Documents/phd-research/data/2018-19/assignment4_wine/classpathRoot"
        );
        MutaGen m = new MutaGen(configuration);
    }

    public Configuration(@NotNull String targetSolutionDirPaths,
                         @NotNull String targetRelativePath,
                         @NotNull String outputDir,
                         String targetClasspath)
    {
        String[] targetLocs = (targetSolutionDirPaths.split(","));
        classpath = targetClasspath;
        targets = createTargetList(targetLocs, targetRelativePath);
        fileOutput = new FileOutput(outputDir, targetRelativePath);
        initConfigFlags();
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

        // Config flags
        initConfigFlags();
        configFlagsMap.put(ConfigFlag.REMOVE_COMPILABILITY_MUTANTS, cli.isOptionSet(OptionNames.GEN_REMOVE_COMPILABILITY_MUTANTS));
        configFlagsMap.put(ConfigFlag.ONLY_FUNCTIONALITY_MUTANTS, cli.isOptionSet(OptionNames.GEN_ONLY_FUNCTIONALITY_MUTANTS));

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

    public boolean getConfigFlagValue(ConfigFlag flag)
    {
        return configFlagsMap.get(flag);
    }
}
