package com.company.Reducers;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class JoinReducer extends Reducer<Text, Text, Text, Text> {

    Text valEmit = new Text();
    int total = 0;

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        boolean isSecondPass = false;
        String value = "";
        String merge;

        for (Text v : values) {
            value = v.toString();

            // Check if this is the second pass
            String[] splittedValue = value.split(" ");
            if (splittedValue.length > 1) {
                isSecondPass = true;
                value = splittedValue[1];
            }

            // If it is the second pass, (prepare to) calculate the max entropy
            if (isSecondPass) {
                int intValue = Integer.parseInt(value);

                if (key.getLength() == 1) {
                    total = intValue;
                } else {
                    value = String.valueOf((float) intValue / total);
                }
            }
        }

        // Append a flag if it's not the second pass.
        // Otherwise only set the value by merging the key with the max entropy value
        if (!isSecondPass) {
            merge = key + " " + value + " " + 1;
        } else {
            if (key.getLength() == 1) return;
            merge = key + "\t" + value;
            key = null;
        }

        valEmit.set(merge);
        context.write(key, valEmit);
    }
}
