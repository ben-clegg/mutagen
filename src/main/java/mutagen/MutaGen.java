package mutagen;

import mutagen.cli.*;
import mutagen.mutation.MutationEngine;
import mutagen.output.FileOutput;

public class MutaGen
{
    private Configuration config;

    private TargetSource target;
    private FileOutput fileOutput;

    public MutaGen(String[] args)
    {
        config = new Configuration(args);

        try
        {
            target = new TargetSource(
                        config.getInputValue(OptionNames.TARGET));
            fileOutput = new FileOutput(
                        config.getInputValue(OptionNames.OUTPUT_DIR),
                        target.getFilename());

        }
        catch (OptionNotSetException optEx)
        {
            optEx.printStackTrace();
            System.exit(ErrorCodes.NO_TARGET.ordinal());
        }

        MutationEngine mutate = new MutationEngine(target);
        mutate.generateMutants();
        mutate.printAllMutants();

        fileOutput.writeMutants(mutate.getMutants());
        fileOutput.writeSummary(mutate.getMutants());
    }

    public static void main(String[] args)
    {
        MutaGen mutaGen = new MutaGen(args);
    }
}
