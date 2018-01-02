package twoDimensions;
public enum Quad {
	SW, SE, NW, NE;

	public static Quad[] onSide(Side s) {
		Quad retval[] = new Quad[2];
		switch(s){
		case EAST:
			retval[0]=SE;
			retval[1]=NE;
			break;
		case WEST:
			retval[0]=SW;
			retval[1]=NW;
			break;
		case SOUTH:
			retval[0]=SW;
			retval[1]=SE;
			break;
		case NORTH:
			retval[0]=NW;
			retval[1]=NE;
			break;
		}
		return retval;
	}
}
