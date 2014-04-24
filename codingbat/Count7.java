public int count7(int n) {
    return n < 7 ? 0 : (n % 10 == 7 ? 1 : 0) + count7(n / 10);
}
