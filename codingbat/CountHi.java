public int countHi(String str) {
    return str.length() < 2 ? 0 : countHi(str.substring(1)) + (str.startsWith("hi") ? 1 : 0);
}
