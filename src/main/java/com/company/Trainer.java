package com.company;

import com.company.MapReduce.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

public class Trainer {

    private final String inputDir;
    private final String outputDir;
    private final File inputFile;
    private final String language;
    private final String jobPrefix;
    private final String bigramDirectory;
    private final String letterMatrixDirectory;
    private final String letterCountDirectory;

    public Trainer(String inputDir, String outputDir, File inputFile, String language, String jobPrefix) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.inputFile = inputFile;
        this.language = language;
        this.jobPrefix = jobPrefix;

        this.bigramDirectory = "trained_" + language;
        this.letterMatrixDirectory = language + "_letterMatrix";
        this.letterCountDirectory = language + "_letterCount" ;
    }

    public String train() throws IOException, InterruptedException, ClassNotFoundException {
        makeBigram();
        mapReduceSingleLetters();
        computeMaxEntropy();

        FileUtil.fullyDelete(new File(outputDir, bigramDirectory));
        FileUtil.fullyDelete(new File(outputDir, letterCountDirectory));

        return letterMatrixDirectory;
    }

    private void makeBigram() throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, String.format("%s%s training", jobPrefix, language));

        Configuration sentenceMapperConf = new Configuration(false);
        ChainMapper.addMapper(job, SentenceMapper.class, Object.class, Text.class, Text.class, IntWritable.class, sentenceMapperConf);

        Configuration wordMapperConf = new Configuration(false);
        ChainMapper.addMapper(job, WordMapper.class, Text.class, IntWritable.class, Text.class, IntWritable.class, wordMapperConf);

        job.setJarByClass(Main.class);
        job.setCombinerClass(WordReducer.class);
        job.setReducerClass(WordReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(inputDir, inputFile.getName()));
        FileOutputFormat.setOutputPath(job, new Path(outputDir, bigramDirectory));

        job.waitForCompletion(true);
    }

    private void mapReduceSingleLetters() throws IOException, InterruptedException, ClassNotFoundException {
        Configuration Conf = new Configuration();
        Job job = Job.getInstance(Conf, String.format("%s%s MR letters", jobPrefix, language));

        job.setJarByClass(Main.class);
        job.setMapperClass(LetterMapper.class);
        job.setCombinerClass(LetterReducer.class);
        job.setReducerClass(LetterReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(outputDir, bigramDirectory + "/part-r-00000"));
        FileOutputFormat.setOutputPath(job, new Path(outputDir, letterCountDirectory));

        job.waitForCompletion(true);
    }

    private void computeMaxEntropy() throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, String.format("%s%s Compute MaxEnt", jobPrefix, language));

        job.setJarByClass(Main.class);
        job.setCombinerClass(ReducerJoin.class);
        job.setReducerClass(ReducerJoin.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        MultipleInputs.addInputPath(job, new Path(outputDir, letterCountDirectory + "/part-r-00000"), TextInputFormat.class, Mapper1.class);
        MultipleInputs.addInputPath(job, new Path(outputDir, bigramDirectory + "/part-r-00000"), TextInputFormat.class, Mapper1.class);
        FileOutputFormat.setOutputPath(job, new Path(outputDir, letterMatrixDirectory));

        job.waitForCompletion(true);
    }
}
