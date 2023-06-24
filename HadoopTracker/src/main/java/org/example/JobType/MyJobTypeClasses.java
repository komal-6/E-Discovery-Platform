package org.example.JobType;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.example.DatabaseFiles.Database;
import org.example.HadoopConfig.HadoopConfiguration;
import org.example.JobDetection.LoadDetectionMain;
import org.example.JobType.EmailHSD.HSDDriver;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;

public class MyJobTypeClasses
{
    public final static String jobname1 = "EmailHSD";
    public static Path src, out_src;
    public static String root_dir, out_dir;
    public static String line;
    public static Job job;
    private final static Logger LOGGER = Logger.getLogger(MyJobTypeClasses.class);
    static
    {
        PropertyConfigurator.configure("W:\\Komal\\IdeaProjects\\HadoopTracker\\logs\\log4j.properties");
    }
    public static void jobconfigure() throws IOException, SQLException, InterruptedException, ClassNotFoundException {

        //create the job
        Database.dbConnection();
        String query = "SELECT * FROM jobdata ORDER BY JobPriority DESC LIMIT 1";
        Statement stmt = Database.con.createStatement();
        Database.rs = stmt.executeQuery(query);
        while (Database.rs.next())
        {
            FileSystem fs = FileSystem.get(HadoopConfiguration.hadoopConfig());
            root_dir = "/" + Database.rs.getString("JobName") + "-" + Database.rs.getInt("Load_ID");
            src = new Path(root_dir);
            fs.mkdirs(src);
            out_dir = root_dir + "/";
            out_src = new Path(out_dir);
            fs.copyFromLocalFile(new Path(Database.rs.getString("InputPath")), src);

//          job = Job.getInstance(HadoopConfiguration.hadoopConfig(),Database.rs.getString("JobName"));

            HSDDriver hsdDriver = new HSDDriver();
            job=hsdDriver.driver(root_dir,out_dir,HadoopConfiguration.hadoopConfig());

            job.submit();
            LOGGER.info("Job successfully submitted");
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new LoadDetectionMain(), 0, 1000);
            job.waitForCompletion(true);
            Database.copyToLocal();
        }
    }
}
