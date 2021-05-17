package com.company;

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
            formatter.printHelp("bin/hadoop jar [this jar-file] [input] [output] -t [directory]", options);

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
