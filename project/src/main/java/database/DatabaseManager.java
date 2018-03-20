package database;

import java.sql.Connection;

public class DatabaseManager {

	public static void main(String[] args) {
		Connection connection = DatabaseConnection.getConnection();
	}

}
