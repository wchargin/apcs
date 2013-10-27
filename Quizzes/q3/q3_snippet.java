// Java Snippet
// This is NOT an SSCCE (sscce.org)
// Include this in a driver to run

// See quiz #3 (issue #7) for more info


///////////////////////////////////////


// synchronized to lock visited flag
public static synchronized boolean pathExists(Graph g, Node start, Node end) {
    for (Node n : graph.getNodes()) {
        n.visited = false;
    }
    Queue<Node> q = new QueueImplementation();
    q.offer(start);
    while (!q.isEmpty()) {
        Node n = q.poll();
        Set<Node> neighbors = n.getNeighbors(); // or getAdjacent()
        for (Node neighbor : neighbors) {
            if (neighbor == end) {
                return true;
            } else if (!neighbor.visited) {
                q.offer(neighbor);
            }
        }
        n.visited = true;
    }
    return false;
}
