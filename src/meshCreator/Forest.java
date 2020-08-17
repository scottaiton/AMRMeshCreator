package meshCreator;

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
	}

	/**
	 * Get a new child node given the parent node and orthant
	 * 
	 * @param parent the parent node
	 * @param q      the orthant of the child
	 * @return the new child
	 */
	private Node getNewChild(Node parent, Orthant q) {
		Node node = new Node(dimension);
		node.setId(curr_id);
		curr_id++;
		nodes.put(node.getId(), node);
		node.setParentId(parent.getId());
		node.setLevel(parent.getLevel() + 1);

		for (int axis = 0; axis < 2; axis++) {
			node.setLength(axis, parent.getLength(axis) / 2);
			if (q.isLowerOnAxis(axis)) {
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Refine the leaf at the given coordinate. If there is no leaf at that
	 * coordinate, nothing will happen.
	 * 
	 * @param coord the coordinate
	 */
	public void refineAt(double[] coord) {
		// TODO Auto-generated method stub

	}

	/**
	 * Coarsen the leaf at the given coordinate. If there is no leaf at that
	 * coordinate, nothing will happen.
	 * 
	 * @param coord the coordinate
	 */
	public void coarsenAt(double[] coord) {
		// TODO Auto-generated method stub

	}

	/**
	 * Add a neighbor at the given coordinate. If there is is impossible to add a
	 * neighbor at that coordinate, nothing will happen.
	 * 
	 * @param coord the coordinate
	 */
	public void addNbrAt(double[] coord) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Get the maximum depth of the forest
	 * 
	 * @return the maximum depth
	 */
	public int getMaxLevel() {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get the node, given the id
	 * 
	 * @param id the id of the node to get
	 * @return the node
	 */
	public Node getNode(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Coarsen the given node
	 * 
	 * @param id the id of the node to coarsen
	 */
	public void coarsenNode(int id) {
		// TODO Auto-generated method stub
	}

	/**
	 * Get the leaf at a given coordinate
	 * 
	 * @param coord the coordinate
	 * @return the leaf, null if there is no leaf
	 */
	public Node getLeafAt(double coord[]) {
		// TODO Auto-generated method stub
		return null;
	}

}
