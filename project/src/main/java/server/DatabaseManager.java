package server;

import com.google.gson.Gson;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * @version 0.1.0
 * @since 04-22-2018
 */
class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    private static Connection connection = null;

    /**
     * This function will return the singleton connection Require a path to a json
     * with values for key username, password, jdbc_prefix, host, port, database,
     * jdbc_setting. See DatabaseConfig
     *
     * @param path_to_config_json: path to config.json file
     * @return java.sql.Connection
     * @author aaronlam
     * @since 04-22-2018
     */
    static Connection getDatabaseConnection(String path_to_config_json) {
        if (connection == null) {
            Gson gson = new Gson();
            Reader reader;
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

    static Connection getDatabaseConnection(String username, String password, String database) {
        if (connection == null) {
            logger.info("Connecting to database " + database + "...");

            try {
                connection = DriverManager.getConnection(database, username, password);
                connection.setAutoCommit(false);
                logger.info("Connected to " + database);
            } catch (Exception e) {
                logger.info("Cannot connect to database:'" + database + "' with error " + e.getMessage());
            }
        }
        return connection;
    }
}

public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    // const variable
    private final static int batchSize = 10000;

    private final static String createTable = "CREATE TABLE `mesowest` (\n" +
            "  `STN` text,\n" +
            "  `DATE` datetime DEFAULT NULL,\n" +
            "  `MNET` double DEFAULT NULL,\n" +
            "  `SLAT` double DEFAULT NULL,\n" +
            "  `SLON` double DEFAULT NULL,\n" +
            "  `SELV` double DEFAULT NULL,\n" +
            "  `TMPF` double DEFAULT NULL,\n" +
            "  `SKNT` double DEFAULT NULL,\n" +
            "  `DRCT` double DEFAULT NULL,\n" +
            "  `GUST` double DEFAULT NULL,\n" +
            "  `PMSL` double DEFAULT NULL,\n" +
            "  `ALTI` double DEFAULT NULL,\n" +
            "  `DWPF` double DEFAULT NULL,\n" +
            "  `RELH` double DEFAULT NULL,\n" +
            "  `WTHR` double DEFAULT NULL,\n" +
            "  `P24I` double DEFAULT NULL\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";

    private final static String insertStmt = "INSERT INTO `cmpe275`.`mesowest`\n" +
            "(`STN`,`DATE`,`MNET`,`SLAT`,`SLON`,`SELV`,`TMPF`,`SKNT`,`DRCT`,`GUST`,`PMSL`,`ALTI`,`DWPF`,`RELH`,`WTHR`,`P24I`)\n" +
            "VALUES\n" +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n";

    private final static String selectStmt = "SELECT * FROM cmpe275.mesowest WHERE  ? <= DATE and DATE <= ?;";
    private static PreparedStatement batchPreparedStmt;
    private static int currBatchSize = batchSize;
    // variable
    private Connection connection;

    DatabaseManager(String path_to_config_json) {
        connection = DatabaseConnection.getDatabaseConnection(path_to_config_json);
        setupConnection();
    }

    DatabaseManager(String username, String password, String database) {
        connection = DatabaseConnection.getDatabaseConnection(username, password, database);
        setupConnection();
    }

    private void setupConnection() {
        try {
            connection.setAutoCommit(false);
            batchPreparedStmt = connection.prepareStatement(insertStmt);
            batchPreparedStmt.setFetchSize(batchSize);
            executeQuery(createTable);
        } catch (SQLException e) {
            logger.info("setupConnection " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * insert a single row to database
     * break the String row into arrays of values and
     * will call insertSingleRow(String[] values) to do insert
     *
     * @param row: a string represent a row to be insert into database
     * @return void
     * @author aaronlam
     * @since 04-22-2018
     */
    void inserSingletRow(String row) {
        String[] values = row.trim().split("\\s+");
        insertSingleRow(values);
    }

    /**
     * String[] values should have 16 field and
     *
     * @param values: a array represent a row to be insert into database
     * @return void
     * @author aaronlam
     * @since 04-22-2018
     */
    void insertSingleRow(String[] values) {
        if (values.length != 16) {
            logger.info("Incorrect format for insert query");
            return;
        }
        try {
            PreparedStatement ps = connection.prepareStatement(insertStmt);
            DateFormat format = new SimpleDateFormat("yyyyMMdd/HHmm");
            for (int i = 0, j = 1; i < values.length; ++i, ++j) {
                if (i == 1) {
                    try {
                        ps.setTimestamp(j, new java.sql.Timestamp(format.parse(values[i]).getTime()));
                    } catch (ParseException e) {
                        ps.setNull(j, Types.TIMESTAMP);
                    }
                } else if (i == 0) {
                    ps.setString(j, values[i]);
                } else {
                    try {
                        ps.setDouble(j, Double.parseDouble(values[i]));
                    } catch (Exception e) {
                        ps.setNull(j, Types.DOUBLE);
                    }
                }
            }
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException ", e.getMessage());
        }
    }

    /**
     * add multiple rows to batch for insert, will use addToBatch (String[] values)
     *
     * @param rows
     * @return void
     * @author aaronlam
     * @since 04-22-2018
     */
    void addToBatch(String rows) {
        // split multiple rows into row
        String[] rowArray = rows.split("\n");
        // for each row, insert row to batch
        for (String row : rowArray) {
            String[] values = row.trim().split("\\s+");
            addToBatch(values);
        }
    }

    /**
     * add a single row to batch for insert
     * @param values
     * @retrun void
     * @author aaronlam
     * @since 04-22-2018
     */
    void addToBatch(String[] values) {
        if (values.length != 16) {
            logger.info("Incorrect format for insert query");
            return;
        }

        try {
            batchPreparedStmt.clearParameters();
            DateFormat format = new SimpleDateFormat("yyyyMMdd/HHmm");
            for (int i = 0, j = 1; i < values.length; ++i, ++j) {
                if (i == 1) {
                    batchPreparedStmt.setTimestamp(j, new java.sql.Timestamp(format.parse(values[i]).getTime()));
                } else if (i == 0) {
                    batchPreparedStmt.setString(j, values[i]);
                } else {
                    batchPreparedStmt.setDouble(j, Double.parseDouble(values[i]));
                }
            }
            batchPreparedStmt.addBatch();
            currBatchSize--;
            if (currBatchSize <= 0) {
                this.commitBatch();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException ", e.getMessage());
        } catch (ParseException e) {
            logger.log(Level.WARNING, "ParseException ", e.getMessage());
        }
    }

    /**
     * commit current batch, will commit all the pending transaction in batch insert
     * @param
     * @return void
     * @@author aaronlam
     * @since 04-22-2018
     */
    void commitBatch() {
        try {
            currBatchSize = batchSize;
            batchPreparedStmt.executeBatch();
            connection.commit();
            batchPreparedStmt.clearParameters();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException ", e.getMessage());
        }
    }

    /**
     * execute query statement
     *
     * @param query: an sql query statement
     * @return null
     * @author aaronlam
     * @since 04-22-2018
     */
    void executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
        } catch (Exception e) {
            logger.info("executeQuery " + e.getStackTrace().toString());
        }
    }

    /**
     * execute the query and return a java.sql.ResultSet
     *
     * @param query
     * @return java.sql.ResultSet
     * @author aaronlam
     * @since 04-22-2018
     */
    ResultSet getResultSet(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            logger.info("getResultSet " + e.getStackTrace().toString());
            return null;
        }
    }

    /**
     * get result by running query to selection data from from_utc to to to_utc
     *
     * @param from_utc
     * @param to_utc
     * @return java.sql.ResultSet
     * @author aaronlam
     * @since 04-22-2018
     */
    ResultSet selectByTimeRanch(String from_utc, String to_utc) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectStmt);
            preparedStatement.setString(1, from_utc);
            preparedStatement.setString(2, to_utc);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException ", e.getMessage());
        }
        return null;
    }

    /**
     * Bellow are functions for future improvement
     */

    /**
     * insert csv file to database
     *
     * @param file_path: path to csv file
     * @author aaronlam
     * @since 04-22-2018
     */
    void insertCSV(String file_path) {
        try {
            InputStream fileStream = new FileInputStream(file_path);
            Reader decoder = new InputStreamReader(fileStream, "UTF-8");
            BufferedReader buffered = new BufferedReader(decoder);

            for (String line = buffered.readLine(); line != null; line = buffered.readLine()) {
                // process line
                String[] tokens = line.trim().split("\\s+"); // split line into different fields

                // valid table row should have 16 fields
                if (tokens.length == 16) {
                    insertSingleRow(tokens);
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "insertGZip error ", e);
        }
    }

    /**
     * insert gzip file to database
     *
     * @param file_path: path to gzip file
     * @author aaronlam
     * @since 04-22-2018
     */
    void insertGZip(String file_path) {
        try {
            InputStream fileStream = new FileInputStream(file_path);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
            BufferedReader buffered = new BufferedReader(decoder);

            for (String line = buffered.readLine(); line != null; line = buffered.readLine()) {
                // process line
                String[] tokens = line.trim().split("\\s+"); // split line into different fields

                // valid table row should have 16 fields
                if (tokens.length == 16) {
                    insertSingleRow(tokens);
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "insertGZip error ", e);
        }
    }
}
