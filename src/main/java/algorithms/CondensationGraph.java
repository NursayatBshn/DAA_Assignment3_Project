package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.*;

public class CondensationGraph {
    public final Graph condensedDAG;
    public final int[] vertexToSCC;
    public final List<List<Integer>> sccList;

    public CondensationGraph(Graph originalGraph, SCCResult sccResult) {
        this.sccList = sccResult.components;
        int numSCC = sccList.size();
        this.vertexToSCC = new int[originalGraph.getV()];

        for (int sccId = 0; sccId < numSCC; sccId++) {
            for (int v : sccList.get(sccId)) {
                vertexToSCC[v] = sccId;
            }
        }

        this.condensedDAG = new Graph(numSCC, true);
        Set<Long> addedEdges = new HashSet<>();

        for (Edge e : originalGraph.getAllEdges()) {
            int sccU = vertexToSCC[e.u];
            int sccV = vertexToSCC[e.v];
            if (sccU != sccV) {
                long edgeKey = (((long) sccU) << 32) | (sccV & 0xFFFFFFFFL);
                if (!addedEdges.contains(edgeKey)) {
                    addedEdges.add(edgeKey);
                    // Вес ребра можно брать из исходного ребра
                    condensedDAG.addEdge(sccU, sccV, e.weight);
                }
            }
        }
    }

    /**
     * Находит критический (самый длинный) путь в графе конденсации.
     * Релаксация рёбер в топологическом порядке.
     */
    public long findCriticalPathLength() {
        TopologicalSortResult topo = Kahn.sort(condensedDAG);
        if (topo.hasCycle) {
            throw new IllegalStateException("Condensation graph must be a DAG!");
        }

        int n = condensedDAG.getV();
        long[] maxDist = new long[n];
        int[] inDegree = new int[n];
        for (Edge e : condensedDAG.getAllEdges()) {
            inDegree[e.v]++;
        }

        // Вершины с inDegree == 0 начинают с длины 0
        long criticalPath = 0;
        for (int u : topo.order) {
            for (Edge e : condensedDAG.getAdj(u)) {
                int v = e.v;
                if (maxDist[u] + e.weight > maxDist[v]) {
                    maxDist[v] = maxDist[u] + e.weight;
                    if (maxDist[v] > criticalPath) {
                        criticalPath = maxDist[v];
                    }
                }
            }
        }
        return criticalPath;
    }
}