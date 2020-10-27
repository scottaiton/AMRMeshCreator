package meshCreator;

import java.util.ArrayList;

/**
 * Represents the neighbor of a patch
 * 
 * @author Scott Aiton
 */
public class Neighbor {
	/**
	 * The side of the patch that the neighbor is on
	 */
	public Side side;
	/**
	 * The type of neighbor
	 */
	public String type;
	/**
	 * The ids of the neighboring patches
	 */
	public ArrayList<Integer> ids;
	/**
	 * The ranks of the neighboring patches
	 */
	public ArrayList<Integer> ranks;
	/**
	 * The orthant of the neighboring patch
	 */
	public Orthant orth_on_coarse;

	/**
	 * Create new Neighbor
	 * @param side the side that the neighbor is on
	 * @param type the type of neighbor can be "coarse", "fine", or "normal"
	 * @param ids the ids of the neighbors
	 * @param ranks the ranks of the neighbors 
	 * @param orth_on_coarse the orthant on the neighbor if "coarse", null otherwise
	 */
	public Neighbor(Side side, String type, ArrayList<Integer> ids, ArrayList<Integer> ranks, Orthant orth_on_coarse) {
		this.side = side;
		this.type = type;
		this.ids = ids;
		this.ranks = ranks;
		this.orth_on_coarse = orth_on_coarse;
	}
}
