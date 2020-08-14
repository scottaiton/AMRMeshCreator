package meshCreator.twoDimensions;

import java.util.ArrayList;

public class Side {
	/**
	 * The number of Cartesian dimensions
	 */
	private int dimension;
	/**
	 * The value of this side
	 */
	private int val;

	/**
	 * Construct new Side
	 * 
	 * @param dimension the number of Cartesian dimensions
	 * @param val       the value
	 */
	public Side(int dimension, int val) {
		this.dimension = dimension;
		this.val = val;
	}

	public static int getNumSidesForDimension(int dimension) {
		return 2 * dimension;
	}

	public static ArrayList<Side> getValuesForDimension(int dimension) {
		ArrayList<Side> ret = new ArrayList<Side>(getNumSidesForDimension(dimension));
		for (int i = 0; i < getNumSidesForDimension(dimension); i++) {
			ret.add(new Side(dimension, i));
		}
		return ret;
	}

	/**
	 * Western side
	 * 
	 * @return a new side object
	 */
	public static Side WEST() {
		return new Side(2, 0b00);
	}

	/**
	 * Eastern side
	 * 
	 * @return a new side object
	 */
	public static Side EAST() {
		return new Side(2, 0b01);
	}

	/**
	 * Southern side
	 * 
	 * @return a new side object
	 */
	public static Side SOUTH() {
		return new Side(2, 0b10);
	}

	/**
	 * Northern side
	 * 
	 * @return a new side object
	 */
	public static Side NORTH() {
		return new Side(2, 0b11);
	}

	/**
	 * Get the index of this side
	 * 
	 * @return the index of this side
	 */
	public int getIndex() {
		return val;
	}

	/**
	 * Get the side opposite on this sides' axis
	 * 
	 * @return the opposite side
	 */
	public Side getOpposite() {
		int new_val = val ^ 0x1; // flip the last bit
		return new Side(dimension, new_val);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Side)) {
			return false;
		}

		Side s = (Side) o;

		return s.dimension == dimension && s.val == val;
	}

}
