package meshCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a forest of trees
 * 
 * @author Scott Aiton
 */
public class Forest {

	/**
	 * The dimension of the forest
	 */
	private int dimension;
	/**
	 * The next available id
	 */
	private int curr_id;
	/**
	 * Map of id to nodes
	 */
	private Map<Integer, Node> nodes;
	/**
	 * The ids of the root nodes
	 */
	private Set<Integer> root_ids;

	/**
	 * Create a new forest with given dimensionality
	 * 
	 * @param dimension the number of Cartesian dimensions
	 */
	public Forest(int dimension) {
		this.dimension = dimension;
		curr_id = 0;
		Node root = new Node(dimension);
		root.setId(curr_id);
		curr_id++;

		nodes = new HashMap<Integer, Node>();
		nodes.put(root.getId(), root);

		root_ids = new HashSet<Integer>();
		root_ids.add(root.getId());
	}

	/**
	 * Get a new child node given the parent node and orthant
	 * 
	 * @param parent the parent node
	 * @param q      the orthant of the child
	 * @return the new child, the neighbor ids will not be set
	 */
	private Node getNewChild(Node parent, Orthant o) {
		Node node = new Node(dimension);

		node.setId(curr_id);
		curr_id++;
		nodes.put(node.getId(), node);

		node.setParentId(parent.getId());
		node.setLevel(parent.getLevel() + 1);

		parent.setChildId(o, node.getId());

		for (int axis = 0; axis < 2; axis++) {
			node.setLength(axis, parent.getLength(axis) / 2);
			if (o.isLowerOnAxis(axis)) {
				node.setStart(axis, parent.getStart(axis));
			} else {
				node.setStart(axis, parent.getStart(axis) + node.getLength(axis));
			}
		}

		return node;
	}

	/**
	 * Get a set of the root ids
	 * 
	 * @return the set
	 */
	public Set<Integer> getRootIds() {
		return new HashSet<Integer>(root_ids);
	}

	/**
	 * Refine the given node
	 * 
	 * @param node the node to refine
	 */
	private void refineNode(Node node) {
		// get new children
		Node[] children = new Node[Orthant.getNumOrthantsForDimension(dimension)];
		for (Orthant o : Orthant.getValuesForDimension(dimension)) {
			children[o.getIndex()] = getNewChild(node, o);
		}
		// set new neighbors
		for (Orthant o : Orthant.getValuesForDimension(dimension)) {
			for (Side s : o.getInteriorSides()) {
				children[o.getIndex()].setNbrId(s, children[o.getNbrOnSide(s).getIndex()].getId());
			}

		}

		// refine parent neighbors if needed
		Node parent = getNode(node.getParentId());
		if (parent != null) {
			for (Side s : Side.getValuesForDimension(dimension)) {
				if (!node.hasNbr(s) && parent.hasNbr(s)) {
					refineNode(getNode(parent.getNbrId(s)));
				}
			}
		}

		// set outer neighbors
		for (Side s : Side.getValuesForDimension(dimension)) {
			Node nbr = getNode(node.getNbrId(s));
			if (nbr != null && nbr.hasChildren()) {
				for (Orthant o : Orthant.GetValuesOnSide(dimension, s)) {
					Node child_nbr = getNode(nbr.getChildId(o.getNbrOnSide(s)));
					children[o.getIndex()].setNbrId(s, child_nbr.getId());
					child_nbr.setNbrId(s.getOpposite(), node.getChildId(o));
				}
			}
		}
	}

	/**
	 * Refine the leaf at the given coordinate. If there is no leaf at that
	 * coordinate, nothing will happen.
	 * 
	 * @param coord the coordinate
	 */
	public void refineAt(double[] coord) {
		Node node = getLeafAt(coord);
		if (node != null) {
			refineNode(node);
		}
	}

