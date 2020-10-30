package meshCreator;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import meshCreator.Orthant;
import meshCreator.Side;

class OrthantTest {

	@Test
	void LowerConstructor() {
		assertEquals(Orthant.Lower(), new Orthant(1, 0b0));
	}

	@Test
	void UpperConstructor() {
		assertEquals(Orthant.Upper(), new Orthant(1, 0b1));
	}

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
	void BSWConstructor() {
		assertEquals(Orthant.BSW(), new Orthant(3, 0b000));
	}

	@Test
	void BSEConstructor() {
		assertEquals(Orthant.BSE(), new Orthant(3, 0b001));
	}

	@Test
	void BNWConstructor() {
		assertEquals(Orthant.BNW(), new Orthant(3, 0b010));
	}

	@Test
	void BNEConstructor() {
		assertEquals(Orthant.BNE(), new Orthant(3, 0b011));
	}

	@Test
	void TSWConstructor() {
		assertEquals(Orthant.TSW(), new Orthant(3, 0b100));
	}

	@Test
	void TSEConstructor() {
		assertEquals(Orthant.TSE(), new Orthant(3, 0b101));
	}

	@Test
	void TNWConstructor() {
		assertEquals(Orthant.TNW(), new Orthant(3, 0b110));
	}

	@Test
	void TNEConstructor() {
		assertEquals(Orthant.TNE(), new Orthant(3, 0b111));
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
	void BSWGetIndex() {
		assertEquals(Orthant.BSW().getIndex(), 0);
	}

	@Test
	void BSEGetIndex() {
		assertEquals(Orthant.BSE().getIndex(), 1);
	}

	@Test
	void BNWGetIndex() {
		assertEquals(Orthant.BNW().getIndex(), 2);
	}

	@Test
	void BNEGetIndex() {
		assertEquals(Orthant.BNE().getIndex(), 3);
	}

	@Test
	void TSWGetIndex() {
		assertEquals(Orthant.TSW().getIndex(), 4);
	}

	@Test
	void TSEGetIndex() {
		assertEquals(Orthant.TSE().getIndex(), 5);
	}

	@Test
	void TNWGetIndex() {
		assertEquals(Orthant.TNW().getIndex(), 6);
	}

	@Test
	void TNEGetIndex() {
		assertEquals(Orthant.TNE().getIndex(), 7);
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
	void BSWIsLowerOnAxis0() {
		assertTrue(Orthant.BSW().isLowerOnAxis(0));
	}

	@Test
	void BSEIsLowerOnAxis0() {
		assertFalse(Orthant.BSE().isLowerOnAxis(0));
	}

	@Test
	void BNWIsLowerOnAxis0() {
		assertTrue(Orthant.BNW().isLowerOnAxis(0));
	}

	@Test
	void BNEIsLowerOnAxis0() {
		assertFalse(Orthant.BNE().isLowerOnAxis(0));
	}

	@Test
	void TSWIsLowerOnAxis0() {
		assertTrue(Orthant.TSW().isLowerOnAxis(0));
	}

	@Test
	void TSEIsLowerOnAxis0() {
		assertFalse(Orthant.TSE().isLowerOnAxis(0));
	}

	@Test
	void TNWIsLowerOnAxis0() {
		assertTrue(Orthant.TNW().isLowerOnAxis(0));
	}

	@Test
	void TNEIsLowerOnAxis0() {
		assertFalse(Orthant.TNE().isLowerOnAxis(0));
	}

	@Test
	void BSWIsLowerOnAxis1() {
		assertTrue(Orthant.BSW().isLowerOnAxis(1));
	}

	@Test
	void BSEIsLowerOnAxis1() {
		assertTrue(Orthant.BSE().isLowerOnAxis(1));
	}

	@Test
	void BNWIsLowerOnAxis1() {
		assertFalse(Orthant.BNW().isLowerOnAxis(1));
	}

	@Test
	void BNEIsLowerOnAxis1() {
		assertFalse(Orthant.BNE().isLowerOnAxis(1));
	}

	@Test
	void TSWIsLowerOnAxis1() {
		assertTrue(Orthant.TSW().isLowerOnAxis(1));
	}

	@Test
	void TSEIsLowerOnAxis1() {
		assertTrue(Orthant.TSE().isLowerOnAxis(1));
	}

	@Test
	void TNWIsLowerOnAxis1() {
		assertFalse(Orthant.TNW().isLowerOnAxis(1));
	}

	@Test
	void TNEIsLowerOnAxis1() {
		assertFalse(Orthant.TNE().isLowerOnAxis(1));
	}

	@Test
	void BSWIsLowerOnAxis2() {
		assertTrue(Orthant.BSW().isLowerOnAxis(2));
	}

	@Test
	void BSEIsLowerOnAxis2() {
		assertTrue(Orthant.BSE().isLowerOnAxis(2));
	}

	@Test
	void BNWIsLowerOnAxis2() {
		assertTrue(Orthant.BNW().isLowerOnAxis(2));
	}

	@Test
	void BNEIsLowerOnAxis2() {
		assertTrue(Orthant.BNE().isLowerOnAxis(2));
	}

	@Test
	void TSWIsLowerOnAxis2() {
		assertFalse(Orthant.TSW().isLowerOnAxis(2));
	}

	@Test
	void TSEIsLowerOnAxis2() {
		assertFalse(Orthant.TSE().isLowerOnAxis(2));
	}

	@Test
	void TNWIsLowerOnAxis2() {
		assertFalse(Orthant.TNW().isLowerOnAxis(2));
	}

	@Test
	void TNEIsLowerOnAxis2() {
		assertFalse(Orthant.TNE().isLowerOnAxis(2));
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
	void BSWGetInteriorSides() {
		Side[] sides = Orthant.BSW().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void BSEGetInteriorSides() {
		Side[] sides = Orthant.BSE().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void BNWGetInteriorSides() {
		Side[] sides = Orthant.BNW().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void BNEGetInteriorSides() {
		Side[] sides = Orthant.BNE().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void TSWGetInteriorSides() {
		Side[] sides = Orthant.TSW().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.BOTTOM());
	}

	@Test
	void TSEGetInteriorSides() {
		Side[] sides = Orthant.TSE().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.BOTTOM());
	}

	@Test
	void TNWGetInteriorSides() {
		Side[] sides = Orthant.TNW().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.BOTTOM());
	}

	@Test
	void TNEGetInteriorSides() {
		Side[] sides = Orthant.TNE().getInteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.BOTTOM());
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
	void BSWGetExteriorSides() {
		Side[] sides = Orthant.BSW().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.BOTTOM());
	}

	@Test
	void BSEGetExteriorSides() {
		Side[] sides = Orthant.BSE().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.BOTTOM());
	}

	@Test
	void BNWGetExteriorSides() {
		Side[] sides = Orthant.BNW().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.BOTTOM());
	}

	@Test
	void BNEGetExteriorSides() {
		Side[] sides = Orthant.BNE().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.BOTTOM());
	}

