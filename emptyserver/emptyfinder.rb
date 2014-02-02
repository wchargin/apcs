def find_empties(directory, dirprefix, fsuffix)
  empty_dirs = Array.new
  Dir.chdir directory do
    Dir["#{dirprefix}**/"].each do |subdir|
      Dir.chdir subdir do
        empty_dirs.push subdir if Dir["**.#{fsuffix}"].empty?
      end
    end
  end
  return empty_dirs
end

def find_empties_sorted(directory, dirprefix, fsuffix)
  empties = find_empties(directory, dirprefix, fsuffix)
  nums = Hash.new
  non_numeric = Array.new
  empties.each do |x|
    # if starts with digits, trim leading zeroes and extract; else, nothing
    num = x.sub(/^#{Regexp.escape dirprefix}0*(\d+).*/, '\1')
    is_num = num !~ /\D/
    if is_num
      n = Integer num
      nums[n] = Array.new unless nums[n]
      nums[n].push x
    else
      non_numeric.push x
    end
  end
  result = Array.new
  nums.sort.each do |k, v|
    v.each do |x|
      result.push x
    end
  end
  result.concat non_numeric.sort
  result
end
