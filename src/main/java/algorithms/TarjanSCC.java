package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TarjanSCC {

    public static SCCResult findSCC(Graph graph) {
        int n = graph.getV();
        int[] index = new int[n];
        int[] lowlink = new int[n];
        boolean[] onStack = new boolean[n];
        Arrays.fill(index, -1);

        int[] tarjanStack = new int[n];
        int tarjanTop = -1;

        List<List<Integer>> components = new ArrayList<>();
        long edgeScans = 0;
        int maxDepth = 0;
        int time = 0;

        // Итеративный стек вызовов: стек вершин u и указателей на текущее ребро edgePtr
        int[] callStack = new int[n];
        int[] edgePtr = new int[n];

        for (int i = 0; i < n; i++) {
            if (index[i] != -1) continue;

            int callTop = 0;
            callStack[callTop] = i;
            edgePtr[i] = 0;

            index[i] = time;
            lowlink[i] = time;
            time++;

            tarjanStack[++tarjanTop] = i;
            onStack[i] = true;

            while (callTop >= 0) {
                if (callTop + 1 > maxDepth) {
                    maxDepth = callTop + 1;
                }

                int u = callStack[callTop];
                List<Edge> adjU = graph.getAdj(u);

                if (edgePtr[u] < adjU.size()) {
                    Edge e = adjU.get(edgePtr[u]++);
                    edgeScans++;
                    int v = e.v;

                    if (index[v] == -1) {
                        // Прямой переход по дереву DFS к новой вершине v
                        index[v] = time;
                        lowlink[v] = time;
                        time++;

                        tarjanStack[++tarjanTop] = v;
                        onStack[v] = true;

                        edgePtr[v] = 0;
                        callTop++;
                        callStack[callTop] = v;
                    } else if (onStack[v]) {
                        // Обратное ребро к вершине текущего компонента
                        lowlink[u] = Math.min(lowlink[u], index[v]);
                    }
                } else {
                    // Возврат из вызова u
                    if (callTop > 0) {
                        int parent = callStack[callTop - 1];
                        lowlink[parent] = Math.min(lowlink[parent], lowlink[u]);
                    }

                    if (lowlink[u] == index[u]) {
                        List<Integer> scc = new ArrayList<>();
                        while (true) {
                            int w = tarjanStack[tarjanTop--];
                            onStack[w] = false;
                            scc.add(w);
                            if (w == u) break;
                        }
                        components.add(scc);
                    }
                    callTop--;
                }
            }
        }

        return new SCCResult(components, edgeScans, maxDepth);
    }
}