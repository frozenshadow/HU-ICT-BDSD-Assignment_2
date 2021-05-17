package com.company.Mappers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SentenceMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Text type to String for string manipulation later
        String stringValue = value.toString();

        // Ignore comments
        if(stringValue.trim().startsWith("#") || stringValue.trim().length() <= 0) return;

        // Transform sentence to lower case for sorting later
        stringValue = stringValue.toLowerCase();

        // Split up the sentence into individual word, ignoring punctuation.
        String[] words = stringValue.replaceAll("[^a-z]", " ").trim().split("\\W+");

        // Map the words
        for (String word : words) context.write(new Text(word), one);
    }
}
