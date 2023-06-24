package org.example.JobType.EmailHSD;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class HSDReducer extends Reducer<LongWritable, Text,LongWritable, Text> {

    /**
     This method is used as a reducer function in a Hadoop MapReduce job. It takes in a LongWritable key and an Iterable of Text values as input, and writes them to the context object.
     @param key the LongWritable key for the input data
     @param values an Iterable of Text values for the input data
     @param context the Context object for the Hadoop MapReduce job
     @throws IOException if an I/O error occurs
     @throws InterruptedException if the thread is interrupted
     */
    @Override
    protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        for (Text value : values) {
            context.write(key, value);
        }

    }
}
