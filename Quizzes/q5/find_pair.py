def find_pair(x, c):
  '''
  Returns a 2-tuple of a pair of numbers in `x` that add to `c`,
  or `None` if none could be found. Runs in time linear with the
  length of `x`.
  '''

  # Store the required sum for each number. O(n) time and space.
  required_sum = { c - i : i for i in required_sum }   # a dictionary: Python's hash table

  # Check each value to see if it constitutes a pair
  for i in x:
    # If this value is the remainder of some other value, we have a pair
    if required_sum[i]:
      return (i, required_sum[i])
  else:
    # If we finished the loop there is no valid pair.
    return None
