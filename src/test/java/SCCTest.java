import algorithms.Kahn;
import algorithms.Kosaraju;
import algorithms.SCCResult;
import algorithms.TopologicalSortResult;
import graph.Edge;
import graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    // Brute Force SCC для кросс-тестирования на n <= 200
    private static int bruteForceSCCCount(Graph g) {
        int n = g.getV();
        boolean[][] reachable = new boolean[n][n];

        for (int start = 0; start < n; start++) {
            Queue<Integer> q = new ArrayDeque<>();
            q.offer(start);
            reachable[start][start] = true;

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : g.getAdj(u)) {
                    if (!reachable[start][e.v]) {
                        reachable[start][e.v] = true;
                        q.offer(e.v);
                    }
                }
            }
        }

        boolean[] assigned = new boolean[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (!assigned[i]) {
                count++;
                assigned[i] = true;
                for (int j = i + 1; j < n; j++) {
                    if (reachable[i][j] && reachable[j][i]) {
                        assigned[j] = true;
                    }
                }
            }
        }
        return count;
    }

    @Test
    void testKosarajuMatchesBruteForceOnRandomGraphs() {
        Random rnd = new Random(42);
        for (int t = 0; t < 20; t++) {
            int n = 40 + rnd.nextInt(40);
            Graph g = new Graph(n, true);
            int edges = n * 3;
            for (int i = 0; i < edges; i++) {
                g.addEdge(rnd.nextInt(n), rnd.nextInt(n), 1);
            }

            SCCResult res = Kosaraju.findSCC(g);
            int expected = bruteForceSCCCount(g);
            assertEquals(expected, res.getComponentCount(), "Число SCC должно совпадать с Brute Force");
        }
    }

    @Test
    void testStackSafetyOn100kVerticesPath() {
        int n = 100_000;
        Graph line = new Graph(n, true);
        for (int i = 0; i < n - 1; i++) {
            line.addEdge(i, i + 1, 1);
        }

        assertDoesNotThrow(() -> {
            SCCResult res = Kosaraju.findSCC(line);
            assertEquals(n, res.getComponentCount(), "В направленной цепочке ровно n компонент");
            assertEquals(n, res.maxDepth, "Глубина стека равна n");
        });
    }

    @Test
    void testKahnValidOrderAndCycleDetection() throws IOException {
        Graph dag = Graph.fromFile("data/graph_dag.txt");
        TopologicalSortResult dagRes = Kahn.sort(dag);
        assertFalse(dagRes.hasCycle, "В data/graph_dag.txt нет циклов");
        assertNotNull(dagRes.order);
        assertEquals(dag.getV(), dagRes.order.size());

        // Проверяем свойство: pos[u] < pos[v] для каждого ребра u -> v
        int[] pos = new int[dag.getV()];
        for (int i = 0; i < dagRes.order.size(); i++) {
            pos[dagRes.order.get(i)] = i;
        }
        for (Edge e : dag.getAllEdges()) {
            assertTrue(pos[e.u] < pos[e.v], "pos[u] должно быть меньше pos[v]");
        }

        Graph cyclic = Graph.fromFile("data/graph_cyclic.txt");
        TopologicalSortResult cyclicRes = Kahn.sort(cyclic);
        assertTrue(cyclicRes.hasCycle, "data/graph_cyclic.txt содержит цикл");
        assertTrue(cyclicRes.order.isEmpty());
    }
}