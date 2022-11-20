package org.cartapp;

import java.sql.*;

public class Database {

    public static void main(String[] args) {
        //final String databaseName = "School Database";
        //DriverManager.getConnection("jdbc:sqlite:C:\\Users\\carte\\IdeaProjects\\DataBaseConnectivity\\src\\main\\resources\\test.db"

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\carte\\OneDrive\\Documents\\Databases\\School Database.db");
            Statement statement = conn.createStatement();
        ) {
            statement.execute("CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY,name TEXT,grade INTEGER,gpa REAL)");
            //statement.execute("INSERT INTO students VALUES(2, 'george', 12, 4.00)");
            statement.execute("SELECT * FROM students WHERE gpa > 3.3");
            ResultSet values = statement.getResultSet();

            while (values.next()) {
                System.out.printf("id: %d, name: %s, grade: %d, gpa: %.3f%n",
                        values.getInt("id"),
                        values.getString("name"),
                        values.getInt("grade"),
                        values.getDouble("gpa"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            //System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
