import algorithms.Kruskal;
import algorithms.MSTResult;
import algorithms.Prim;
import graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {
    
    @Test 
    void testDisconnectGraphForest() throws IOException {
        Graph g = Graph.fromFile("data/graph_disconnected.txt");
        MSTResult kruskalRes = Kruskal.findMST(g);
        MSTResult primRes = Prim.findMST(g);

        assertEquals(kruskalRes.totalWeight, primRes.totalWeight, "Все остовного леса должны совпадать");
        assertEquals(2, kruskalRes.componentCount, "В graph_disconnected ровно 2 коспоненты связности");
        assertEquals(2, primRes.componentCount, "В graph_disconnected ровно 2 компоненты связности");
        assertEquals(4, kruskalRes.edges.size(), "Лес из 6 вершин и 2 компонент должен содержать 4 ребра");
    }

    @Test 
    void testKruskalAndPrimAgreementOn100RandomGraph() {
        Random rnd = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 20 + rnd.nextInt(30);
            Graph g = new Graph(n, false);

            for (int i = 1; i < n; i++) {
                int p = rnd.nextInt(i);
                int w = 1 +rnd.nextInt(100);
                g.addEdge(p, i, w);
            }

            int extraEdge = 2 * n;
            for (int e = 0; e < extraEdge; e++) {
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                if (u != v) {
                    g.addEdge(u, v, 1 + rnd.nextInt(100));
                }
            }

            MSTResult kruskalRes = Kruskal.findMST(g);
            MSTResult primRes = Prim.findMST(g);

            assertEquals(kruskalRes.totalWeight, primRes.totalWeight, "Веса MST Kruskal и Prim должны строго совпадать");
            assertEquals(n - 1, kruskalRes.edges.size());
            assertEquals(n - 1, primRes.edges.size());
            assertEquals(1, kruskalRes.componentCount);
            assertEquals(1, primRes.componentCount);
        }
    }
}
