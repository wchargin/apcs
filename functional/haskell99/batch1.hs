module Batch1 where

lastElem :: [a] -> a
lastElem [] = error "empty list"
lastElem [x] = x
lastElem (x:xs) = lastElem xs
