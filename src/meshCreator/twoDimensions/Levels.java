package meshCreator.twoDimensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Levels {
	public ArrayList<ArrayList<Patch>> levels;
	public transient ArrayList<QuadTree> trees;
	public transient ArrayList<Map<Integer, Patch>> patch_maps;

	public Levels(QuadTree root) {
		int max_level = root.getMaxLevel();
		int curr_level = max_level;
		levels = new ArrayList<ArrayList<Patch>>(max_level);
		trees = new ArrayList<QuadTree>(max_level);
		patch_maps = new ArrayList<Map<Integer, Patch>>(max_level);

		// first level
		trees.add(root.deepCopy());
		levels.add(extract_level(curr_level, max_level));
		patch_maps.add(get_patch_map(curr_level, max_level));
		curr_level--;

		// rest of the levels
		while (curr_level > 0) {
			trees.add(trees.get(max_level - curr_level - 1).deepCopy());
			levels.add(extract_level(curr_level, max_level));
			patch_maps.add(get_patch_map(curr_level, max_level));
			curr_level--;
		}

	}

	private Map<Integer, Patch> get_patch_map(int curr_level, int max_level) {
		Map<Integer, Patch> patch_map = new HashMap<Integer, Patch>();
		for (Patch p : levels.get(max_level - curr_level)) {
			patch_map.put(p.id, p);
		}
		return patch_map;
	}

	private ArrayList<Patch> extract_level(int curr_level, int max_level) {
		ArrayList<Patch> patches = new ArrayList<Patch>();
		Queue<QuadTree> q2 = new LinkedList<QuadTree>();
		Set<QuadTree> visited2 = new HashSet<QuadTree>();
		QuadTree finest_node = trees.get(max_level - curr_level);
		while (finest_node.hasChildren() && finest_node.level < curr_level) {
			finest_node = finest_node.getChild(Orthant.SW());
		}
		q2.add(finest_node);
		while (!q2.isEmpty()) {
			QuadTree curr = q2.remove();
			visited2.add(curr);
			patches.add(new Patch(curr, curr_level, max_level));
			for (Side s : Side.getValuesForDimension(2)) {
				if (curr.nbr(s) == null && curr.hasParent() && curr.getParent().nbr(s) != null) {
					QuadTree next = curr.getParent().nbr(s);
					if (!q2.contains(next) && !visited2.contains(next)) {
						q2.add(next);
					}
				} else if (curr.level < curr_level && curr.nbr(s) != null && curr.nbr(s).hasChildren()) {
					QuadTree next = curr.nbr(s).getChild(Orthant.SW());
					if (!q2.contains(next) && !visited2.contains(next)) {
						q2.add(next);
					}
				} else if (curr.nbr(s) != null) {
					QuadTree next = curr.nbr(s);
					if (!q2.contains(next) && !visited2.contains(next)) {
						q2.add(next);
					}
				}
				if (curr.level == curr_level && curr.hasChildren()) {
					curr.coarsen();
				}
			}
		}
		return patches;
	}
}
