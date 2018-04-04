package server;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

/**
 * Database config, to be used by Gson parser to get data from db_config.json
 */
class DatabaseConfig {
    String username;
    String password;
    String host;
    int port;
    String database;
    String jdbc_prefix;
    String jdbc_setting;
}

/**
 * This is a singleton class use to get connection with database
 *
 * @author aaronlam
 * @version 0.0.1
 * @since 2018-03-26
 */
class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    private static Connection connection = null;

    /**
     * This function will return the singleton connection Require a path to a json
     * with values for key username, password, jdbc_prefix, host, port, database,
     * jdbc_setting. Checkout DatabaseConfig
     *
     * @param path_to_config_json: path to config.json file
     * @return java.sql.Connection
     * @exception FileNotFoundException or SQLException
     *
     * @author aaronlam
     * @version 0.0.1
     * @since 2018-03-26
     */
    public static Connection getDatabaseConnection(String path_to_config_json) {
        if (connection == null) {
            Gson gson = new Gson();
            Reader reader = null;
            try {
                reader = new FileReader(path_to_config_json);
            } catch (FileNotFoundException e) {
                logger.info("FileNotFoundException: " + e.getMessage());
                return null;
            }
            DatabaseConfig config = gson.fromJson(reader, DatabaseConfig.class);
            logger.info("Connecting to database " + config.database + "...");
            String username = config.username;
            String password = config.password;
            String database = String.format("%s%s:%s/%s%s", config.jdbc_prefix, config.host, config.port,
                    config.database, config.jdbc_setting);
            try {
                connection = DriverManager.getConnection(database, username, password);
                connection.setAutoCommit(false);
                logger.info("Connected to " + config.database);
            } catch (Exception e) {
                logger.info("Cannot connect to database:'" + database + "' with error " + e.getMessage());
            }
        }
        return connection;
    }
}

public class DatabaseManager {
    private Connection connection = null;

    public DatabaseManager(String path_to_config_json) {
        this.connection = DatabaseConnection.getDatabaseConnection(path_to_config_json);
    }

}
