package com.company;

import com.company.Mappers.SentenceMapper;
import com.company.Mappers.WordMapper;
import com.company.Reducers.GenericReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class LanguageProcessor {

    private final HashMap<String, LinkedHashMap<String, Float>> languageMatrixList = new HashMap<>();
    private final String outputDir;
    private final String jobPrefix;

    public LanguageProcessor(HashMap<String, String> languageMatrixLocations, String outputDir, String jobPrefix) throws IOException {
        this.outputDir = outputDir;
        this.jobPrefix = jobPrefix;

        for (Map.Entry<String, String> location : languageMatrixLocations.entrySet()) {
            loadMatrix(location.getKey(), new File(outputDir, location.getValue() + "/part-r-00000"));
        }
    }

    public String guessLanguage(String UUID) throws IOException {
        HashMap<String, Float> languageScores = new HashMap<>();

        File file = new File(outputDir, UUID + "_inputMatrix/part-r-00000");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().length() <= 0) continue;

            String[] keyValue = line.split("\t");

            for (Map.Entry<String, LinkedHashMap<String, Float>> e : languageMatrixList.entrySet()) {
                Float langLetterMaxEnt = e.getValue().get(keyValue[0]);
                if (langLetterMaxEnt == null) langLetterMaxEnt = (float) 0;

                Float currLanguageScore = languageScores.get(e.getKey());
                if (currLanguageScore == null) currLanguageScore = (float) 0;

                Float newScore = Float.parseFloat(keyValue[1]) * langLetterMaxEnt + currLanguageScore;
                languageScores.put(e.getKey(), newScore);
            }
        }

        Optional<Map.Entry<String, Float>> max = languageScores.entrySet().stream().max(Map.Entry.comparingByValue());
        return max.get().getKey();
    }

    public void processUnknownLanguage(File file) throws IOException, InterruptedException, ClassNotFoundException {
        HashMap<String, Integer> languageLineCount = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().startsWith("#") || line.trim().length() <= 0) continue;

            // Create a GUID for the temporary file
            String fileUUID = String.valueOf(java.util.UUID.randomUUID());

            // Create the temporary file and write the current line to it
            File tempFile = File.createTempFile(fileUUID, ".txt");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(line);
            writer.close();

            // Use the temporary file as input for Hadoop.
            // If there is another less i/o intensive way, please let me know :)
            makeBigram(tempFile.getAbsolutePath(), fileUUID);

            // Remove the temporary upon completion
            tempFile.delete();

            String resultLanguage = guessLanguage(fileUUID);

            if (languageLineCount.get(resultLanguage) == null) {
                languageLineCount.put(resultLanguage, 1);
            } else {
                Integer currLineCount = languageLineCount.get(resultLanguage);
                languageLineCount.put(resultLanguage, currLineCount + 1);
            }

            FileUtil.fullyDelete(new File(outputDir, fileUUID + "_inputMatrix"));
        }

        for (Map.Entry<String, Integer> e : languageLineCount.entrySet()) {
            System.out.printf("%s: %d lines\n", e.getKey(), e.getValue());
        }
    }

    private void makeBigram(String inputFile, String UUID) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, String.format("%s generate matrix", jobPrefix));

        Configuration sentenceMapperConf = new Configuration(false);
        ChainMapper.addMapper(job, SentenceMapper.class, Object.class, Text.class, Text.class, IntWritable.class, sentenceMapperConf);

        Configuration wordMapperConf = new Configuration(false);
        ChainMapper.addMapper(job, WordMapper.class, Text.class, IntWritable.class, Text.class, IntWritable.class, wordMapperConf);

        job.setJarByClass(Main.class);
        job.setCombinerClass(GenericReducer.class);
        job.setReducerClass(GenericReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(inputFile));
        FileOutputFormat.setOutputPath(job, new Path(outputDir, UUID + "_inputMatrix"));

        job.waitForCompletion(true);
    }

    private void loadMatrix(String language, File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        LinkedHashMap<String, Float> matrix = new LinkedHashMap<>();

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().length() <= 0) continue;

            String[] pair = line.split("\t");
            matrix.put(pair[0], Float.valueOf(pair[1]));
        }

        languageMatrixList.put(language, matrix);
    }
}
