public int[] zeroFront(int[] nums) {
    int zeroes = 0;
    for (int i : nums) if (i == 0) zeroes++;
    int[] newNums = new int[nums.length];
    int j = zeroes;
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] == 0) {
            continue;
        }
        newNums[j++] = nums[i];
    }
    return newNums;
}
