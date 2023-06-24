package org.example.HadoopConfig;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;


public class HadoopConfiguration
{
    public static Configuration hadoopConfig()
    {
       final String HADOOP_HOME = "W:\\Komal\\hadoop-3.3.4\\bin";
       final String PROPERTY_NAME = "fs.defaultFS";
       final Configuration config = new Configuration();
       config.addResource(new Path(HADOOP_HOME + "/conf/core-site.xml"));
       config.get(PROPERTY_NAME);
       config.set(PROPERTY_NAME, "hdfs://localhost:9678");

       config.addResource(new Path("W:\\Komal\\hadoop-3.3.4\\etc\\hadoop\\core-site.xml"));
       config.addResource(new Path("W:\\Komal\\hadoop-3.3.4\\etc\\hadoop\\hdfs-site.xml"));
       config.addResource(new Path("W:\\Komal\\hadoop-3.3.4\\etc\\hadoop\\mapred-site.xml"));
       config.addResource(new Path("W:\\Komal\\hadoop-3.3.4\\etc\\hadoop\\yarn-site.xml"));
       return config;
    }
}
