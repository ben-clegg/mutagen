package mutagen;

import mutagen.cli.*;
import mutagen.mutation.Mutant;
import mutagen.mutation.strategy.EqualityConfusion;
import mutagen.mutation.strategy.MutationStrategy;
import mutagen.mutation.strategy.UnbalancedBrackets;

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
        List<Mutant> eqMutants = eqMut.createAllMutants();

        for (Mutant m : eqMutants)
        {
            System.out.println(m.toString());
        }

        MutationStrategy brktMut = new UnbalancedBrackets(target.getLines());
        List<Mutant> brktMutants = brktMut.createAllMutants();

        for (Mutant m : brktMutants)
        {
            System.out.println(m.toString());
        }

    }

    public static void main(String[] args)
    {
        MutaGen mutaGen = new MutaGen(args);
    }
}
