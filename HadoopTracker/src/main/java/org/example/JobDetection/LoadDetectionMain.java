package org.example.JobDetection;

import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.example.DatabaseFiles.Database;
import org.example.JobType.MyJobTypeClasses;

import java.io.IOException;
import java.sql.*;
import java.util.TimerTask;


public class LoadDetectionMain extends TimerTask
{
    public static JobStatus jobStatus;
    private final static Logger LOGGER = Logger.getLogger(LoadDetectionMain.class);

    static
    {
        PropertyConfigurator.configure("W:\\Komal\\IdeaProjects\\HadoopTracker\\logs\\log4j.properties");
    }

    @Override
    public void run()
    {
        try
        {
            if (isLoadDetected())
            {
                LOGGER.info("After load detection");
            }

        }
        catch (IOException | InterruptedException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoadDetected() throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        //Get the JobStatus
        jobStatus = MyJobTypeClasses.job.getStatus();

       if (jobStatus.getState() == JobStatus.State.PREP)               //Preparing the job
        {
            Database.updateStatus();
            LOGGER.info("Job is preparing");
        }
        else if (jobStatus.getState() == JobStatus.State.RUNNING)        //Running the job
        {
            Database.updateStatus();
            LOGGER.info("Job is running");
        }
        else if(jobStatus.getState() == JobStatus.State.SUCCEEDED)      //Success the job
        {
            Database.updateStatus();
            LOGGER.info("Job is succeeded");
        }
        else if(jobStatus.getState() == JobStatus.State.FAILED)         //Failing the job
        {
             Database.updateStatus();
             LOGGER.error("Job is failed");
        }
        else if(jobStatus.getState() == JobStatus.State.KILLED)         //Killing the job
        {
            Database.updateStatus();
            LOGGER.error("Job is Killed");
        }
        else
        {
           LOGGER.error("Job is Undefined");
        }
        return false;

    }

}