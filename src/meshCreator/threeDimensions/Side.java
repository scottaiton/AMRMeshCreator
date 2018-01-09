package meshCreator.threeDimensions;

public enum Side {
	WEST, EAST, SOUTH, NORTH,BOTTOM,TOP;
	
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
		case BOTTOM:
			return TOP;
		case TOP:
			return BOTTOM;
		}
		return null;
	}
	
}
