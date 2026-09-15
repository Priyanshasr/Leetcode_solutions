class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];

        // dp[i][j] = true if s[i...j] is a palindrome
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) == s.charAt(j) &&
                    (j - i <= 2 || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                }
            }
        }

        // dp2[i] = maximum palindromes possible in s[0...i-1]
        int[] dp2 = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            dp2[i] = dp2[i - 1];

            for (int j = 0; j < i; j++) {
                if (i - j >= k && dp[j][i - 1]) {
                    dp2[i] = Math.max(dp2[i], dp2[j] + 1);
                }
            }
        }

        return dp2[n];
    }
}