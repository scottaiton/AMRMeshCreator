package meshCreator;

import java.util.ArrayList;
import java.util.Arrays;

public class Patch {
	public int id = -1;
	public int parent_id = -1;
	public int parent_rank = -1;
	public Orthant orth_on_parent = null;
	public int rank = 0;
	public int[] child_ids;
	public int[] child_ranks;
	public double[] starts;
	public double[] lengths;

	public ArrayList<Neighbor> nbrs = new ArrayList<Neighbor>();

	public Patch(int dimension) {
		child_ids = new int[Orthant.getNumOrthantsForDimension(dimension)];
		Arrays.fill(child_ids, -1);
		child_ranks = new int[Orthant.getNumOrthantsForDimension(dimension)];
		Arrays.fill(child_ranks, -1);
		starts = new double[dimension];
		Arrays.fill(starts, 0);
		lengths = new double[dimension];
		Arrays.fill(lengths, 1);
	}

	public void updateNbrRankFor(int nbr_id, int new_rank) {
		for (Neighbor nbr : nbrs) {
			for (int i = 0; i < nbr.ids.size(); i++) {
				if (nbr.ids.get(i) == nbr_id) {
					nbr.ranks.set(i, new_rank);
				}
			}
		}
	}

	public void updateChildRankFor(int child_id, int new_rank) {
		for (int i = 0; i < child_ids.length; i++) {
			if (child_ids[i] == child_id) {
				child_ranks[i] = new_rank;
				break;
			}
		}
	}

	public void updateParentRankFor(int id, int new_rank) {
		if (parent_id == id) {
			parent_rank = new_rank;
		}
	}

	public boolean coordIsInside(double[] coord) {
		for (int i = 0; i < starts.length; i++) {
			if (coord[i] < starts[i] || coord[i] > starts[i] + lengths[i]) {
				return false;
			}
		}
		return true;
	}
}