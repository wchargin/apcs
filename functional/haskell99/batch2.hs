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

-- not sure about the efficiency of this implementation
split :: Int -> [a] -> ([a], [a])
split 0 xs = ([], xs)
split n l@(x:xs) = (a ++ [head b], tail b)
  where s = split (n - 1) l
        a = fst s
        b = snd s

-- The specification for this problem indicates two questionable behaviors:
--  (1) "both limits included"
--  (2) "start counting with 1"
-- I have elected to ignore both of these requirements, on the grounds
-- described in Edsger Dijkstra's "Why Numbering Should Start At Zero" (a
-- paper that also describes the superiority of closed-open intervals).
slice :: Int -> Int -> [a] -> [a]
slice a b = (drop a) . (take b)

rotate :: Int -> [a] -> [a]
rotate n xs = right ++ left
  where s = split n xs
        left = fst s
        right = snd s

removeAt :: Int -> [a] -> (a, [a])
removeAt n xs = (head right, left ++ tail right)
  where s = split n xs
        left = fst s
        right = snd s
