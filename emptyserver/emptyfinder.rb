require 'set'

def find_empties directory, dirprefix, fsuffix
  empty_dirs = Set.new
  Dir.chdir directory do
    Dir["#{dirprefix}**/"].each do |subdir|
      Dir.chdir subdir do
        dirsuffix = subdir[1 .. -dirprefix.length]
        empty_dirs.add dirsuffix if Dir["**.#{fsuffix}"].empty?
      end
    end
  end
  return empty_dirs.collect
end
