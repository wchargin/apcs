public int[] shiftLeft(int[] nums) {
    if (nums.length == 0) return nums;
    int last = nums[0];
    for (int i = 1; i < nums.length; i++) {
        nums[i - 1] = nums[i];
    }
    nums[nums.length - 1] = last;
    return nums;
}
