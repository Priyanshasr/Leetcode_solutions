import java.util.*;

class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int n = nums1.length;
        long k = (long) k1 + k2;
        long[] diff = new long[n];
        long max = 0, sum = 0;

        for (int i = 0; i < n; i++) {
            diff[i] = Math.abs((long) nums1[i] - nums2[i]);
            max = Math.max(max, diff[i]);
            sum += diff[i];
        }

        if (sum <= k) return 0;

        Arrays.sort(diff);

        long level = max;
        long count = 0;

        for (int i = n - 1; i >= 0; i--) {
            count++;
            long next = i > 0 ? diff[i - 1] : 0;
            long cost = (level - next) * count;

            if (cost <= k) {
                k -= cost;
                level = next;
            } else {
                long reduce = k / count;
                long rem = k % count;
                level -= reduce;

                long ans = 0;
                for (int j = 0; j < n; j++) {
                    long d = Math.min(diff[j], level);
                    if (rem > 0 && diff[j] >= level && d == level) {
                        d--;
                        rem--;
                    }
                    ans += d * d;
                }
                return ans;
            }
        }

        return 0;
    }
}