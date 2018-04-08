package server;

import com.google.gson.Gson;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

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
     * @throws FileNotFoundException or SQLException
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
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    private Connection connection = null;

    public DatabaseManager(String path_to_config_json) {
        this.connection = DatabaseConnection.getDatabaseConnection(path_to_config_json);
    }

    /**
     * insert csv file to database
     * @param file_path
     */
    public void insertCSV(String file_path) {
        try {
            InputStream fileStream = new FileInputStream(file_path);
            Reader decoder = new InputStreamReader(fileStream, "UTF-8");
            BufferedReader buffered = new BufferedReader(decoder);

            for (String line = buffered.readLine(); line != null; line = buffered.readLine()) {
                // process line
                line = line.trim(); // trim, remove white spaces at begin and end
                String[] tokens = line.split(" +"); // split line into different fields

                // valid table row should have 16 fields
                if (tokens.length == 16) {

                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "insertGZip error " , e);
        }
    }

    /**
     * insert gzip file to database
     * @param file_path
     */
    public void insertGZip(String file_path) {
        try {
            InputStream fileStream = new FileInputStream(file_path);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
            BufferedReader buffered = new BufferedReader(decoder);

            for (String line = buffered.readLine(); line != null; line = buffered.readLine()) {
                // process line
                line = line.trim(); // trim, remove white spaces at begin and end
                String[] tokens = line.split(" +"); // split line into different fields

                // valid table row should have 16 fields
                if (tokens.length == 16) {

                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "insertGZip error " , e);
        }
    }

    /**
     * execute insert statement
     * @param insertStmt
     */
    public void insertQuery(String insertStmt) {
        try {
            Statement statement = connection.createStatement();
            if (!statement.execute(insertStmt)) {
                logger.info("something wrong with the insert statement [" + insertStmt + "]");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "insertQuery error " , e);
        }
    }

    /**
     * execute the query and return a java.sql.ResultSet
     * @param query
     * @return java.sql.ResultSet
     */
    public ResultSet getResultSet(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            logger.log(Level.WARNING, "getResultSet error " , e);
            return null;
        }
    }

    /**
     * execute the query, write result to a tmp file, return the file path (String)
     * @param query
     * @return String file_path
     */
    public String getCSV(String query) {
        String file_path = null;

        return file_path;
    }
}
