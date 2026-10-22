package benchmark;

import graph.Graph;

import java.util.Random;

/**
 * Deterministic Graph Generator matching Assignment 3 specifications.
 * Uses new Random(42) for every generated graph.
 */
public class GraphGenerator {

    /**
     * Undirected connected graph for MST (Kruskal & Prim).
     */
    public static Graph generateUndirected(int n, int m) {
        Graph g = new Graph(n, false);
        Random rnd = new Random(42);

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }

        // Shuffle perm
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }

        // Random spanning tree to ensure connectivity
        for (int i = 1; i < n; i++) {
            int p = perm[rnd.nextInt(i)];
            int c = perm[i];
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(p, c, w);
        }

        // Remaining edges: a = rnd.nextInt(n), b = rnd.nextInt(n) with b != a
        for (int i = n - 1; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) {
                b = rnd.nextInt(n);
            }
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(a, b, w);
        }

        return g;
    }

    /**
     * Directed random graph for SCC (Kosaraju / Tarjan).
     */
    public static Graph generateDirected(int n, int m) {
        Graph g = new Graph(n, true);
        Random rnd = new Random(42);

        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) {
                b = rnd.nextInt(n);
            }
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(a, b, w);
        }

        return g;
    }

    /**
     * Directed Acyclic Graph (DAG) for Kahn and DAG Shortest Paths.
     */
    public static Graph generateDAG(int n, int m) {
        Graph g = new Graph(n, true);
        Random rnd = new Random(42);

        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[i] = i;
        }

        // Shuffle rank
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = rank[i];
            rank[i] = rank[j];
            rank[j] = tmp;
        }

        // For every edge, orient from lower rank to higher rank
        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) {
                b = rnd.nextInt(n);
            }
            if (rank[a] > rank[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(a, b, w);
        }

        return g;
    }
}