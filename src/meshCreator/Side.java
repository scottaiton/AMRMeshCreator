package meshCreator;

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

	/**
	 * Get the number of valid values for a given dimension
	 * 
	 * @param dimension the number of Cartesian dimensions
	 * @return the number of values
	 */
	public static int getNumSidesForDimension(int dimension) {
		return 2 * dimension;
	}

	/**
	 * Get an array of all the valid values for a given dimension
	 * 
	 * @param dimension the number of Cartesian dimensions
	 * @return the array of values
	 */
	public static Side[] getValuesForDimension(int dimension) {
		Side[] ret = new Side[getNumSidesForDimension(dimension)];
		for (int i = 0; i < getNumSidesForDimension(dimension); i++) {
			ret[i] = new Side(dimension, i);
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

	@Override
	public int hashCode() {
		return (dimension << 16) | val;
	}

	/**
	 * Get the axis that this side lies on
	 * 
	 * @return the axis
	 */
	public Integer getAxis() {
		return val >> 1;
	}

	/**
	 * Return whether this side is lower on its axis
	 * 
	 * @return whether this side is lower on its axis
	 */
	public boolean isLowerOnAxis() {
		return (val & 0b1) == 0;
	}

	@Override
	public String toString() {
		String str = new String();
		if (dimension == 2) {
			if (this.equals(Side.WEST())) {
				str = "WEST";
			} else if (this.equals(Side.EAST())) {
				str = "EAST";
			} else if (this.equals(Side.SOUTH())) {
				str = "SOUTH";
			} else if (this.equals(Side.NORTH())) {
				str = "NORTH";
			}
		}
		if (str.isEmpty()) {
			str = "Dimension: " + dimension + " Value: " + val;
		}
		return str;
	}

}
