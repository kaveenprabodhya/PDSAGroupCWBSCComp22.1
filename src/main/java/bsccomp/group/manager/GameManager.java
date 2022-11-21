package bsccomp.group.manager;

import bsccomp.group.common.GraphAdjacencyListImpl;
import bsccomp.group.common.models.*;
import bsccomp.group.identifyminimumconnectors.IdentifyMinimumConnectors;
import bsccomp.group.identifyshortestpath.IdentifyShortestPath;
import bsccomp.group.maximumprofit.FindMaximumProfit;

import java.sql.*;
import java.util.*;

public class GameManager {
    IdentifyMinimumConnectors minimumConnectors;
    FindMaximumProfit maximumProfit;
    IdentifyShortestPath shortestPath;
    public static Connection connection = null;
    private static PreparedStatement preparedStatement = null;

    public GameManager() {
        minimumConnectors = new IdentifyMinimumConnectors(new GraphAdjacencyListImpl());
        maximumProfit = new FindMaximumProfit();
        shortestPath = new IdentifyShortestPath(new GraphAdjacencyListImpl());
    }

    public void startFindMinimumConnectors() {
        minimumConnectors.findMinimumConnectors();
    }

    public HashMap<Vertex, HashMap<Vertex, Edge>> getGraphOfMinimumConnectors() {
        return minimumConnectors.getAdjacencyList().returnList();
    }

    public List<Vertex> getListOfMinimumConnectorsVertices() {
        return minimumConnectors.getVerticesList();
    }

    public String getStartNodeOfMinimumConnectors() {
        return minimumConnectors.getStartNode().getName();
    }

    public int getMinimumDistance() {
        return minimumConnectors.calculateMinimumDistance();
    }

    public List<Pair<Vertex, List<Pair<Vertex, Edge>>>> getAnswersForMinimumConnectors() {
        return minimumConnectors.getAnswerList();
    }

