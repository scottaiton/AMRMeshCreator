package meshCreator.twoDimensions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class QuadTree {

	public int level;
	public double x_start = 0;
	public double y_start = 0;
	public double x_length = 1;
	public double y_length = 1;
	public int id;

	public QuadTree parent;
	private QuadTree children[];
	private QuadTree nbrs[];

	public QuadTree() {
		level = 1;
		nbrs = new QuadTree[4];
	}

	public QuadTree(Quad q, QuadTree parent) {
		this();
		this.parent = parent;
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

	public QuadTree nbr(Side s) {
		return nbrs[s.ordinal()];
	}

	public void setNbr(Side s, QuadTree t) {
		nbrs[s.ordinal()] = t;
	}

	public QuadTree child(Quad q) {
		return children[q.ordinal()];
	}

	public void setChild(Quad q, QuadTree t) {
		children[q.ordinal()] = t;
	}

	public QuadTree[] children(Side s) {
		QuadTree[] retval = new QuadTree[2];
		Quad qs[] = Quad.onSide(s);
		retval[0] = child(qs[0]);
		retval[1] = child(qs[1]);
		return retval;
	}

	public boolean hasChildren() {
		return children != null;
	}

	public void refine() {
		children = new QuadTree[4];
		for (Quad q : Quad.values()) {
			setChild(q, new QuadTree(q, this));
		}
		// set new neighbors
		// sw
		child(Quad.SW).setNbr(Side.EAST, child(Quad.SE));
		child(Quad.SW).setNbr(Side.NORTH, child(Quad.NW));
		// se
		child(Quad.SE).setNbr(Side.WEST, child(Quad.SW));
		child(Quad.SE).setNbr(Side.NORTH, child(Quad.NE));
		// nw
		child(Quad.NW).setNbr(Side.EAST, child(Quad.NE));
		child(Quad.NW).setNbr(Side.SOUTH, child(Quad.SW));
		// ne
		child(Quad.NE).setNbr(Side.WEST, child(Quad.NW));
		child(Quad.NE).setNbr(Side.SOUTH, child(Quad.SE));

		// refine neighbors if needed
		for (Side s : Side.values()) {
			if (nbr(s) == null && parent != null && parent.nbr(s) != null) {
				parent.nbr(s).refine();
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
				child(q).drawLeafs(g, x_orig, y_orig, size);
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
				for (QuadTree t : children) {
					t.refineAt(x, y);
				}
			} else {
				refine();
			}
		}
	}

	private boolean isPotentialNbr(Side s, double x, double y) {
		switch (s) {
		case WEST:
			return (x_start - x_length < x && x < x_start && y_start < y && y < y_start
					+ y_length);

		case EAST:
			return (x_start + x_length < x && x < x_start + 2 * x_length
					&& y_start < y && y < y_start + y_length);
		case SOUTH:
			return (x_start < x && x < x_start + x_length
					&& y_start - y_length < y && y < y_start);
		case NORTH:
			return (x_start < x && x < x_start + x_length
					&& y_start + y_length < y && y < y_start + 2 * y_length);
		}
		return false;

	}

	public void drawPotentialNbr(GraphicsContext g, int x_orig, int y_orig,
			int size, double x, double y) {
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
			for (QuadTree child : children) {
				child.reconcile();
			}
		}
	}

	public boolean isInside(double x, double y) {
		return (x_start < x && x < x_start + x_length && y_start < y && y < y_start
				+ y_length);
	}

	public void coarsenAt(double x, double y) {
		if (isInside(x, y)) {
			if (hasChildren()) {
				boolean have_coarsend = false;
				for (QuadTree t : children) {
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
			for (QuadTree t : children) {
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
			children = null;
		}
	}

	static public void indexNodes(QuadTree root) {
		int curr_i = 0;
		Queue<QuadTree> q = new LinkedList<QuadTree>();
		Set<QuadTree> visited = new HashSet<QuadTree>();
		q.add(root);
		while (!q.isEmpty()) {
			QuadTree curr = q.remove();
			visited.add(curr);
			curr.id = curr_i;
			curr_i++;
			for (QuadTree t : curr.nbrs) {
				if (t != null &&!q.contains(t)&& !visited.contains(t)) {
					q.add(t);
				}
			}
			if (curr.hasChildren()) {
				for (QuadTree t : curr.children) {
					if (t != null &&!q.contains(t)&& !visited.contains(t)) {
						q.add(t);
					}
				}
			}
		}
	}

}
