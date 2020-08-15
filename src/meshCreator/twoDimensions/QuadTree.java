package meshCreator.twoDimensions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class QuadTree {

	public int level;
	public double starts[];
	public double lengths[];
	public int id;

	private AtomicInteger curr_id;
	private Map<Integer, QuadTree> tree_map;

	private int parent_id = -1;
	private int child_ids[];
	private int nbr_ids[];

	public QuadTree() {
		tree_map = new HashMap<Integer, QuadTree>();
		level = 1;
		curr_id = new AtomicInteger();
		id = curr_id.getAndIncrement();
		tree_map.put(id, this);
		starts = new double[] { 0, 0 };
		lengths = new double[] { 1, 1 };
		nbr_ids = new int[] { -1, -1, -1, -1 };
	}

	public QuadTree(Orthant q, QuadTree parent) {
		tree_map = parent.tree_map;
		curr_id = parent.curr_id;
		id = curr_id.getAndIncrement();
		tree_map.put(id, this);
		nbr_ids = new int[] { -1, -1, -1, -1 };
		this.parent_id = parent.id;
		level = parent.level + 1;

		lengths = new double[2];
		starts = new double[2];
		for (int axis = 0; axis < 2; axis++) {
			lengths[axis] = parent.lengths[axis] / 2;
			if (q.isLowerOnAxis(axis)) {
				starts[axis] = parent.starts[axis];
			} else {
				starts[axis] = parent.starts[axis] + lengths[axis];
			}
		}
	}

	private QuadTree(QuadTree t_old, Map<Integer, QuadTree> new_map, AtomicInteger new_curr_id) {
		level = t_old.level;
		starts = new double[2];
		starts[0] = t_old.starts[0];
		starts[1] = t_old.starts[1];
		lengths = new double[2];
		lengths[0] = t_old.lengths[0];
		lengths[1] = t_old.lengths[1];

		id = t_old.id;

		curr_id = new_curr_id;
		tree_map = new_map;
		tree_map.put(id, this);

		parent_id = t_old.parent_id;
		nbr_ids = new int[4];
		for (int i = 0; i < 4; i++) {
			nbr_ids[i] = t_old.nbr_ids[i];
		}
		if (t_old.hasChildren()) {
			child_ids = new int[4];
			for (int i = 0; i < 4; i++) {
				child_ids[i] = t_old.child_ids[i];
			}
		}

	}

	public QuadTree deepCopy() {
		AtomicInteger new_curr_id = new AtomicInteger(curr_id.get());
		Map<Integer, QuadTree> new_tree_map = new HashMap<Integer, QuadTree>();
		for (QuadTree t : tree_map.values()) {
			new QuadTree(t, new_tree_map, new_curr_id);
		}
		return new_tree_map.get(id);
	}

	public QuadTree nbr(Side s) {
		return tree_map.get(nbr_ids[s.getIndex()]);
	}

	public void setNbr(Side s, QuadTree t) {
		if (t == null) {
			nbr_ids[s.getIndex()] = -1;
		} else {
			nbr_ids[s.getIndex()] = t.id;
		}
	}

	public boolean hasParent() {
		return parent_id != -1;
	}

	public QuadTree getParent() {
		return tree_map.get(parent_id);
	}

	public QuadTree getChild(Orthant q) {
		return tree_map.get(child_ids[q.getIndex()]);
	}

	public void setChild(Orthant q, QuadTree t) {
		child_ids[q.getIndex()] = t.id;
	}

	public QuadTree[] children(Side s) {
		QuadTree[] retval = new QuadTree[2];
		Orthant qs[] = Orthant.GetValuesOnSide(2, s);
		retval[0] = getChild(qs[0]);
		retval[1] = getChild(qs[1]);
		return retval;
	}

	public boolean hasChildren() {
		return child_ids != null;
	}

	public void refine() {
		child_ids = new int[4];
		for (Orthant q : Orthant.getValuesForDimension(2)) {
			setChild(q, new QuadTree(q, this));
		}
		// set new neighbors
		for (Orthant orth : Orthant.getValuesForDimension(2)) {
			for (Side s : orth.getInteriorSides()) {
				getChild(orth).setNbr(s, getChild(orth.getNbrOnSide(s)));
			}
		}

		// refine neighbors if needed
		for (Side s : Side.getValuesForDimension(2)) {
			if (nbr(s) == null && hasParent() && getParent().nbr(s) != null) {
				getParent().nbr(s).refine();
			}
		}

		// set outer neighbors
		for (Side s : Side.getValuesForDimension(2)) {
			if (nbr(s) != null && nbr(s).hasChildren()) {
				QuadTree childs[] = children(s);
				QuadTree nbr_childs[] = nbr(s).children(s.getOpposite());
				childs[0].setNbr(s, nbr_childs[0]);
				nbr_childs[0].setNbr(s.getOpposite(), childs[0]);
				childs[1].setNbr(s, nbr_childs[1]);
				nbr_childs[1].setNbr(s.getOpposite(), childs[1]);
			}
		}
	}

	public void drawLeafs(GraphicsContext g, int x_orig, int y_orig, double size) {
		if (hasChildren()) {
			for (Orthant q : Orthant.getValuesForDimension(2)) {
				getChild(q).drawLeafs(g, x_orig, y_orig, size);
			}
		} else {
			int x_px = x_orig + (int) (starts[0] * size);
			int y_px = y_orig - (int) ((starts[1] + lengths[1]) * size);
			int x_ln = (int) Math.ceil(size * lengths[0]);
			int y_ln = (int) Math.ceil(size * lengths[1]);
			g.setFill(Color.WHITE);
			g.setStroke(Color.RED);
			g.fillRect(x_px, y_px, x_ln, y_ln);
			g.strokeRect(x_px, y_px, x_ln, y_ln);
		}
		if (level == 1) {
			int x_px = x_orig + (int) (starts[0] * size);
			int y_px = y_orig - (int) ((starts[1] + lengths[1]) * size);
			g.setStroke(Color.BLACK);
			g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
			g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
		}
	}

	public void refineAt(double x, double y) {
		if (isInside(x, y)) {
			if (hasChildren()) {
				for (int child_id : child_ids) {
					tree_map.get(child_id).refineAt(x, y);
				}
			} else {
				refine();
			}
		}
	}

	private boolean isPotentialNbr(Side s, double x, double y) {
		if (s.equals(Side.WEST())) {
			return (starts[0] - lengths[0] < x && x < starts[0] && starts[1] < y && y < starts[1] + lengths[1]);
		} else if (s.equals(Side.EAST())) {
			return (starts[0] + lengths[0] < x && x < starts[0] + 2 * lengths[0] && starts[1] < y
					&& y < starts[1] + lengths[1]);
		} else if (s.equals(Side.SOUTH())) {
			return (starts[0] < x && x < starts[0] + lengths[0] && starts[1] - lengths[1] < y && y < starts[1]);
		} else if (s.equals(Side.NORTH())) {
			return (starts[0] < x && x < starts[0] + lengths[0] && starts[1] + lengths[1] < y
					&& y < starts[1] + 2 * lengths[1]);
		}
		return false;

	}

	public void drawPotentialNbr(GraphicsContext g, int x_orig, int y_orig, int size, double x, double y) {
		int x_px = 0;
		int y_px = 0;
		if (isPotentialNbr(Side.WEST(), x, y)) {
			x_px = x_orig + (int) (starts[0] - lengths[0]) * size;
			y_px = y_orig - (int) (starts[1] + lengths[1]) * size;
		} else if (isPotentialNbr(Side.EAST(), x, y)) {
			x_px = x_orig + (int) (starts[0] + lengths[0]) * size;
			y_px = y_orig - (int) (starts[1] + lengths[1]) * size;
		} else if (isPotentialNbr(Side.SOUTH(), x, y)) {
			x_px = x_orig + (int) (starts[0]) * size;
			y_px = y_orig - (int) (starts[1]) * size;
		} else if (isPotentialNbr(Side.NORTH(), x, y)) {
			x_px = x_orig + (int) (starts[0]) * size;
			y_px = y_orig - (int) (starts[1] + 2 * lengths[1]) * size;
		}
		g.setFill(Color.LIGHTGRAY);
		g.setStroke(Color.BLACK);
		g.fillRect(x_px, y_px, size, size);
		g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
		g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
	}

	public boolean isPotentialNbr(double x, double y) {
		for (Side s : Side.getValuesForDimension(2)) {
			if (nbr(s) == null && isPotentialNbr(s, x, y))
				return true;
		}
		return false;
	}

	public Side getSide(double x, double y) {
		for (Side s : Side.getValuesForDimension(2)) {
			if (isPotentialNbr(s, x, y))
				return s;
		}
		return null;
	}

	public void setRelativeTo(Side s) {
		QuadTree nbr = nbr(s);
		lengths[0] = nbr.lengths[0];
		lengths[1] = nbr.lengths[1];
		if (s.equals(Side.EAST())) {
			starts[0] = nbr.starts[0] - lengths[0];
			starts[1] = nbr.starts[1];
		} else if (s.equals(Side.WEST())) {
			starts[0] = nbr.starts[0] + lengths[0];
			starts[1] = nbr.starts[1];
		} else if (s.equals(Side.NORTH())) {
			starts[0] = nbr.starts[0];
			starts[1] = nbr.starts[1] - lengths[1];
		} else if (s.equals(Side.SOUTH())) {
			starts[0] = nbr.starts[0];
			starts[1] = nbr.starts[1] + lengths[1];
		}
	}

	public void reconcile() {
		for (Side s : Side.getValuesForDimension(2)) {
			if (nbr(s) != null && nbr(s).hasChildren()) {
				for (QuadTree nbr_child : nbr(s).children(s.getOpposite())) {
					if (nbr_child.hasChildren()) {
						refine();
						break;
					}
				}
			}
		}
		if (hasChildren()) {
			for (int child_id : child_ids) {
				tree_map.get(child_id).reconcile();
			}
		}
	}

	public boolean isInside(double x, double y) {
		return (starts[0] < x && x < starts[0] + lengths[0] && starts[1] < y && y < starts[1] + lengths[1]);
	}

	public void coarsenAt(double x, double y) {
		if (isInside(x, y)) {
			if (hasChildren()) {
				boolean have_coarsend = false;
				for (int child_id : child_ids) {
					QuadTree t = tree_map.get(child_id);
					if (t.isInside(x, y) && t.hasChildren()) {
						have_coarsend = true;
						t.coarsenAt(x, y);
					}
				}
				if (!have_coarsend) {
					coarsen();
				}
			}
		}
	}

	public void coarsen() {
		if (hasChildren()) {
			for (int child_id : child_ids) {
				QuadTree t = tree_map.get(child_id);
				// coarsen children if needed
				if (t.hasChildren()) {
					t.coarsen();
				}
				for (Side s : Side.getValuesForDimension(2)) {
					QuadTree nbr = t.nbr(s);
					if (nbr != null) {
						// coarsen nbr if needed
						if (nbr.hasChildren()) {
							nbr.coarsen();
						}
						// null out nbrs
						nbr.setNbr(s.getOpposite(), null);
						t.setNbr(s, null);
					}
				}
			}
			for (int child_id : child_ids) {
				tree_map.remove(child_id);
			}
			child_ids = null;
		}
	}

	public String toJson() {
		StringBuilder builder = new StringBuilder();

		return null;
	}

	static public int indexNodes(QuadTree root) {
		/*
		 * int curr_i = 0; Queue<QuadTree> q = new LinkedList<QuadTree>(); Set<QuadTree>
		 * visited = new HashSet<QuadTree>(); q.add(root); while (!q.isEmpty()) {
		 * QuadTree curr = q.remove(); visited.add(curr); curr.id = curr_i; curr_i++;
		 * for (QuadTree t : curr.nbrs) { if (t != null &&!q.contains(t)&&
		 * !visited.contains(t)) { q.add(t); } } if (curr.hasChildren()) { for (QuadTree
		 * t : curr.children) { if (t != null &&!q.contains(t)&& !visited.contains(t)) {
		 * q.add(t); } } } } return visited.size();
		 */
		return 0;
	}

	public int getMaxLevel() {
		int max_level = 0;
		for (QuadTree t : tree_map.values()) {
			max_level = Math.max(max_level, t.level);
		}
		return max_level;
	}

	public QuadTree getLeafAt(double x, double y) {
		// TODO Auto-generated method stub
		if (isInside(x, y)) {
			if (hasChildren()) {
				QuadTree leaf = null;
				for (int child_id : child_ids) {
					leaf = tree_map.get(child_id).getLeafAt(x, y);
					if (leaf != null)
						break;
				}
				return leaf;
			} else {
				return this;
			}
		} else {
			return null;
		}
	}

}