    public boolean checkAnswersOfMinimumConnectors(List<Pair<Vertex, List<Pair<Vertex, Edge>>>> pairList) {
        boolean[] answers = new boolean[9];
        int count = 0;
        Arrays.fill(answers, false);
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : getAnswersForMinimumConnectors()) {
            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
                for (Pair<Vertex, List<Pair<Vertex, Edge>>> answerListPair : pairList) {
                    for (Pair<Vertex, Edge> answerEdgePair : answerListPair.getValue()) {
                        if (vertexListPair.getKey().getName().equalsIgnoreCase(answerListPair.getKey().getName())
                                && vertexEdgePair.getKey().getName().equalsIgnoreCase(answerEdgePair.getKey().getName())
                                && (vertexEdgePair.getValue().getWeight() == answerEdgePair.getValue().getWeight())) {
                            answers[count] = true;
                            count++;
                        }
                    }
                }
            }
        }
        return isAllTrue(answers);
    }

    private static boolean isAllTrue(boolean[] array) {
        for (boolean b : array)
            if (!b)
                return false;
        return true;
    }

    public void startFindMaximumProfit() {
        maximumProfit.calMaximumProfit();
//        maximumProfit.print();
    }

    public Item[] getMaximumProfitItemList() {
        return maximumProfit.getItems();
    }

    public int getMaximumProfitFor10kgs() {
        int[][] k = maximumProfit.getK();
        return k[10][10];
    }

    public boolean checkAnswersOfMaximumProfit(int answer) {
        return answer == getMaximumProfitFor10kgs();
    }

    public void startFindShortestPath() {
        shortestPath.findShortestPath();
    }

    public List<Vertex> getListOfShortestPathVertices() {
        return shortestPath.getVerticesList();
    }

    public HashMap<Vertex, HashMap<Vertex, Edge>> getGraphOfShortestPath() {
        return shortestPath.getAdjacencyList().returnList();
    }

    public String getShortestPathSource() {
        return shortestPath.getSourceVertex().getName();
    }

    public List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> getAnswersForShortestPath() {
        return shortestPath.getTable();
    }

    public List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> removedStarterSourceNode() {
        List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> table = shortestPath.getTable();
        List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> temp = new LinkedList<>();
        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> pair : table) {
            if (!Objects.isNull(pair.getValue().getKey())) {
                temp.add(pair);
            }
        }

        return temp;
    }

    public Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> getSourceDataOfShortestPath() {
        return shortestPath.getTable().get(0);
    }

    public boolean checkAnswersOfShortestPath(List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> pairList) {
        boolean[] answers = new boolean[9];
        int count = 0;
        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> pairGenerated : removedStarterSourceNode()) {
            for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> answerPair : pairList) {
                if (pairGenerated.getKey().getName().equalsIgnoreCase(answerPair.getKey().getName())
                        && pairGenerated.getValue().getKey().getName().equalsIgnoreCase(answerPair.getValue().getKey().getName())
                        && (Objects.equals(pairGenerated.getValue().getValue().getKey(), answerPair.getValue().getValue().getKey()))
                ) {
                    answers[count] = true;
                    count++;
                }
            }
        }
        return isAllTrue(answers);
    }

    public void printShortestPathTable() {
        shortestPath.printTable();
    }

    public static void makeJDBCConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pdsa_cw", "root", "qwerty@123");
        if (connection != null) {
            System.out.println("Connection successful");
        } else {
            System.out.println("Failed to make connection");
        }
    }

    public static void addMinimumConnectorsAnswersDataToDB(List<Pair<Vertex, List<Pair<Vertex, Edge>>>> userAnswerList, User user) throws SQLException {
        User createdUser = addUserDetailsDataToDB(user);
        String query = "INSERT INTO minimumconnectors(vertex, toVertex, edge, user_id) VALUES (?,?,?,?)";
        try {
            for (Pair<Vertex, List<Pair<Vertex, Edge>>> answerListPair : userAnswerList) {
                for (Pair<Vertex, Edge> answerEdgePair : answerListPair.getValue()) {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, answerListPair.getKey().getName());
                    preparedStatement.setString(2, answerEdgePair.getKey().getName());
                    preparedStatement.setInt(3, answerEdgePair.getValue().getWeight());
                    preparedStatement.setInt(4, createdUser.getUserId());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addShortestPathAnswersDataToDB(List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> userAnswerList,
                                                      User user) throws SQLException {
        User createdUser = addUserDetailsDataToDB(user);
        String query = "INSERT INTO shortestpath(vertex, prevVertex, distanceFromSrc, user_id) VALUES (?,?,?,?)";
        try {
            for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> answerListPair : userAnswerList) {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, answerListPair.getKey().getName());
                preparedStatement.setString(2, (Objects.isNull(answerListPair.getValue().getKey())
                        ? null
                        : answerListPair.getValue().getKey().getName())
                );
                preparedStatement.setInt(3, answerListPair.getValue().getValue().getKey());
                preparedStatement.setInt(4, createdUser.getUserId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMaximumProfitAnswersDataToDB(String maximumProfitAnswer, User user) throws SQLException {
        User createdUser = addUserDetailsDataToDB(user);
        String query = "INSERT INTO maximumprofit(maximum_profit, user_id) VALUES (?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, maximumProfitAnswer);
            preparedStatement.setInt(2, createdUser.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static User addUserDetailsDataToDB(User user) throws SQLException {
        boolean b = readUserDataFromDB(user);
        if (!b) {
            String query = "INSERT INTO users(username) VALUES (?)";
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            int i = preparedStatement.executeUpdate();
            if (i == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return user;
        }
        return user;
    }

    private static boolean readUserDataFromDB(User user) throws SQLException {
        String query = "SELECT id FROM users WHERE username = '" + user.getUsername() + "'";
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user.setUserId(resultSet.getInt("id"));
            return true;
        }
        return false;
    }
}
