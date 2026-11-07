
import java.util.*;

class Solution {
    public int[] assignTasks(int[] servers, int[] tasks) {
        PriorityQueue<int[]> idle = new PriorityQueue<>(
            (a, b) -> {
                if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
                return Integer.compare(a[1], b[1]);
            }
        );

        PriorityQueue<int[]> busy = new PriorityQueue<>(
            (a, b) -> {
                if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
                if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
                return Integer.compare(a[2], b[2]);
            }
        );

        int n = servers.length;
        int m = tasks.length;
        int[] ans = new int[m];

        for (int i = 0; i < n; i++) {
            idle.offer(new int[]{servers[i], i});
        }

        for (int j = 0; j < m; j++) {

            // Move completed servers back to idle.
            while (!busy.isEmpty() && busy.peek()[0] <= j) {
                int[] s = busy.poll();
                idle.offer(new int[]{s[1], s[2]});
            }

            if (!idle.isEmpty()) {
                int[] s = idle.poll();

                int weight = s[0];
                int index = s[1];

                ans[j] = index;
                busy.offer(new int[]{j + tasks[j], weight, index});

            } else {
                // Wait for the earliest server to become free.
                int[] s = busy.poll();

                int finishTime = s[0];
                int weight = s[1];
                int index = s[2];

                ans[j] = index;
                busy.offer(new int[]{
                    finishTime + tasks[j], weight, index
                });
            }
        }

        return ans;
    }
}
