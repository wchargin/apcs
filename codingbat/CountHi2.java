public int countHi2(String str) {
  // regex those "xhi"s out of there with a positive lookbehind
  str = str.replaceAll("(?<=x)hi", "");
  
  return str.length() < 2 ? 0 : countHi2(str.substring(1)) + (str.startsWith("hi") ? 1 : 0);
}
