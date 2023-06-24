package org.example;

import org.example.DatabaseFiles.Database;

import java.io.IOException;
import java.sql.SQLException;

public class Main
{
    public static void main(String[] args) throws SQLException, IOException, InterruptedException, ClassNotFoundException
    {
        Database.insertData();
    }
}
