package com.waveproject.upload;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.opencsv.*;
import com.opencsv.CSVReaderBuilder;

public class ReadDataCSV {
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    private static String host = null;
    private static String user = null;
    private static String passwd = null;
    public static String readAllDataAtOnce(String file) {
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            properties.load(classLoader.getResourceAsStream(File.separator + "dbProperties.properties"));
            host = properties.getProperty("url");
            user = properties.getProperty("username");
            passwd = properties.getProperty("password");
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        try {
            // Setup the connection with the DB
            connect = DriverManager
                .getConnection("jdbc:mysql://" + host + "/rohit?" +
                    "user=" + user + "&password=" + passwd);


            // Create an object of file reader 
            // class with CSV file as a parameter. 
            FileReader filereader = new FileReader(file);

            // create csvReader object and skip first Line 
            //CSVReader csvReader2 = new CSVReaderBuilder(reader).
            CSVReader csvReader = new CSVReaderBuilder(filereader).build();
            List < String[] > allData = new ArrayList < String[] > ();
            allData = csvReader.readAll();

            String[] lastRow = allData.get(allData.size() - 1);
            String reportid = lastRow[1];

            boolean checkPresent = checkExistingData(reportid);
            if (!checkPresent) {
                Date date = null; // your date

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                StringBuffer finalDate = new StringBuffer(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day));
                java.sql.Date d1 = java.sql.Date.valueOf(finalDate.toString());
                preparedStatement = connect
                    .prepareStatement("insert into  rohit.reports values (?,?)");
                // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
                // Parameters start with 1
                preparedStatement.setInt(1, Integer.parseInt(reportid));
                preparedStatement.setDate(2, d1); //new java.sql.Date(year, month, day));
                preparedStatement.executeUpdate();
                //connect.commit();
                for (String[] row: allData) {
                    int nextData = 1;
                    PreparedStatement preparedStatement2 = connect.prepareStatement("select max(id)+1 from rohit.reportsData");
                    ResultSet rs3 = preparedStatement2.executeQuery();
                    while (rs3.next()) {
                        nextData = rs3.getInt(1);
                    }
                    //Inserting all records into database
                    preparedStatement = null;
                    String dateValue = null;
                    String hoursWorked = null;
                    String employeeId = null;
                    String jobGroup = null;
                    for (String cell: row) {
                        //System.out.print(cell + "\t");
                        if (cell.equalsIgnoreCase("date") || cell.equalsIgnoreCase("report id")) {
                            break;
                        } else if (dateValue == null) {
                            dateValue = cell;
                        } else if (hoursWorked == null) {
                            hoursWorked = cell;
                        } else if (employeeId == null) {
                            employeeId = cell;
                        } else if (jobGroup == null) {
                            jobGroup = cell;
                        } else {

                        }
                    }
                    if (dateValue != null && hoursWorked != null && employeeId != null && jobGroup != null) {
                        preparedStatement = connect
                            .prepareStatement("insert into  rohit.reportsData (id,report_id,employee_id,dateWorked,hoursWorked,jobGroup) values (?,?,?,?,?,?)");
                        preparedStatement.setInt(1, nextData);
                        preparedStatement.setInt(2, Integer.parseInt(reportid));
                        preparedStatement.setString(3, employeeId);
                        String[] dateParts = dateValue.split("/");
                        dateValue = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                        java.sql.Date d2 = java.sql.Date.valueOf(dateValue);
                        preparedStatement.setDate(4, d2); //new java.sql.Date(year, month, day));
                        preparedStatement.setString(5, hoursWorked);
                        preparedStatement.setString(6, jobGroup);

                        preparedStatement.executeUpdate();

                    }
                    //System.out.println(); 
                }
                return "Insert Complete for the New Report";
            } else {
                //message that insert was unsucessful as the report id was already present
                return "Duplicate report found. Avoiding re entry";
            }

        } catch (Exception e) {
            //e.printStackTrace(); 
            return "Exception while report processing " + e.getMessage();
        }
    }

    public static boolean checkExistingData(String reportid) throws Exception {
        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();
        // Result set get the result of the SQL query
        resultSet = statement
            .executeQuery("select * from rohit.reports where id = " + reportid);
        if (resultSet.next()) {
            //Data already exists. Show message
            return true;
        } else {
            return false;
        }

    }
}