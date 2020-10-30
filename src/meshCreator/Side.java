package meshCreator;

import java.util.ArrayList;

public class Side {
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
	public Side(int val) {
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
			ret[i] = new Side(i);
		}
		return ret;
	}

	/**
	 * Western side
	 * 
	 * @return a new side object
	 */
	public static Side WEST() {
		return new Side(0b00);
	}

	/**
	 * Eastern side
	 * 
	 * @return a new side object
	 */
	public static Side EAST() {
		return new Side(0b01);
	}

	/**
	 * Southern side
	 * 
	 * @return a new side object
	 */
	public static Side SOUTH() {
		return new Side(0b10);
	}

	/**
	 * Northern side
	 * 
	 * @return a new side object
	 */
	public static Side NORTH() {
		return new Side(0b11);
	}

	/**
	 * Bottom side
	 * 
	 * @return a new side object
	 */
	public static Side BOTTOM() {
		return new Side(0b100);
	}

	/**
	 * Top side
	 * 
	 * @return a new side object
	 */
	public static Side TOP() {
		return new Side(0b101);
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
		return new Side(new_val);
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

		return s.val == val;
	}

	@Override
	public int hashCode() {
		return val;
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
		if (this.equals(Side.WEST())) {
			str = "WEST";
		} else if (this.equals(Side.EAST())) {
			str = "EAST";
		} else if (this.equals(Side.SOUTH())) {
			str = "SOUTH";
		} else if (this.equals(Side.NORTH())) {
			str = "NORTH";
		} else if (this.equals(Side.BOTTOM())) {
			str = "BOTTOM";
		} else if (this.equals(Side.TOP())) {
			str = "TOP";
		} else {
			str = "Value: " + val;
		}
		return str;
	}

	public static Side fromString(String str) {
		Side ret = null;
		if(str.equals("WEST")) {
			ret = Side.WEST();
		}else if(str.equals("EAST")) {
			ret = Side.EAST();
		}else if(str.equals("SOUTH")) {
			ret = Side.SOUTH();
		}else if(str.equals("NORTH")) {
			ret = Side.NORTH();
		}else if(str.equals("BOTTOM")) {
			ret = Side.BOTTOM();
		}else if(str.equals("TOP")) {
			ret = Side.TOP();
		}else {
			ret = new Side(Integer.parseInt(str.substring(7)));
		}
		return ret;
	}

}
