module Batch4 where

isPrime :: Int -> Bool
isPrime x = and [x `mod` n /= 0 | n <- [2..max]]
  where max = ceiling $ sqrt $ fromIntegral x

gcd' :: Int -> Int -> Int
gcd' a b
  | b == 0    = abs a
  | otherwise = gcd' b $ a `mod` b
