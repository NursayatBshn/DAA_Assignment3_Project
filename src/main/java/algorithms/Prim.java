package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    
    public static MSTResult findMst(Graph graph) {
        int n = graph.getV();
        boolean[] inMST = new boolean[n];
        List<Edge> mstEdges = new ArrayList<>();
        long totalWeight = 0;
        long edgeScans = 0;
        long[] comparisons = new long[1];
        int components = 0;

        PriorityQueue<Edge> pq = new PriorityQueue<>((e1, e2) -> {
            comparisons[0]++;
            return Integer.compare(e1.weight, e2.weight);
        });

        for (int startVertex = 0; startVertex < n; startVertex++) {
            if (inMST[startVertex]) {
                continue;
            }

            components++;
            inMST[startVertex] = true;
            for (Edge edge : graph.getAdj(startVertex)) {
                edgeScans++;
                pq.offer(edge);
            }

            while (!pq.isEmpty()) {
                Edge minEdge = pq.poll();
                int nextVertex = minEdge.v;

                if (inMST[nextVertex]) {
                    continue;
                }

                inMST[nextVertex] = true;
                mstEdges.add(minEdge);
                totalWeight += minEdge.weight;

                for (Edge edge : graph.getAdj(nextVertex)) {
                    edgeScans++;
                    if(!inMST[edge.v]) {
                        pq.offer(edge);
                    }
                }
            }
        }
        return new MSTResult(totalWeight, mstEdges, components, 0, edgeScans, comparisons[0]);
    }
}
