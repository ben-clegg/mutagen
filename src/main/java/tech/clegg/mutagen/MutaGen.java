package tech.clegg.mutagen;

import tech.clegg.mutagen.conf.*;
import tech.clegg.mutagen.mutation.MutationEngine;
import tech.clegg.mutagen.properties.MutantFlag;

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
        if(configuration.getConfigFlagValue(ConfigFlag.ONLY_MUTAGEN_MUTANTS))
            engine.applyFilterToStrategies(ms -> ms.getFlags().contains(MutantFlag.MUTAGEN_UNIQUE));
        if(configuration.getConfigFlagValue(ConfigFlag.ONLY_MUTAGEN_MUTANTS_UPDATED))
            engine.applyFilterToStrategies(ms -> ms.getFlags().contains(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY));
        if(configuration.getConfigFlagValue(ConfigFlag.ONLY_MAJOR_MUTANTS))
            engine.applyFilterToStrategies(ms -> ms.getFlags().contains(MutantFlag.USES_MAJOR));

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