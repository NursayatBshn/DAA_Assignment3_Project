import algorithms.DAGPathResult;
import algorithms.DAGShortestPath;

import graph.Edge;
import graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestPathTest {

    // Эталонный алгоритм Беллмана-Форда с виртуальным супер-источником для n <= 100
    private static long[] bellmanFordWithSuperSource(Graph g) {
        int n = g.getV();
        int[] inDegree = new int[n];
        for (Edge e : g.getAllEdges()) {
            inDegree[e.v]++;
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                dist[i] = 0;
            }
        }

        // V - 1 итераций релаксации всех рёбер
        for (int i = 0; i < n - 1; i++) {
            for (Edge e : g.getAllEdges()) {
                if (dist[e.u] != Long.MAX_VALUE) {
                    if (dist[e.u] + e.weight < dist[e.v]) {
                        dist[e.v] = dist[e.u] + e.weight;
                    }
                }
            }
        }
        return dist;
    }

    @Test
    void testShortestPathWithNegativeWeightsFile() throws IOException {
        Graph g = Graph.fromFile("data/graph_dag.txt");
        DAGPathResult res = DAGShortestPath.findShortestPaths(g);

        long[] expectedDist = bellmanFordWithSuperSource(g);
        assertArrayEquals(expectedDist, res.dist, "Расстояния должны совпадать с Беллманом-Фордом");

        // Проверяем восстановление пути до вершины 4
        List<Integer> path = res.getPath(4);
        assertFalse(path.isEmpty());
        assertEquals(4, path.get(path.size() - 1)); // конечная точка пути
        assertEquals(0, path.get(0)); // путь начался в истоке 0
    }

    @Test
    void testCrossCheckWithBellmanFordOnRandomDAGs() {
        Random rnd = new Random(42);

        for (int t = 0; t < 25; t++) {
            int n = 30 + rnd.nextInt(30);
            Graph g = new Graph(n, true);

            // Генерируем рёбра строго от меньшего индекса к большему (гарантия отсутствия циклов)
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (rnd.nextDouble() < 0.25) { // 25% вероятность ребра
                        // Допускаем отрицательные веса от -20 до +100
                        int weight = rnd.nextInt(121) - 20;
                        g.addEdge(i, j, weight);
                    }
                }
            }

            DAGPathResult dagRes = DAGShortestPath.findShortestPaths(g);
            long[] expectedDist = bellmanFordWithSuperSource(g);

            assertArrayEquals(expectedDist, dagRes.dist, "Кратчайшие расстояния в DAG должны совпадать с Беллманом-Фордом");
            assertEquals(g.getE(), dagRes.edgeScans, "В DAG число edgeScans должно быть ровно m");
        }
    }
}