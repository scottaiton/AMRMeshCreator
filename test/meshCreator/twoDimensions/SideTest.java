package meshCreator.twoDimensions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class SideTest {

	@Test
	void WestConstructor() {
		assertEquals(Side.WEST(), new Side(2, 0b00));
	}

	@Test
	void EastConstructor() {
		assertEquals(Side.EAST(), new Side(2, 0b01));
	}

	@Test
	void SouthConstructor() {
		assertEquals(Side.SOUTH(), new Side(2, 0b10));
	}

	@Test
	void NorthConstructor() {
		assertEquals(Side.NORTH(), new Side(2, 0b11));
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
	void GetNumSidesForDimension2() {
		assertEquals(Side.getNumSidesForDimension(2), 4);
	}

	@Test
	void GetValuesForDimension2() {
		ArrayList<Side> values = Side.getValuesForDimension(2);
		assertEquals(values.size(), 4);
		assertEquals(values.get(0), Side.WEST());
		assertEquals(values.get(1), Side.EAST());
		assertEquals(values.get(2), Side.SOUTH());
		assertEquals(values.get(3), Side.NORTH());
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
	void EqualsDifferentSideSameDimension() {
		Side s1 = Side.WEST();
		Side s2 = Side.EAST();
		assertNotEquals(s1, s2);
	}

	@Test
	void EqualsDifferentSideDifferentDimension() {
		Side s1 = Side.WEST();
		Side s2 = new Side(3,0);
		assertNotEquals(s1, s2);
	}
}
