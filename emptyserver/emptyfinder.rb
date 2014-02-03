def find_empties(directory, dirprefix, fsuffix)
  empty_dirs = []
  Dir.chdir directory do
    Dir["#{dirprefix}**/"].each do |subdir|
      Dir.chdir subdir do
        empty_dirs.push subdir if Dir["**.#{fsuffix}"].empty?
      end
    end
  end
  empty_dirs
end

def find_empties_sorted(directory, dirprefix, fsuffix)
  empties = find_empties(directory, dirprefix, fsuffix)
  nums = Hash.new { |h,k| h[k] = [] }
  non_numeric = []
  empties.each do |x|
    # if starts with digits, trim leading zeroes and extract; else, nothing
    num = x.sub(/^#{Regexp.escape dirprefix}0*(\d+).*/, '\1')
    if num !~ /\D/ # is a number
      nums[Integer num].push x
    else
      non_numeric.push x
    end
  end
  result = []
  nums.sort.each { |k, v| result.concat v }
  result.concat non_numeric.sort
end
