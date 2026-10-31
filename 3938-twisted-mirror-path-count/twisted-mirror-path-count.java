
class Solution {
    private static final int MOD = 1_000_000_007;

    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int[][] right = new int[m][n];
        int[][] down = new int[m][n];

        // Start at (0, 0)
        right[0][0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                int r = right[i][j];
                int d = down[i][j];

                if (grid[i][j] == 0) {

                    // Empty cell: choose right or down
                    int ways = (r + d) % MOD;

                    if (j + 1 < n) {
                        right[i][j + 1] =
                            (right[i][j + 1] + ways) % MOD;
                    }

                    if (i + 1 < m) {
                        down[i + 1][j] =
                            (down[i + 1][j] + ways) % MOD;
                    }

                } else {

                    // Mirror: right turns down
                    if (i + 1 < m) {
                        down[i + 1][j] =
                            (down[i + 1][j] + r) % MOD;
                    }

                    // Mirror: down turns right
                    if (j + 1 < n) {
                        right[i][j + 1] =
                            (right[i][j + 1] + d) % MOD;
                    }
                }
            }
        }

        return (right[m - 1][n - 1]
              + down[m - 1][n - 1]) % MOD;
    }
}
