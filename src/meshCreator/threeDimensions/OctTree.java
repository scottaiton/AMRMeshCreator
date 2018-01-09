package meshCreator.threeDimensions;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

public class OctTree {

	public int level;
	public double x_start = 0;
	public double y_start = 0;
	public double z_start = 0;
	public double x_length = 1;
	public double y_length = 1;
	public double z_length = 1;
	private boolean selected = false;
	public int id;
	private Cube pot_nbr;
	private Cube cube;
	private Cube outer_cube;

	public static Group cubes;
	private static PhongMaterial selected_mat;
	private static PhongMaterial wire_mat;
	private static PhongMaterial trans_mat;

	public OctTree parent;
	public OctTree children[];
	public OctTree nbrs[];

	public OctTree() {
		if (cubes == null) {
			cubes = new Group();
			selected_mat = new PhongMaterial();
			selected_mat.setDiffuseColor(Color.RED);
			selected_mat.setSpecularColor(Color.RED);
			wire_mat = new PhongMaterial();
			wire_mat.setDiffuseColor(Color.BLACK);
			wire_mat.setSpecularColor(Color.BLACK);
			trans_mat = new PhongMaterial();
			trans_mat.setDiffuseColor(Color.TRANSPARENT);
			trans_mat.setSpecularColor(Color.TRANSPARENT);
		}
		level = 1;
		nbrs = new OctTree[6];
		selected = true;
		generateCubes();
	}

	private void generateCubes() {
		cube = new Cube((float) x_start, (float) y_start, (float) z_start, (float) z_length);
		cube.setCullFace(CullFace.NONE);
		if (selected) {
			cube.setMaterial(selected_mat);
			cube.setDrawMode(DrawMode.FILL);
		} else {
			cube.setMaterial(wire_mat);
			cube.setDrawMode(DrawMode.LINE);
		}
		if(!hasChildren()) {
		cubes.getChildren().add(cube);
		}
		if (level == 1) {
			outer_cube = new Cube(x_start, y_start, z_start, z_length);
			outer_cube.setMaterial(trans_mat);
			outer_cube.setDrawMode(DrawMode.FILL);
			outer_cube.setOnMouseExited(e -> {
				cubes.getChildren().remove(pot_nbr);
			});
			outer_cube.setOnMouseMoved(e -> {
				PickResult pr = e.getPickResult();
				Side s = getClosestSide(pr.getIntersectedPoint());
				cubes.getChildren().remove(pot_nbr);
				double x = x_start;
				double y = y_start;
				double z = z_start;
				switch (s) {
				case WEST:
					x -= x_length;
					break;
				case EAST:
					x += x_length;
					break;
				case SOUTH:
					y -= y_length;
					break;
				case NORTH:
					y += y_length;
					break;
				case BOTTOM:
					z -= z_length;
					break;
				case TOP:
					z += z_length;
					break;
				}

				pot_nbr = new Cube(x, y, z, z_length);
				pot_nbr.setCullFace(CullFace.NONE);
				pot_nbr.setDrawMode(DrawMode.LINE);
				pot_nbr.setMaterial(wire_mat);
				cubes.getChildren().add(pot_nbr);
			});
			outer_cube.setOnMouseClicked(e -> {
				if (e.isStillSincePress()) {
					addPoentialNbr();
				}
			});
			cubes.getChildren().add(outer_cube);
		}
	}

	private void addPoentialNbr() {
		cubes.getChildren().remove(pot_nbr);
		new OctTree(pot_nbr, getPotentialNbrsOf(pot_nbr));
		pot_nbr = null;
	}

	private Collection<Pair<Side, OctTree>> getPotentialNbrsOf(Cube cube) {
		LinkedList<Pair<Side, OctTree>> nbrs = new LinkedList<Pair<Side, OctTree>>();
		Queue<OctTree> q = new LinkedList<OctTree>();
		Set<OctTree> visited = new HashSet<OctTree>();
		q.add(this);
		while (!q.isEmpty()) {
			OctTree curr = q.remove();
			visited.add(curr);
			for (Side s : Side.values()) {
				OctTree nbr = curr.nbr(s);
				if (nbr == null) {
					if (curr.isPotentialNbr(cube, s)) {
						nbrs.add(new Pair<Side, OctTree>(s, curr));
					}
				} else if (!q.contains(nbr) && !visited.contains(nbr)) {
					q.add(nbr);
				}
			}
		}
		return nbrs;
	}

