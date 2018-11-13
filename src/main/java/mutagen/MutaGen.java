package mutagen;

import mutagen.cli.*;
import mutagen.mutation.Mutant;
import mutagen.mutation.strategy.EqualityConfusion;
import mutagen.mutation.strategy.MutationStrategy;

import java.util.List;

public class MutaGen
{
    private Configuration config;

    private TargetSource target;

    public MutaGen(String[] args)
    {
        config = new Configuration(args);

        try
        {
            target = new TargetSource(config.getInputValue(OptionNames.TARGET));
        }
        catch (OptionNotSetException optEx)
        {
            optEx.printStackTrace();
            System.exit(ErrorCodes.NO_TARGET.ordinal());
        }

        System.out.println(target.getLines());

        MutationStrategy eqMut = new EqualityConfusion(target.getLines());
        List<Mutant> eqMutants = eqMut.createMutants();

        for (Mutant m : eqMutants)
        {
            System.out.println(m.getModifiedLines(target));
        }

    }

    public static void main(String[] args)
    {
        MutaGen mutaGen = new MutaGen(args);
    }
}
