module Batch4 where

isPrime :: Int -> Bool
isPrime x = and [x `mod` n /= 0 | n <- [2..max]]
  where max = ceiling $ sqrt $ fromIntegral x

gcd' :: Int -> Int -> Int
gcd' a b
  | b == 0    = abs a
  | otherwise = gcd' b $ a `mod` b

coprime :: Int -> Int -> Bool
coprime a b = 1 == gcd' a b

totient :: Int -> Int
totient 1 = 1
totient m = length $ filter (coprime m) [1 .. m - 1]

primeFactors :: Int -> [Int]
primeFactors 1 = []
primeFactors n =
  case factors of []     -> [n]
                  (x:xs) -> x : primeFactors (n `div` x)
  where factors = filter ((== 0) . (n `mod`)) [2..max]
        max     = ceiling $ sqrt $ fromIntegral x
