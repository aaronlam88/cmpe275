package server;

//import io.grpc.comm.Request;
//import io.grpc.comm.Response;

import client.ProjectClient;
import com.cmpe275.grpcComm.Request;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskManager {
    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    HashMap<String, InternalClient> routingTable;
    DatabaseManager databaseManager;
    Stack<String> roundRobin;
    String currentId = "SELF";

    public TaskManager(HashMap<String, InternalClient> routingTable, DatabaseManager databaseManager) {
        // init routing table
        this.routingTable = routingTable;
        // init round robin
        for (String id : routingTable.keySet()) {
            roundRobin.add(id);
        }
        // get databaseManager to go work
        this.databaseManager = databaseManager;
    }

    /**
     * addNode: insert new node to cluster
     *
     * @param id
     * @param internalClient
     */
    public void addNode(String id, InternalClient internalClient) {
        routingTable.put(id, internalClient);
        roundRobin.add(id);
    }

    public void putTask(Request request) {
        if (currentId.equalsIgnoreCase("SELF")) {
            databaseManager.addToBatch(request.getPutRequest().getDatFragment().getData().toStringUtf8());
        } else {
            InternalClient internalClient = routingTable.get(currentId);
            internalClient.putHandler(request);
        }
    }

    public void putDone() {
        if (currentId.equalsIgnoreCase("SELF")) {
            databaseManager.commitBatch();
        }
        roundRobin.push(currentId);
        currentId = roundRobin.pop();
    }

    public LinkedList<String> getTask(String from_utc, String to_utc) {
        LinkedList<String> result = new LinkedList<>();
        for (InternalClient internalClient : routingTable.values()) {
            for (String line : internalClient.getHandler(from_utc, to_utc).split("\n")) {
                result.add(line);
            }
        }

        ResultSet resultSet = databaseManager.selectByTimeRanch(from_utc, to_utc);
        try {
            ResultSetMetaData metadata = resultSet.getMetaData();
            int numberOfColumns = metadata.getColumnCount();

            while (resultSet.next()) {
                StringBuffer row = new StringBuffer();
                for (int i = 1; i <= numberOfColumns; ++i) {
                    if (i == 1) {
                        row.append(resultSet.getString(i));
                    } else if (i == 2) {
                        row.append(resultSet.getTimestamp(i));
                    } else {
                        row.append(resultSet.getDouble(i));
                    }
                    row.append("\t");
                }
                result.add(row.append("\n").toString());
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException ", e.getMessage());
        }

        return result;
    }
}
