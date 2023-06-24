package org.example.JobType.EmailHSD;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


import java.io.IOException;

public class HSDDriver {
    public Job driver(String inPath, String outPath, Configuration conf) throws IOException
    {

        FileSystem fileSystem=FileSystem.get(conf);

        Job job = Job.getInstance(conf, "ReadWriteData");

        job.setMapperClass(HSDMapper.class);
        job.setReducerClass(HSDReducer.class);
        job.setNumReduceTasks(1);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(NLineInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        NLineInputFormat.setNumLinesPerSplit(job, 1);
        NLineInputFormat.setInputPaths(job, new Path(inPath));


        Path outputDir = new Path(outPath);

        if (fileSystem.exists(outputDir)){
            fileSystem.delete(outputDir,true);
        }

        FileOutputFormat.setOutputPath(job, outputDir);

        return job;
}
}
