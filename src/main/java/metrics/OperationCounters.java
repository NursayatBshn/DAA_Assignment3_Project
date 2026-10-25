package metrics;

/**
 * Encapsulates the four core algorithmic counters required by Assignment 3:
 * - dsuOps: find and union calls
 * - edgeScans: edges inspected during traversals and relaxations
 * - comparisons: weight comparisons performed in priority queues and sorting
 * - maxDepth: deepest level reached by the explicit DFS stack
 */
public class OperationCounters {
    public long dsuOps;
    public long edgeScans;
    public long comparisons;
    public int maxDepth;

    public OperationCounters() {
        reset();
    }

    public OperationCounters(long dsuOps, long edgeScans, long comparisons, int maxDepth) {
        this.dsuOps = dsuOps;
        this.edgeScans = edgeScans;
        this.comparisons = comparisons;
        this.maxDepth = maxDepth;
    }

    public void reset() {
        this.dsuOps = 0;
        this.edgeScans = 0;
        this.comparisons = 0;
        this.maxDepth = 0;
    }

    public void addDsuOp() {
        this.dsuOps++;
    }

    public void addEdgeScan() {
        this.edgeScans++;
    }

    public void addComparison() {
        this.comparisons++;
    }

    public void updateMaxDepth(int depth) {
        if (depth > this.maxDepth) {
            this.maxDepth = depth;
        }
    }

    @Override
    public String toString() {
        return String.format("dsuOps=%d, edgeScans=%d, comparisons=%d, maxDepth=%d",
                dsuOps, edgeScans, comparisons, maxDepth);
    }
}