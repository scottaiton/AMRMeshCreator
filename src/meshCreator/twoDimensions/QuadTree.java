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
	public double x_start = 0;
	public double y_start = 0;
	public double x_length = 1;
	public double y_length = 1;
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
		nbr_ids = new int[] { -1, -1, -1, -1 };
	}

	public QuadTree(Quad q, QuadTree parent) {
		tree_map = parent.tree_map;
		curr_id = parent.curr_id;
		id = curr_id.getAndIncrement();
		tree_map.put(id, this);
		nbr_ids = new int[] { -1, -1, -1, -1 };
		this.parent_id = parent.id;
		level = parent.level + 1;
		x_length = parent.x_length / 2;
		y_length = parent.y_length / 2;
		switch (q) {
		case SW:
			x_start = parent.x_start;
			y_start = parent.y_start;
			break;
		case SE:
			x_start = parent.x_start + x_length;
			y_start = parent.y_start;
			break;
		case NW:
			x_start = parent.x_start;
			y_start = parent.y_start + y_length;
			break;
		case NE:
			x_start = parent.x_start + x_length;
			y_start = parent.y_start + y_length;
			break;
		}

	}

	private QuadTree(QuadTree t_old, Map<Integer, QuadTree> new_map, AtomicInteger new_curr_id) {
		level = t_old.level;
		x_start = t_old.x_start;
		y_start = t_old.y_start;
		x_length = t_old.x_length;
		y_length = t_old.y_length;

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
		return tree_map.get(nbr_ids[s.ordinal()]);
	}

	public void setNbr(Side s, QuadTree t) {
		if (t == null) {
			nbr_ids[s.ordinal()] = -1;
		} else {
			nbr_ids[s.ordinal()] = t.id;
		}
	}

	public boolean hasParent() {
		return parent_id != -1;
	}

	public QuadTree getParent() {
		return tree_map.get(parent_id);
	}

	public QuadTree getChild(Quad q) {
		return tree_map.get(child_ids[q.ordinal()]);
	}

	public void setChild(Quad q, QuadTree t) {
		child_ids[q.ordinal()] = t.id;
	}

	public QuadTree[] children(Side s) {
		QuadTree[] retval = new QuadTree[2];
		Quad qs[] = Quad.onSide(s);
		retval[0] = getChild(qs[0]);
		retval[1] = getChild(qs[1]);
		return retval;
	}

	public boolean hasChildren() {
		return child_ids != null;
	}

	public void refine() {
		child_ids = new int[4];
		for (Quad q : Quad.values()) {
			setChild(q, new QuadTree(q, this));
		}
		// set new neighbors
		// sw
		getChild(Quad.SW).setNbr(Side.EAST, getChild(Quad.SE));
		getChild(Quad.SW).setNbr(Side.NORTH, getChild(Quad.NW));
		// se
		getChild(Quad.SE).setNbr(Side.WEST, getChild(Quad.SW));
		getChild(Quad.SE).setNbr(Side.NORTH, getChild(Quad.NE));
		// nw
		getChild(Quad.NW).setNbr(Side.EAST, getChild(Quad.NE));
		getChild(Quad.NW).setNbr(Side.SOUTH, getChild(Quad.SW));
		// ne
		getChild(Quad.NE).setNbr(Side.WEST, getChild(Quad.NW));
		getChild(Quad.NE).setNbr(Side.SOUTH, getChild(Quad.SE));

		// refine neighbors if needed
		for (Side s : Side.values()) {
			if (nbr(s) == null && hasParent() && getParent().nbr(s) != null) {
				getParent().nbr(s).refine();
			}
		}

		// set outer neighbors
		for (Side s : Side.values()) {
			if (nbr(s) != null && nbr(s).hasChildren()) {
				QuadTree childs[] = children(s);
				QuadTree nbr_childs[] = nbr(s).children(s.opposite());
				childs[0].setNbr(s, nbr_childs[0]);
				nbr_childs[0].setNbr(s.opposite(), childs[0]);
				childs[1].setNbr(s, nbr_childs[1]);
				nbr_childs[1].setNbr(s.opposite(), childs[1]);
			}
		}
	}

	public void drawLeafs(GraphicsContext g, int x_orig, int y_orig, double size) {
		if (hasChildren()) {
			for (Quad q : Quad.values()) {
				getChild(q).drawLeafs(g, x_orig, y_orig, size);
			}
		} else {
			int x_px = x_orig + (int) (x_start * size);
			int y_px = y_orig - (int) ((y_start + y_length) * size);
			int x_ln = (int) Math.ceil(size * x_length);
			int y_ln = (int) Math.ceil(size * y_length);
			g.setFill(Color.WHITE);
			g.setStroke(Color.RED);
			g.fillRect(x_px, y_px, x_ln, y_ln);
			g.strokeRect(x_px, y_px, x_ln, y_ln);
		}
		if (level == 1) {
			int x_px = x_orig + (int) (x_start * size);
			int y_px = y_orig - (int) ((y_start + y_length) * size);
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
		switch (s) {
		case WEST:
			return (x_start - x_length < x && x < x_start && y_start < y && y < y_start + y_length);

		case EAST:
			return (x_start + x_length < x && x < x_start + 2 * x_length && y_start < y && y < y_start + y_length);
		case SOUTH:
			return (x_start < x && x < x_start + x_length && y_start - y_length < y && y < y_start);
		case NORTH:
			return (x_start < x && x < x_start + x_length && y_start + y_length < y && y < y_start + 2 * y_length);
		}
		return false;

	}

	public void drawPotentialNbr(GraphicsContext g, int x_orig, int y_orig, int size, double x, double y) {
		int x_px = 0;
		int y_px = 0;
		if (isPotentialNbr(Side.WEST, x, y)) {
			x_px = x_orig + (int) (x_start - x_length) * size;
			y_px = y_orig - (int) (y_start + y_length) * size;
		} else if (isPotentialNbr(Side.EAST, x, y)) {
			x_px = x_orig + (int) (x_start + x_length) * size;
			y_px = y_orig - (int) (y_start + y_length) * size;
		} else if (isPotentialNbr(Side.SOUTH, x, y)) {
			x_px = x_orig + (int) (x_start) * size;
			y_px = y_orig - (int) (y_start) * size;
		} else if (isPotentialNbr(Side.NORTH, x, y)) {
			x_px = x_orig + (int) (x_start) * size;
			y_px = y_orig - (int) (y_start + 2 * y_length) * size;
		}
		g.setFill(Color.LIGHTGRAY);
		g.setStroke(Color.BLACK);
		g.fillRect(x_px, y_px, size, size);
		g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
		g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
	}

	public boolean isPotentialNbr(double x, double y) {
		for (Side s : Side.values()) {
			if (nbr(s) == null && isPotentialNbr(s, x, y))
				return true;
		}
		return false;
	}

	public Side getSide(double x, double y) {
		for (Side s : Side.values()) {
			if (isPotentialNbr(s, x, y))
				return s;
		}
		return null;
	}

	public void setRelativeTo(Side s) {
		QuadTree nbr = nbr(s);
		x_length = nbr.x_length;
		y_length = nbr.y_length;
		switch (s) {
		case EAST:
			x_start = nbr.x_start - x_length;
			y_start = nbr.y_start;
			break;
		case WEST:
			x_start = nbr.x_start + x_length;
			y_start = nbr.y_start;
			break;
		case NORTH:
			x_start = nbr.x_start;
			y_start = nbr.y_start - y_length;
			break;
		case SOUTH:
			x_start = nbr.x_start;
			y_start = nbr.y_start + y_length;
			break;
		}
	}

	public void reconcile() {
		for (Side s : Side.values()) {
			if (nbr(s) != null && nbr(s).hasChildren()) {
				for (QuadTree nbr_child : nbr(s).children(s.opposite())) {
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
		return (x_start < x && x < x_start + x_length && y_start < y && y < y_start + y_length);
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
				for (Side s : Side.values()) {
					QuadTree nbr = t.nbr(s);
					if (nbr != null) {
						// coarsen nbr if needed
						if (nbr.hasChildren()) {
							nbr.coarsen();
						}
						// null out nbrs
						nbr.setNbr(s.opposite(), null);
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
					if(leaf!=null)
						break;
				}
				return leaf;
			} else {
				return this;
			}
		}else {
			return null;
		}
	}

}
