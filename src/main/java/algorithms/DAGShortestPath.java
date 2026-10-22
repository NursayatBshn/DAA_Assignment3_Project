package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.Arrays;
import java.util.List;

public class DAGShortestPath {

    public static DAGPathResult findShortestPaths(Graph graph) {
        int n = graph.getV();
        TopologicalSortResult topo = Kahn.sort(graph);
        if (topo.hasCycle) {
            throw new IllegalArgumentException("Graph contains a cycle, cannot compute DAG shortest paths.");
        }

        int[] inDegree = new int[n];
        for (Edge e : graph.getAllEdges()) {
            inDegree[e.v]++;
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        // Виртуальный супер-источник: вершины с in-degree 0 начинают с 0
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                dist[i] = 0;
            }
        }

        long edgeScans = 0;
        List<Integer> order = topo.order;

        for (int u : order) {
            if (dist[u] != Long.MAX_VALUE) {
                for (Edge edge : graph.getAdj(u)) {
                    edgeScans++;
                    int v = edge.v;
                    long newDist = dist[u] + edge.weight;
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        parent[v] = u;
                    }
                }
            } else {
                // Сканируем рёбра даже недостижимых вершин для подсчёта edgeScans
                for (Edge ignored : graph.getAdj(u)) {
                    edgeScans++;
                }
            }
        }

        return new DAGPathResult(dist, parent, edgeScans);
    }
}