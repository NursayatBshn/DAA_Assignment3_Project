package algorithms;

import graph.Edge;
import java.util.Collections;
import java.util.List;

public class MSTResult {
    public final long totalWeight;
    public final List<Edge> edges;
    public final int componentCount;
    public final long dsuOps;
    public final long edgeScans;
    public final long comparisons;

    public MSTResult(long totalWeight, List<Edge> edges, int componentCount, long dsuOps, long edgeScans, long comparisons) {
        this.totalWeight = totalWeight;
        this.edges = Collections.unmodifiableList(edges);
        this.componentCount = componentCount;
        this.dsuOps = dsuOps;
        this.edgeScans = edgeScans;
        this.comparisons = comparisons;
    }
}
