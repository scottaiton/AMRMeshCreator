package meshCreator.twoDimensions;

public enum Quad {
	SW, SE, NW, NE;

	public static Quad[] onSide(Side s) {
		Quad retval[] = new Quad[2];
		if (s.equals(Side.WEST())) {
			retval[0] = SE;
			retval[1] = NE;
		} else if (s.equals(Side.EAST())) {
			retval[0] = SW;
			retval[1] = NW;
		} else if (s.equals(Side.SOUTH())) {
			retval[0] = SW;
			retval[1] = SE;
		} else if (s.equals(Side.NORTH())) {
			retval[0] = NW;
			retval[1] = NE;
		}
		return retval;
	}
}
