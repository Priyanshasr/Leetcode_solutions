class Solution {
    public long[] resultArray(int[] nums, int k) {
        int n = nums.length;
        long[] ans = new long[k];

        long[] dp = new long[k];

        for (int num : nums) {
            long[] next = new long[k];

            int rem = num % k;

            // Subarray containing only the current element
            next[rem]++;

            // Extend previous subarrays
            for (int r = 0; r < k; r++) {
                if (dp[r] > 0) {
                    int newRem = (r * rem) % k;
                    next[newRem] += dp[r];
                }
            }

            dp = next;

            // Add subarrays ending here to the answer
            for (int r = 0; r < k; r++) {
                ans[r] += dp[r];
            }
        }

        return ans;
    }
}