public String[] mergeTwo(String[] a, String[] b, int n) {
    String[] merged = new String[n];
    int ai = 0, bi = 0;
    int i = 0;
    while (i < merged.length) {
        String ax = a[ai], bx = b[bi];
        String[] addFrom = ax.compareTo(bx) < 0 ? a : b;
        String toAdd = addFrom == a ? ax : bx;
        if (i == 0 || !toAdd.equals(merged[i - 1])) {
            merged[i++] = toAdd;
        }
        if (addFrom == a) {
            ai++;
        } else {
            bi++;
        }
    }
    return merged;
}
