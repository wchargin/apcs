module Batch3 where

insertAt :: Int -> a -> [a] -> [a]
insertAt 0 x ys = x:ys
insertAt n x l@(y:ys) = y : insertAt (n - 1) x ys
