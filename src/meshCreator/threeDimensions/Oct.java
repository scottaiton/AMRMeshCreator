package meshCreator.threeDimensions;

public enum Oct {
	BSW, BSE, BNW, BNE, TSW, TSE, TNW, TNE;

	public static Oct[] onSide(Side s) {
		Oct retval[] = new Oct[4];
		switch (s) {
		case EAST:
			retval[0] = BSE;
			retval[1] = BNE;
			retval[2] = TSE;
			retval[3] = TNE;
			break;
		case WEST:
			retval[0] = BSW;
			retval[1] = BNW;
			retval[2] = TSW;
			retval[3] = TNW;
			break;
		case SOUTH:
			retval[0] = BSW;
			retval[1] = BSE;
			retval[2] = TSW;
			retval[3] = TSE;
			break;
		case NORTH:
			retval[0] = BNW;
			retval[1] = BNE;
			retval[2] = TNW;
			retval[3] = TNE;
			break;
		case BOTTOM:
			retval[0] = BSW;
			retval[1] = BSE;
			retval[2] = BNW;
			retval[3] = BNE;
			break;
		case TOP:
			retval[0] = TSW;
			retval[1] = TSE;
			retval[2] = TNW;
			retval[3] = TNE;
			break;
		}
		return retval;
	}

	public static Side[] sides(Oct q) {
		Side retval[] = new Side[3];
		// x direction
		switch (q) {
		case BSE:
		case BNE:
		case TNE:
		case TSE:
			retval[0] = Side.WEST;
			break;
		case BSW:
		case BNW:
		case TSW:
		case TNW:
			retval[0] = Side.EAST;
			break;
		}
		// y direction
		switch (q) {
		case BNE:
		case TNE:
		case BNW:
		case TNW:
			retval[1] = Side.SOUTH;
			break;
		case BSW:
		case BSE:
		case TSE:
		case TSW:
			retval[1] = Side.NORTH;
			break;
		}
		// z direction
		switch (q) {
		case TNE:
		case TNW:
		case TSE:
		case TSW:
			retval[2] = Side.BOTTOM;
			break;
		case BSW:
		case BNE:
		case BSE:
		case BNW:
			retval[2] = Side.TOP;
			break;
		}
		return retval;
	}

	public static Oct getOct(Oct q, Side s) {
		switch (s) {
		case WEST:
			switch (q) {
			case BNE:
				return BNW;
			case BSE:
				return BSW;
			case TNE:
				return TNW;
			case TSE:
				return TSW;
			default:
				break;
			}
			break;
		case EAST:
			switch (q) {
			case BNW:
				return BNE;
			case BSW:
				return BSE;
			case TNW:
				return TNE;
			case TSW:
				return TSE;
			default:
				break;
			}
			break;
		case SOUTH:
			switch (q) {
			case BNE:
				return BSE;
			case BNW:
				return BSW;
			case TNE:
				return TSE;
			case TNW:
				return TSW;
			default:
				break;
			}
			break;
		case NORTH:
			switch (q) {
			case BSE:
				return BNE;
			case BSW:
				return BNW;
			case TSE:
				return TNE;
			case TSW:
				return TNW;
			default:
				break;
			}
			break;
		case BOTTOM:
			switch (q) {
			case TSW:
				return BSW;
			case TSE:
				return BSE;
			case TNW:
				return BNW;
			case TNE:
				return BNE;
			default:
				break;
			}
			break;
		case TOP:
			switch (q) {
			case BSW:
				return TSW;
			case BSE:
				return TSE;
			case BNW:
				return TNW;
			case BNE:
				return TNE;
			default:
				break;
			}
			break;

		}
		// TODO Auto-generated method stub
		return null;
	}
}
