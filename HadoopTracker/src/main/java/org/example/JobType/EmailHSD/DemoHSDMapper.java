/*
package org.example.JobType.EmailHSD;

import java.io.IOException;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;
import org.example.JobType.EmailHSD;
import org.example.JobType.EmailHSD.HSDMapper;

class Map3 extends Mapper<LongWritable, Text, LongWritable, Text> {

*
     This method is the run method of Map3 class which is executed by Hadoop MapReduce framework.
     It retrieves the path of the input file from the context, checks if it is empty or not, and runs the
     super class's run method. If the input file is empty, it logs an info message, sets the context status to
     "Empty input file", increments the custom counter named "EmptyInputFile", writes "0" to the output file, and returns.
     @param context the Hadoop Context object containing the configuration and other context information
     @throws IOException if an I/O error occurs while accessing the input file system
     @throws InterruptedException if the MapReduce job is interrupted while executing


    public void run(Context context) throws IOException, InterruptedException {
        final Logger log = Logger.getLogger(Map3.class);
        Path filePath = ((FileSplit) context.getInputSplit()).getPath();
        FileSystem fs = FileSystem.get(context.getConfiguration());
        FileStatus[] status = fs.listStatus(filePath);
        boolean isEmptyFile = false;
        for (FileStatus fileStatus : status) {
            if (fileStatus.getLen() == 0) {
                isEmptyFile = true;
                break;
            }
        }
        if (isEmptyFile) {
            log.info("Path file is empty");
            // Write "0" to the output file
            context.write( new LongWritable(0), new Text("Path file is empty"));
            return;
        }
        super.run(context);
    }

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] fields;
        fields = value.toString().split("\\s+", 2);
        Text outputString = null;
        LongWritable id = null;
        if (fields.length >= 2)
        {
            //id of the text file
            id = new LongWritable(Long.parseLong(fields[0]));

            //call EmailHSD method of the hsd class
            EmailHSD hsd = new EmailHSD();

            //Line numbers of the HSD
            outputString = hsd.extractHSD(fields[0], fields[1]);
        }

        context.write(id, outputString);
    }
}

public class DemoHSDMapper {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        FileSystem fileSystem=FileSystem.get(conf);

        Job job = Job.getInstance(conf, "ReadWriteData");
        job.setJarByClass(HSDMapper.class);
        job.setMapperClass(Map3.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path("W:\\Dharmik\\Project 3\\Email_Path 2.txt"));
        MultipleOutputs.addNamedOutput(job, "output", TextOutputFormat.class, LongWritable.class, Text.class);

        Path outputDir = new Path("W:\\Dharmik\\Project 3\\EmailHsd\\Output\\Output3");

        if (fileSystem.exists(outputDir)){
            fileSystem.delete(outputDir,true);
        }

        FileOutputFormat.setOutputPath(job, outputDir);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
*/
