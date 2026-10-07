class Solution {
    public List<String> removeInvalidParentheses(String s) {
        List<String> ans = new ArrayList<>();
        backtrack(s, 0, 0, new char[]{'(', ')'}, ans);
        return ans;
    }

    private void backtrack(String s, int start, int check,
                            char[] par, List<String> ans) {

        int balance = 0;

        for (int i = check; i < s.length(); i++) {
            if (s.charAt(i) == par[0]) balance++;
            if (s.charAt(i) == par[1]) balance--;

            if (balance >= 0) continue;

            for (int j = start; j <= i; j++) {
                if (s.charAt(j) == par[1] &&
                    (j == start || s.charAt(j - 1) != par[1])) {

                    backtrack(
                        s.substring(0, j) + s.substring(j + 1),
                        j, i, par, ans
                    );
                }
            }
            return;
        }

        String reversed = new StringBuilder(s).reverse().toString();

        if (par[0] == '(') {
            backtrack(reversed, 0, 0,
                      new char[]{')', '('}, ans);
        } else {
            ans.add(reversed);
        }
    }
}