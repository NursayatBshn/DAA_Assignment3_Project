package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {
    
    public static MSTResult findMST(Graph graph) {
        int n = graph.getV();
        DSU dsu = new DSU(n);
        List<Edge> mstEdges = new ArrayList<>();
        long totalWeight = 0;
        long edgeScans = 0;
        long[] comparisons = new long[1];

        List<Edge> edges = new ArrayList<>(graph.getAllEdges());
        edges.sort((e1,e2) -> {
            comparisons[0]++;
            return Integer.compare(e1.weight, e2.weight);
        });

        for (Edge edge : edges) {
            edgeScans++;
            if (dsu.find(edge.u) != dsu.find(edge.v)) {
                dsu.union(edge.u, edge.v);
                mstEdges.add(edge);
                totalWeight += edge.weight;
                if (mstEdges.size() == n - 1) {
                    break;
                }
            }
        }

        long dsuOps = dsu.getFindCount() + dsu.getSuccessfulUnionCount();
        return new MSTResult(totalWeight, mstEdges, dsu.components(), dsuOps, edgeScans, comparisons[0]);
    }
}