	private boolean isPotentialNbr(Cube cube, Side s) {
		switch (s) {
		case WEST:
			return cube.x == x_start - x_length && cube.y == y_start && cube.z == z_start;
		case EAST:
			return cube.x == x_start + x_length && cube.y == y_start && cube.z == z_start;
		case SOUTH:
			return cube.x == x_start && cube.y == y_start - y_length && cube.z == z_start;
		case NORTH:
			return cube.x == x_start && cube.y == y_start + y_length && cube.z == z_start;
		case BOTTOM:
			return cube.x == x_start && cube.y == y_start && cube.z == z_start - z_length;
		case TOP:
			return cube.x == x_start && cube.y == y_start && cube.z == z_start + z_length;
		}
		return false;
	}

	public OctTree(Oct q, OctTree parent) {
		nbrs = new OctTree[6];
		selected = false;
		this.parent = parent;
		level = parent.level + 1;
		x_length = parent.x_length / 2;
		y_length = parent.y_length / 2;
		z_length = parent.z_length / 2;
		// x_start
		switch (q) {
		case BSW:
			selected = parent.selected;
		case BNW:
		case TSW:
		case TNW:
			x_start = parent.x_start;
			break;
		case BSE:
		case BNE:
		case TSE:
		case TNE:
			x_start = parent.x_start + x_length;
			break;
		}
		// y_start
		switch (q) {
		case BSW:
		case BSE:
		case TSW:
		case TSE:
			y_start = parent.y_start;
			break;
		case BNW:
		case BNE:
		case TNW:
		case TNE:
			y_start = parent.y_start + y_length;
			break;
		}
		// z_start
		switch (q) {
		case BSW:
		case BSE:
		case BNW:
		case BNE:
			z_start = parent.z_start;
			break;
		case TNW:
		case TNE:
		case TSW:
		case TSE:
			z_start = parent.z_start + z_length;
			break;
		}
		generateCubes();
	}

	public OctTree(Cube pot_nbr, Collection<Pair<Side, OctTree>> nbrs) {
		this.nbrs = new OctTree[6];
		x_start = pot_nbr.x;
		x_length = pot_nbr.length;
		y_start = pot_nbr.y;
		y_length = pot_nbr.length;
		z_start = pot_nbr.z;
		z_length = pot_nbr.length;
		level = 1;
		for (Pair<Side, OctTree> p : nbrs) {
			Side s = p.getLeft();
			OctTree t = p.getRight();
			t.setNbr(s, this);
			setNbr(s.opposite(), t);
		}
		generateCubes();
		reconcile();
	}

	public void finish_setup(ByteBuffer n_buf, Map<Integer, OctTree> map) {
		level = n_buf.getInt();
		int parent_id = n_buf.getInt();
		if (parent_id != -1) {
			if (map.containsKey(parent_id)) {
				parent = map.get(parent_id);
			} else {
				parent = new OctTree(parent_id);
				map.put(parent_id, parent);
			}
		}
		x_length = n_buf.getDouble();
		y_length = n_buf.getDouble();
		z_length = n_buf.getDouble();
		x_start = n_buf.getDouble();
		y_start = n_buf.getDouble();
		z_start = n_buf.getDouble();
		nbrs = new OctTree[6];
		for (Side s : Side.values()) {
			int nbr_id = n_buf.getInt();
			if (nbr_id != -1) {
				if (map.containsKey(nbr_id)) {
					setNbr(s, map.get(nbr_id));
				} else {
					setNbr(s, new OctTree(nbr_id));
					map.put(nbr_id, nbr(s));
				}
			}
		}
		for(int i=0;i<8;i++) {
			int child_id = n_buf.getInt();
			if (child_id != -1) {
				if(children==null) {
					children= new OctTree[8];
				}
				if (map.containsKey(child_id)) {
					children[i]= map.get(child_id);
				} else {
					children[i]= new OctTree(child_id);
					map.put(child_id, children[i]);
				}
			}
		}
		generateCubes();
	}

