package meshCreator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ForestTest {

	private Set<Side> set(Side[] sides) {
		Set<Side> ret = new HashSet<Side>();
		for (Side s : sides) {
			ret.add(s);
		}
		return ret;
	}

	private Set<Side> intersection(Side[] sides1, Side[] sides2) {
		Set<Side> s1 = set(sides1);
		s1.retainAll(set(sides2));
		return s1;
	}

	@Test
	void DefaultConstructor() {
		Forest forest = new Forest(2);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertEquals(forest.getMaxLevel(), 0);
	}

	@Test
	void RefineAtGood() {
		Forest forest = new Forest(2);
		double[] coord = { .5, .5 };
		forest.refineAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 1);

		Node root = forest.getNode(0);
		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			assertFalse(child.hasChildren());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
		}
	}

	@Test
	void RefineAtTwice() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 2);

		Node root = forest.getNode(0);
		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			if (o.equals(Orthant.SW())) {
				assertTrue(child.hasChildren());
				for (Orthant child_o : Orthant.getValuesForDimension(2)) {
					int child_child_id = child.getChildId(child_o);
					Node child_child = forest.getNode(child_child_id);
					assertEquals(child_child.getId(), child_child_id);
					assertTrue(child_child.hasParent());
					for (Side s : child_o.getInteriorSides()) {
						assertTrue(child_child.hasNbr(s));
						assertEquals(child_child.getNbrId(s), child.getChildId(child_o.getNbrOnSide(s)));
					}
					for (Side s : child_o.getExteriorSides()) {
						assertFalse(child_child.hasNbr(s));
					}
				}
			} else {
				assertFalse(child.hasChildren());
			}
		}
	}

	@Test
	void RefineNodeGood() {
		Forest forest = new Forest(2);
		forest.refineNode(0);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 1);

		Node root = forest.getNode(0);
		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			assertFalse(child.hasChildren());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
		}
	}

	@Test
	void RefineNodeNonLeaf() {
		Forest forest = new Forest(2);
		forest.refineNode(0);
		forest.refineNode(0);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 1);

		Node root = forest.getNode(0);
		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			assertFalse(child.hasChildren());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
		}
	}

	@Test
	void RefineNodeTwice() {
		Forest forest = new Forest(2);
		Node root = forest.getRootNode();
		forest.refineNode(root.getId());
		forest.refineNode(root.getChildId(Orthant.SW()));

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 2);

		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			if (o.equals(Orthant.SW())) {
				assertTrue(child.hasChildren());
				for (Orthant child_o : Orthant.getValuesForDimension(2)) {
					int child_child_id = child.getChildId(child_o);
					Node child_child = forest.getNode(child_child_id);
					assertEquals(child_child.getId(), child_child_id);
					assertTrue(child_child.hasParent());
					for (Side s : child_o.getInteriorSides()) {
						assertTrue(child_child.hasNbr(s));
						assertEquals(child_child.getNbrId(s), child.getChildId(child_o.getNbrOnSide(s)));
					}
					for (Side s : child_o.getExteriorSides()) {
						assertFalse(child_child.hasNbr(s));
					}
				}
			} else {
				assertFalse(child.hasChildren());
			}
		}
	}

	@Test
	void RefineNodeThrice() {
		Forest forest = new Forest(2);
		Node root = forest.getRootNode();
		forest.refineNode(root.getId());
		Node child = forest.getNode(root.getChildId(Orthant.SW()));
		forest.refineNode(child.getId());
		forest.refineNode(child.getChildId(Orthant.NE()));

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 3);

		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		// southwest root
		{
			Orthant o = Orthant.SW();
			int child_id = root.getChildId(o);
			child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertTrue(child.hasChildren());
			for (Orthant child_o : Orthant.getValuesForDimension(2)) {
				int child_child_id = child.getChildId(child_o);
				Node child_child = forest.getNode(child_child_id);
				assertEquals(child_child.getId(), child_child_id);
				assertTrue(child_child.hasParent());
				for (Side s : Side.getValuesForDimension(2)) {
					if (intersection(o.getExteriorSides(), child_o.getExteriorSides()).contains(s)) {
						assertFalse(child_child.hasNbr(s));
					} else if (set(child_o.getExteriorSides()).contains(s)) {
						assertTrue(child_child.hasNbr(s));
						assertEquals(child_child.getNbrId(s),
								forest.getNode(root.getChildId(o.getNbrOnSide(s))).getChildId(child_o.getNbrOnSide(s)));
					} else {
						assertTrue(child_child.hasNbr(s));
						assertEquals(child_child.getNbrId(s), child.getChildId(child_o.getNbrOnSide(s)));
					}
				}
				if (child_o.equals(Orthant.NE())) {
					assertTrue(child_child.hasChildren());
				} else {
					assertFalse(child_child.hasChildren());
				}
			}
		}
		// southeast root
		{
			Orthant o = Orthant.SE();
			int child_id = root.getChildId(o);
			child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertTrue(child.hasChildren());
		}
		// northwest root
		{
			Orthant o = Orthant.NW();
			int child_id = root.getChildId(o);
			child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertTrue(child.hasChildren());
		}
		// northeast root
		{
			Orthant o = Orthant.NE();
			int child_id = root.getChildId(o);
			child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertFalse(child.hasChildren());
		}
	}

	@Test
	void RefineAtThrice() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		forest.refineAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 3);

		Node root = forest.getNode(0);
		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		// southwest root
		{
			Orthant o = Orthant.SW();
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertTrue(child.hasChildren());
			for (Orthant child_o : Orthant.getValuesForDimension(2)) {
				int child_child_id = child.getChildId(child_o);
				Node child_child = forest.getNode(child_child_id);
				assertEquals(child_child.getId(), child_child_id);
				assertTrue(child_child.hasParent());
				for (Side s : Side.getValuesForDimension(2)) {
					if (intersection(o.getExteriorSides(), child_o.getExteriorSides()).contains(s)) {
						assertFalse(child_child.hasNbr(s));
					} else if (set(child_o.getExteriorSides()).contains(s)) {
						assertTrue(child_child.hasNbr(s));
						assertEquals(child_child.getNbrId(s),
								forest.getNode(root.getChildId(o.getNbrOnSide(s))).getChildId(child_o.getNbrOnSide(s)));
					} else {
						assertTrue(child_child.hasNbr(s));
						assertEquals(child_child.getNbrId(s), child.getChildId(child_o.getNbrOnSide(s)));
					}
				}
				if (child_o.equals(Orthant.NE())) {
					assertTrue(child_child.hasChildren());
				} else {
					assertFalse(child_child.hasChildren());
				}
			}
		}
		// southeast root
		{
			Orthant o = Orthant.SE();
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertTrue(child.hasChildren());
		}
		// northwest root
		{
			Orthant o = Orthant.NW();
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertTrue(child.hasChildren());
		}
		// northeast root
		{
			Orthant o = Orthant.NE();
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
			assertFalse(child.hasChildren());
		}
	}

	@Test
	void CoarsenAtRoot() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.coarsenAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertEquals(forest.getMaxLevel(), 0);
	}

	@Test
	void CoarsenAtAfterRefineAt() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.coarsenAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasParent());
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getMaxLevel(), 0);
		assertEquals(forest.getNode(1), null);
	}

	@Test
	void CoarsenAtAfterRefineAtTwiceFinest() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		forest.coarsenAt(coord);

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 1);

		Node root = forest.getNode(0);
		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			assertFalse(child.hasChildren());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
		}
	}

	@Test
	void CoarsenAtAfterRefineAtThriceCoarsesr() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		forest.refineAt(coord);
		coord[0] = .25;
		coord[1] = .75;
		forest.coarsenAt(coord);

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasParent());
		assertTrue(forest.getNode(0).hasChildren());
		assertEquals(forest.getMaxLevel(), 2);
	}

	@Test
	void CoarsenAtAfterRefineAtTwiceCoarsest() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		coord[0] = .75;
		coord[1] = .75;
		forest.coarsenAt(coord);

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasParent());
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getMaxLevel(), 0);
		assertEquals(forest.getNode(1), null);
	}

	@Test
	void CoarsenNodeRoot() {
		Forest forest = new Forest(2);
		forest.coarsenNode(0);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertEquals(forest.getMaxLevel(), 0);
	}

	@Test
	void CoarsenNodeBad() {
		Forest forest = new Forest(2);
		forest.coarsenNode(69);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertEquals(forest.getMaxLevel(), 0);
	}

	@Test
	void CoarsenNodeAfterRefineAt() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.coarsenNode(forest.getRootNode().getChildId(Orthant.SW()));
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasParent());
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getMaxLevel(), 0);
		assertEquals(forest.getNode(1), null);
	}

	@Test
	void CoarsenNodeAfterRefineAtTwiceFinest() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		forest.coarsenAt(coord);
		forest.coarsenNode(forest.getNode(forest.getRootNode().getChildId(Orthant.SW())).getChildId(Orthant.SW()));

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 1);

		Node root = forest.getNode(0);
		assertEquals(root.getId(), 0);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertEquals(child.getId(), child_id);
			assertTrue(child.hasParent());
			assertFalse(child.hasChildren());
			for (Side s : o.getInteriorSides()) {
				assertTrue(child.hasNbr(s));
				assertEquals(child.getNbrId(s), root.getChildId(o.getNbrOnSide(s)));
			}
			for (Side s : o.getExteriorSides()) {
				assertFalse(child.hasNbr(s));
			}
		}
	}

	@Test
	void CoarsenNodeAfterRefineAtTwiceCoarser() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		forest.coarsenNode(forest.getRootNode().getChildId(Orthant.NW()));

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasParent());
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getMaxLevel(), 0);
		assertEquals(forest.getNode(1), null);
		assertEquals(forest.getNode(7), null);
	}

	@Test
	void CoarsenNodeAfterRefineAtTwiceCoarserNodeWithChildren() {
		Forest forest = new Forest(2);
		double[] coord = { .3, .3 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		forest.coarsenNode(forest.getRootNode().getChildId(Orthant.SW()));

		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasParent());
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getMaxLevel(), 0);
		assertEquals(forest.getNode(1), null);
	}

	@Test
	void RefineAtBad() {
		Forest forest = new Forest(2);
		double[] coord = { .5, 2 };
		forest.refineAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
	}

	@Test
	void RefineNodeBad() {
		Forest forest = new Forest(2);
		forest.refineNode(1000);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
	}

	@Test
	void CoarsenAtBad() {
		Forest forest = new Forest(2);
		double[] coord = { .5, 2 };
		forest.coarsenAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
	}

	@Test
	void AddNbrAtBad() {
		Forest forest = new Forest(2);
		double[] coord = { -.5, 1.5 };
		forest.addNbrAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 1);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
	}

	@Test
	void IsPotentialNbrSingleRoot() {
		Forest forest = new Forest(2);
		double[] coord = { .5, .5 };
		double[] starts = new double[2];
		double[] lengths = new double[2];
		assertFalse(forest.coordIsPotentialNbr(coord, starts, lengths));
		coord[0] = -2;
		assertFalse(forest.coordIsPotentialNbr(coord, starts, lengths));
		coord[0] = -.5;
		assertTrue(forest.coordIsPotentialNbr(coord, starts, lengths));
		assertEquals(-1, starts[0]);
		assertEquals(0, starts[1]);
		assertEquals(1, lengths[0]);
		assertEquals(1, lengths[1]);
		coord[0] = 1.5;
		assertTrue(forest.coordIsPotentialNbr(coord, starts, lengths));
		assertEquals(1, starts[0]);
		assertEquals(0, starts[1]);
		assertEquals(1, lengths[0]);
		assertEquals(1, lengths[1]);
		coord[0] = 3;
		assertFalse(forest.coordIsPotentialNbr(coord, starts, lengths));
		coord[1] = -1;
		assertFalse(forest.coordIsPotentialNbr(coord, starts, lengths));
		coord[0] = .5;
		coord[1] = 2.5;
		assertFalse(forest.coordIsPotentialNbr(coord, starts, lengths));
		coord[1] = -2.5;
		assertFalse(forest.coordIsPotentialNbr(coord, starts, lengths));
	}

	@Test
	void AddNbrAtWest() {
		Forest forest = new Forest(2);
		double[] coord = { -.5, .5 };
		forest.addNbrAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 2);
		assertTrue(forest.getRootIds().contains(0));
		assertTrue(forest.getRootIds().contains(1));

		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getNode(0).getNbrId(Side.WEST()), 1);

		assertEquals(forest.getNode(1).getId(), 1);
		assertFalse(forest.getNode(1).hasChildren());
		assertEquals(forest.getNode(1).getNbrId(Side.EAST()), 0);
	}

	@Test
	void AddNbrAtNorth() {
		Forest forest = new Forest(2);
		double[] coord = { .5, 1.5 };
		forest.addNbrAt(coord);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 2);
		assertTrue(forest.getRootIds().contains(0));
		assertTrue(forest.getRootIds().contains(1));

		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getNode(0).getNbrId(Side.NORTH()), 1);

		assertEquals(forest.getNode(1).getId(), 1);
		assertFalse(forest.getNode(1).hasChildren());
		assertEquals(forest.getNode(1).getNbrId(Side.SOUTH()), 0);
	}

	@Test
	void AddNbrAtWestThenNorth() {
		Forest forest = new Forest(2);
		double[] west = { -.5, .5 };
		forest.addNbrAt(west);
		double[] north = { .5, 1.5 };
		forest.addNbrAt(north);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 3);
		assertTrue(forest.getRootIds().contains(0));
		assertTrue(forest.getRootIds().contains(1));
		assertTrue(forest.getRootIds().contains(2));

		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getNode(0).getNbrId(Side.WEST()), 1);

		assertEquals(forest.getNode(1).getId(), 1);
		assertFalse(forest.getNode(1).hasChildren());
		assertFalse(forest.getNode(1).hasParent());
		assertEquals(forest.getNode(1).getNbrId(Side.EAST()), 0);

		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getNode(0).getNbrId(Side.NORTH()), 2);

		assertEquals(forest.getNode(2).getId(), 2);
		assertFalse(forest.getNode(2).hasChildren());
		assertFalse(forest.getNode(2).hasParent());
		assertEquals(forest.getNode(2).getNbrId(Side.SOUTH()), 0);
	}

	@Test
	void AddNbrAtWestThenNorthThenNorthWest() {
		Forest forest = new Forest(2);
		double[] west = { -.5, .5 };
		forest.addNbrAt(west);
		double[] north = { .5, 1.5 };
		forest.addNbrAt(north);
		double[] northwest = { -.5, 1.5 };
		forest.addNbrAt(northwest);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 4);
		assertTrue(forest.getRootIds().contains(0));
		assertTrue(forest.getRootIds().contains(1));
		assertTrue(forest.getRootIds().contains(2));
		assertTrue(forest.getRootIds().contains(3));

		assertEquals(forest.getNode(0).getId(), 0);
		assertFalse(forest.getNode(0).hasChildren());
		assertEquals(forest.getNode(0).getNbrId(Side.WEST()), 1);
		assertEquals(forest.getNode(0).getNbrId(Side.NORTH()), 2);

		assertEquals(forest.getNode(1).getId(), 1);
		assertFalse(forest.getNode(1).hasChildren());
		assertFalse(forest.getNode(1).hasParent());
		assertEquals(forest.getNode(1).getNbrId(Side.EAST()), 0);
		assertEquals(forest.getNode(1).getNbrId(Side.NORTH()), 3);

		assertEquals(forest.getNode(2).getId(), 2);
		assertFalse(forest.getNode(2).hasChildren());
		assertFalse(forest.getNode(2).hasParent());
		assertEquals(forest.getNode(2).getNbrId(Side.SOUTH()), 0);
		assertEquals(forest.getNode(2).getNbrId(Side.WEST()), 3);

		assertEquals(forest.getNode(3).getId(), 3);
		assertFalse(forest.getNode(3).hasChildren());
		assertFalse(forest.getNode(3).hasParent());
		assertEquals(forest.getNode(3).getNbrId(Side.SOUTH()), 1);
		assertEquals(forest.getNode(3).getNbrId(Side.EAST()), 2);
	}

	@Test
	void AddNbrAtNorthThenNorthWestThenWestAfterRefineAtTwiceRoot() {
		Forest forest = new Forest(2);
		double[] coord = { .2, .2 };
		forest.refineAt(coord);
		forest.refineAt(coord);
		double[] north = { .2, 1.5 };
		forest.addNbrAt(north);
		double[] northwest = { -.2, 1.5 };
		forest.addNbrAt(northwest);
		double[] west = { -.2, .5 };
		forest.addNbrAt(west);
		assertEquals(forest.getRootNode().getId(), 0);
		assertEquals(forest.getRootIds().size(), 4);
		assertTrue(forest.getRootIds().contains(0));
		assertEquals(forest.getMaxLevel(), 2);

		int other_root_id = 0;
		for (int root_id : forest.getRootIds()) {
			if (root_id != 0) {
				other_root_id = root_id;
			}
		}

		Node root = forest.getNode(other_root_id);
		assertEquals(root.getId(), other_root_id);
		assertFalse(root.hasParent());
		assertTrue(root.hasChildren());

		// check the children
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			int child_id = root.getChildId(o);
			Node child = forest.getNode(child_id);
			assertFalse(child.hasChildren());
			if (o.equals(Orthant.SE())) {
				assertFalse(child.hasNbr(Side.SOUTH()));
				assertTrue(child.hasNbr(Side.EAST()));
				assertEquals(forest.getRootNode().getChildId(Orthant.SW()), child.getNbrId(Side.EAST()));
				assertEquals(child.getId(),
						forest.getNode(forest.getRootNode().getChildId(Orthant.SW())).getNbrId(Side.WEST()));
			} else if (o.equals(Orthant.NE())) {
				assertFalse(child.hasNbr(Side.NORTH()));
				assertTrue(child.hasNbr(Side.EAST()));
				assertEquals(forest.getRootNode().getChildId(Orthant.NW()), child.getNbrId(Side.EAST()));
				assertEquals(child.getId(),
						forest.getNode(forest.getRootNode().getChildId(Orthant.NW())).getNbrId(Side.WEST()));
			} else {
				for (Side s : o.getExteriorSides()) {
					assertFalse(child.hasNbr(s));
				}
			}
		}
	}

	@Test
	void GetLeaftAtAfterRefineAtTwiceRoot() {
		Forest forest = new Forest(2);
		double[] coord = { .2, .2 };
		forest.refineAt(coord);
		forest.refineAt(coord);

		assertEquals(forest.getLeafAt(coord).getLevel(), 2);
		assertTrue(forest.getLeafAt(coord).coordIsInside(coord));

		double[] coord2 = { .75, .75 };
		assertEquals(forest.getLeafAt(coord2).getLevel(), 1);
		assertTrue(forest.getLeafAt(coord2).coordIsInside(coord2));
	}

	@Test
	void GetLeaftAtDefaultConstructor() {
		Forest forest = new Forest(2);

		double[] coord2 = { .75, .75 };
		assertEquals(forest.getLeafAt(coord2).getLevel(), 0);
		assertEquals(forest.getLeafAt(coord2).getId(), 0);
		assertTrue(forest.getLeafAt(coord2).coordIsInside(coord2));
	}

	@Test
	void GetLeaftAtInvalid() {
		Forest forest = new Forest(2);

		double[] coord2 = { 1.75, .75 };
		assertEquals(forest.getLeafAt(coord2), null);
	}

	@Test
	void GetLeaftAfterAddNbrAt() {
		Forest forest = new Forest(2);

		double[] coord2 = { 1.75, .75 };
		forest.addNbrAt(coord2);

		assertEquals(forest.getLeafAt(coord2).getLevel(), 0);
		assertEquals(forest.getLeafAt(coord2).getId(), 1);
		assertTrue(forest.getLeafAt(coord2).coordIsInside(coord2));
	}

	@Test
	void IsPotentialNbrAfterAddNbrAdd() {
		Forest forest = new Forest(2);

		double[] coord2 = { 1.75, .75 };
		forest.addNbrAt(coord2);

		double[] coord = { 2.75, .75 };
		double[] starts = { 2.75, .75 };
		double[] lengths = { 2.75, .75 };
		assertTrue(forest.coordIsPotentialNbr(coord, starts, lengths));
	}
}
