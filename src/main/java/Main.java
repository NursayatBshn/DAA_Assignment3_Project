import algorithms.*;
import graph.Graph;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DAA Assignment 3: Quick Demo ===");
        try {
            // 1. Демонстрация MST на data/graph_disconnected.txt
            Graph gDisc = Graph.fromFile("data/graph_disconnected.txt");
            MSTResult mst = Kruskal.findMST(gDisc);
            System.out.printf("[MST Kruskal] Forest weight: %d, components: %d%n", 
                    mst.totalWeight, mst.componentCount);

            // 2. Демонстрация Kahn на data/graph_cyclic.txt
            Graph gCyclic = Graph.fromFile("data/graph_cyclic.txt");
            TopologicalSortResult topo = Kahn.sort(gCyclic);
            System.out.printf("[Kahn Topo] Cycle detected in cyclic graph: %b%n", topo.hasCycle);

            // 3. Демонстрация DAG Shortest Paths на data/graph_dag.txt
            Graph gDag = Graph.fromFile("data/graph_dag.txt");
            DAGPathResult paths = DAGShortestPath.findShortestPaths(gDag);
            System.out.printf("[DAG Paths] Sum of distances: %d, path to vertex 4: %s%n", 
                    paths.sumOfDistances, paths.getPath(4));

            System.out.println("\nTo run the full benchmark on 10 graphs, execute:");
            System.out.println("mvn compile && java -Xmx4g -cp target/classes benchmark.BenchmarkRunner");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}