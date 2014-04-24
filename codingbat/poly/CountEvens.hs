module CountEvens where

countEvens :: Integral a => [a] -> Int
countEvens = length . filter even
