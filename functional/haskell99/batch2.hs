module Batch2 where

import Data.List
data MultiElement a = Single a | Multiple Int a deriving (Show)

encodeModified :: (Eq a) => [a] -> [MultiElement a]
encodeModified = map f . group
  where f xs = (case length xs of 1 -> Single
                                  _ -> Multiple (length xs))
               (head xs)

decodeModified :: [MultiElement a] -> [a]
decodeModified = concatMap decodeMultiElement
  where decodeMultiElement (Single x) = [x]
        decodeMultiElement (Multiple n x) = replicate n x

encodeDirect :: (Eq a) => [a] -> [MultiElement a]
encodeDirect [] = []
encodeDirect (x:xs) = (createElement these) : encodeDirect others
  where these = x : takeWhile (== x) xs
        others = dropWhile (== x) xs
        createElement xs = case length xs of 1 -> Single
                                             _ -> Multiple (length xs)
                         $ head xs

-- changing order of parameters from question because
-- (1) it seems more Haskell style
-- (2) it works better with built-ins
repli :: Int -> [a] -> [a]
repli = concatMap . replicate

dupli :: [a] -> [a]
dupli = repli 2

-- The inner function here works by maintaining three arguments:
--  (1) a list of completed items that is periodically enlarged
--  (2) a list of the current run being processed
--  (3) a list of all unprocessed items
-- 
-- At each iteration, the function either modifies the current run, if it is
-- of length less than the maximum length, or clears the current run and
-- copies it into the completed list.
dropEvery :: Int -> [a] -> [a]
dropEvery n as = f [] [] as
  where f xs ys [] = xs ++ ys
        f xs ys (z:zs) = (if length ys == n - 1
                          then f (xs ++ ys) []
                          else f xs (ys ++ [z]))
                          zs
