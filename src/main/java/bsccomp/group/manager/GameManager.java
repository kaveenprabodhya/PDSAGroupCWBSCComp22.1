package bsccomp.group.manager;

import bsccomp.group.common.GraphAdjacencyListImpl;
import bsccomp.group.common.models.Edge;
import bsccomp.group.common.models.Item;
import bsccomp.group.common.models.Pair;
import bsccomp.group.common.models.Vertex;
import bsccomp.group.identifyminimumconnectors.IdentifyMinimumConnectors;
import bsccomp.group.identifyshortestpath.IdentifyShortestPath;
import bsccomp.group.maximumprofit.FindMaximumProfit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        return minimumConnectors.getVertexList();
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
        for (boolean b : array) if (!b) return false;
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

    public HashMap<Vertex, HashMap<Vertex, Edge>> getGraphOfShortestPath() {
        return shortestPath.getAdjacencyList().returnList();
    }

    public String getShortestPathSource() {
        return shortestPath.getSourceVertex().getName();
    }

    public List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> getAnswersForShortestPath() {
        return shortestPath.getTable();
    }

    public boolean checkAnswersOfShortestPath(List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> pairList) {
        boolean[] answers = new boolean[10];
        int count = 0;
        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> pairGenerated : getAnswersForShortestPath()) {
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

    public static void makeJDBCConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pdsa_cw", "root", "qwerty@123");
        if (connection != null) {
            System.out.println("Connection successful");
        } else {
            System.out.println("Failed to make connection");
        }
    }

    public static void addMinimumConnectorsAnswersDataToDB(List<Pair<Vertex, List<Pair<Vertex, Edge>>>> userAnswerList, String username) {
        String query = "INSERT INTO minimumconnectors(Vertex, ToVertex, Edge, Username) VALUES (?,?,?,?)";

        try {
            for (Pair<Vertex, List<Pair<Vertex, Edge>>> answerListPair : userAnswerList) {
                for (Pair<Vertex, Edge> answerEdgePair : answerListPair.getValue()) {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, answerListPair.getKey().getName());
                    preparedStatement.setString(2, answerEdgePair.getKey().getName());
                    preparedStatement.setInt(3, answerEdgePair.getValue().getWeight());
                    preparedStatement.setString(4, username);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addShortestPathAnswersDataToDB(List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> userAnswerList,
                                                      String username) {
        String query = "INSERT INTO shortestpath VALUES (?,?,?,?)";
        try {
            for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> answerListPair : userAnswerList) {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, answerListPair.getKey().getName());
                preparedStatement.setString(2, answerListPair.getValue().getKey().getName());
                preparedStatement.setInt(3, answerListPair.getValue().getValue().getKey());
                preparedStatement.setString(4, username);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMaximumProfitAnswersDataToDB(String maximumProfitAnswer, String username) {
        String query = "INSERT INTO maximumprofit VALUES (?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, maximumProfitAnswer);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
