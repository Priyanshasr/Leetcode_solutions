import java.util.*;

class Solution {
    public int minSumOfLengths(int[] arr, int target) {
        int n = arr.length;
        int INF = n + 1;

        // best[i] = minimum length of a valid subarray
        // completely inside indices [0 ... i-1]
        int[] best = new int[n + 1];
        Arrays.fill(best, INF);

        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);

        int prefix = 0;
        int ans = INF;

        for (int i = 1; i <= n; i++) {
            prefix += arr[i - 1];

            // Don't use the current subarray
            best[i] = best[i - 1];

            if (map.containsKey(prefix - target)) {
                int start = map.get(prefix - target);

                // Current subarray is [start, i-1]
                int len = i - start;

                // Previous subarray must end before 'start'
                if (best[start] != INF) {
                    ans = Math.min(ans, len + best[start]);
                }

                // This current subarray may become the best one
                best[i] = Math.min(best[i], len);
            }

            map.put(prefix, i);
        }

        return ans == INF ? -1 : ans;
    }
}