	public OctTree(int id) {
		this.id = id;
	}

	public OctTree nbr(Side s) {
		return nbrs[s.ordinal()];
	}

	public void setNbr(Side s, OctTree t) {
		nbrs[s.ordinal()] = t;
	}

	public OctTree child(Oct q) {
		return children[q.ordinal()];
	}

	public void setChild(Oct q, OctTree t) {
		children[q.ordinal()] = t;
	}

	public OctTree[] children(Side s) {
		OctTree[] retval = new OctTree[4];
		Oct qs[] = Oct.onSide(s);
		for (int i = 0; i < 4; i++) {
			retval[i] = child(qs[i]);
		}
		return retval;
	}

	public boolean hasChildren() {
		return children != null;
	}

	public void refine() {
		children = new OctTree[8];
		for (Oct q : Oct.values()) {
			setChild(q, new OctTree(q, this));
		}
		// set new neighbors
		for (Oct q : Oct.values()) {
			for (Side s : Oct.sides(q)) {
				child(q).setNbr(s, child(Oct.getOct(q, s)));
			}

		}

		// refine neighbors if needed
		for (Side s : Side.values()) {
			if (nbr(s) == null && parent != null && parent.nbr(s) != null) {
				parent.nbr(s).refine();
			}
		}

		// set outer neighbors
		for (Side s : Side.values()) {
			if (nbr(s) != null && nbr(s).hasChildren()) {
				OctTree childs[] = children(s);
				OctTree nbr_childs[] = nbr(s).children(s.opposite());
				for (int i = 0; i < 4; i++) {
					childs[i].setNbr(s, nbr_childs[i]);
					nbr_childs[i].setNbr(s.opposite(), childs[i]);
				}
			}
		}
		cubes.getChildren().remove(cube);
	}

	public void reconcile() {
		for (Side s : Side.values()) {
			if (nbr(s) != null && nbr(s).hasChildren()) {
				for (OctTree nbr_child : nbr(s).children(s.opposite())) {
					if (nbr_child.hasChildren()) {
						refine();
						break;
					}
				}
			}
		}
		if (hasChildren()) {
			for (OctTree child : children) {
				child.reconcile();
			}
		}
	}

