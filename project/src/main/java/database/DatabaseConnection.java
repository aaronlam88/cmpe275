package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static DatabaseConnection databaseConnection = null;
	public static Connection connection = null;
	
	private DatabaseConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		
		System.out.println("MySQL JDBC Driver Registered!");

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root", "040188Lin!");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

	public static Connection getConnection() {
		if (connection == null) {
			databaseConnection = new DatabaseConnection();
		}

		return connection;
	}
}
