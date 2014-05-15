module Batch4 where

isPrime :: Int -> Bool
isPrime x = and [x `mod` n /= 0 | n <- [2..max]]
  where max = ceiling $ sqrt $ fromIntegral x
