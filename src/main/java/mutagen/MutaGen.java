package mutagen;

import mutagen.conf.*;
import mutagen.mutation.MutationEngine;
import mutagen.properties.MutantFlag;

public class MutaGen
{
    private Configuration config;



    public MutaGen(Configuration configuration)
    {
        config = configuration;

        MutationEngine engine = new MutationEngine(config.getTargets());

        if (configuration.getConfigFlagValue(ConfigFlag.REMOVE_COMPILABILITY_MUTANTS))
            engine.applyFilterToStrategies(ms -> !ms.getFlags().contains(MutantFlag.COMPILABILITY));
        if(configuration.getConfigFlagValue(ConfigFlag.ONLY_FUNCTIONALITY_MUTANTS))
            engine.applyFilterToStrategies(ms -> ms.getFlags().contains(MutantFlag.FUNCTIONALITY));

        engine.generateMutants();
        engine.printAllMutants();

        config.getFileOutput().writeMutants(engine.getMutants());
        config.getFileOutput().writeSummary(engine.getMutants());
    }

    public static void main(String[] args)
    {
        CLIReader cli = new CLIReader(args);
        Configuration conf = new Configuration(cli);
        MutaGen mutaGen = new MutaGen(conf);
    }
}
