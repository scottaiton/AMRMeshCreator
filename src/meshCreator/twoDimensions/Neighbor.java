package meshCreator.twoDimensions;

import java.util.ArrayList;

public class Neighbor {
	public String side;
	public String type;
	public ArrayList<Integer> ids;
	public ArrayList<Integer> ranks;
	public int orth_on_coarse = 0;

	public Neighbor(String side, String type, ArrayList<Integer> ids, ArrayList<Integer> ranks, int orth_on_coarse) {
		this.side = side;
		this.type = type;
		this.ids = ids;
		this.ranks = ranks;
		this.orth_on_coarse = orth_on_coarse;
	}
}