	/**
	 * Remove the children the given node
	 * 
	 * @param node the node to refine
	 */
	private void removeChildren(Node node) {
		if (node.hasChildren()) {
			for (Orthant o : Orthant.getValuesForDimension(dimension)) {
				Node child = getNode(node.getChildId(o));

				// remove children
				removeChildren(child);

				// check if child's neighbors need children removed
				for (Side s : Side.getValuesForDimension(dimension)) {
					if (child.hasNbr(s)) {
						Node child_nbr = getNode(child.getNbrId(s));
						// coarsen nbr if needed
						if (child_nbr.hasChildren()) {
							removeChildren(child_nbr);
						}
						// set nbr ids to -1
						child_nbr.setNbrId(s.getOpposite(), -1);
					}
				}
				// remove child from nodes map
				nodes.remove(child.getId());
				// set child id to -1
				node.setChildId(o, -1);
			}
		}
	}

	/**
	 * Coarsen the leaf at the given coordinate. If there is no leaf at that
	 * coordinate, nothing will happen.
	 * 
	 * @param coord the coordinate
	 */
	public void coarsenAt(double[] coord) {
		Node node = getLeafAt(coord);
		if (node != null && node.hasParent()) {
			removeChildren(getNode(node.getParentId()));
		}
	}

	/**
	 * Add a neighbor at the given coordinate. If there is is impossible to add a
	 * neighbor at that coordinate, nothing will happen.
	 * 
	 * @param coord the coordinate
	 */
	public void addNbrAt(double[] coord) {
		Node new_root = null;
		for (int root_id : root_ids) {
			Node root = getNode(root_id);
			for (Side s : Side.getValuesForDimension(dimension)) {
				if (coordIsPotentialNbrOnSide(root, coord, s)) {
					if (new_root == null) {
						new_root = new Node(dimension);
						new_root.setId(curr_id);
						nodes.put(curr_id, new_root);
						curr_id++;
						for (int i = 0; i < dimension; i++) {
							new_root.setLength(i, root.getLength(i));
							new_root.setStart(i, root.getStart(i));
						}
						if (s.isLowerOnAxis()) {
							new_root.setStart(s.getAxis(),
									new_root.getStart(s.getAxis()) - new_root.getLength(s.getAxis()));
						} else {
							new_root.setStart(s.getAxis(),
									new_root.getStart(s.getAxis()) + new_root.getLength(s.getAxis()));

						}
					}
					root.setNbrId(s, new_root.getId());
					new_root.setNbrId(s.getOpposite(), root.getId());
				}
			}
		}
		if (new_root != null) {
			root_ids.add(new_root.getId());
			reconcileRoot(new_root);
		}

	}

	/**
	 * Add necessary children to make sure refinement maintains 2:1 balance
	 * 
	 * @param node the node to reconcile
	 */
	private void reconcileRoot(Node new_root) {
		for (Side s : Side.getValuesForDimension(dimension)) {
			if (new_root.hasNbr(s)) {
				reconcileOnSide(new_root, s);
			}
		}
	}

	/**
	 * Add necessary children to make sure refinement on given side maintains 2:1
	 * balance
	 * 
	 * @param node the node to reconcile
	 * @param s    the side to reconcile on
	 */
	private void reconcileOnSide(Node node, Side s) {
		if (node.hasNbr(s)) {
			Node nbr = getNode(node.getNbrId(s));
			if (!node.hasChildren() && nbr.hasChildren()) {
				for (Orthant o : Orthant.GetValuesOnSide(dimension, s.getOpposite())) {
					Node nbr_child = getNode(nbr.getChildId(o));
					if (nbr_child.hasChildren()) {
						refineNode(node);
						break;
					}
				}
			}
			if (node.hasChildren()) {
				for (Orthant o : Orthant.GetValuesOnSide(dimension, s)) {
					Node child = getNode(node.getChildId(o));
					reconcileOnSide(child, s);
				}
			}
		}
	}

