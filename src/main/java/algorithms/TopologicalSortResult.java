package algorithms;

import java.util.Collections;
import java.util.List;

public class TopologicalSortResult {
    public final List<Integer> order;
    public final boolean hasCycle;
    public final long edgeScans;

    public TopologicalSortResult(List<Integer> order, boolean hasCycle, long edgeScans) {
        this.order = order != null ? Collections.unmodifiableList(order) : Collections.emptyList();
        this.hasCycle = hasCycle;
        this.edgeScans = edgeScans;
    }
}