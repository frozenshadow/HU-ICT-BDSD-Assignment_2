package com.company.MapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LetterMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Text type to String for string manipulation later
        String stringValue = value.toString();

        // Map the first letter of the letter pair
        context.write(new Text(stringValue.substring(0, 1)), one);
    }
}
