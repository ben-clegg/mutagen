package mutagen;

import mutagen.cli.*;

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

    }

    public static void main(String[] args)
    {
        MutaGen mutaGen = new MutaGen(args);
    }
}
