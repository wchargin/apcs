/*
 * bintree.c
 * C parentless binary tree implementation
 * William Chargin
 * 16 September 2013
 */
#include <stdlib.h>
#include <time.h> /* for random */
#include "bintree.h"

/* create and initialize new tree */
bintree* bt_new() {
    bintree *tree;
    tree = malloc(sizeof(bintree));
    tree -> root = NULL;
    return tree;
}

btnode* bt_mknode(int value) {
    btnode *n;
    n = malloc(sizeof(btnode));
    n -> value = value;
    n -> left = NULL;
    n -> right = NULL;
    return n;
}

void bt_freenode(btnode *n) {
    free(n);
}

/* recursive helper method for bt_add */
void bth_add(btnode *n, int value) {
    btnode** ref;
    int nv;
    
    nv = n -> value;
    if (value == nv) {
        /* already exists */
        return;
    }
    ref = value < nv ? &(n -> left) : &(n -> right);
    if (*ref == NULL) {
        *ref = bt_mknode(value);
    } else {
        bth_add(*ref, value);
    }
}

/* add to tree */
void bt_add(bintree *tree, int value) {
    if (tree -> root == NULL) {
        tree -> root = bt_mknode(value);
    } else {
        bth_add(tree -> root, value);
    }
}

/* recursive helper method for bt_remove */
void bth_remove(btnode *parent, btnode *node) {
    btnode **ref = node == parent -> left ? &(parent -> left) : &(parent -> right);
    if (node -> left == NULL && node -> right == NULL) {
        /* node is leaf */
        *ref = NULL;
    } else if (node -> left == NULL || node -> right == NULL) {
        /* node has one child; just swap */
        *ref = (node -> left == NULL ? node -> right : node -> left);
    } else {
        /* node has two children */
        bool usesuccessor = rand() & 0x1; /* last bit */
        btnode *m = usesuccessor /* successor or predecessor */
                ? node -> right
                : node -> left;
        while ((usesuccessor ? m -> left : m -> right) != NULL) {
            m = usesuccessor ? m -> left : m -> right;
        }
        
        /* m is now node's successor or predecessor */
        *ref = m;
        
        *(usesuccessor ? &(m -> left) : &(m -> right)) = 
        (usesuccessor ? node -> left : node -> right);
    }
    bt_freenode(node);
}

/* remove from tree */
void bt_remove(bintree *tree, int value) {
    btnode *p; /* parent */
    
    p = tree -> root;
    if (p == NULL) {
        return;
    }

    while (p != NULL) {
        btnode *check;
        int pval = p -> value;
        check = value < pval ? p -> left : p -> right;
        if (check == NULL) {
            /* not in tree */
            return;
        } else if (check -> value == value) {
            /* p is the parent of check, which has value `value' */
            bth_remove(p, check);
        } else {
            p = check;
        }
    }
}

/* test if value is in tree */
bool bt_contains(bintree *tree, int value) {
    btnode *p = tree -> root;
    while (p != NULL && p -> value != value) {
        p = value < p -> value ? p -> left : p -> right;
    }
    return p != NULL;
}

/* find deepest common ancestor */
btnode* bt_ancestor(bintree *tree, int v1, int v2) {
    btnode *a;
    bool c, d; /* comparisons */
    a = tree -> root;
    
    if (!(bt_contains(tree, v1) && bt_contains(tree, v2))) {
        return NULL;
    }
    
    c = d = false;
    
    while (a != NULL) {
        int v = a -> value;
        c = v1 < v;
        d = v2 < v;
        if (c ^ d) {
            /* diverged; a is the common ancestor */
            return a;
        } else {
            a = c ? a -> left : a -> right;
        }
    }
    
    /* something weird happened */
    return NULL;
}

/* find successor node */
btnode* bt_successor(bintree *tree, btnode *n) {
    btnode *succ = NULL;
    
    if (n -> right != NULL) {
        /* easy case: n has a right tree */
        succ = n -> right;
        while (succ -> left != NULL) {
            succ = succ -> left;
        }
    } 
    else {
        /* harder case: need to traverse */
        btnode *t = tree -> root; /* traversal node */
        
        /* goal: find deepest ancestor s.t. n is a descendant of left child */
        /* traverse down tree and save succ at each valid step */
        while (t != NULL) {
            if (n -> value < t -> value) {
                /* n is a left child */
                succ = t;
            } else if (n -> value == t -> value) {
                /* at target node; stop */
                break;
            }
            /* advance pointer down tree */
            t = (n -> value < t -> value) ? t -> left : t -> right;
        }
    }
    
    return succ;
}

/* find predecessor node */
btnode* bt_predecessor(bintree *tree, btnode *n) {
    btnode *pred = NULL;
    
    if (n -> left != NULL) {
        /* easy case: n has a left tree */
        pred = n -> left;
        while (pred -> right != NULL) {
            pred = pred -> right;
        }
    } 
    else {
        /* harder case: need to traverse */
        btnode *t = tree -> root; /* traversal node */
        
        /* goal: find deepest ancestor s.t. n is a descendant of right child */
        /* traverse down tree and save pred at each valid step */
        while (t != NULL) {
            if (n -> value > t -> value) {
                /* n is a right child */
                pred = t;
            } else if (n -> value == t -> value) {
                /* at target node; stop */
                break;
            }
            /* advance pointer down tree */
            t = (n -> value < t -> value) ? t -> left : t -> right;
        }
    }
    
    return pred;
}
