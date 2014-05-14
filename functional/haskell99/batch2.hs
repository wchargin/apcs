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
