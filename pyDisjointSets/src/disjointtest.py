'''
Unit test for disjoint sets
Created on Oct 6, 2013

@author: William Chargin
'''
import unittest
from disjoint import *

class DisjointSetTest(unittest.TestCase):

    def setUp(self):
        self.set = None

    def tearDown(self):
        pass
    
    def test_linked(self):
        self.set= LinkedDisjointSet()
        self.do_test()
        
    def do_test(self):
        class Element:
            def __init__(self, val):
                self.val = val
                
        items = [Element(i) for i in list(range(16))]
        
        # setup test
        for i in items:
            self.set.make_set(i)
        
        # make sure each is contained
        for i in items:
            self.assertEqual(self.set.find_set(i), i,
                             "{0!s} is not its own parent".format(i))
            
        # union some stuff
        for i in range(len(items))[::2]:
            self.set.union(items[i], items[i + 1])
            
        # make sure pairs are okay
        for i in range(len(items)):
            j = self.set.find_set(items[i])
            n = None if i + 1 >= len(items) else items[i + 1]
            p = None if i <= 0 else items[i - 1]
            self.assertTrue(j in [items[i], n, p])

if __name__ == "__main__":
    # import sys;sys.argv = ['', 'Test.testName']
    unittest.main()