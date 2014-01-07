#!/usr/bin/env python

# edit_distance.py
# Python edit distance calculator
# Uses a dictionary for memoization, and Levenshtein's algorithm

def ld(a, b):
  '''
  Accepts two words `a' and `b' and finds the Levenshtein distance
  (edit distance) between them.
  '''
  memo = {}
  def step(i, j):
    if (i, j) not in memo:
      memo[(i, j)] = max(i, j) if min(i, j) is 0 else min(
        step(i-1, j) + 1,                                 # first word:  \pm
        step(i, j-1) + 1,                                 # second word: \mp
        step(i-1, j-1) + (0 if a[i-1] is b[j-1] else 1))  # substitution
    return memo[(i, j)]
  return step(len(a), len(b))
