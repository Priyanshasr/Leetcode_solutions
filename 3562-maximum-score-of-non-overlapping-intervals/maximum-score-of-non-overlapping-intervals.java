import java.util.*;

class Solution {

    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        // start, end, weight, original index
        int[][] arr = new int[n][4];

        for (int i = 0; i < n; i++) {
            arr[i][0] = intervals.get(i).get(0);
            arr[i][1] = intervals.get(i).get(1);
            arr[i][2] = intervals.get(i).get(2);
            arr[i][3] = i;
        }

        // Sort by end time
        Arrays.sort(arr, (a, b) -> {
            if (a[1] != b[1])
                return Integer.compare(a[1], b[1]);

            return Integer.compare(a[0], b[0]);
        });

        // dp[i][k] = best answer using first i intervals
        // with exactly k intervals selected
        List<Integer>[][] dp = new ArrayList[n + 1][5];
        long[][] value = new long[n + 1][5];

        for (int i = 0; i <= n; i++) {
            Arrays.fill(value[i], Long.MIN_VALUE);
        }

        value[0][0] = 0;
        dp[0][0] = new ArrayList<>();

        for (int i = 1; i <= n; i++) {

            // Option 1: Don't take current interval
            for (int k = 0; k <= 4; k++) {
                if (value[i - 1][k] != Long.MIN_VALUE) {
                    value[i][k] = value[i - 1][k];
                    dp[i][k] = new ArrayList<>(dp[i - 1][k]);
                }
            }

            // Find last non-overlapping interval
            int prev = findPrevious(arr, i - 1);

            // Option 2: Take current interval
            for (int k = 1; k <= 4; k++) {

                if (value[prev + 1][k - 1] == Long.MIN_VALUE)
                    continue;

                long newValue =
                        value[prev + 1][k - 1] + arr[i - 1][2];

                List<Integer> candidate =
                        new ArrayList<>(dp[prev + 1][k - 1]);

                candidate.add(arr[i - 1][3]);

                // IMPORTANT:
                // Sort original indices before lexicographical comparison
                Collections.sort(candidate);

                if (newValue > value[i][k] ||
                    (newValue == value[i][k] &&
                     smaller(candidate, dp[i][k]))) {

                    value[i][k] = newValue;
                    dp[i][k] = candidate;
                }
            }
        }

        // Find best answer among 0..4 intervals
        List<Integer> answer = new ArrayList<>();
        long best = Long.MIN_VALUE;

        for (int k = 0; k <= 4; k++) {

            if (value[n][k] == Long.MIN_VALUE)
                continue;

            if (value[n][k] > best ||
                (value[n][k] == best &&
                 smaller(dp[n][k], answer))) {

                best = value[n][k];
                answer = dp[n][k];
            }
        }

        return answer.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    // Binary search for the last interval whose end < current start
    private int findPrevious(int[][] arr, int index) {

        int low = 0;
        int high = index - 1;
        int ans = -1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            if (arr[mid][1] < arr[index][0]) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return ans;
    }

    // Lexicographical comparison
    private boolean smaller(List<Integer> a, List<Integer> b) {

        if (a == null)
            return false;

        if (b == null)
            return true;

        int len = Math.min(a.size(), b.size());

        for (int i = 0; i < len; i++) {

            if (!a.get(i).equals(b.get(i))) {
                return a.get(i) < b.get(i);
            }
        }

        return a.size() < b.size();
    }
}