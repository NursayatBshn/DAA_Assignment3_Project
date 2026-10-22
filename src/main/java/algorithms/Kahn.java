package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Kahn {

    public static TopologicalSortResult sort(Graph graph) {
        int n = graph.getV();
        int[] inDegree = new int[n];
        long edgeScans = 0;

        for (Edge e : graph.getAllEdges()) {
            inDegree[e.v]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> order = new ArrayList<>(n);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);

            for (Edge edge : graph.getAdj(u)) {
                edgeScans++;
                inDegree[edge.v]--;
                if (inDegree[edge.v] == 0) {
                    queue.offer(edge.v);
                }
            }
        }

        boolean hasCycle = order.size() < n;
        return new TopologicalSortResult(hasCycle ? null : order, hasCycle, edgeScans);
    }
}