	/**
	 * Check if the coordinate is a potential neighbor on a given side of the patch
	 * 
	 * @param node  the node
	 * @param coord the coordinate
	 * @param s     the side
	 * @return true if it is a potential neighbor on that side
	 */
	private boolean coordIsPotentialNbrOnSide(Node node, double[] coord, Side s) {
		boolean is_potential = false;
		if (!node.hasNbr(s)) {
			double[] nbr_start = new double[dimension];
			double[] nbr_lengths = new double[dimension];
			for (int i = 0; i < dimension; i++) {
				nbr_start[i] = node.getStart(i);
				nbr_lengths[i] = node.getLength(i);
			}
			if (s.isLowerOnAxis()) {
				nbr_start[s.getAxis()] -= nbr_lengths[s.getAxis()];
			} else {
				nbr_start[s.getAxis()] += nbr_lengths[s.getAxis()];
			}
			is_potential = true;
			for (int i = 0; i < dimension; i++) {
				if (coord[i] < nbr_start[i] || coord[i] > nbr_start[i] + nbr_lengths[i]) {
					is_potential = false;
				}
			}

		}
		return is_potential;
	}

	/**
	 * Check if the given coordinate is a potential neighbor
	 * 
	 * @param coord   the coordinate
	 * @param starts  return the starting coordinate of the potential neighbor
	 * @param lengths return the starting lengths of the potential neighbor
	 * @return true if it is a potential neighbor, starts and lengths will be set
	 */
	public boolean coordIsPotentialNbr(double[] coord, double[] starts, double[] lengths) {
		for (int root_id : root_ids) {
			Node root = getNode(root_id);
			for (Side s : Side.getValuesForDimension(dimension)) {
				if (coordIsPotentialNbrOnSide(root, coord, s)) {
					for (int i = 0; i < dimension; i++) {
						starts[i] = root.getStart(i);
						lengths[i] = root.getLength(i);
					}
					if (s.isLowerOnAxis()) {
						starts[s.getAxis()] -= lengths[s.getAxis()];
					} else {
						starts[s.getAxis()] += lengths[s.getAxis()];
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the maximum depth of the forest
	 * 
	 * @return the maximum depth
	 */
	public int getMaxLevel() {
		int max = 0;
		for (Node node : nodes.values()) {
			max = Math.max(max, node.getLevel());
		}
		return max;
	}

	/**
	 * Get a deep copy of the forest
	 * 
	 * @return the copy
	 */
	public Forest deepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get to starting root node
	 * 
	 * @return the starting root node
	 */
	public Node getRootNode() {
		return nodes.get(0);
	}

	/**
	 * Get the node, given the id
	 * 
	 * @param id the id of the node to get
	 * @return the node, null if the node does not exist
	 */
	public Node getNode(int id) {
		return nodes.get(id);
	}

	/**
	 * Coarsen the given node
	 * 
	 * @param id the id of the node to coarsen
	 */
	public void coarsenNode(int id) {
		Node node = getNode(id);
		if (node != null && node.hasParent()) {
			removeChildren(getNode(node.getParentId()));
		}
	}

	/**
	 * Get the leaf at a given coordinate
	 * 
	 * @param coord the coordinate
	 * @return the leaf, null if there is no leaf
	 */
	public Node getLeafAt(double coord[]) {
		for (int root_id : root_ids) {
			Node root = getNode(root_id);
			if (root.coordIsInside(coord)) {
				return getLeafAt(root, coord);
			}
		}
		return null;
	}

	/**
	 * Given a node and a coordinate, get the leaf at that coordinate
	 * 
	 * @param node  the node
	 * @param coord the coordinate
	 * @return the leaf
	 */
	private Node getLeafAt(Node node, double[] coord) {
		if (node.hasChildren()) {
			Node child = getNode(node.getChildId(getOrthant(node, coord)));
			return getLeafAt(child, coord);
		} else {
			return node;
		}
	}

	/**
	 * Get the orthant of the node that the coordinate lies in
	 * 
	 * @param node  the node
	 * @param coord the coordinate
	 * @return the orthant that the node lies in
	 */
	private Orthant getOrthant(Node node, double[] coord) {
		int orth_val = 0;
		for (int i = 0; i < dimension; i++) {
			double mid_point = node.getStart(i) + node.getLength(i) / 2;
			if (coord[i] >= mid_point) {
				orth_val |= 0b1 << i;
			}
		}
		return new Orthant(dimension, orth_val);
	}

}
