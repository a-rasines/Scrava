package backend.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.blocks.conditional.BiggerOrEqualThanBlock;
import domain.blocks.conditional.BiggerThanBlock;
import domain.blocks.conditional.EqualsBlock;
import domain.blocks.conditional.SmallerOrEqualThanBlock;
import domain.blocks.conditional.SmallerThanBlock;
import domain.values.BooleanLiteral;
import domain.values.NumberLiteral;
import domain.values.StringLiteral;

public class ComparatorsTest {
	
	private final NumberLiteral<Integer> BIG = new NumberLiteral<Integer>(2, null);
	private final NumberLiteral<Integer> SMALL = new NumberLiteral<Integer>(1, null);
	private final NumberLiteral<Double> SMALLD = new NumberLiteral<Double>(1.0, null);
	@Test
	public void biggerTest() {
		assertTrue(new BiggerThanBlock(BIG, SMALL).value());
		
		assertFalse(new BiggerThanBlock(SMALL, SMALL).value());
		assertFalse(new BiggerThanBlock(SMALL, BIG).value());
		
		assertEquals(new BiggerThanBlock(BIG, SMALL).getCode(), "2 > 1");
		assertEquals(new BiggerThanBlock(SMALL, SMALL).getCode(), "1 > 1");
		assertEquals(new BiggerThanBlock(SMALL, BIG).getCode(), "1 > 2");
	}
	
	@Test
	public void biggerOrEqualTest() {
		assertTrue(new BiggerOrEqualThanBlock(BIG, SMALL).value());
		assertTrue(new BiggerOrEqualThanBlock(SMALL, SMALL).value());
		
		assertFalse(new BiggerOrEqualThanBlock(SMALL, BIG).value());
		
		assertEquals(new BiggerOrEqualThanBlock(BIG, SMALL).getCode(), "2 >= 1");
		assertEquals(new BiggerOrEqualThanBlock(SMALL, SMALL).getCode(), "1 >= 1");
		assertEquals(new BiggerOrEqualThanBlock(SMALL, BIG).getCode(), "1 >= 2");
	}
	
	@Test
	public void equalsTest() {
		
		assertTrue(new EqualsBlock(new BooleanLiteral(true, null), new BooleanLiteral(true, null)).value());
		assertTrue(new EqualsBlock(new StringLiteral("test", null), new StringLiteral("test", null)).value());
		assertTrue(new EqualsBlock(SMALL, SMALL).value());
		assertTrue(new EqualsBlock(SMALL, SMALLD).value());
		assertTrue(new EqualsBlock(SMALLD, SMALL).value());
		assertTrue(new EqualsBlock(SMALLD, SMALLD).value());
		
		assertFalse(new EqualsBlock(new BooleanLiteral(true, null), new BooleanLiteral(false, null)).value());
		assertFalse(new EqualsBlock(new StringLiteral("test", null), new StringLiteral("aaa", null)).value());
		assertFalse(new EqualsBlock(SMALL, BIG).value());
		
		assertEquals(new EqualsBlock(new BooleanLiteral(true, null), new BooleanLiteral(false, null)).getCode(), "true == false");
		assertEquals(new EqualsBlock(new StringLiteral("test", null), new StringLiteral("aaa", null)).getCode(), "\"test\".equals(\"aaa\") /*The == operator doesn't work well for the variable's type*/");
		assertEquals(new EqualsBlock(SMALL, BIG).getCode(), "1 == 2");
	}
	
	@Test
	public void smallerOrEqualTest() {
		
		assertFalse(new SmallerOrEqualThanBlock(BIG, SMALL).value());
		
		assertTrue(new SmallerOrEqualThanBlock(SMALL, SMALL).value());
		assertTrue(new SmallerOrEqualThanBlock(SMALL, BIG).value());
		
		assertEquals(new SmallerOrEqualThanBlock(BIG, SMALL).getCode(), "2 <= 1");
		assertEquals(new SmallerOrEqualThanBlock(SMALL, SMALL).getCode(), "1 <= 1");
		assertEquals(new SmallerOrEqualThanBlock(SMALL, BIG).getCode(), "1 <= 2");
	}
	
	@Test
	public void smallerTest() {
		
		assertFalse(new SmallerThanBlock(BIG, SMALL).value());
		assertFalse(new SmallerThanBlock(SMALL, SMALL).value());
		
		assertTrue(new SmallerThanBlock(SMALL, BIG).value());
		
		assertEquals(new SmallerThanBlock(BIG, SMALL).getCode(), "2 < 1");
		assertEquals(new SmallerThanBlock(SMALL, SMALL).getCode(), "1 < 1");
		assertEquals(new SmallerThanBlock(SMALL, BIG).getCode(), "1 < 2");
		
	}
}
