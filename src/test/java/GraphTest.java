import graph.Graph;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void testFileParsing() throws IOException {
        Graph g = Graph.fromFile("data/graph_disconnected.txt");
        assertEquals(6, g.getV());
        assertEquals(4, g.getE());
        assertFalse(g.isDirected());

        Graph cyclic = Graph.fromFile("data/graph_cyclic.txt");
        assertEquals(4, cyclic.getV());
        assertEquals(4, cyclic.getE());
        assertTrue(cyclic.isDirected());
    }

    @Test
    void testSelfLoopsAndParallelEdges() {
        Graph g = new Graph(3, false);
        // Петля (self-loop)
        assertDoesNotThrow(() -> g.addEdge(0, 0, 5));
        // Параллельные ребра
        assertDoesNotThrow(() -> g.addEdge(0, 1, 10));
        assertDoesNotThrow(() -> g.addEdge(0, 1, 20));

        assertEquals(3, g.getE());
        assertEquals(3, g.getAdj(0).size()); // 1 петля + 2 параллельных ребра
    }
}