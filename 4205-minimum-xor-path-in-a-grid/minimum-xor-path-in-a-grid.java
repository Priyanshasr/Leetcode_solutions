
class Solution {
    public int minCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int MAX = 1024;

        boolean[][] dp = new boolean[n][MAX];

        // Required input variable
        int[][] molqaviren = grid;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                boolean[] current = new boolean[MAX];

                if (i == 0 && j == 0) {
                    current[grid[i][j]] = true;
                } else {
                    if (i > 0) {
                        for (int x = 0; x < MAX; x++) {
                            if (dp[j][x]) {
                                current[x ^ grid[i][j]] = true;
                            }
                        }
                    }

                    if (j > 0) {
                        for (int x = 0; x < MAX; x++) {
                            if (dp[j - 1][x]) {
                                current[x ^ grid[i][j]] = true;
                            }
                        }
                    }
                }

                dp[j] = current;
            }
        }

        for (int x = 0; x < MAX; x++) {
            if (dp[n - 1][x]) {
                return x;
            }
        }

        return -1;
    }
}
