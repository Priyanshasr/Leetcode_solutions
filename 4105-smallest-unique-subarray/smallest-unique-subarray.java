
class Solution {
    public int smallestUniqueSubarray(int[] nums) {
        int n = nums.length;

        int[] sa = buildSuffixArray(nums);
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        // lcp[r] = LCP of suffixes sa[r] and sa[r - 1]
        int[] lcp = new int[n];
        int h = 0;

        for (int i = 0; i < n; i++) {
            int r = rank[i];

            if (r == 0) {
                h = 0;
                continue;
            }

            int j = sa[r - 1];

            while (i + h < n && j + h < n
                    && nums[i + h] == nums[j + h]) {
                h++;
            }

            lcp[r] = h;

            if (h > 0) {
                h--;
            }
        }

        int answer = n;

        for (int r = 0; r < n; r++) {
            int longestCommonPrefix = lcp[r];

            // LCP with the next suffix
            if (r + 1 < n) {
                longestCommonPrefix = Math.max(
                    longestCommonPrefix, lcp[r + 1]
                );
            }

            int suffixLength = n - sa[r];

            // A unique prefix must be longer than every LCP
            // and must fit inside this suffix.
            if (longestCommonPrefix < suffixLength) {
                answer = Math.min(
                    answer, longestCommonPrefix + 1
                );
            }
        }

        return answer;
    }

    private int[] buildSuffixArray(int[] nums) {
        int n = nums.length;
        int N = n + 1;

        // Add a sentinel smaller than every input value.
        int[] a = new int[N];

        for (int i = 0; i < n; i++) {
            a[i] = nums[i] + 1;
        }

        a[n] = 0;

        int maxValue = 0;

        for (int value : a) {
            maxValue = Math.max(maxValue, value);
        }

        int[] p = new int[N];
        int[] c = new int[N];

        // Initial counting sort
        int[] count = new int[Math.max(N, maxValue + 1)];

        for (int value : a) {
            count[value]++;
        }

        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        for (int i = N - 1; i >= 0; i--) {
            p[--count[a[i]]] = i;
        }

        int classes = 1;
        c[p[0]] = 0;

        for (int i = 1; i < N; i++) {
            if (a[p[i]] != a[p[i - 1]]) {
                classes++;
            }

            c[p[i]] = classes - 1;
        }

        int[] pn = new int[N];
        int[] cn = new int[N];

        // Doubling algorithm
        for (int len = 1; len < N; len <<= 1) {

            for (int i = 0; i < N; i++) {
                pn[i] = p[i] - len;

                if (pn[i] < 0) {
                    pn[i] += N;
                }
            }

            count = new int[classes];

            for (int i = 0; i < N; i++) {
                count[c[pn[i]]]++;
            }

            for (int i = 1; i < classes; i++) {
                count[i] += count[i - 1];
            }

            for (int i = N - 1; i >= 0; i--) {
                p[--count[c[pn[i]]]] = pn[i];
            }

            int newClasses = 1;
            cn[p[0]] = 0;

            for (int i = 1; i < N; i++) {
                int cur = p[i];
                int prev = p[i - 1];

                int curSecond = (cur + len) % N;
                int prevSecond = (prev + len) % N;

                if (c[cur] != c[prev]
                        || c[curSecond] != c[prevSecond]) {
                    newClasses++;
                }

                cn[cur] = newClasses - 1;
            }

            int[] temp = c;
            c = cn;
            cn = temp;

            classes = newClasses;

            if (classes == N) {
                break;
            }
        }

        // Remove sentinel suffix
        int[] sa = new int[n];

        for (int i = 1; i < N; i++) {
            sa[i - 1] = p[i];
        }

        return sa;
    }
}
