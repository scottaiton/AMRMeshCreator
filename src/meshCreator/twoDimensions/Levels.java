package meshCreator.twoDimensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import meshCreator.Forest;
import meshCreator.Node;
import meshCreator.Orthant;
import meshCreator.Side;

public class Levels {
	public ArrayList<ArrayList<Patch>> levels;
	public transient ArrayList<Forest> forests;
	public transient ArrayList<Map<Integer, Patch>> patch_maps;

	public Levels(Forest forest) {
		int max_level = forest.getMaxLevel();
		int curr_level = max_level;
		levels = new ArrayList<ArrayList<Patch>>(max_level);
		forests = new ArrayList<Forest>(max_level);
		patch_maps = new ArrayList<Map<Integer, Patch>>(max_level);

		// first level
		forests.add(forest.deepCopy());
		levels.add(extract_level(curr_level, max_level));
		patch_maps.add(get_patch_map(curr_level, max_level));
		curr_level--;

		// rest of the levels
		while (curr_level > 0) {
			forests.add(forests.get(max_level - curr_level - 1).deepCopy());
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
		Queue<Integer> q2 = new LinkedList<Integer>();
		Set<Integer> visited2 = new HashSet<Integer>();
		Forest forest = forests.get(max_level - curr_level);
		Node finest_node = forest.getRootNode();
		while (finest_node.hasChildren() && finest_node.getLevel() < curr_level) {
			finest_node = forest.getNode(finest_node.getChildId(Orthant.SW()));
		}
		q2.add(finest_node.getId());
		while (!q2.isEmpty()) {
			Node curr = forest.getNode(q2.remove());
			visited2.add(curr.getId());
			patches.add(new Patch(curr, curr_level, max_level));
			Node parent = forest.getNode(curr.getParentId());
			for (Side s : Side.getValuesForDimension(2)) {
				Node neighbor = forest.getNode(curr.getNbrId(s));
				if (curr.hasNbr(s) && curr.hasParent() && parent.hasNbr(s)) {
					int next_id = parent.getNbrId(s);
					if (!q2.contains(next_id) && !visited2.contains(next_id)) {
						q2.add(next_id);
					}
				} else if (curr.getLevel() < curr_level && curr.hasNbr(s) && neighbor.hasChildren()) {
					int next_id = neighbor.getChildId(Orthant.SW());
					if (!q2.contains(next_id) && !visited2.contains(next_id)) {
						q2.add(next_id);
					}
				} else if (curr.hasNbr(s)) {
					int next_id = neighbor.getNbrId(s);
					if (!q2.contains(next_id) && !visited2.contains(next_id)) {
						q2.add(next_id);
					}
				}
				if (curr.getLevel() == curr_level && curr.hasChildren()) {
					forest.coarsenNode(curr.getId());
				}
			}
		}
		return patches;
	}
}
