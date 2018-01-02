package threeDimensions;

public enum Side {
	WEST, EAST, SOUTH, NORTH;
	
	public Side opposite() {
		switch(this){
		case EAST:
			return WEST;
		case WEST:
			return EAST;
		case SOUTH:
			return NORTH;
		case NORTH:
			return SOUTH;
		}
		return NORTH;
	}
	
}
