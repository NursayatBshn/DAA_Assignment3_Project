package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class Kosaraju {

    public static SCCResult findSCC(Graph graph) {
        int n = graph.getV();
        long edgeScans = 0;
        int maxDepth = 0;

        // Первый проход: итеративный DFS для получения finish order
        boolean[] visited = new boolean[n];
        int[] finishOrder = new int[n];
        int finishIndex = 0;

        int[] stack = new int[n];
        int[] edgePtr = new int[n];

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;

            int top = 0;
            stack[top] = i;
            visited[i] = true;
            edgePtr[i] = 0;

            while (top >= 0) {
                if (top + 1 > maxDepth) {
                    maxDepth = top + 1;
                }

                int u = stack[top];
                List<Edge> adjU = graph.getAdj(u);

                if (edgePtr[u] < adjU.size()) {
                    Edge e = adjU.get(edgePtr[u]++);
                    edgeScans++;
                    int v = e.v;
                    if (!visited[v]) {
                        visited[v] = true;
                        edgePtr[v] = 0;
                        top++;
                        stack[top] = v;
                    }
                } else {
                    finishOrder[finishIndex++] = u;
                    top--;
                }
            }
        }

        // Построение транспонированного графа G^T
        List<List<Integer>> revAdj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            revAdj.add(new ArrayList<>());
        }
        for (Edge e : graph.getAllEdges()) {
            revAdj.get(e.v).add(e.u);
        }

        // Второй проход: итеративный DFS по G^T в порядке убывания времени выхода
        boolean[] revVisited = new boolean[n];
        List<List<Integer>> components = new ArrayList<>();

        for (int i = n - 1; i >= 0; i--) {
            int root = finishOrder[i];
            if (revVisited[root]) continue;

            List<Integer> currentSCC = new ArrayList<>();
            int top = 0;
            stack[top] = root;
            revVisited[root] = true;
            edgePtr[root] = 0;

            while (top >= 0) {
                if (top + 1 > maxDepth) {
                    maxDepth = top + 1;
                }

                int u = stack[top];
                List<Integer> adjU = revAdj.get(u);

                if (edgePtr[u] < adjU.size()) {
                    int v = adjU.get(edgePtr[u]++);
                    edgeScans++;
                    if (!revVisited[v]) {
                        revVisited[v] = true;
                        edgePtr[v] = 0;
                        top++;
                        stack[top] = v;
                    }
                } else {
                    currentSCC.add(u);
                    top--;
                }
            }
            components.add(currentSCC);
        }

        return new SCCResult(components, edgeScans, maxDepth);
    }
}