	@Test
	void TSWGetExteriorSides() {
		Side[] sides = Orthant.TSW().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void TSEGetExteriorSides() {
		Side[] sides = Orthant.TSE().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.SOUTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void TNWGetExteriorSides() {
		Side[] sides = Orthant.TNW().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.WEST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void TNEGetExteriorSides() {
		Side[] sides = Orthant.TNE().getExteriorSides();
		assertEquals(sides.length, 3);
		assertEquals(sides[0], Side.EAST());
		assertEquals(sides[1], Side.NORTH());
		assertEquals(sides[2], Side.TOP());
	}

	@Test
	void GetNumOrthantsForDimension2() {
		assertEquals(Orthant.getNumOrthantsForDimension(2), 4);
	}

	@Test
	void GetNumOrthantsForDimension3() {
		assertEquals(Orthant.getNumOrthantsForDimension(3), 8);
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
	void GetValuesForDimension3() {
		Orthant[] values = Orthant.getValuesForDimension(3);
		assertEquals(values.length, 8);
		assertEquals(values[0], Orthant.BSW());
		assertEquals(values[1], Orthant.BSE());
		assertEquals(values[2], Orthant.BNW());
		assertEquals(values[3], Orthant.BNE());
		assertEquals(values[4], Orthant.TSW());
		assertEquals(values[5], Orthant.TSE());
		assertEquals(values[6], Orthant.TNW());
		assertEquals(values[7], Orthant.TNE());
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
	void BNEGetNbrOnSideTop() {
		assertEquals(Orthant.BNE().getNbrOnSide(Side.TOP()), Orthant.TNE());
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
	void GetValuesOnSideWest3() {
		Orthant[] orthants = Orthant.GetValuesOnSide(3, Side.WEST());
		assertEquals(orthants.length, 4);
		assertEquals(orthants[0], Orthant.BSW());
		assertEquals(orthants[1], Orthant.BNW());
		assertEquals(orthants[2], Orthant.TSW());
		assertEquals(orthants[3], Orthant.TNW());
	}

	@Test
	void LowerToString() {
		assertEquals(Orthant.Lower().toString(), "LOWER");
	}

	@Test
	void UpperToString() {
		assertEquals(Orthant.Upper().toString(), "UPPER");
	}

	@Test
	void SWToString() {
		assertEquals(Orthant.SW().toString(), "SW");
	}

	@Test
	void SEToString() {
		assertEquals(Orthant.SE().toString(), "SE");
	}

	@Test
	void NWToString() {
		assertEquals(Orthant.NW().toString(), "NW");
	}

	@Test
	void NEToString() {
		assertEquals(Orthant.NE().toString(), "NE");
	}

	@Test
	void BSWToString() {
		assertEquals(Orthant.BSW().toString(), "BSW");
	}

	@Test
	void BSEToString() {
		assertEquals(Orthant.BSE().toString(), "BSE");
	}

	@Test
	void BNWToString() {
		assertEquals(Orthant.BNW().toString(), "BNW");
	}

	@Test
	void BNEToString() {
		assertEquals(Orthant.BNE().toString(), "BNE");
	}

	@Test
	void TSWToString() {
		assertEquals(Orthant.TSW().toString(), "TSW");
	}

	@Test
	void TSEToString() {
		assertEquals(Orthant.TSE().toString(), "TSE");
	}

	@Test
	void TNWToString() {
		assertEquals(Orthant.TNW().toString(), "TNW");
	}

	@Test
	void TNEToString() {
		assertEquals(Orthant.TNE().toString(), "TNE");
	}

	@Test
	void FourDToString() {
		assertEquals((new Orthant(4, 0)).toString(), "[Value: 6, BOTTOM, SOUTH, WEST]");
	}

	@Test
	void Invalid3DToString() {
		assertEquals((new Orthant(3, 100)).toString(), "INVALID 3D VALUE: 100");
	}

	@Test
	void Invalid2DToString() {
		assertEquals((new Orthant(2, 100)).toString(), "INVALID 2D VALUE: 100");
	}

	@Test
	void Invalid1DToString() {
		assertEquals((new Orthant(1, 100)).toString(), "INVALID 1D VALUE: 100");
	}

	@Test
	void LowerFromString() {
		assertEquals(Orthant.fromString("LOWER"), Orthant.Lower());
	}

	@Test
	void UpperFromString() {
		assertEquals(Orthant.fromString("UPPER"), Orthant.Upper());
	}

	@Test
	void SWFromString() {
		assertEquals(Orthant.fromString("SW"), Orthant.SW());
	}

	@Test
	void SEFromString() {
		assertEquals(Orthant.fromString("SE"), Orthant.SE());
	}

	@Test
	void NWFromString() {
		assertEquals(Orthant.fromString("NW"), Orthant.NW());
	}

	@Test
	void NEFromString() {
		assertEquals(Orthant.fromString("NE"), Orthant.NE());
	}

	@Test
	void BSWFromString() {
		assertEquals(Orthant.fromString("BSW"), Orthant.BSW());
	}

	@Test
	void BSEFromString() {
		assertEquals(Orthant.fromString("BSE"), Orthant.BSE());
	}

	@Test
	void BNWFromString() {
		assertEquals(Orthant.fromString("BNW"), Orthant.BNW());
	}

	@Test
	void BNEFromString() {
		assertEquals(Orthant.fromString("BNE"), Orthant.BNE());
	}

	@Test
	void TSWFromString() {
		assertEquals(Orthant.fromString("TSW"), Orthant.TSW());
	}

	@Test
	void TSEFromString() {
		assertEquals(Orthant.fromString("TSE"), Orthant.TSE());
	}

	@Test
	void TNWFromString() {
		assertEquals(Orthant.fromString("TNW"), Orthant.TNW());
	}

	@Test
	void TNEFromString() {
		assertEquals(Orthant.fromString("TNE"), Orthant.TNE());
	}

	@Test
	void InvalidFromString() {
		assertThrows(IllegalArgumentException.class, () -> {
			Orthant.fromString("BLAH");
		});
	}

	@Test
	void BSWCollapseOnAxis0() {
		assertEquals(Orthant.BSW().collapseOnAxis(0), Orthant.SW());
	}

	@Test
	void BSWCollapseOnAxis1() {
		assertEquals(Orthant.BSW().collapseOnAxis(1), Orthant.SW());
	}

	@Test
	void BSWCollapseOnAxis2() {
		assertEquals(Orthant.BSW().collapseOnAxis(2), Orthant.SW());
	}

	@Test
	void BSECollapseOnAxis0() {
		assertEquals(Orthant.BSE().collapseOnAxis(0), Orthant.SW());
	}

	@Test
	void BSECollapseOnAxis1() {
		assertEquals(Orthant.BSE().collapseOnAxis(1), Orthant.SE());
	}

	@Test
	void BSECollapseOnAxis2() {
		assertEquals(Orthant.BSE().collapseOnAxis(2), Orthant.SE());
	}

	@Test
	void BNWCollapseOnAxis0() {
		assertEquals(Orthant.BNW().collapseOnAxis(0), Orthant.SE());
	}

	@Test
	void BNWCollapseOnAxis1() {
		assertEquals(Orthant.BNW().collapseOnAxis(1), Orthant.SW());
	}

	@Test
	void BNWCollapseOnAxis2() {
		assertEquals(Orthant.BNW().collapseOnAxis(2), Orthant.NW());
	}

	@Test
	void BNECollapseOnAxis0() {
		assertEquals(Orthant.BNE().collapseOnAxis(0), Orthant.SE());
	}

	@Test
	void BNECollapseOnAxis1() {
		assertEquals(Orthant.BNE().collapseOnAxis(1), Orthant.SE());
	}

	@Test
	void BNECollapseOnAxis2() {
		assertEquals(Orthant.BNE().collapseOnAxis(2), Orthant.NE());
	}

	@Test
	void TSWCollapseOnAxis0() {
		assertEquals(Orthant.TSW().collapseOnAxis(0), Orthant.NW());
	}

	@Test
	void TSWCollapseOnAxis1() {
		assertEquals(Orthant.TSW().collapseOnAxis(1), Orthant.NW());
	}

	@Test
	void TSWCollapseOnAxis2() {
		assertEquals(Orthant.TSW().collapseOnAxis(2), Orthant.SW());
	}

	@Test
	void TSECollapseOnAxis0() {
		assertEquals(Orthant.TSE().collapseOnAxis(0), Orthant.NW());
	}

	@Test
	void TSECollapseOnAxis1() {
		assertEquals(Orthant.TSE().collapseOnAxis(1), Orthant.NE());
	}

	@Test
	void TSECollapseOnAxis2() {
		assertEquals(Orthant.TSE().collapseOnAxis(2), Orthant.SE());
	}

	@Test
	void TNWCollapseOnAxis0() {
		assertEquals(Orthant.TNW().collapseOnAxis(0), Orthant.NE());
	}

	@Test
	void TNWCollapseOnAxis1() {
		assertEquals(Orthant.TNW().collapseOnAxis(1), Orthant.NW());
	}

	@Test
	void TNWCollapseOnAxis2() {
		assertEquals(Orthant.TNW().collapseOnAxis(2), Orthant.NW());
	}

	@Test
	void TNECollapseOnAxis0() {
		assertEquals(Orthant.TNE().collapseOnAxis(0), Orthant.NE());
	}

	@Test
	void TNECollapseOnAxis1() {
		assertEquals(Orthant.TNE().collapseOnAxis(1), Orthant.NE());
	}

	@Test
	void TNECollapseOnAxis2() {
		assertEquals(Orthant.TNE().collapseOnAxis(2), Orthant.NE());
	}

	@Test
	void SWCollapseOnAxis0() {
		assertEquals(Orthant.SW().collapseOnAxis(0), Orthant.Lower());
	}

	@Test
	void SWCollapseOnAxis1() {
		assertEquals(Orthant.SW().collapseOnAxis(1), Orthant.Lower());
	}

	@Test
	void SECollapseOnAxis0() {
		assertEquals(Orthant.SE().collapseOnAxis(0), Orthant.Lower());
	}

	@Test
	void SECollapseOnAxis1() {
		assertEquals(Orthant.SE().collapseOnAxis(1), Orthant.Upper());
	}

	@Test
	void NWCollapseOnAxis0() {
		assertEquals(Orthant.NW().collapseOnAxis(0), Orthant.Upper());
	}

	@Test
	void NWCollapseOnAxis1() {
		assertEquals(Orthant.NW().collapseOnAxis(1), Orthant.Lower());
	}

	@Test
	void NECollapseOnAxis0() {
		assertEquals(Orthant.NE().collapseOnAxis(0), Orthant.Upper());
	}

	@Test
	void NECollapseOnAxis1() {
		assertEquals(Orthant.NE().collapseOnAxis(1), Orthant.Upper());
	}

	@Test
	void UpperCollapseOnAxis0() {
		assertEquals(Orthant.Upper().collapseOnAxis(0), null);
	}

	@Test
	void LowerCollapseOnAxis0() {
		assertEquals(Orthant.Lower().collapseOnAxis(0), null);
	}

	@Test
	void CollapseOnAxisThrowsWithNegativeAxis() {
		assertThrows(IllegalArgumentException.class, () -> {
			Orthant.Upper().collapseOnAxis(-1);
		});
	}

	@Test
	void CollapseOnAxisThrowsWithTooLargeAxis() {
		assertThrows(IllegalArgumentException.class, () -> {
			Orthant.Upper().collapseOnAxis(2);
		});
	}

}
