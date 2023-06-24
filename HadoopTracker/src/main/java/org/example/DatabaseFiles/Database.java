package org.example.DatabaseFiles;

import java.io.*;
import java.sql.*;
import java.sql.Connection;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.example.HadoopConfig.HadoopConfiguration;
import org.example.JobDetection.LoadDetectionMain;
import org.example.JobType.MyJobTypeClasses;


public class Database
{
    private final static String URL = "jdbc:mysql://localhost:3306/my_project_db";
    private final static String USER = "root";
    private final static String PASS = "root";
    public static Connection con;
    public static String root_dir;
    public static ResultSet rs;
    public static JobStatus.State jobstatus;
    private final static Logger LOGGER = Logger.getLogger(Database.class);

   static {
        PropertyConfigurator.configure("W:\\Komal\\IdeaProjects\\HadoopTracker\\logs\\log4j.properties");
       try
       {
           Class.forName("com.mysql.cj.jdbc.Driver");
           con = DriverManager.getConnection(URL, USER, PASS);
       }
       catch (ClassNotFoundException | SQLException e)
       {
           e.printStackTrace();
       }
   }

    public static void dbConnection() throws SQLException
    {
        if (con != null && !con.isClosed())
        {
            LOGGER.info("Connection is successful");
        }
        else
        {
            LOGGER.error("Connection is not successful");
        }
    }
    public static void dbCloseConnection() throws SQLException {
        if(con == null && con.isClosed())
        {
            LOGGER.error("Connection is closed");
        }
        else
        {
            LOGGER.error("Connection is not closed");
        }
    }

    public static void insertData() throws SQLException, IOException, InterruptedException, ClassNotFoundException {
        dbConnection();
        PreparedStatement stmt = con.prepareStatement("INSERT INTO jobdata (JobName,JobTypeID,JobStatusID,InputPath,OutputPath,JobPriority) VALUES (?, ?, ?, ?, ?, ?)");
        stmt.setString(1, MyJobTypeClasses.jobname1);
        stmt.setInt(2,1);
        stmt.setInt(3,1);
        stmt.setString(4, "W:\\Komal\\Dharmik\\Email_Path.txt");
        stmt.setString(5, "W:\\Komal\\Outputs\\out1");
        stmt.setInt(6,1);
        stmt.executeUpdate();

     /*   stmt.setString(1,DriverManagerClass.jobname2);
        stmt.setInt(2,1);
        stmt.setInt(3,1);
        stmt.setString(4, "W:\\Komal\\Dharmik\\Mail2\\File.txt");
        stmt.setString(5, "W:\\Komal\\Outputs\\out2");
        stmt.setInt(6,1);
        stmt.addBatch();*/

        //int[] insertdata = stmt.executeBatch();
        LOGGER.info("Insert data successfully");
        createHadoopDir();
    }

    public static void createHadoopDir() throws SQLException, IOException, InterruptedException, ClassNotFoundException
    {
        dbConnection();
        String query = "SELECT * FROM jobdata ORDER BY Load_ID DESC LIMIT 1";
        Statement stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        while (rs.next())
        {
            FileSystem fs = FileSystem.get(HadoopConfiguration.hadoopConfig());
            root_dir = "/" + rs.getString("JobName") + "-" + rs.getInt("Load_ID");
            Path src = new Path(root_dir);
            fs.mkdirs(src);
            fs.copyFromLocalFile(new Path(rs.getString("InputPath")),src);
            LOGGER.info("Directory is created successfully created");
        }
         MyJobTypeClasses.jobconfigure();
    }
    public static void updateStatus() throws SQLException {
        dbConnection();
        String query = "UPDATE jobdata SET JobStatusID = (SELECT ID FROM jobstatus WHERE Status=?) ORDER BY Load_ID DESC LIMIT 1";
        PreparedStatement pstmt = con.prepareStatement(query);
        jobstatus = LoadDetectionMain.jobStatus.getState();

        //int id = rs.getInt("Load_ID");
        pstmt.setString(1,String.valueOf(jobstatus));
        //pstmt.setInt(2,id);
        pstmt.executeUpdate();
        LOGGER.info("Status Updated Successfully");
    }

    public static void copyToLocal() throws SQLException, IOException
    {
        String query = "SELECT * FROM jobdata ORDER BY Load_ID DESC LIMIT 1";
        PreparedStatement pstmt = con.prepareStatement(query);
        rs = pstmt.executeQuery();
        while(rs.next())
        {
            MyJobTypeClasses.root_dir = "/" + Database.rs.getString("JobName") + "-" + Database.rs.getString("Load_ID");
            MyJobTypeClasses.src = new Path(root_dir);
            String out_dir = root_dir + "/" + root_dir + "-out/";
            Path out_src = new Path(out_dir);
            FileSystem fs = FileSystem.get(HadoopConfiguration.hadoopConfig());
            FileStatus[] status = fs.listStatus(MyJobTypeClasses.src);
            if(fs.exists(MyJobTypeClasses.src))
            {
                  for(int i=0;i<status.length;i++)
                  {
                      if(fs.exists(out_src))
                      {
                         String local_out_path = rs.getString("OutputPath");
                         Path local_src = new Path(local_out_path);
                         fs.copyToLocalFile(out_src,local_src);
                      }
                      else{
                          LOGGER.error("Files does not exists");
                      }
                  }
                LOGGER.info("Copy to local successful");
            }
            else{
                LOGGER.error("Directory does not exists");
            }
        }
        statusFile();
    }


   public static void statusFile() throws SQLException, IOException
    {
        File outputfile = new File("W:\\Komal\\JobFileStatus\\FileStatus.txt");
        BufferedWriter bwriter = new BufferedWriter(new FileWriter(outputfile));
       // String jobstatus = String.valueOf(LoadDetectionMain.jobStatus.getState());
        String query = "SELECT d.Load_ID, d.JobName, d.JobStatusID, s.Status FROM jobdata d JOIN jobstatus s ON d.JobStatusID = s.ID ORDER BY Load_ID DESC LIMIT 1";
        Statement stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        while(rs.next())
        {
            int load_id = rs.getInt("Load_ID");
            String jobname = rs.getString("JobName");
            int statusid = rs.getInt("JobStatusID");
            String statusname = rs.getString("Status");
            bwriter.write(load_id + "\t" + jobname + "\t" + statusid + "\t" +statusname);
            bwriter.newLine();
            LOGGER.info("File write successfully");
        }
       bwriter.close();
    }
}
