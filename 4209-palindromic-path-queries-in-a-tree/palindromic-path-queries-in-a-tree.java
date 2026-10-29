
import java.util.*;

class Solution {
    public List<Boolean> palindromePath(
            int n, int[][] edges, String s, String[] queries) {

        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        int LOG = 1;
        while ((1 << LOG) <= n) {
            LOG++;
        }

        int[][] up = new int[LOG][n];
        int[] depth = new int[n];
        int[] tin = new int[n];
        int[] tout = new int[n];
        int[] parent = new int[n];
        int[] iter = new int[n];
        int[] stack = new int[n];

        Arrays.fill(parent, -1);

        int top = 0;
        int timer = 0;

        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[top - 1];

            if (iter[u] == 0) {
                tin[u] = timer++;

                up[0][u] = parent[u];
                for (int j = 1; j < LOG; j++) {
                    up[j][u] = up[j - 1][up[j - 1][u]];
                }
            }

            if (iter[u] < graph[u].size()) {
                int v = graph[u].get(iter[u]++);

                if (v == parent[u]) {
                    continue;
                }

                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            } else {
                tout[u] = timer - 1;
                top--;
            }
        }

        Fenwick bit = new Fenwick(n);
        char[] chars = s.toCharArray();

        // Each character update affects the root-prefix masks
        // of every node in that node's subtree.
        for (int u = 0; u < n; u++) {
            int mask = 1 << (chars[u] - 'a');
            bit.rangeXor(tin[u], tout[u], mask);
        }

        List<Boolean> answer = new ArrayList<>();

        for (String query : queries) {
            String[] parts = query.split(" ");

            if (parts[0].equals("update")) {
                int u = Integer.parseInt(parts[1]);
                char c = parts[2].charAt(0);

                if (chars[u] == c) {
                    continue;
                }

                int oldMask = 1 << (chars[u] - 'a');
                int newMask = 1 << (c - 'a');
                int delta = oldMask ^ newMask;

                bit.rangeXor(tin[u], tout[u], delta);
                chars[u] = c;

            } else {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);

                int ancestor = lca(u, v, up, depth);

                int mask = bit.query(tin[u])
                         ^ bit.query(tin[v])
                         ^ (1 << (chars[ancestor] - 'a'));

                answer.add((mask & (mask - 1)) == 0);
            }
        }

        return answer;
    }

    private int lca(int u, int v, int[][] up, int[] depth) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        int LOG = up.length;
        int diff = depth[u] - depth[v];

        for (int j = 0; j < LOG; j++) {
            if ((diff & (1 << j)) != 0) {
                u = up[j][u];
            }
        }

        if (u == v) {
            return u;
        }

        for (int j = LOG - 1; j >= 0; j--) {
            if (up[j][u] != up[j][v]) {
                u = up[j][u];
                v = up[j][v];
            }
        }

        return up[0][u];
    }

    static class Fenwick {
        private final int[] bit;
        private final int n;

        Fenwick(int n) {
            this.n = n;
            bit = new int[n + 2];
        }

        private void update(int index, int value) {
            for (int i = index + 1; i <= n + 1; i += i & -i) {
                bit[i] ^= value;
            }
        }

        void rangeXor(int left, int right, int value) {
            update(left, value);
            update(right + 1, value);
        }

        int query(int index) {
            int result = 0;

            for (int i = index + 1; i > 0; i -= i & -i) {
                result ^= bit[i];
            }

            return result;
        }
    }
}
