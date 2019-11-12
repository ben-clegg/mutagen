package mutagen.conf;

import org.apache.commons.cli.*;

public class CLIReader
{
    private CommandLine cli;
    private Options opts;

    private void initOptions()
    {
        opts = new Options();

        opts.addOption(Option.builder(OptionNames.SHORT_TARGET_DIR)
                .longOpt(OptionNames.TARGET_DIR)
                .hasArg()
                .desc("Path to the directories containing source files to mutate.")
                .build());

        opts.addOption(Option.builder(OptionNames.SHORT_TARGET_RELATIVE_PATH)
                .longOpt(OptionNames.TARGET_RELATIVE_PATH)
                .hasArg()
                .desc("Name of the solution to mutate (e.g. Classname.java).")
                .build());

        opts.addOption(Option.builder(OptionNames.SHORT_OUTPUT_DIR)
                .longOpt(OptionNames.OUTPUT_DIR)
                .hasArg()
                .desc("Path to the directory to store mutants in.")
                .build());

        opts.addOption(Option.builder(OptionNames.SHORT_CLASSPATH)
                .longOpt(OptionNames.CLASSPATH)
                .hasArg()
                .desc("Classpath for model solution.")
                .build());
    }

    public CLIReader(String[] args)
    {
        initOptions();

        CommandLineParser commandLineParser = new DefaultParser();

        try
        {
            cli = commandLineParser.parse(opts, args);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            System.err.println("Unexpected parsing error, check configuration.");
            System.exit(1);
        }
    }

    public String getInputValue(String optStr) throws OptionNotSetException
    {
        if(cli.hasOption(optStr))
        {
            return cli.getOptionValue(optStr);
        }
        Option o = opts.getOption(optStr);
        StringBuilder msg = new StringBuilder();
        msg.append("Option not set: (-" + o.getOpt());

        if(o.hasLongOpt())
            msg.append(" / --" + o.getLongOpt());
        msg.append(") - " + o.getDescription());

        throw new OptionNotSetException(msg.toString());
    }


}
