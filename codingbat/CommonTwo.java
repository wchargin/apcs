public int commonTwo(String[] a, String[] b) {
    if (a.length == 0 || b.length == 0) {
        return 0;
    }
    int duplicates = 0;
    int ai = 0, bi = 0;
    String ax, bx;
    while (ai < a.length && bi < b.length) {
        ax = a[ai];
        bx = b[bi];
        if (ax.equals(bx)) {
            duplicates++;
        }
        
        String[] ahead = ax.compareTo(bx) > 0 ? a : b;
        
        // Skip ahead past any duplicate elements within list
        if (ahead == a) {
            while (bi < b.length && b[bi].equals(bx)) {
                bi++;
            }
        } else {
            while (ai < a.length && a[ai].equals(ax)) {
                ai++;
            }
        }
    }
    return duplicates;
}
