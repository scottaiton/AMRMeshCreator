package meshCreator.twoDimensions;

import java.util.ArrayList;

public class Patch {
	public int id;
	public int parent_id;
	public int parent_rank = 0;
	public int orth_on_parent;
	public int rank = 0;
	public int[] child_ids;
	public int[] child_ranks;
	private double[] starts;
	private double[] lengths;

	public ArrayList<Neighbor> nbrs = new ArrayList<Neighbor>();

	public Patch(QuadTree node, int curr_level, int num_levels) {
		this.id = node.id;
		if (node.level < curr_level) {
			parent_id = node.id;
			orth_on_parent = -1;
			if (curr_level < num_levels) {
				child_ids = new int[] { node.id, -1, -1, -1 };
				child_ranks = new int[4];
			}
		} else {
			if (!node.hasParent()) {
				parent_id = -1;
				orth_on_parent = -1;
			} else {
				parent_id = node.getParent().id;
				orth_on_parent = 0;
				while (node != node.getParent().getChild(Orthant.getValuesForDimension(2)[orth_on_parent])) {
					orth_on_parent++;
				}
			}
			if (node.hasChildren()) {
				child_ids = new int[4];
				child_ranks = new int[4];
				for (Orthant q : Orthant.getValuesForDimension(2)) {
					child_ids[q.getIndex()] = node.getChild(q).id;
				}
			} else if (curr_level < num_levels) {
				child_ids = new int[] { node.id, -1, -1, -1 };
				child_ranks = new int[4];
			}
		}
		starts = new double[2];
		lengths = new double[2];
		for(int i=0;i<2;i++) {
			starts[i]=node.starts[i];
			lengths[i]=node.lengths[i];
		}

		for (Side side : Side.getValuesForDimension(2)) {
			if (node.nbr(side) == null && node.hasParent() && node.getParent().nbr(side) != null) {
				ArrayList<Integer> ids = new ArrayList<Integer>();
				ArrayList<Integer> ranks = new ArrayList<Integer>();
				ids.add(node.getParent().nbr(side).id);
				ranks.add(0);
				int orth_on_coarse = 0;
				if (orth_on_parent == Orthant.GetValuesOnSide(2,side)[1].getIndex()) {
					orth_on_coarse = 1;
				}
				nbrs.add(new Neighbor(side.toString(), "coarse", ids, ranks, orth_on_coarse));
			} else if (node.level < curr_level && node.nbr(side) != null && node.nbr(side).hasChildren()) {
				ArrayList<Integer> ids = new ArrayList<Integer>();
				ArrayList<Integer> ranks = new ArrayList<Integer>();
				for (Orthant q : Orthant.GetValuesOnSide(2,side.getOpposite())) {
					ids.add(node.nbr(side).getChild(q).id);
					ranks.add(0);
				}
				nbrs.add(new Neighbor(side.toString(), "fine", ids, ranks, 0));
			} else if (node.nbr(side) != null) {
				ArrayList<Integer> ids = new ArrayList<Integer>();
				ArrayList<Integer> ranks = new ArrayList<Integer>();
				ids.add(node.nbr(side).id);
				ranks.add(0);
				nbrs.add(new Neighbor(side.toString(), "normal", ids, ranks, 0));
			}
		}
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

	public boolean hasParent() {
		return parent_id != -1;
	}

	public void updateChildRankFor(int child_id, int new_rank) {
		for (int i = 0; i < 4; i++) {
			if (child_ids[i] == child_id) {
				child_ranks[i] = new_rank;
				break;
			}
		}
	}

	public void updateParentRank(int new_rank) {
		parent_rank=new_rank;
	}

	public boolean hasChildren() {
		return child_ids != null;
	}

}