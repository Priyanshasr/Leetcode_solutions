
class Solution {
    private int m, n;
    private int[][] grid;

    public int closedIsland(int[][] grid) {
        this.grid = grid;
        m = grid.length;
        n = grid[0].length;

        int count = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0 && dfs(i, j)) {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean dfs(int r, int c) {
        if (r < 0 || r >= m || c < 0 || c >= n) {
            return false;
        }

        if (grid[r][c] == 1) {
            return true;
        }

        grid[r][c] = 1;

        boolean up = dfs(r - 1, c);
        boolean down = dfs(r + 1, c);
        boolean left = dfs(r, c - 1);
        boolean right = dfs(r, c + 1);

        return up && down && left && right;
    }
}
