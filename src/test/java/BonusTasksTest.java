import algorithms.*;
import graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BonusTasksTest {

    @Test
    void testTarjanMatchesKosarajuComponentsAndHalfScans() {
        Random rnd = new Random(100);

        for (int t = 0; t < 15; t++) {
            int n = 50 + rnd.nextInt(50);
            int m = n * 4;
            Graph g = new Graph(n, true);
            for (int i = 0; i < m; i++) {
                g.addEdge(rnd.nextInt(n), rnd.nextInt(n), 1 + rnd.nextInt(50));
            }

            SCCResult kosaraju = Kosaraju.findSCC(g);
            SCCResult tarjan = TarjanSCC.findSCC(g);

            // Число компонент должно строго совпадать
            assertEquals(kosaraju.getComponentCount(), tarjan.getComponentCount(),
                    "Число компонент Тарьяна должно быть идентично Косарайю");

            // Тарьян просматривает ровно m ребер (в один проход), а Косарайю - 2m (в два прохода)
            assertEquals(m, tarjan.edgeScans, "Тарьян делает ровно m edgeScans");
            assertEquals(2L * m, kosaraju.edgeScans, "Косарайю делает ровно 2m edgeScans");
        }
    }

    @Test
    void testTarjanStackSafetyOn100k() {
        int n = 100_000;
        Graph line = new Graph(n, true);
        for (int i = 0; i < n - 1; i++) {
            line.addEdge(i, i + 1, 1);
        }

        assertDoesNotThrow(() -> {
            SCCResult res = TarjanSCC.findSCC(line);
            assertEquals(n, res.getComponentCount());
            assertEquals(n, res.maxDepth);
        });
    }

    @Test
    void testCondensationGraphIsDAGAndFindsCriticalPath() {
        Graph cyclic = new Graph(6, true);
        // Цикл 1: 0 -> 1 -> 2 -> 0
        cyclic.addEdge(0, 1, 5);
        cyclic.addEdge(1, 2, 5);
        cyclic.addEdge(2, 0, 5);
        // Переход в цикл 2
        cyclic.addEdge(2, 3, 10);
        // Цикл 2: 3 -> 4 -> 3
        cyclic.addEdge(3, 4, 3);
        cyclic.addEdge(4, 3, 3);
        // Выход в вершину 5
        cyclic.addEdge(4, 5, 7);

        SCCResult scc = TarjanSCC.findSCC(cyclic);
        assertEquals(3, scc.getComponentCount(), "Должно быть ровно 3 компоненты конденсации");

        CondensationGraph condensation = new CondensationGraph(cyclic, scc);
        assertFalse(Kahn.sort(condensation.condensedDAG).hasCycle, "Граф конденсации обязан быть DAG");

        long criticalPath = condensation.findCriticalPathLength();
        assertEquals(17, criticalPath, "Критический путь: ребро (вес 10) + ребро (вес 7) = 17");
    }
}