package mutagen;

import mutagen.cli.*;
import mutagen.mutation.MutationEngine;
import mutagen.output.FileOutput;

public class MutaGen
{
    private Configuration config;



    public MutaGen(Configuration configuration)
    {
        config = configuration;

        MutationEngine mutate = new MutationEngine(config.getTargets());
        mutate.generateMutants();
        mutate.printAllMutants();

        config.getFileOutput().writeMutants(mutate.getMutants());
        config.getFileOutput().writeSummary(mutate.getMutants());
    }

    public static void main(String[] args)
    {
        CLIReader cli = new CLIReader(args);
        Configuration conf = new Configuration(cli);
        MutaGen mutaGen = new MutaGen(conf);
    }
}
