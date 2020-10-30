package meshCreator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LevelsTest {

	@Test
	void TestSinglePatch2d() {
		Forest forest = new Forest(2);
		Levels levels = new Levels(forest);
		assertEquals(1, levels.getNumLevels());
		assertEquals(1, levels.getLevel(0).size());
		assertEquals(forest, levels.getForest());
		// check the patch
		Patch p = levels.getLevel(0).get(0);
		assertTrue(p.nbrs.isEmpty());
		Node node = forest.getNode(0);
		assertEquals(node.getId(), p.id);
		assertEquals(0, p.rank);
		assertEquals(-1, p.parent_id);
		assertEquals(-1, p.parent_rank);
		assertEquals(4, p.child_ids.length);
		assertEquals(4, p.child_ranks.length);
		for (int i = 0; i < 4; i++) {
			assertEquals(-1, p.child_ids[0]);
			assertEquals(-1, p.child_ranks[0]);
		}
		assertEquals(2, p.lengths.length);
		assertEquals(2, p.starts.length);
		for (int i = 0; i < 2; i++) {
			assertEquals(1, p.lengths[0]);
			assertEquals(0, p.starts[0]);
		}
		assertEquals(0, p.nbrs.size());
	}

	@Test
	void TestSinglePatch3d() {
		Forest forest = new Forest(3);
		Levels levels = new Levels(forest);
		assertEquals(1, levels.getNumLevels());
		assertEquals(1, levels.getLevel(0).size());
		// check the patch
		Patch p = levels.getLevel(0).get(0);
		assertTrue(p.nbrs.isEmpty());
		Node node = forest.getNode(0);
		assertEquals(node.getId(), p.id);
		assertEquals(0, p.rank);
		assertEquals(-1, p.parent_id);
		assertEquals(-1, p.parent_rank);
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
		assertEquals(0, p.nbrs.size());
	}

	@Test
	void TestQuadUniform2d() {
		Forest forest = new Forest(2);
		double[] coord = { 0.5, 0.5 };
		forest.refineAt(coord);
		Levels levels = new Levels(forest);
		assertEquals(2, levels.getNumLevels());
		// check coarser level
		{
			assertEquals(1, levels.getLevel(1).size());
			// check the coarser patch
			Patch p = levels.getLevel(1).get(0);
			assertTrue(p.nbrs.isEmpty());
			Node node = forest.getNode(0);
			assertEquals(node.getId(), p.id);
			assertEquals(0, p.rank);
			assertEquals(node.getParentId(), p.parent_id);
			assertEquals(-1, p.parent_rank);
			assertEquals(4, p.child_ids.length);
			assertEquals(4, p.child_ranks.length);
			for (Orthant o : Orthant.getValuesForDimension(2)) {
				assertEquals(node.getChildId(o), p.child_ids[o.getIndex()]);
				assertEquals(0, p.child_ranks[o.getIndex()]);
			}
			assertEquals(2, p.lengths.length);
			assertEquals(2, p.starts.length);
			for (int i = 0; i < 2; i++) {
				assertEquals(1, p.lengths[i]);
				assertEquals(0, p.starts[i]);
			}
			assertEquals(0, p.nbrs.size());
		}
		// check finer level
		{
			assertEquals(4, levels.getLevel(0).size());
			Patch parent = levels.getLevel(1).get(0);
			// check the southwest patch
			{
				Patch p = getChildPatch(levels, 0, parent, Orthant.SW());
				Node node = forest.getNode(p.id);
				assertEquals(node.getId(), p.id);
				assertEquals(0, p.rank);
				assertEquals(node.getParentId(), p.parent_id);
				assertEquals(0, p.parent_rank);
				assertEquals(4, p.child_ids.length);
				assertEquals(4, p.child_ranks.length);
				for (Orthant o : Orthant.getValuesForDimension(2)) {
					assertEquals(node.getChildId(o), p.child_ids[o.getIndex()]);
					assertEquals(-1, p.child_ranks[o.getIndex()]);
				}
				assertEquals(2, p.lengths.length);
				assertEquals(2, p.starts.length);
				for (int i = 0; i < 2; i++) {
					assertEquals(node.getLength(i), p.lengths[i]);
					assertEquals(node.getStart(i), p.starts[i]);
				}
				assertEquals(2, p.nbrs.size());
				Neighbor east = getNeighbor(p, Side.EAST());
				assertEquals("NORMAL", east.type);
				assertEquals(1, east.ids.size());
				assertEquals(node.getNbrId(Side.EAST()), east.ids.get(0));
				assertEquals(1, east.ranks.size());
				assertEquals(0, east.ranks.get(0));
				Neighbor north = getNeighbor(p, Side.NORTH());
				assertEquals("NORMAL", north.type);
				assertEquals(1, north.ids.size());
				assertEquals(node.getNbrId(Side.NORTH()), north.ids.get(0));
				assertEquals(1, north.ranks.size());
				assertEquals(0, north.ranks.get(0));
			}
		}
	}

	@Test
	void TestOctUniform3d() {
		Forest forest = new Forest(3);
		double[] coord = { 0.5, 0.5, 0.5 };
		forest.refineAt(coord);
		Levels levels = new Levels(forest);
		assertEquals(2, levels.getNumLevels());
		// check coarser level
		{
			assertEquals(1, levels.getLevel(1).size());
			// check the coarser patch
			Patch p = levels.getLevel(1).get(0);
			assertTrue(p.nbrs.isEmpty());
			Node node = forest.getNode(0);
			assertEquals(node.getId(), p.id);
			assertEquals(0, p.rank);
			assertEquals(node.getParentId(), p.parent_id);
			assertEquals(-1, p.parent_rank);
			assertEquals(8, p.child_ids.length);
			assertEquals(8, p.child_ranks.length);
			for (Orthant o : Orthant.getValuesForDimension(3)) {
				assertEquals(node.getChildId(o), p.child_ids[o.getIndex()]);
				assertEquals(0, p.child_ranks[o.getIndex()]);
			}
			assertEquals(3, p.lengths.length);
			assertEquals(3, p.starts.length);
			for (int i = 0; i < 3; i++) {
				assertEquals(1, p.lengths[i]);
				assertEquals(0, p.starts[i]);
			}
			assertEquals(0, p.nbrs.size());
		}
		// check finer level
		{
			assertEquals(8, levels.getLevel(0).size());
			Patch parent = levels.getLevel(1).get(0);
			// check the bottom-south-west patch
			{
				Patch p = getChildPatch(levels, 0, parent, Orthant.BSW());
				Node node = forest.getNode(p.id);
				assertEquals(node.getId(), p.id);
				assertEquals(0, p.rank);
				assertEquals(node.getParentId(), p.parent_id);
				assertEquals(0, p.parent_rank);
				assertEquals(8, p.child_ids.length);
				assertEquals(8, p.child_ranks.length);
				for (Orthant o : Orthant.getValuesForDimension(3)) {
					assertEquals(node.getChildId(o), p.child_ids[o.getIndex()]);
					assertEquals(-1, p.child_ranks[o.getIndex()]);
				}
				assertEquals(3, p.lengths.length);
				assertEquals(3, p.starts.length);
				for (int i = 0; i < 3; i++) {
					assertEquals(node.getLength(i), p.lengths[i]);
					assertEquals(node.getStart(i), p.starts[i]);
				}

				assertEquals(3, p.nbrs.size());

				Neighbor east = getNeighbor(p, Side.EAST());
				assertEquals("NORMAL", east.type);
				assertEquals(1, east.ids.size());
				assertEquals(node.getNbrId(Side.EAST()), east.ids.get(0));
				assertEquals(1, east.ranks.size());
				assertEquals(0, east.ranks.get(0));

				Neighbor north = getNeighbor(p, Side.NORTH());
				assertEquals("NORMAL", north.type);
				assertEquals(1, north.ids.size());
				assertEquals(node.getNbrId(Side.NORTH()), north.ids.get(0));
				assertEquals(1, north.ranks.size());
				assertEquals(0, north.ranks.get(0));

				Neighbor top = getNeighbor(p, Side.TOP());
				assertEquals("NORMAL", top.type);
				assertEquals(1, top.ids.size());
				assertEquals(node.getNbrId(Side.TOP()), top.ids.get(0));
				assertEquals(1, top.ranks.size());
				assertEquals(0, top.ranks.get(0));
			}
		}
	}

	@Test
	void TestRefinedSE2d() {
		Forest forest = new Forest(2);
		double[] coord = { 0.75, 0.25 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		Levels levels = new Levels(forest);
		assertEquals(3, levels.getNumLevels());
		assertEquals(1, levels.getLevel(2).size());
		assertEquals(4, levels.getLevel(1).size());
		assertEquals(7, levels.getLevel(0).size());

		Patch root = levels.getLevel(2).get(0);

		Patch lv1_sw_patch = getChildPatch(levels, 1, root, Orthant.SW());
		assertEquals(root.id, lv1_sw_patch.parent_id);
		assertEquals(0, lv1_sw_patch.parent_rank);
		assertEquals(lv1_sw_patch.id, lv1_sw_patch.child_ids[0]);
		assertEquals(0, lv1_sw_patch.child_ranks[0]);
		for (int i = 1; i < 4; i++) {
			assertEquals(-1, lv1_sw_patch.child_ids[i]);
			assertEquals(-1, lv1_sw_patch.child_ranks[i]);
		}

		Patch lv0_sw_patch = getChildPatch(levels, 0, root, Orthant.SW());
		assertEquals(lv0_sw_patch.id, lv0_sw_patch.parent_id);
		assertEquals(0, lv0_sw_patch.parent_rank);
		for (int i = 0; i < 4; i++) {
			assertEquals(-1, lv0_sw_patch.child_ids[i]);
			assertEquals(-1, lv0_sw_patch.child_ranks[i]);
		}

		// check fine neighbor
		Patch lv1_se_patch = getChildPatch(levels, 1, root, Orthant.SE());
		Neighbor fine_nbr = getNeighbor(lv0_sw_patch, Side.EAST());
		assertEquals("FINE", fine_nbr.type);
		assertEquals(2, fine_nbr.ids.size());
		assertEquals(lv1_se_patch.child_ids[0], fine_nbr.ids.get(0));
		assertEquals(lv1_se_patch.child_ids[2], fine_nbr.ids.get(1));
		assertEquals(2, fine_nbr.ranks.size());
		assertEquals(0, fine_nbr.ranks.get(0));
		assertEquals(0, fine_nbr.ranks.get(1));
		assertEquals(null, fine_nbr.orth_on_coarse);

		// check coarse neighbor
		Patch lv0_mid_south = getChildPatch(levels, 0, lv1_se_patch, Orthant.SW());
		Neighbor coarse_nbr = getNeighbor(lv0_mid_south, Side.WEST());
		assertEquals("COARSE", coarse_nbr.type);
		assertEquals(1, coarse_nbr.ids.size());
		assertEquals(lv0_sw_patch.id, coarse_nbr.ids.get(0));
		assertEquals(1, coarse_nbr.ranks.size());
		assertEquals(0, coarse_nbr.ranks.get(0));
		assertEquals(Orthant.Lower(), coarse_nbr.orth_on_coarse);
	}

	@Test
	void Test2RefinedSE2d() {
		Forest forest = new Forest(2);
		double[] coord = { 0.8, 0.2 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		forest.refineAt(coord);
		Levels levels = new Levels(forest);
		assertEquals(4, levels.getNumLevels());
		assertEquals(1, levels.getLevel(3).size());
		assertEquals(4, levels.getLevel(2).size());
		assertEquals(7, levels.getLevel(1).size());
		assertEquals(10, levels.getLevel(0).size());

		Patch root = levels.getLevel(3).get(0);

		Patch lv2_sw_patch = getChildPatch(levels, 2, root, Orthant.SW());
		assertEquals(root.id, lv2_sw_patch.parent_id);
		assertEquals(0, lv2_sw_patch.parent_rank);
		assertEquals(lv2_sw_patch.id, lv2_sw_patch.child_ids[0]);
		assertEquals(0, lv2_sw_patch.child_ranks[0]);
		for (int i = 1; i < 4; i++) {
			assertEquals(-1, lv2_sw_patch.child_ids[i]);
			assertEquals(-1, lv2_sw_patch.child_ranks[i]);
		}

		Patch lv1_sw_patch = getChildPatch(levels, 1, root, Orthant.SW());
		assertEquals(lv1_sw_patch.id, lv1_sw_patch.parent_id);
		assertEquals(0, lv1_sw_patch.parent_rank);
		assertEquals(lv2_sw_patch.id, lv2_sw_patch.child_ids[0]);
		assertEquals(0, lv2_sw_patch.child_ranks[0]);
		for (int i = 1; i < 4; i++) {
			assertEquals(-1, lv2_sw_patch.child_ids[i]);
			assertEquals(-1, lv2_sw_patch.child_ranks[i]);
		}

	}

	@Test
	void TestChangeRankAtLevel2RefinedSE2d() {
		Forest forest = new Forest(2);
		double[] coord = { 0.75, 0.25 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		Levels levels = new Levels(forest);
		levels.changeRankAt(coord, 2, 1);

		Patch root = levels.getLevel(2).get(0);
		assertEquals(1, root.rank);
		for (int i = 0; i < 4; i++) {
			assertEquals(0, root.child_ranks[i]);
		}
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			Patch child = getChildPatch(levels, 1, root, o);
			assertEquals(1, child.parent_rank);
		}
	}

	@Test
	void TestChangeRankAtLevel1RefinedSE2d() {
		Forest forest = new Forest(2);
		double[] coord = { 0.75, 0.25 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		Levels levels = new Levels(forest);
		double[] ref_coord = { 0.25, 0.25 };
		levels.changeRankAt(ref_coord, 1, 1);

		Patch root = levels.getLevel(2).get(0);
		assertEquals(1, root.child_ranks[0]);
		Patch lv1_sw_child = getChildPatch(levels, 1, root, Orthant.SW());
		assertEquals(1,lv1_sw_child.rank);
		assertEquals(0,lv1_sw_child.parent_rank);
		assertEquals(0,lv1_sw_child.child_ranks[0]);
		Patch lv0_sw_child = getChildPatch(levels, 0, root, Orthant.SW());
		assertEquals(1,lv0_sw_child.parent_rank);

		Patch lv1_se_child = getChildPatch(levels, 1, root, Orthant.SE());
		assertEquals(1,getNeighbor(lv1_se_child,Side.WEST()).ranks.get(0));

		Patch lv1_nw_child = getChildPatch(levels, 1, root, Orthant.NW());
		assertEquals(1,getNeighbor(lv1_nw_child,Side.SOUTH()).ranks.get(0));
	}

	private Neighbor getNeighbor(Patch p, Side side) {
		for (Neighbor n : p.nbrs) {
			if (n.side.equals(side)) {
				return n;
			}
		}
		return null;
	}

	private Patch getChildPatch(Levels levels, int level, Patch parent, Orthant o) {
		for (Patch p : levels.levels.get(level)) {
			if (p.id == parent.child_ids[o.getIndex()]) {
				return p;
			}

		}
		return null;
	}
}
