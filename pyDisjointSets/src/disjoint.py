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

class DisjointSetForest(object):
    '''
    A disjoint-set forest implementation of a disjoint set.
    '''
    
    def init_node(self, x):
        '''
        Initializes the given value for use in this disjoint set forest.
        '''
        if not hasattr(x, "parent"):
            x.parent = {}
        if not hasattr(x, "rank"):
            x.rank = {}
    
    def make_set(self, x):
        '''
        Creates a new set whose only member (and thus representative) is x.
        '''
        self.init_node(x)
        x.parent[self] = x
        x.rank[self] = 0
    
    def find_set(self, x):
        '''
        Returns the representative of the set containing x.
        '''
        self.init_node(x)
        traversed = []
        while x is not x.parent[self]:
            traversed.append(x)
            x = x.parent[self]
        for t in traversed:
            # path compression step
            t.parent[self] = x
        return x
    
    def union(self, x, y):
        '''
        Unites the dynamic sets that contain x and y into a new set that is
        the union of these two sets.
        '''
        self.link(self.find_set(x), self.find_set(y))
        
    def link(self, x, y):
        '''
        Modifies the parents and ranks for a union operation, assuming that
        the two parameters are root nodes.
        '''
        
        # Make sure both are root nodes
        xin = x is x.parent[self]
        yin = y is y.parent[self]
        if not xin or not yin:
            if not xin and not yin:
                raise ValueError('Neither key is a root')
            else:
                raise ValueError('{0!s} is not a root'
                                 .format('y' if xin else 'x'))
        
        if x.rank[self] > y.rank[self]:
            y.parent[self] = x
        else:
            x.parent[self] = y
            if x.rank[self] is y.rank[self]:
                y.rank[self] += 1
