package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DAGPathResult {
    public final long[] dist;
    public final int[] parent;
    public final long edgeScans;
    public final long sumOfDistances;

    public DAGPathResult(long[] dist, int[] parent, long edgeScans) {
        this.dist = dist;
        this.parent = parent;
        this.edgeScans = edgeScans;

        long sum = 0;
        for (long d : dist) {
            sum += d;
        }
        this.sumOfDistances = sum;
    }

    /**
     * Востанавливает путь от стартового истока до целефой вершины target
     */
    public List<Integer> getPath(int target) {
        if (dist[target] == Long.MAX_VALUE) {
            return Collections.emptyList();
        }
        List<Integer> path = new ArrayList<>();
        int curr = target;
        while (curr != -1) {
            path.add(curr);
            curr = parent[curr];
        }
        Collections.reverse(path);
        return path;
    }
}
