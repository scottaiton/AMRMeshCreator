package meshCreator.twoDimensions;

public class Orthant {
	/**
	 * The number of Cartesian dimensions
	 */
	private int dimension;
	/**
	 * The value of this orthant
	 */
	private int val;

	/**
	 * Construct a new Orthant
	 * 
	 * @param dimension the number of Cartesian dimensions
	 * @param val       the value
	 */
	public Orthant(int dimension, int val) {
		this.dimension = dimension;
		this.val = val;
	}

	/**
	 * Get the number of valid values for a given dimension
	 * 
	 * @param dimension the number of Cartesian dimensions
	 * @return the number of values
	 */
	public static int getNumOrthantsForDimension(int dimension) {
		return 0b1 << dimension;
	}

	/**
	 * Get an array of all the valid values for a given dimension
	 * 
	 * @param dimension the number of Cartesian dimensions
	 * @return the array of values
	 */
	public static Orthant[] getValuesForDimension(int dimension) {
		Orthant[] ret = new Orthant[getNumOrthantsForDimension(dimension)];
		for (int i = 0; i < getNumOrthantsForDimension(dimension); i++) {
			ret[i] = new Orthant(dimension, i);
		}
		return ret;
	}

	/**
	 * Southwestern Quadrant
	 * 
	 * @return the new Orthant
	 */
	public static Orthant SW() {
		return new Orthant(2, 0b00);
	}

	/**
	 * Southeastern Quadrant
	 * 
	 * @return the new Orthant
	 */
	public static Orthant SE() {
		return new Orthant(2, 0b01);
	}

	/**
	 * Northwestern Quadrant
	 * 
	 * @return the new Orthant
	 */
	public static Orthant NW() {
		return new Orthant(2, 0b10);
	}

	/**
	 * Northeastern Quadrant
	 * 
	 * @return the new Orthant
	 */
	public static Orthant NE() {
		return new Orthant(2, 0b11);
	}

	/**
	 * Get the index of this orthant
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return val;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Orthant)) {
			return false;
		}

		Orthant orth = (Orthant) o;

		return orth.dimension == dimension && orth.val == val;
	}

	/**
	 * Return whether or not the Orthant is lower on a specific axis
	 * 
	 * @param axis the axis
	 * @return whether or not it's lower
	 */
	public boolean isLowerOnAxis(int axis) {
		return (val & (0b1 << axis)) == 0;
	}

	/**
	 * Get the exterior sides on this orthant
	 * 
	 * @return the sides, in sorted order
	 */
	public Side[] getExteriorSides() {
		Side[] ret = new Side[dimension];
		for (int i = 0; i < dimension; i++) {
			int side_val = ((i << 1) | ((val >> i) & 0b1));
			ret[i] = new Side(dimension, side_val);
		}
		return ret;
	}

	/**
	 * Get the interior sides on this orthant
	 * 
	 * @return the sides, in sorted order
	 */
	public Side[] getInteriorSides() {
		Side[] ret = new Side[dimension];
		for (int i = 0; i < dimension; i++) {
			int side_val = ((i << 1) | ((val >> i) & 0b1)) ^ 0b1;
			ret[i] = new Side(dimension, side_val);
		}
		return ret;
	}

	/**
	 * Get the neighboring orthant on a particular side
	 * 
	 * @param s the side
	 * @return the orthant
	 */
	public Orthant getNbrOnSide(Side s) {
		int nbr_val = val ^ (1 << s.getAxis());
		return new Orthant(dimension, nbr_val);
	}

	/**
	 * Get the orthants that lie on a particular side.
	 * 
	 * @param dimension the number of Cartesian dimensions
	 * @param s         the side
	 * @return the orthants, in sorted order
	 */
	public static Orthant[] GetValuesOnSide(int dimension, Side s) {
		Orthant[] ret = new Orthant[getNumOrthantsForDimension(dimension) / 2];
		int bit_to_insert = s.getAxis();
		int set_bit = s.isLowerOnAxis() ? 0x0 : 0x1;
		int lower_mask = ~((~0x0) << bit_to_insert);
		int upper_mask = (~0x0) << (bit_to_insert + 1);
		for (int i = 0; i < ret.length; i++) {
			int new_value = (i << 1) & upper_mask;
			new_value |= i & lower_mask;
			new_value |= set_bit << bit_to_insert;
			ret[i] = new Orthant(dimension, new_value);
		}
		return ret;
	}

	@Override
	public String toString() {
		String ret = "[";
		Side[] exterior_sides = getExteriorSides();
		for (int i = 1; i < dimension; i++) {
			ret += exterior_sides[dimension - i].toString() + ", ";

		}
		ret += exterior_sides[0]+"]";
		return ret;
	}

}
