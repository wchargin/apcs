package disjoint.maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import disjoint.maze.MazeGenerator.MazeNode;

public class MazeSolver {

	public static List<MazeNode> solve(Maze m) {
		List<MazeNode> path = new ArrayList<>();

		// not in map: not visited
		// value=false: visiting
		// value=true: visited (exhausted)
		Map<MazeNode, Boolean> paint = new HashMap<>();
		Map<MazeNode, MazeNode> parents = new HashMap<>();

		Stack<MazeNode> nodes = new Stack<>();
		nodes.push(m.getStart());

		while (!nodes.isEmpty()) {
			MazeNode node = nodes.pop();
			paint.put(node, false);
			List<MazeNode> neighbors = new ArrayList<>(Arrays.asList(node.getNorth(),
					node.getEast(), node.getSouth(), node.getWest()));
			while (neighbors.contains(null)) {
				neighbors.remove(null);
			}
			for (MazeNode neighbor : neighbors) {
				if (paint.containsKey(neighbor)) {
					continue;
				}
				paint.put(neighbor, false);
				nodes.push(neighbor);
				parents.put(neighbor, node);
				if (neighbor == m.getEnd()) {
					// finish
					path.add(neighbor);
					while ((neighbor = parents.get(neighbor)) != null) {
						path.add(neighbor);
					}
					Collections.reverse(path);
					return path;
				}
			}
			paint.put(node, true);
		}

		return null;
	}

}
