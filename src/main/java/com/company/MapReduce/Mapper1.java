package com.company.MapReduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {

    Text keyEmit = new Text();
    Text valEmit = new Text();
    private final Text word = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken());
            keyEmit.set(word);

            word.set(itr.nextToken());
            valEmit.set(word);

            context.write(keyEmit, valEmit);
        }
    }
}
