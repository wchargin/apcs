# Benchmarker
from disjoint import *
from random import randint

setsize = 1 << 20
opcount = 1 << 14

if __name__ == "__main__":
    print("pyDisjointSets benchmark initialized. Creating set...")
    ds = DisjointSetForest()
    print("Set initialized. Creating node class...")
    class Element:
        def __init__(self, value):
            self.value = value
        def __repr__(self):
            return "Element({0!s})".format(self.value)
        def __str__(self):
            return str(self.value)
    print("Node class initialized. Creating array...")
    elems = [Element(i) for i in range(setsize)]
    print("Array initialized. Making sets...")
    for elem in elems:
        ds.make_set(elem)
    ct = len(elems)
    print("Made sets from {0!s} elements. Unioning...".format(ct))
    for i in range(opcount):
        ds.union(elems[randint(0, ct - 1)], elems[randint(0, ct - 1)])
    print("Unioned a bunch of stuff. Finding...");
    for i in range(opcount):
        ds.find_set(elems[randint(0, ct - 1)])
    print("Found a bunch of sets. Unioning...");
    for i in range(2 * opcount):
        ds.union(elems[randint(0, ct - 1)], elems[randint(0, ct - 1)])
    print("Unioned some moar sets. Finishing...")
    print("Done.")