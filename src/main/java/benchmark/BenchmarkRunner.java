package benchmark;

import algorithms.*;
import graph.Graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class BenchmarkRunner {

    private static final int[] N_SIZES = {1_000, 3_162, 10_000, 31_623, 100_000};
    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        System.out.println("=== Starting Assignment 3 Graph Benchmark ===");
        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        File csvFile = new File("results/results.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            // Заголовок строго по ТЗ
            writer.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");

            for (int n : N_SIZES) {
                int[] mDensities = {4 * n, 20 * n};
                String[] densityNames = {"sparse", "dense"};

                for (int d = 0; d < 2; d++) {
                    int m = mDensities[d];
                    String density = densityNames[d];
                    System.out.printf("%n--- Benchmarking n=%d, m=%d (%s) ---%n", n, m, density);

                    // 1. Undirected graph for MST (Kruskal & Prim)
                    Graph undir = GraphGenerator.generateUndirected(n, m);
                    benchmarkMST(writer, undir, density, n, m);

                    // 2. Directed graph for Kosaraju
                    Graph dir = GraphGenerator.generateDirected(n, m);
                    benchmarkKosaraju(writer, dir, density, n, m);

                    // 3. DAG for Kahn and DAG Shortest Paths
                    Graph dag = GraphGenerator.generateDAG(n, m);
                    benchmarkDAG(writer, dag, density, n, m);
                }
            }
        }

        System.out.println("%n=== Benchmark successfully completed! Output saved to results/results.csv ===");
    }

    private static void benchmarkMST(PrintWriter writer, Graph g, String density, int n, int m) {
        // Kruskal
        double[] kruskalTimes = new double[RUNS];
        MSTResult kruskalRes = null;
        for (int r = 0; r < RUNS; r++) {
            long t0 = System.nanoTime();
            kruskalRes = Kruskal.findMST(g);
            kruskalTimes[r] = (System.nanoTime() - t0) / 1e6;
        }
        double kruskalTime = median(kruskalTimes);
        writeRow(writer, "Kruskal", density, n, m, kruskalTime,
                kruskalRes.dsuOps, kruskalRes.edgeScans, kruskalRes.comparisons, 0, kruskalRes.totalWeight);

        // Prim
        double[] primTimes = new double[RUNS];
        MSTResult primRes = null;
        for (int r = 0; r < RUNS; r++) {
            long t0 = System.nanoTime();
            primRes = Prim.findMST(g);
            primTimes[r] = (System.nanoTime() - t0) / 1e6;
        }
        double primTime = median(primTimes);
        writeRow(writer, "Prim", density, n, m, primTime,
                0, primRes.edgeScans, primRes.comparisons, 0, primRes.totalWeight);

        // Self-checking assertion
        if (kruskalRes.totalWeight != primRes.totalWeight) {
            throw new IllegalStateException(String.format("MST weights mismatch! Kruskal=%d, Prim=%d",
                    kruskalRes.totalWeight, primRes.totalWeight));
        }
        System.out.printf("  [MST] Agreement verified: weight = %d%n", kruskalRes.totalWeight);
    }

    private static void benchmarkKosaraju(PrintWriter writer, Graph g, String density, int n, int m) {
        double[] times = new double[RUNS];
        SCCResult res = null;
        for (int r = 0; r < RUNS; r++) {
            long t0 = System.nanoTime();
            res = Kosaraju.findSCC(g);
            times[r] = (System.nanoTime() - t0) / 1e6;
        }
        double medTime = median(times);

        // Self-checking assertion: Kosaraju scans exactly 2m edges
        if (res.edgeScans != 2L * m) {
            throw new IllegalStateException(String.format("Kosaraju edgeScans mismatch! Expected=%d, Actual=%d",
                    2L * m, res.edgeScans));
        }

        writeRow(writer, "Kosaraju", density, n, m, medTime,
                0, res.edgeScans, 0, res.maxDepth, res.getComponentCount());
        System.out.printf("  [Kosaraju] Verified: SCC count = %d, edgeScans = %d, maxDepth = %d%n",
                res.getComponentCount(), res.edgeScans, res.maxDepth);
    }

    private static void benchmarkDAG(PrintWriter writer, Graph g, String density, int n, int m) {
        // Kahn
        double[] kahnTimes = new double[RUNS];
        TopologicalSortResult kahnRes = null;
        for (int r = 0; r < RUNS; r++) {
            long t0 = System.nanoTime();
            kahnRes = Kahn.sort(g);
            kahnTimes[r] = (System.nanoTime() - t0) / 1e6;
        }
        double kahnTime = median(kahnTimes);

        // Self-checking assertion
        if (kahnRes.hasCycle || kahnRes.order.size() != n) {
            throw new IllegalStateException("Kahn failed to find topological order in DAG!");
        }
        if (kahnRes.edgeScans != m) {
            throw new IllegalStateException(String.format("Kahn edgeScans mismatch! Expected=%d, Actual=%d",
                    m, kahnRes.edgeScans));
        }

        writeRow(writer, "Kahn", density, n, m, kahnTime,
                0, kahnRes.edgeScans, 0, 0, kahnRes.order.size());
        System.out.printf("  [Kahn] Verified: ordered vertices = %d, edgeScans = %d%n",
                kahnRes.order.size(), kahnRes.edgeScans);

        // DAG Shortest Paths
        double[] pathTimes = new double[RUNS];
        DAGPathResult pathRes = null;
        for (int r = 0; r < RUNS; r++) {
            long t0 = System.nanoTime();
            pathRes = DAGShortestPath.findShortestPaths(g);
            pathTimes[r] = (System.nanoTime() - t0) / 1e6;
        }
        double pathTime = median(pathTimes);

        // Self-checking assertion: DAG relaxation scans exactly m edges
        if (pathRes.edgeScans != m) {
            throw new IllegalStateException(String.format("DAG Shortest Path edgeScans mismatch! Expected=%d, Actual=%d",
                    m, pathRes.edgeScans));
        }

        writeRow(writer, "DagShortestPath", density, n, m, pathTime,
                0, pathRes.edgeScans, 0, 0, pathRes.sumOfDistances);
        System.out.printf("  [DAG Paths] Verified: sum of distances = %d, edgeScans = %d%n",
                pathRes.sumOfDistances, pathRes.edgeScans);
    }

    private static void writeRow(PrintWriter writer, String algorithm, String graphDensity,
                                 int n, int m, double timeMs,
                                 long dsuOps, long edgeScans, long comparisons, int maxDepth, long result) {
        writer.printf("%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d%n",
                algorithm, graphDensity, n, m, timeMs, dsuOps, edgeScans, comparisons, maxDepth, result);
    }

    private static double median(double[] arr) {
        double[] copy = Arrays.copyOf(arr, arr.length);
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }
}