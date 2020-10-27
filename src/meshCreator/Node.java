package meshCreator;

/**
 * Represents a node in a tree
 * 
 * @author Scott Aiton
 */
public class Node {

	/**
	 * The number of Cartesian dimensions
	 */
	private int dimension;
	/**
	 * The number of nodes down from the root of the tree
	 */
	private int level;
	/**
	 * The coordinate of the lower left of the node
	 */
	private double starts[];
	/**
	 * The lengths of the node
	 */
	private double lengths[];

	/**
	 * A unique id in the forest
	 */
	private int id;
	/**
	 * The parent id, set to -1 if there is no parent
	 */
	private int parent_id;
	/**
	 * The ids of the neighboring nodes, set to -1 if there is no neighboring node
	 */
	private int nbr_ids[];
	/**
	 * The ids of the child nodes, all set to -1 if there are not children.
	 */
	private int child_ids[];

	/**
	 * Default constructor
	 * 
	 * All, ids will be set to -1, starts will be set to 0, and lengths will be set
	 * to 1
	 * 
	 * @param dimension the number of Cartesian dimensions
	 */
	public Node(int dimension) {
		this.dimension = dimension;
		level = 0;

		starts = new double[dimension];
		lengths = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			starts[i] = 0;
			lengths[i] = 1;
		}

		id = -1;
		parent_id = -1;

		nbr_ids = new int[Side.getNumSidesForDimension(dimension)];
		for (int i = 0; i < nbr_ids.length; i++) {
			nbr_ids[i] = -1;
		}

		child_ids = new int[Orthant.getNumOrthantsForDimension(dimension)];
		for (int i = 0; i < child_ids.length; i++) {
			child_ids[i] = -1;
		}
	}

	/**
	 * @return the id of this node
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id of this node
	 * 
	 * @param id the node's id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get the number of levels from the root of the tree
	 * 
	 * @return the number of levels
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Set the number of levels from the root
	 * 
	 * @param level the number of levels
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Check if there is a neighbor on a given side
	 * 
	 * @param s the side
	 * @return whether there is a neighbor
	 */
	public boolean hasNbr(Side s) {
		return nbr_ids[s.getIndex()] != -1;
	}

	/**
	 * Get the id of a neighbor on a given side
	 * 
	 * @param s the side
	 * @return the id of the neighbor, will be -1 if there is no neighbor
	 */
	public int getNbrId(Side s) {
		return nbr_ids[s.getIndex()];
	}

	/**
	 * Set the id of a neighbor
	 * 
	 * @param s      the side
	 * @param nbr_id the id
	 */
	public void setNbrId(Side s, int nbr_id) {
		nbr_ids[s.getIndex()] = nbr_id;
	}

	/**
	 * Check if there is a parent node
	 * 
	 * @return whether there is a parent node
	 */
	public boolean hasParent() {
		return parent_id != -1;
	}

	/**
	 * Get the id of the parent node
	 * 
	 * @return the id, will be -1 if there is no parent
	 */
	public int getParentId() {
		return parent_id;
	}

	/**
	 * Set the id of the parent node
	 * 
	 * @param parent_id the id
	 */
	public void setParentId(int parent_id) {
		this.parent_id = parent_id;
	}

	/**
	 * Check if this node has children
	 * 
	 * @return whether this node has children
	 */
	public boolean hasChildren() {
		return child_ids[0] != -1;
	}

	/**
	 * Get the id of a child
	 * 
	 * @param o the orthant of the child
	 * @return the id
	 */
	public int getChildId(Orthant o) {
		return child_ids[o.getIndex()];
	}

	/**
	 * Set the id of a child
	 * 
	 * @param o        the orthant of the child
	 * @param child_id the id
	 */
	public void setChildId(Orthant o, int child_id) {
		child_ids[o.getIndex()] = child_id;
	}

	/**
	 * Check if a coordinate is inside this node
	 * 
	 * @param coord the coordinate
	 * @return whether the coordinate is inside this node
	 */
	public boolean coordIsInside(double[] coord) {
		for (int i = 0; i < dimension; i++) {
			if (coord[i] < starts[i] || coord[i] > starts[i] + lengths[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the length along a given axis
	 * 
	 * @param axis the axis
	 * @return the length along the axis
	 */
	public double getLength(int axis) {
		return lengths[axis];
	}

	/**
	 * Set the length along a given axis
	 * 
	 * @param axis   the axis
	 * @param length the length
	 */
	public void setLength(int axis, double length) {
		lengths[axis] = length;
	}

	/**
	 * Get the starting coordinate along a given axis
	 * 
	 * @param axis the axis
	 * @return the starting coordinate along the axis
	 */
	public double getStart(int axis) {
		return starts[axis];
	}

	/**
	 * Set the starting coordinate along a given axis
	 * 
	 * @param axis  the axis
	 * @param start the starting coordinate
	 */
	public void setStart(int axis, double start) {
		starts[axis] = start;
	}

	/**
	 * Get the dimension
	 * @returnt the dimension
	 */
	public int getDimension() {
		return dimension;
	}
}
