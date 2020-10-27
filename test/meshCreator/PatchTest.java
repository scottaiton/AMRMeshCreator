package meshCreator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class PatchTest {

	@Test
	void PatchConstructor2D() {
		Patch p = new Patch(2);
		assertEquals(-1, p.id);
		assertEquals(-1, p.parent_id);
		assertEquals(-1, p.parent_rank);
		assertEquals(null, p.orth_on_parent);
		assertEquals(0, p.rank);
		assertEquals(4, p.child_ids.length);
		assertEquals(4, p.child_ranks.length);
		for (int i = 0; i < 4; i++) {
			assertEquals(-1, p.child_ids[i]);
			assertEquals(-1, p.child_ranks[i]);
		}
		assertEquals(2, p.lengths.length);
		assertEquals(2, p.starts.length);
		for (int i = 0; i < 2; i++) {
			assertEquals(1, p.lengths[i]);
			assertEquals(0, p.starts[i]);
		}
	}

	@Test
	void PatchConstructor3D() {
		Patch p = new Patch(3);
		assertEquals(-1, p.id);
		assertEquals(-1, p.parent_id);
		assertEquals(-1, p.parent_rank);
		assertEquals(null, p.orth_on_parent);
		assertEquals(0, p.rank);
		assertEquals(8, p.child_ids.length);
		assertEquals(8, p.child_ranks.length);
		for (int i = 0; i < 8; i++) {
			assertEquals(-1, p.child_ids[i]);
			assertEquals(-1, p.child_ranks[i]);
		}
		assertEquals(3, p.lengths.length);
		assertEquals(3, p.starts.length);
		for (int i = 0; i < 3; i++) {
			assertEquals(1, p.lengths[i]);
			assertEquals(0, p.starts[i]);
		}
	}

	@Test
	void UpdateParentRankForExists() {
		Patch p = new Patch(3);
		p.parent_id = 1;
		p.parent_rank = 0;
		p.updateParentRankFor(1, 1);
		assertEquals(1, p.parent_rank);
	}

	@Test
	void UpdateParentRankForDoesNotExists() {
		Patch p = new Patch(3);
		p.parent_id = 2;
		p.parent_rank = 0;
		p.updateParentRankFor(1, 1);
		assertEquals(0, p.parent_rank);
	}

	@Test
	void UpdateChildRankForExists() {
		Patch p = new Patch(3);
		for (int i = 0; i < 8; i++) {
			p.child_ranks[i] = 0;
			p.child_ids[i] = i + 1;
		}
		p.updateChildRankFor(8, 1);
		assertEquals(1, p.child_ranks[7]);
	}

	@Test
	void UpdateChildRankForDoesNotExists() {
		Patch p = new Patch(3);
		for (int i = 0; i < 8; i++) {
			p.child_ranks[i] = 0;
			p.child_ids[i] = i + 1;
		}
		p.updateChildRankFor(9, 1);
		for (int i = 0; i < 8; i++) {
			assertEquals(0, p.child_ranks[i]);
		}
	}

	@Test
	void UpdateNbrRankForDoesNotExists() {
		Patch p = new Patch(3);

		ArrayList<Integer> nbr1_ids = new ArrayList<Integer>();
		nbr1_ids.add(1);
		nbr1_ids.add(2);
		ArrayList<Integer> nbr1_ranks = new ArrayList<Integer>();
		nbr1_ranks.add(0);
		nbr1_ranks.add(0);
		Neighbor nbr1 = new Neighbor(Side.WEST(), "normal", nbr1_ids, nbr1_ranks, null);
		p.nbrs.add(nbr1);
		p.updateNbrRankFor(9, 1);
		assertEquals(0, p.nbrs.get(0).ranks.get(0));
		assertEquals(0, p.nbrs.get(0).ranks.get(1));
	}

	@Test
	void UpdateNbrRankForExists() {
		Patch p = new Patch(3);

		ArrayList<Integer> nbr1_ids = new ArrayList<Integer>();
		nbr1_ids.add(1);
		nbr1_ids.add(2);
		ArrayList<Integer> nbr1_ranks = new ArrayList<Integer>();
		nbr1_ranks.add(0);
		nbr1_ranks.add(0);
		Neighbor nbr1 = new Neighbor(Side.WEST(), "normal", nbr1_ids, nbr1_ranks, null);
		p.nbrs.add(nbr1);
		p.updateNbrRankFor(2, 1);
		assertEquals(0, p.nbrs.get(0).ranks.get(0));
		assertEquals(1, p.nbrs.get(0).ranks.get(1));
	}
}
