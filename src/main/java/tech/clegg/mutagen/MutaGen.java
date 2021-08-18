package tech.clegg.mutagen;

import tech.clegg.mutagen.conf.*;
import tech.clegg.mutagen.mutation.Mutant;
import tech.clegg.mutagen.mutation.MutationEngine;
import tech.clegg.mutagen.properties.MutantFlag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            engine.applyFilterToStrategies(ms -> ms.getFlags().contains(MutantFlag.MUTAGEN_THESIS_FINAL));
        if(configuration.getConfigFlagValue(ConfigFlag.ONLY_MAJOR_MUTANTS))
            engine.applyFilterToStrategies(ms -> ms.getFlags().contains(MutantFlag.USES_MAJOR));

        engine.printEnabledStrategies();
        engine.generateMutants();
        engine.printAllMutants();

        // Get generated mutants
        Collection<Mutant> generatedMutants = engine.getMutants();

        // Show clear equivalent mutants
        ClearEquivalentMutantsChecker equivMutsChecker = new ClearEquivalentMutantsChecker(engine.getTargets());
        Collection<Mutant> clearEquivalents = equivMutsChecker.getClearEquivalentMutants(generatedMutants);
        equivMutsChecker.printMutantsAsEquivalents(clearEquivalents);

        // Remove clear equivalents from generated pool
        List<Mutant> filteredPool = new ArrayList<>(generatedMutants);
        filteredPool.removeAll(clearEquivalents);

        config.getFileOutput().writeMutants(filteredPool);
        config.getFileOutput().writeSummary(filteredPool);
    }

    public static void main(String[] args)
    {
        CLIReader cli = new CLIReader(args);
        Configuration conf = new Configuration(cli);
        MutaGen mutaGen = new MutaGen(conf);
    }
}
