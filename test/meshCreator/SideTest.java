package meshCreator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import meshCreator.Side;

class SideTest {

	@Test
	void WestConstructor() {
		assertEquals(Side.WEST(), new Side(0b00));
	}

	@Test
	void EastConstructor() {
		assertEquals(Side.EAST(), new Side(0b01));
	}

	@Test
	void SouthConstructor() {
		assertEquals(Side.SOUTH(), new Side(0b10));
	}

	@Test
	void NorthConstructor() {
		assertEquals(Side.NORTH(), new Side(0b11));
	}

	@Test
	void WestGetOpposite() {
		assertEquals(Side.WEST().getOpposite(), Side.EAST());
	}

	@Test
	void EastGetOpposite() {
		assertEquals(Side.EAST().getOpposite(), Side.WEST());
	}

	@Test
	void SouthGetOpposite() {
		assertEquals(Side.SOUTH().getOpposite(), Side.NORTH());
	}

	@Test
	void NorthGetOpposite() {
		assertEquals(Side.NORTH().getOpposite(), Side.SOUTH());
	}

	@Test
	void WestGetIndex() {
		assertEquals(Side.WEST().getIndex(), 0);
	}

	@Test
	void EastGetIndex() {
		assertEquals(Side.EAST().getIndex(), 1);
	}

	@Test
	void SouthGetIndex() {
		assertEquals(Side.SOUTH().getIndex(), 2);
	}

	@Test
	void NorthGetIndex() {
		assertEquals(Side.NORTH().getIndex(), 3);
	}

	@Test
	void WestGetAxis() {
		assertEquals(Side.WEST().getAxis(), 0);
	}

	@Test
	void EastGetAxis() {
		assertEquals(Side.EAST().getAxis(), 0);
	}

	@Test
	void SouthGetAxis() {
		assertEquals(Side.SOUTH().getAxis(), 1);
	}

	@Test
	void NorthGetAxis() {
		assertEquals(Side.NORTH().getAxis(), 1);
	}

	@Test
	void WestIsLowerOnAxis() {
		assertTrue(Side.WEST().isLowerOnAxis());
	}

	@Test
	void EastIsLowerOnAxis() {
		assertFalse(Side.EAST().isLowerOnAxis());
	}

	@Test
	void SouthIsLowerOnAxis() {
		assertTrue(Side.SOUTH().isLowerOnAxis());
	}

	@Test
	void NorthIsLowerOnAxis() {
		assertFalse(Side.NORTH().isLowerOnAxis());
	}

	@Test
	void GetNumSidesForDimension2() {
		assertEquals(Side.getNumSidesForDimension(2), 4);
	}

	@Test
	void GetValuesForDimension2() {
		Side[] values = Side.getValuesForDimension(2);
		assertEquals(values.length, 4);
		assertEquals(values[0], Side.WEST());
		assertEquals(values[1], Side.EAST());
		assertEquals(values[2], Side.SOUTH());
		assertEquals(values[3], Side.NORTH());
	}

	@Test
	void EqualsOtherObject() {
		Object o = new Object();
		Side s = Side.WEST();
		assertNotEquals(s, o);
	}

	@Test
	void EqualsSameObject() {
		Side s = Side.WEST();
		assertEquals(s, s);
	}

	@Test
	void EqualsDifferentSideSame() {
		Side s1 = Side.WEST();
		Side s2 = Side.EAST();
		assertNotEquals(s1, s2);
	}

	@Test
	void WestToString() {
		assertEquals(Side.WEST().toString(), "WEST");
	}

	@Test
	void EastToString() {
		assertEquals(Side.EAST().toString(), "EAST");
	}

	@Test
	void SouthToString() {
		assertEquals(Side.SOUTH().toString(), "SOUTH");
	}

	@Test
	void NorthToString() {
		assertEquals(Side.NORTH().toString(), "NORTH");
	}

	@Test
	void ToString6() {
		assertEquals((new Side(6)).toString(), "Value: 6");
	}

	@Test
	void ToString7() {
		assertEquals((new Side(7)).toString(), "Value: 7");
	}
}
