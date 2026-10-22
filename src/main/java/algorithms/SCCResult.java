package algorithms;

import java.util.Collections;
import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;
    public final long edgeScans;
    public final int maxDepth;

    public SCCResult(List<List<Integer>> components, long edgeScan, int maxDepth) {
        this.components = Collections.unmodifiableList(components);
        this.edgeScans = edgeScan;
        this.maxDepth = maxDepth;
    }

    public int getComponentCount() {
        return components.size();
    }
}
