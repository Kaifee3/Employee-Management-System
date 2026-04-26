package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Mysql {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/employee_db";
        String user = "root";
        String password = "123";

        Connection con = null;
        Statement stmt = null;
        Scanner scanner = null;

        try {
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect Database
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully!");

            stmt = con.createStatement();

            // Create Table
            String createTableSql = "CREATE TABLE IF NOT EXISTS emp1 (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50), " +
                    "age INT NOT NULL)";
            stmt.executeUpdate(createTableSql);

            scanner = new Scanner(System.in);

            // Show all data first
            displayAll(stmt);

            // Menu Loop
            while (true) {
                System.out.println("\n===== Employee Management System =====");
                System.out.println("1. Insert Employee");
                System.out.println("2. Update Employee");
                System.out.println("3. Delete Employee");
                System.out.println("4. Fetch Employee (XML/JSON)");
                System.out.println("5. Display All Employees");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                switch (choice) {

                    // INSERT
                    case 1:
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter Age: ");
                        int age = scanner.nextInt();

                        String insertSql = "INSERT INTO emp1(name, age) VALUES (?, ?)";
                        PreparedStatement ps = con.prepareStatement(insertSql);
                        ps.setString(1, name);
                        ps.setInt(2, age);

                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                            System.out.println("Employee inserted successfully!");
                        }
                        ps.close();
                        break;

                    // UPDATE
                    case 2:
                        System.out.print("Enter Employee ID to Update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter New Name: ");
                        String newName = scanner.nextLine();

                        System.out.print("Enter New Age: ");
                        int newAge = scanner.nextInt();

                        String updateSql = "UPDATE emp1 SET name=?, age=? WHERE id=?";
                        PreparedStatement ups = con.prepareStatement(updateSql);
                        ups.setString(1, newName);
                        ups.setInt(2, newAge);
                        ups.setInt(3, updateId);

                        int updateRows = ups.executeUpdate();
                        if (updateRows > 0) {
                            System.out.println("Employee updated successfully!");
                        } else {
                            System.out.println("Employee not found!");
                        }
                        ups.close();
                        break;

                    // DELETE
                    case 3:
                        System.out.print("Enter Employee ID to Delete: ");
                        int deleteId = scanner.nextInt();

                        String deleteSql = "DELETE FROM emp1 WHERE id=?";
                        PreparedStatement dps = con.prepareStatement(deleteSql);
                        dps.setInt(1, deleteId);

                        int deleteRows = dps.executeUpdate();
                        if (deleteRows > 0) {
                            System.out.println("Employee deleted successfully!");
                        } else {
                            System.out.println("Employee not found!");
                        }
                        dps.close();
                        break;

                    // FETCH XML / JSON
                    case 4:
                        System.out.print("Enter format (XML/JSON): ");
                        String format = scanner.nextLine();

                        System.out.print("Enter Employee ID: ");
                        int empId = scanner.nextInt();

                        String fetchQuery = "SELECT * FROM emp1 WHERE id=?";
                        PreparedStatement pstmt = con.prepareStatement(fetchQuery);
                        pstmt.setInt(1, empId);

                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            Map<String, Object> employee = new HashMap<>();
                            employee.put("id", rs.getInt("id"));
                            employee.put("name", rs.getString("name"));
                            employee.put("age", rs.getInt("age"));

                            ObjectMapper objectMapper = new ObjectMapper();
                            XmlMapper xmlMapper = new XmlMapper();

                            if (format.equalsIgnoreCase("JSON")) {
                                String jsonOutput = objectMapper
                                        .writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(employee);

                                System.out.println("\nEmployee Details (JSON):");
                                System.out.println(jsonOutput);

                            } else if (format.equalsIgnoreCase("XML")) {
                                String xmlOutput = xmlMapper
                                        .writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(employee);

                                System.out.println("\nEmployee Details (XML):");
                                System.out.println(xmlOutput);

                            } else {
                                System.out.println("Invalid format!");
                            }

                        } else {
                            System.out.println("Employee not found!");
                        }

                        rs.close();
                        pstmt.close();
                        break;

                    // DISPLAY ALL
                    case 5:
                        displayAll(stmt);
                        break;

                    // EXIT
                    case 6:
                        System.out.println("Exiting program...");
                        return;

                    default:
                        System.out.println("Invalid Choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
                if (scanner != null) scanner.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to Display All Employees
    public static void displayAll(Statement stmt) throws SQLException {
        String selectSql = "SELECT * FROM emp1";
        ResultSet rs = stmt.executeQuery(selectSql);

        System.out.println("\n===== Employee Records =====");
        System.out.printf("%-10s%-20s%-10s%n", "ID", "Name", "Age");

        while (rs.next()) {
            System.out.printf("%-10d%-20s%-10d%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"));
        }

        rs.close();
    }
}