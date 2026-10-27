package com.napier.DevOps_Lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class App {

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    public static void main(String[] args) {
        // Create new Application and connect to database
        App a = new App();

        if(args.length < 1){
            a.connect("localhost:33060", 30000);
        }else{
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        Department dept = a.getDepartment("Development");
        ArrayList<Employee> employees = a.getSalariesByDepartment(dept);

        // Print salary report
        a.printSalaries(employees);

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
                System.out.println("Successfully disconnected");
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    // ၂။ Lab 6 အတွက် ထည့်သွင်းရမည့် printSalaries method
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));

        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    // Department တစ်ခုကို ရှာရန် (Lab 6 / Lab 7 အတွက် လိုအပ်သည်)
    public Department getDepartment(String dept_name)
    {
        try
        {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT dept_no, dept_name "
                            + "FROM departments "
                            + "WHERE dept_name = '" + dept_name + "'";
            var rset = stmt.executeQuery(strSelect);
            if (rset.next())
            {
                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");
                return dept;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }

    // Department အလိုက် ဝန်ထမ်းစာရင်းနှင့် လစာများကို ထုတ်ယူရန်
    public ArrayList<Employee> getSalariesByDepartment(Department dept)
    {
        if (dept == null)
            return null;
        try
        {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, dept_emp, salaries "
                            + "WHERE employees.emp_no = dept_emp.emp_no "
                            + "AND dept_emp.emp_no = salaries.emp_no "
                            + "AND dept_emp.dept_no = '" + dept.dept_no + "' "
                            + "AND salaries.to_date = '9999-01-01'";
            var rset = stmt.executeQuery(strSelect);
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    // Employee နံပါတ်ဖြင့် တစ်ဦးချင်းစီ ရှာရန် (Integration Test အတွက် လိုအပ်သည်)
    public Employee getEmployee(int ID)
    {
        try
        {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;
            var rset = stmt.executeQuery(strSelect);
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

}