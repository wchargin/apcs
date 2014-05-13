module Batch1 where

lastElem :: [a] -> a
lastElem [] = error "empty list"
lastElem [x] = x
lastElem (x:xs) = lastElem xs

secondLastElem :: [a] -> a
secondLastElem [] = error "empty list"
secondLastElem [x] = error "list too short"
secondLastElem [x,y] = x
secondLastElem (x:xs) = lastElem xs

(!!!) :: [a] -> Int -> a
[] !!! _ = error "index too large"
(x:xs) !!! 0 = x
(x:xs) !!! n = xs !!! (n - 1)

length' :: [a] -> Int
length [] = 0
length (x:xs) = 1 + length xs
