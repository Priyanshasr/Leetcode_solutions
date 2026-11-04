
import java.util.*;

class Solution {

    private TreeMap<Integer, Integer> low = new TreeMap<>();
    private TreeMap<Integer, Integer> high = new TreeMap<>();

    private int lowSize = 0;
    private int highSize = 0;

    private long lowSum = 0;
    private long highSum = 0;

    private void addLow(int val) {
        low.put(val, low.getOrDefault(val, 0) + 1);
        lowSize++;
        lowSum += val;
    }

    private void addHigh(int val) {
        high.put(val, high.getOrDefault(val, 0) + 1);
        highSize++;
        highSum += val;
    }

    private void removeLow(int val) {
        int count = low.get(val);
        if (count == 1) {
            low.remove(val);
        } else {
            low.put(val, count - 1);
        }
        lowSize--;
        lowSum -= val;
    }

    private void removeHigh(int val) {
        int count = high.get(val);
        if (count == 1) {
            high.remove(val);
        } else {
            high.put(val, count - 1);
        }
        highSize--;
        highSum -= val;
    }

    private void rebalance() {
        int target = (lowSize + highSize + 1) / 2;

        while (lowSize > target) {
            int val = low.lastKey();
            removeLow(val);
            addHigh(val);
        }

        while (lowSize < target) {
            int val = high.firstKey();
            removeHigh(val);
            addLow(val);
        }
    }

    private void add(int val) {
        if (low.isEmpty() || val <= low.lastKey()) {
            addLow(val);
        } else {
            addHigh(val);
        }
        rebalance();
    }

    private void remove(int val) {
        if (low.containsKey(val)) {
            removeLow(val);
        } else {
            removeHigh(val);
        }
        rebalance();
    }

    private long getCost() {
        long median = low.lastKey();

        return median * lowSize - lowSum
             + highSum - median * highSize;
    }

    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;

        // Store the input midway as required.
        int[] maritovexi = nums;

        long[] cost = new long[n - x + 1];

        low.clear();
        high.clear();
        lowSize = highSize = 0;
        lowSum = highSum = 0;

        // Build the first window.
        for (int i = 0; i < x; i++) {
            add(maritovexi[i]);
        }

        cost[0] = getCost();

        // Calculate the cost of every sliding window.
        for (int i = x; i < n; i++) {
            remove(maritovexi[i - x]);
            add(maritovexi[i]);

            cost[i - x + 1] = getCost();
        }

        // Dynamic programming.
        long INF = Long.MAX_VALUE / 4;

        long[][] dp = new long[k + 1][n + 1];

        for (int i = 1; i <= k; i++) {
            Arrays.fill(dp[i], INF);
        }

        for (int i = 0; i <= n; i++) {
            dp[0][i] = 0;
        }

        for (int t = 1; t <= k; t++) {
            for (int i = 1; i <= n; i++) {

                // Skip the current position.
                dp[t][i] = dp[t][i - 1];

                // Select a window ending at position i - 1.
                if (i >= x && dp[t - 1][i - x] != INF) {

                    long take = dp[t - 1][i - x]
                              + cost[i - x];

                    dp[t][i] = Math.min(dp[t][i], take);
                }
            }
        }

        return dp[k][n];
    }
}
