package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Universal Graph representation using Adjacency Lists.
 * Supports directed and undirected graphs, self-loops, and parallel edges.
 */
public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> allEdges;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.adj.add(new ArrayList<>());
        }
        this.allEdges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int weight) {
        if (u < 0 || u >= n || v < 0 || v >= n) {
            throw new IndexOutOfBoundsException("Vertex index out of range 0.." + (n - 1));
        }
        Edge edge = new Edge(u, v, weight);
        adj.get(u).add(edge);
        allEdges.add(edge);

        if (!directed && u != v) {
            // Для неориентированного графа добавляем обратное ребро в список смежности
            adj.get(v).add(new Edge(v, u, weight));
        }
    }

    public int getV() {
        return n;
    }

    public int getE() {
        return allEdges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getAdj(int u) {
        return adj.get(u);
    }

    public List<Edge> getAllEdges() {
        return Collections.unmodifiableList(allEdges);
    }

    /**
     * Reads graph from file. Format:
     * n m directed|undirected
     * u v w (m lines)
     */
    public static Graph fromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();
            if (firstLine == null || firstLine.trim().isEmpty()) {
                throw new IllegalArgumentException("File is empty: " + filePath);
            }

            String[] header = firstLine.trim().split("\\s+");
            int n = Integer.parseInt(header[0]);
            int m = Integer.parseInt(header[1]);
            String type = header[2].toLowerCase();
            boolean directed = type.equals("directed");

            Graph graph = new Graph(n, directed);
            for (int i = 0; i < m; i++) {
                String line = reader.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                int w = Integer.parseInt(parts[2]);
                graph.addEdge(u, v, w);
            }
            return graph;
        }
    }
}