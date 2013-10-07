'''
disjoint.py
Disjoint set module with various implementations
Created on Oct 6, 2013

@author: William Chargin
'''

class LinkedDisjointSet(object):
    '''
    A linked-list implementation of a disjoint set.
    '''

    def __init__(self):
        '''
        Constructor
        '''
        
    def iter_set(self, x):
        '''
        Returns a generator to iterate over the set containing x, starting at
        the representative.
        '''
        x = self.find_set(x);
        while x:
            yield x
            x = x.next[self]
        
    def init_node(self, x):
        '''
        Initializes the given value for use in this linked disjoint set.
        '''
        if not hasattr(x, "representative"):
            x.representative = {}
        if not hasattr(x, "next"):
            x.next = {}
        
    def make_set(self, x):
        '''
        Creates a new set whose only member (and thus representative) is x.
        '''
        self.init_node(x)
        x.representative[self] = x
        x.next[self] = None
        pass
        
    def find_set(self, x):
        '''
        Returns the representative of the set containing x.
        '''
        self.init_node(x)
        return x.representative[self]
    
    def union(self, x, y):
        '''
        Unites the dynamic sets that contain x and y into a new set that is
        the union of these two sets.
        '''
        self.init_node(x)
        self.init_node(y)
        yrep = self.find_set(y)
        for i in self.iter_set(x):
            i.representative[self] = yrep
        while y.next[self]:
            y = y.next[self]
        y.next = x
