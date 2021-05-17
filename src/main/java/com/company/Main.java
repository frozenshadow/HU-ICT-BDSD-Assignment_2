package com.company;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // Initialize program options
        CLIOptions options = new CLIOptions(args);

        // Delete output dir to prevent Hadoop errors
        File outputDir = new File(args[1]);
        FileUtils.deleteDirectory(outputDir);

        // Train each language found in the training set directory
        File trainingSetDir = new File(options.commandLine.getOptionValue("t"));
        File[] trainingSetDirListing = trainingSetDir.listFiles();
        HashMap<String, String> matrixLocations = new HashMap<>();

        if (trainingSetDirListing != null) {
            for (File file : trainingSetDirListing) {
                String language = Helpers.noExtensionFileName(file);

                Trainer trainer = new Trainer(
                        options.commandLine.getOptionValue("t"),
                        args[1],
                        file,
                        language,
                        "bdsd_assignment_2 - ");
                matrixLocations.put(language, trainer.train());
            }
        }

        LanguageProcessor languageProcessor = new LanguageProcessor(
                matrixLocations,
                args[1],
                "bdsd_assignment_2 - ");

        // Processing unknown language files
        File inputDir = new File(args[0]);
        File[] inputDirListing = inputDir.listFiles();
        if (inputDirListing != null) {
            for (File file : inputDirListing) languageProcessor.processUnknownLanguage(file);
        }
    }
}
