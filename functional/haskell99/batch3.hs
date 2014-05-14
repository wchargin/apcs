module Batch3 where

import Data.Functor
import System.Random

insertAt :: Int -> a -> [a] -> [a]
insertAt 0 x ys = x:ys
insertAt n x l@(y:ys) = y : insertAt (n - 1) x ys

-- Again, using half-open intervals [a, b).
range :: Int -> Int -> [Int]
range x y = case x >= y of
  True  -> []
  False -> x:range (x + 1) y

randomSelect :: [a] -> Int -> IO [a]
randomSelect _ 0 = return []
randomSelect xs n = do
  index <- randomRIO (0, length xs - 1)
  let elem = xs !! index
  (elem:) <$> randomSelect xs (n - 1)

uniqSelect :: [a] -> Int -> IO [a]
uniqSelect _ 0 = return []
uniqSelect xs n = do
  index <- randomRIO (0, length xs - 1)
  let elem = xs !! index
      remainder = take index xs ++ (tail $ drop index xs)
  (elem:) <$> uniqSelect remainder (n - 1)

permute :: [a] -> IO [a]
permute xs = uniqSelect xs (length xs)
