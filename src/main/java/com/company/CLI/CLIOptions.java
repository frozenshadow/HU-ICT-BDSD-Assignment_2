package com.company.CLI;

import org.apache.commons.cli.*;

public class CLIOptions {

    public CommandLine commandLine;

    public CLIOptions(String[] args) {
        Options options = setupOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }

    private Options setupOptions() {
        Options options = new Options();

        Option trainingDirInput = new Option("t", "train", true, "training set input directory");
        options.addOption(trainingDirInput);

        return options;
    }
}
