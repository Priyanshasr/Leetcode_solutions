
import java.util.*;

class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        List<Integer>[] tree = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            tree[parent[i]].add(i);
        }

        int[] ans = new int[n];
        int[] previous = new int[n];
        int[] last = new int[26];

        Arrays.fill(last, -1);
        Arrays.fill(ans, 1);

        // Iterative DFS: 0 means entering, 1 means exiting.
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{0, 0});

        while (!stack.isEmpty()) {
            int[] cur = stack.pop();
            int node = cur[0];
            int state = cur[1];
            int ch = s.charAt(node) - 'a';

            if (state == 0) {
                previous[node] = last[ch];
                last[ch] = node;

                stack.push(new int[]{node, 1});

                for (int child : tree[node]) {
                    stack.push(new int[]{child, 0});
                }
            } else {
                last[ch] = previous[node];

                int newParent = previous[node] != -1
                        ? previous[node]
                        : parent[node];

                if (newParent != -1) {
                    ans[newParent] += ans[node];
                }
            }
        }

        return ans;
    }
}
