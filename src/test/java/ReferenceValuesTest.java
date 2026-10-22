import algorithms.*;
import benchmark.GraphGenerator;
import graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReferenceValuesTest {

    @Test
    void testReferenceValuesSmallSparse() {
        int n = 1000;
        int m = 4000;

        // 1. Undirected graph -> MST
        Graph undir = GraphGenerator.generateUndirected(n, m);
        MSTResult kruskal = Kruskal.findMST(undir);
        MSTResult prim = Prim.findMST(undir);
        assertEquals(147_970, kruskal.totalWeight, "Kruskal MST weight must match Table 7");
        assertEquals(147_970, prim.totalWeight, "Prim MST weight must match Table 7");

        // 2. Directed graph -> SCC
        Graph dir = GraphGenerator.generateDirected(n, m);
        SCCResult scc = Kosaraju.findSCC(dir);
        assertEquals(27, scc.getComponentCount(), "Kosaraju SCC count must match Table 7");
        assertEquals(2L * m, scc.edgeScans, "Kosaraju must scan exactly 2m edges");

        // 3. DAG -> Kahn & Shortest Paths
        Graph dag = GraphGenerator.generateDAG(n, m);
        TopologicalSortResult topo = Kahn.sort(dag);
        assertEquals(m, topo.edgeScans, "Kahn must scan exactly m edges");
        assertEquals(n, topo.order.size(), "All vertices must be topologically ordered");

        DAGPathResult dagPaths = DAGShortestPath.findShortestPaths(dag);
        assertEquals(m, dagPaths.edgeScans, "DAG relaxation must scan exactly m edges");
        assertEquals(396_274, dagPaths.sumOfDistances, "Sum of DAG distances must match Table 7");
    }

    @Test
    void testReferenceValuesSmallDense() {
        int n = 1000;
        int m = 20_000;

        Graph undir = GraphGenerator.generateUndirected(n, m);
        assertEquals(30_363, Kruskal.findMST(undir).totalWeight);
        assertEquals(30_363, Prim.findMST(undir).totalWeight);

        Graph dir = GraphGenerator.generateDirected(n, m);
        assertEquals(1, Kosaraju.findSCC(dir).getComponentCount());

        Graph dag = GraphGenerator.generateDAG(n, m);
        assertEquals(252_411, DAGShortestPath.findShortestPaths(dag).sumOfDistances);
    }
}