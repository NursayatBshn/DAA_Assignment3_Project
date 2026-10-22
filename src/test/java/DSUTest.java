import algorithms.DSU;
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {

    // Наивная эталонная реализация для кросс-тестирования
    static class NaiveDSU {
        private final int[] id;
        private int count;

        public NaiveDSU(int n) {
            id = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
            }
            count = n;
        }

        public int find(int x) {
            return id[x];
        }

        public boolean union(int x, int y) {
            int idX = find(x);
            int idY = find(y);
            if (idX == idY) return false;
            for (int i = 0; i < id.length; i++) {
                if (id[i] == idY) {
                    id[i] = idX;
                }
            }
            count--;
            return true;
        }

        public int components() {
            return count;
        }
    }

    @Test
    void testBasicOperations() {
        DSU dsu = new DSU(5);
        assertEquals(5, dsu.components());

        assertTrue(dsu.union(0, 1));
        assertEquals(4, dsu.components());
        assertEquals(dsu.find(0), dsu.find(1));

        assertFalse(dsu.union(0, 1)); // Повторное объединение
        assertEquals(4, dsu.components());

        assertTrue(dsu.union(2, 3));
        assertTrue(dsu.union(1, 3));
        assertEquals(2, dsu.components());
        assertEquals(dsu.find(0), dsu.find(2));
    }

    @Test
    void testCrossCheckWithNaiveRandomUnions() {
        int n = 500;
        int operations = 2000;
        DSU dsu = new DSU(n);
        NaiveDSU naive = new NaiveDSU(n);
        Random rnd = new Random(1337);

        for (int i = 0; i < operations; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            boolean dsuResult = dsu.union(u, v);
            boolean naiveResult = naive.union(u, v);

            assertEquals(naiveResult, dsuResult, "Результат union должен совпадать с эталоном");
            assertEquals(naive.components(), dsu.components(), "Число компонент должно совпадать");
        }

        // Проверяем попарную связность всех пар после серии объединений
        for (int i = 0; i < 200; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            boolean dsuSame = (dsu.find(u) == dsu.find(v));
            boolean naiveSame = (naive.find(u) == naive.find(v));
            assertEquals(naiveSame, dsuSame, "Связность пар вершин должна совпадать");
        }

        assertTrue(dsu.getFindCount() > 0, "Счетчик find должен расти");
        assertTrue(dsu.getSuccessfulUnionCount() > 0, "Счетчик успешных union должен расти");
    }
}