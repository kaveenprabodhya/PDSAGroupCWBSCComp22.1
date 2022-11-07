package bsccomp.group.common;

public class Graph {
    private String vertex;
    private int weight;

    public Graph() {
    }

    public Graph(String vertex, int weight) {
        this.vertex = vertex;
        this.weight = weight;
    }

    public String getVertex() {
        return vertex;
    }

    public void setVertex(String vertex) {
        this.vertex = vertex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
