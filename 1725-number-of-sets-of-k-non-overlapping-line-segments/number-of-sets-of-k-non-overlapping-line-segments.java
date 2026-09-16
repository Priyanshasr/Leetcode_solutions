class Solution {
    private static final int MOD = 1_000_000_007;

    public int numberOfSets(int n, int k) {
        int[][] f = new int[n + 1][k + 1];
        int[][] g = new int[n + 1][k + 1];

        f[1][0] = 1;

        for (int i = 2; i <= n; i++) {
            for (int j = 0; j <= k; j++) {

                // Not drawing a segment at point i
                f[i][j] = (f[i - 1][j] + g[i - 1][j]) % MOD;

                // Continue an existing segment
                g[i][j] = g[i - 1][j];

                if (j > 0) {
                    // Start a new segment
                    g[i][j] = (g[i][j] + f[i - 1][j - 1]) % MOD;

                    // Continue/extend a segment
                    g[i][j] = (g[i][j] + g[i - 1][j - 1]) % MOD;
                }
            }
        }

        return (f[n][k] + g[n][k]) % MOD;
    }
}