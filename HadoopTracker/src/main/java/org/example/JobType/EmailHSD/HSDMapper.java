package org.example.JobType.EmailHSD;

import java.io.IOException;

import org.apache.hadoop.io.*;

class HSDMapper extends org.apache.hadoop.mapreduce.Mapper<LongWritable, Text, LongWritable, Text> {

    /**
     Maps the input key-value pairs to output key-value pairs by calling the EmailHSD method of the hsd class to generate the HSD
     for the input text and writes the output to the context.
     @param key the input key as a LongWritable
     @param value the input value as a Text
     @param context the context object for the current map operation
     @throws IOException if an I/O error occurs during the map operation
     @throws InterruptedException if the map operation is interrupted
     */
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] fields = new String[0];
        if (value != null) {
            fields = value.toString().split("\\S+", 2);
        }
        //id of the text file
        LongWritable id = new LongWritable(Long.parseLong(fields[0]));

        //call EmailHSD method of the hsd class
        EmailHSD hsd = new EmailHSD();

        //Line numbers of the HSD
        Text outputString = hsd.extractHSD(fields[0], fields[1]);

        context.write(id, outputString);
    }

}





