package meshCreator.twoDimensions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class OrthantTest {

	@Test
	void SWConstructor() {
		assertEquals(Orthant.SW(), new Orthant(2, 0b00));
	}

	@Test
	void SEConstructor() {
		assertEquals(Orthant.SE(), new Orthant(2, 0b01));
	}

	@Test
	void NWConstructor() {
		assertEquals(Orthant.NW(), new Orthant(2, 0b10));
	}

	@Test
	void NEConstructor() {
		assertEquals(Orthant.NE(), new Orthant(2, 0b11));
	}

	@Test
	void SWGetIndex() {
		assertEquals(Orthant.SW().getIndex(), 0);
	}

	@Test
	void SEGetIndex() {
		assertEquals(Orthant.SE().getIndex(), 1);
	}

	@Test
	void NWGetIndex() {
		assertEquals(Orthant.NW().getIndex(), 2);
	}

	@Test
	void NEGetIndex() {
		assertEquals(Orthant.NE().getIndex(), 3);
	}

	@Test
	void SWIsLowerOnAxis0() {
		assertTrue(Orthant.SW().isLowerOnAxis(0));
	}

	@Test
	void SEIsLowerOnAxis0() {
		assertFalse(Orthant.SE().isLowerOnAxis(0));
	}

	@Test
	void NWIsLowerOnAxis0() {
		assertTrue(Orthant.NW().isLowerOnAxis(0));
	}

	@Test
	void NEIsLowerOnAxis0() {
		assertFalse(Orthant.NE().isLowerOnAxis(0));
	}

	@Test
	void SWIsLowerOnAxis1() {
		assertTrue(Orthant.SW().isLowerOnAxis(1));
	}

	@Test
	void SEIsLowerOnAxis1() {
		assertTrue(Orthant.SE().isLowerOnAxis(1));
	}

	@Test
	void NWIsLowerOnAxis1() {
		assertFalse(Orthant.NW().isLowerOnAxis(1));
	}

	@Test
	void NEIsLowerOnAxis1() {
		assertFalse(Orthant.NE().isLowerOnAxis(1));
	}

	@Test
	void SWGetInteriorSides() {
		Side[] sides = Orthant.SW().getInteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.NORTH());
	}

	@Test
	void SEGetInteriorSides() {
		Side[] sides = Orthant.SE().getInteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.NORTH());
	}

	@Test
	void NWGetInteriorSides() {
		Side[] sides = Orthant.NW().getInteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.SOUTH());
	}

	@Test
	void NEGetInteriorSides() {
		Side[] sides = Orthant.NE().getInteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.SOUTH());
	}

	@Test
	void SWGetExteriorSides() {
		Side[] sides = Orthant.SW().getExteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.SOUTH());
	}

	@Test
	void SEGetExteriorSides() {
		Side[] sides = Orthant.SE().getExteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.SOUTH());
	}

	@Test
	void NWGetExteriorSides() {
		Side[] sides = Orthant.NW().getExteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.NORTH());
	}

	@Test
	void NEGetExteriorSides() {
		Side[] sides = Orthant.NE().getExteriorSides();
		assertEquals(sides.length, 2);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.NORTH());
	}

	@Test
	void GetNumOrthantsForDimension2() {
		assertEquals(Orthant.getNumOrthantsForDimension(2), 4);
	}

	@Test
	void GetValuesForDimension2() {
		Orthant[] values = Orthant.getValuesForDimension(2);
		assertEquals(values.length, 4);
		assertEquals(values[0], Orthant.SW());
		assertEquals(values[1], Orthant.SE());
		assertEquals(values[2], Orthant.NW());
		assertEquals(values[3], Orthant.NE());
	}

	@Test
	void EqualsOtherObject() {
		Object o = new Object();
		Orthant s = Orthant.SE();
		assertNotEquals(s, o);
	}

	@Test
	void EqualsSameObject() {
		Orthant s = Orthant.SE();
		assertEquals(s, s);
	}

	@Test
	void EqualsDifferentOrthantSameDimension() {
		Orthant s1 = Orthant.SE();
		Orthant s2 = Orthant.NW();
		assertNotEquals(s1, s2);
	}

	@Test
	void EqualsDifferentOrthantDifferentDimension() {
		Orthant s1 = Orthant.SW();
		Orthant s2 = new Orthant(3, 0);
		assertNotEquals(s1, s2);
	}

	@Test
	void SWGetNbrOnSideWest() {
		assertEquals(Orthant.SW().getNbrOnSide(Side.WEST()), Orthant.SE());
	}

	@Test
	void SWGetNbrOnSideEast() {
		assertEquals(Orthant.SW().getNbrOnSide(Side.EAST()), Orthant.SE());
	}

	@Test
	void SWGetNbrOnSideSouth() {
		assertEquals(Orthant.SW().getNbrOnSide(Side.SOUTH()), Orthant.NW());
	}

	@Test
	void SWGetNbrOnSideNorth() {
		assertEquals(Orthant.SW().getNbrOnSide(Side.NORTH()), Orthant.NW());
	}

	@Test
	void SEGetNbrOnSideWest() {
		assertEquals(Orthant.SE().getNbrOnSide(Side.WEST()), Orthant.SW());
	}

	@Test
	void SEGetNbrOnSideEast() {
		assertEquals(Orthant.SE().getNbrOnSide(Side.EAST()), Orthant.SW());
	}

	@Test
	void SEGetNbrOnSideSouth() {
		assertEquals(Orthant.SE().getNbrOnSide(Side.SOUTH()), Orthant.NE());
	}

	@Test
	void SEGetNbrOnSideNorth() {
		assertEquals(Orthant.SE().getNbrOnSide(Side.NORTH()), Orthant.NE());
	}

	@Test
	void NWGetNbrOnSideWest() {
		assertEquals(Orthant.NW().getNbrOnSide(Side.WEST()), Orthant.NE());
	}

	@Test
	void NWGetNbrOnSideEast() {
		assertEquals(Orthant.NW().getNbrOnSide(Side.EAST()), Orthant.NE());
	}

	@Test
	void NWGetNbrOnSideSouth() {
		assertEquals(Orthant.NW().getNbrOnSide(Side.SOUTH()), Orthant.SW());
	}

	@Test
	void NWGetNbrOnSideNorth() {
		assertEquals(Orthant.NW().getNbrOnSide(Side.NORTH()), Orthant.SW());
	}

	@Test
	void NEGetNbrOnSideWest() {
		assertEquals(Orthant.NE().getNbrOnSide(Side.WEST()), Orthant.NW());
	}

	@Test
	void NEGetNbrOnSideEast() {
		assertEquals(Orthant.NE().getNbrOnSide(Side.EAST()), Orthant.NW());
	}

	@Test
	void NEGetNbrOnSideSouth() {
		assertEquals(Orthant.NE().getNbrOnSide(Side.SOUTH()), Orthant.SE());
	}

	@Test
	void NEGetNbrOnSideNorth() {
		assertEquals(Orthant.NE().getNbrOnSide(Side.NORTH()), Orthant.SE());
	}

	@Test
	void GetValuesOnSideWest2() {
		Orthant[] orthants = Orthant.GetValuesOnSide(2, Side.WEST());
		assertEquals(orthants.length, 2);
		assertEquals(orthants[0], Orthant.SW());
		assertEquals(orthants[1], Orthant.NW());
	}

	@Test
	void GetValuesOnSideEast2() {
		Orthant[] orthants = Orthant.GetValuesOnSide(2, Side.EAST());
		assertEquals(orthants.length, 2);
		assertEquals(orthants[0], Orthant.SE());
		assertEquals(orthants[1], Orthant.NE());
	}

	@Test
	void GetValuesOnSideSouth2() {
		Orthant[] orthants = Orthant.GetValuesOnSide(2, Side.SOUTH());
		assertEquals(orthants.length, 2);
		assertEquals(orthants[0], Orthant.SW());
		assertEquals(orthants[1], Orthant.SE());
	}

	@Test
	void GetValuesOnSideNorth2() {
		Orthant[] orthants = Orthant.GetValuesOnSide(2, Side.NORTH());
		assertEquals(orthants.length, 2);
		assertEquals(orthants[0], Orthant.NW());
		assertEquals(orthants[1], Orthant.NE());
	}

	@Test
	void SWToString() {
		assertEquals(Orthant.SW().toString(), "[SOUTH, WEST]");
	}

	@Test
	void SEToString() {
		assertEquals(Orthant.SE().toString(), "[SOUTH, EAST]");
	}

	@Test
	void NWToString() {
		assertEquals(Orthant.NW().toString(), "[NORTH, WEST]");
	}

	@Test
	void NEToString() {
		assertEquals(Orthant.NE().toString(), "[NORTH, EAST]");
	}
}
