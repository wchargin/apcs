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