	public void coarsen() {
		if (hasChildren()) {
			for (OctTree t : children) {
				// coarsen children if needed
				if (t.hasChildren()) {
					t.coarsen();
				}
				for (Side s : Side.values()) {
					OctTree nbr = t.nbr(s);
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
				cubes.getChildren().remove(t.cube);
			}
			children = null;
			cubes.getChildren().add(cube);
		}
	}

	static public int indexNodes(OctTree root) {
		int curr_i = 0;
		Queue<OctTree> q = new LinkedList<OctTree>();
		Set<OctTree> visited = new HashSet<OctTree>();
		q.add(root);
		while (!q.isEmpty()) {
			OctTree curr = q.remove();
			visited.add(curr);
			curr.id = curr_i;
			curr_i++;
			for (OctTree t : curr.nbrs) {
				if (t != null && !q.contains(t) && !visited.contains(t)) {
					q.add(t);
				}
			}
			if (curr.hasChildren()) {
				for (OctTree t : curr.children) {
					if (t != null && !q.contains(t) && !visited.contains(t)) {
						q.add(t);
					}
				}
			}
		}
		return visited.size();
	}

	private Side getClosestSide(Point3D intersect) {
		Point3D center = new Point3D(x_start + x_length / 2, y_start + y_length / 2, z_start + z_length / 2);
		Point3D diff = intersect.subtract(center);
		double x = diff.getX();
		double y = diff.getY();
		double z = diff.getZ();
		Side retval;
		if (Math.abs(x) > Math.abs(y) && Math.abs(x) > Math.abs(z)) {
			if (x < 0) {
				retval = Side.WEST;
			} else {
				retval = Side.EAST;
			}
		} else if (Math.abs(y) > Math.abs(z)) {
			if (y < 0) {
				retval = Side.SOUTH;
			} else {
				retval = Side.NORTH;
			}
		} else {
			if (z < 0) {
				retval = Side.BOTTOM;
			} else {
				retval = Side.TOP;
			}
		}
		return retval;
	}

	private boolean attemptMoveSelected(Side s) {
		if (hasChildren()) {
			for (Oct q : Oct.values()) {
				if (child(q).attemptMoveSelected(s))
					return true;
			}
		}
		if (!selected) {
			return false;
		} else {
			if (nbr(s) == null && parent != null && parent.nbr(s) != null) {
				// coarse case
				OctTree nbr = parent.nbr(s);
				toggleSelected();
				nbr.toggleSelected();
			} else if (nbr(s) != null && nbr(s).hasChildren()) {
				// refined case
				OctTree[] nbr_children = nbr(s).children(s.opposite());
				toggleSelected();
				nbr_children[0].toggleSelected();
			} else if (nbr(s) != null) {
				// normal case
				toggleSelected();
				nbr(s).toggleSelected();
			}
			return true;
		}
	}

	public void moveSelected(Side move_s) {
		Queue<OctTree> q = new LinkedList<OctTree>();
		Set<OctTree> visited = new HashSet<OctTree>();
		q.add(this);
		while (!q.isEmpty()) {
			OctTree curr = q.remove();
			if (curr.attemptMoveSelected(move_s)) {
				break;
			}
			visited.add(curr);
			for (Side s : Side.values()) {
				OctTree nbr = curr.nbr(s);
				if (nbr != null && !q.contains(nbr) && !visited.contains(nbr)) {
					q.add(nbr);
				}
			}
		}
	}

	public void toggleSelected() {
		selected = !selected;
		if (selected) {
			cube.setMaterial(selected_mat);
			cube.setDrawMode(DrawMode.FILL);
		} else {
			cube.setMaterial(wire_mat);
			cube.setDrawMode(DrawMode.LINE);
		}
	}

	private boolean attemptRefineSelected() {
		if (hasChildren()) {
			for (Oct q : Oct.values()) {
				if (child(q).attemptRefineSelected())
					return true;
			}
		}
		if (!selected) {
			return false;
		} else {
			refine();
			toggleSelected();
			return true;
		}
	}

	public void refineSelected() {
		Queue<OctTree> q = new LinkedList<OctTree>();
		Set<OctTree> visited = new HashSet<OctTree>();
		q.add(this);
		while (!q.isEmpty()) {
			OctTree curr = q.remove();
			if (curr.attemptRefineSelected()) {
				break;
			}
			visited.add(curr);
			for (Side s : Side.values()) {
				OctTree nbr = curr.nbr(s);
				if (nbr != null && !q.contains(nbr) && !visited.contains(nbr)) {
					q.add(nbr);
				}
			}
		}
	}

	private boolean attemptCoarsenSelected() {
		if (hasChildren()) {
			for (OctTree t : children) {
				if (t.selected) {
					coarsen();
					toggleSelected();
					return true;
				} else if (t.attemptCoarsenSelected()) {
					return true;
				}
			}
		}
		return false;
	}

	public void coarsenSelected() {
		Queue<OctTree> q = new LinkedList<OctTree>();
		Set<OctTree> visited = new HashSet<OctTree>();
		q.add(this);
		while (!q.isEmpty()) {
			OctTree curr = q.remove();
			if (curr.attemptCoarsenSelected()) {
				break;
			}
			visited.add(curr);
			for (Side s : Side.values()) {
				OctTree nbr = curr.nbr(s);
				if (nbr != null && !q.contains(nbr) && !visited.contains(nbr)) {
					q.add(nbr);
				}
			}
		}
	}

}
