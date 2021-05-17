package com.company.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordMapper extends Mapper<Text, IntWritable, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);

    public void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
        // Text type to String for string manipulation later
        String stringValue = key.toString();

        // Transform word to lower case for sorting later
        stringValue = stringValue.toLowerCase();

        // Split up the word into individual letters
        String[] letters = stringValue.split("");

        // Map the letters in pairs
        for (int i = 1; i < stringValue.length(); i++) {
            context.write(new Text(stringValue.substring(i-1, 1+i)), one);
        }
    }
}
