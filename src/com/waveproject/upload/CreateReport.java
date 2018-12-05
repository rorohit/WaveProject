package com.waveproject.upload;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CreateReport {
    List < ReportDataView > list = new ArrayList < ReportDataView > ();
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    private static String host = null;
    private static String user = null;
    private static String passwd = null;
    public List < ReportDataView > createReportData() {
        String startDate = null;
        String endDate = null;
        String employeeId = null;
        Double hoursWorked = 0.0;
        Double totalSalary = 0.0;
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("dbProperties.properties"));
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
            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                .executeQuery("select * from rohit.reportsData order by employee_id,dateWorked");
            while (resultSet.next()) {
                if (startDate == null) {
                    Date dateWorked = resultSet.getDate(4);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateWorked);
                    if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
                        startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/01";
                        endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/15";
                    } else {
                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/16";
                        endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.getMaximum(Calendar.DAY_OF_MONTH));
                    }
                    employeeId = resultSet.getString(3);
                    hoursWorked = resultSet.getDouble(5);
                    if (resultSet.getString(6).equals("A")) {
                        totalSalary = hoursWorked * 20;
                    } else {
                        totalSalary = hoursWorked * 30;
                    }
                } else {
                    Date dateWorked = resultSet.getDate(4);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateWorked);
                    if (resultSet.getString(3).equals(employeeId)) {
                        if (startDate.substring(0, 4).equals(Integer.toString(cal.get(Calendar.YEAR)))) {
                            if (startDate.substring(5, 7).equals(Integer.toString(cal.get(Calendar.MONTH) + 1))) {
                                if (Integer.parseInt(startDate.substring(8)) <= cal.get(Calendar.DAY_OF_MONTH) && Integer.parseInt(endDate.substring(8)) >= cal.get(Calendar.DAY_OF_MONTH)) {
                                    hoursWorked = hoursWorked + resultSet.getDouble(5);
                                    if (resultSet.getString(6).equals("A")) {
                                        totalSalary = totalSalary + resultSet.getDouble(5) * 20;
                                    } else {
                                        totalSalary = totalSalary + resultSet.getDouble(5) * 30;
                                    }
                                } else {
                                    ReportDataView rs = new ReportDataView();
                                    rs.setStartData(startDate);
                                    rs.setEndData(endDate);
                                    rs.setEmployeeId(employeeId);
                                    rs.setHoursWorked(hoursWorked.toString());
                                    rs.setTotalSalary(totalSalary.toString());
                                    list.add(rs);
                                    if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
                                        startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/01";
                                        endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/15";
                                    } else {
                                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                                        startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/16";
                                        endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.getMaximum(Calendar.DAY_OF_MONTH));
                                    }
                                    hoursWorked = resultSet.getDouble(5);
                                    if (resultSet.getString(6).equals("A")) {
                                        totalSalary = hoursWorked * 20;
                                    } else {
                                        totalSalary = hoursWorked * 30;
                                    }
                                }
                            } else {
                                ReportDataView rs = new ReportDataView();
                                rs.setStartData(startDate);
                                rs.setEndData(endDate);
                                rs.setEmployeeId(employeeId);
                                rs.setHoursWorked(hoursWorked.toString());
                                rs.setTotalSalary(totalSalary.toString());
                                list.add(rs);
                                if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
                                    startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/01";
                                    endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/15";
                                } else {
                                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                                    startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/16";
                                    endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.getMaximum(Calendar.DAY_OF_MONTH));
                                }
                                hoursWorked = resultSet.getDouble(5);
                                if (resultSet.getString(6).equals("A")) {
                                    totalSalary = hoursWorked * 20;
                                } else {
                                    totalSalary = hoursWorked * 30;
                                }

                            }
                        } else {
                            ReportDataView rs = new ReportDataView();
                            rs.setStartData(startDate);
                            rs.setEndData(endDate);
                            rs.setEmployeeId(employeeId);
                            rs.setHoursWorked(hoursWorked.toString());
                            rs.setTotalSalary(totalSalary.toString());
                            list.add(rs);
                            if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
                                startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/01";
                                endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/15";
                            } else {
                                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                                startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/16";
                                endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.getMaximum(Calendar.DAY_OF_MONTH));
                            }
                            hoursWorked = resultSet.getDouble(5);
                            if (resultSet.getString(6).equals("A")) {
                                totalSalary = hoursWorked * 20;
                            } else {
                                totalSalary = hoursWorked * 30;
                            }

                        }
                    } else {
                        ReportDataView rs = new ReportDataView();
                        rs.setStartData(startDate);
                        rs.setEndData(endDate);
                        rs.setEmployeeId(employeeId);
                        rs.setHoursWorked(hoursWorked.toString());
                        rs.setTotalSalary(totalSalary.toString());
                        list.add(rs);
                        employeeId = resultSet.getString(3);
                        if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
                            startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/01";
                            endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/15";
                        } else {
                            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                            startDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/16";
                            endDate = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.getMaximum(Calendar.DAY_OF_MONTH));
                        }
                        hoursWorked = resultSet.getDouble(5);
                        if (resultSet.getString(6).equals("A")) {
                            totalSalary = hoursWorked * 20;
                        } else {
                            totalSalary = hoursWorked * 30;
                        }

                    }
                }

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }
}