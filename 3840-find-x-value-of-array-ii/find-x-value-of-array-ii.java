class Solution {

    class Node {
        int prod;
        int[] cnt;

        Node(int k) {
            cnt = new int[k];
        }
    }

    int k;
    Node[] tree;

    public int[] resultArray(int[] nums, int k, int[][] queries) {
        this.k = k;

        int n = nums.length;
        tree = new Node[4 * n];

        build(1, 0, n - 1, nums);

        int[] ans = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {

            int index = queries[i][0];
            int value = queries[i][1];
            int start = queries[i][2];
            int x = queries[i][3];

            // Permanent update
            update(1, 0, n - 1, index, value);

            // Query [start, n-1]
            Node res = query(1, 0, n - 1, start, n - 1);

            ans[i] = res.cnt[x];
        }

        return ans;
    }

    void build(int node, int l, int r, int[] nums) {

        tree[node] = new Node(k);

        if (l == r) {
            int v = nums[l] % k;

            tree[node].prod = v;
            tree[node].cnt[v] = 1;

            return;
        }

        int mid = (l + r) / 2;

        build(node * 2, l, mid, nums);
        build(node * 2 + 1, mid + 1, r, nums);

        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }

    void update(int node, int l, int r, int index, int value) {

        if (l == r) {

            int v = value % k;

            tree[node] = new Node(k);
            tree[node].prod = v;
            tree[node].cnt[v] = 1;

            return;
        }

        int mid = (l + r) / 2;

        if (index <= mid) {
            update(node * 2, l, mid, index, value);
        } else {
            update(node * 2 + 1, mid + 1, r, index, value);
        }

        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }

    Node query(int node, int l, int r, int ql, int qr) {

        if (ql <= l && r <= qr) {
            return tree[node];
        }

        int mid = (l + r) / 2;

        if (qr <= mid) {
            return query(node * 2, l, mid, ql, qr);
        }

        if (ql > mid) {
            return query(node * 2 + 1, mid + 1, r, ql, qr);
        }

        Node left = query(node * 2, l, mid, ql, qr);
        Node right = query(node * 2 + 1, mid + 1, r, ql, qr);

        return merge(left, right);
    }

    Node merge(Node left, Node right) {

        Node res = new Node(k);

        // Product of the complete combined segment
        res.prod = (left.prod * right.prod) % k;

        // Prefixes completely inside the left segment
        for (int rem = 0; rem < k; rem++) {
            res.cnt[rem] += left.cnt[rem];
        }

        // Prefixes that start in left and continue into right
        for (int rem = 0; rem < k; rem++) {

            int newRem = (left.prod * rem) % k;

            res.cnt[newRem] += right.cnt[rem];
        }

        return res;
    }
}