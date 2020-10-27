package meshCreator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NodeTest {
	@Test
	void getDimension2d() {
		Node node = new Node(2);
		assertEquals(node.getDimension(), 2);
	}

	@Test
	void getDimension3d() {
		Node node = new Node(3);
		assertEquals(node.getDimension(), 3);
	}

	@Test
	void testDefault2DConstructorGetId() {
		Node node = new Node(2);
		assertEquals(node.getId(), -1);
	}

	@Test
	void testDefault2DConstructorSetId() {
		Node node = new Node(2);
		node.setId(2);
		assertEquals(node.getId(), 2);
	}

	@Test
	void testDefault2DConstructorGetLevel() {
		Node node = new Node(2);
		assertEquals(node.getLevel(), 0);
	}

	@Test
	void testDefault2DConstructorSetLevel() {
		Node node = new Node(2);
		node.setLevel(1);
		assertEquals(node.getLevel(), 1);
	}

	@Test
	void testDefault2DConstructorHasNbr() {
		Node node = new Node(2);
		for (Side s : Side.getValuesForDimension(2)) {
			assertFalse(node.hasNbr(s));
		}
	}

	@Test
	void testDefault2DConstructorGetNbrId() {
		Node node = new Node(2);
		for (Side s : Side.getValuesForDimension(2)) {
			assertEquals(node.getNbrId(s), -1);
		}
	}

	@Test
	void testDefault2DConstructorSetNbrId() {
		for (Side s : Side.getValuesForDimension(2)) {
			Node node = new Node(2);
			node.setNbrId(s, 2);
			assertEquals(node.getNbrId(s), 2);
			assertTrue(node.hasNbr(s));
		}
	}

	@Test
	void testDefault2DConstructorHasParent() {
		Node node = new Node(2);
		assertFalse(node.hasParent());
	}

	@Test
	void testDefault2DConstructorGetParentId() {
		Node node = new Node(2);
		assertEquals(node.getParentId(), -1);
	}

	@Test
	void testDefault2DConstructorSetParentId() {
		Node node = new Node(2);
		node.setParentId(3);
		assertEquals(node.getParentId(), 3);
		assertTrue(node.hasParent());
	}

	@Test
	void testDefault2DConstructorHasChildren() {
		Node node = new Node(2);
		assertFalse(node.hasChildren());
	}

	@Test
	void testDefault2DConstructorGetChildId() {
		Node node = new Node(2);
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			assertEquals(node.getChildId(o), -1);
		}
	}

	@Test
	void testDefault2DConstructorSetChildId() {
		Node node = new Node(2);
		for (Orthant o : Orthant.getValuesForDimension(2)) {
			node.setChildId(o, o.getIndex());
			assertEquals(node.getChildId(o), o.getIndex());
		}
		assertTrue(node.hasChildren());
	}

	@Test
	void testDefault2DConstructorIsInside() {
		Node node = new Node(2);
		double[] coord = new double[2];
		coord[0] = -1;
		coord[1] = -1;
		assertFalse(node.coordIsInside(coord));
		coord[0] = .5;
		coord[1] = -1;
		assertFalse(node.coordIsInside(coord));
		coord[0] = 2;
		coord[1] = -1;
		assertFalse(node.coordIsInside(coord));
		coord[0] = 3;
		coord[1] = 1;
		assertFalse(node.coordIsInside(coord));
		coord[0] = .2;
		coord[1] = .3;
		assertTrue(node.coordIsInside(coord));
	}

	@Test
	void testDefault2DConstructorGetLength() {
		Node node = new Node(2);
		for (int i = 0; i < 2; i++) {
			assertEquals(node.getLength(i), 1);
		}
	}

	@Test
	void testDefault2DConstructorSetLength() {
		Node node = new Node(2);
		for (int i = 0; i < 2; i++) {
			node.setLength(i, 1 + i);
			assertEquals(node.getLength(i), 1 + i);
		}
	}

	@Test
	void testDefault2DConstructorGetStart() {
		Node node = new Node(2);
		for (int i = 0; i < 2; i++) {
			assertEquals(node.getStart(i), 0);
		}
	}

	@Test
	void testDefault2DConstructorSetStart() {
		Node node = new Node(2);
		for (int i = 0; i < 2; i++) {
			node.setStart(i, 1 + i);
			assertEquals(node.getStart(i), 1 + i);
		}
	}

}
