package algorithms;

/**
 * Disjoint Set Union (DSU) with Union by Rank and Path Compression.
 */
public class DSU {
    private int[] parent;
    private int[] rank;
    private int numComponents;
    private long findCount;
    private long successfulUnionCount;

    public DSU(int n) {
        makeSet(n);
    }

    public void makeSet(int n) {
        parent = new int[n];
        rank = new int[n];
        numComponents = n;
        findCount = 0;
        successfulUnionCount = 0;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        findCount++;
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        numComponents--;
        successfulUnionCount++;
        return true;
    }

    public int components() {
        return numComponents;
    }

    public long getFindCount() {
        return findCount;
    }

    public long getSuccessfulUnionCount() {
        return successfulUnionCount;
    }

    public void resetCounters() {
        findCount = 0;
        successfulUnionCount = 0;
    }
}