package backend.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.blocks.conditional.BiggerOrEqualThanBlock;
import domain.blocks.conditional.EqualsBlock;
import domain.blocks.conditional.SmallerOrEqualThanBlock;
import domain.blocks.conditional.SmallerThanBlock;
import domain.blocks.operators.AddOperator;
import domain.blocks.operators.AppendOperator;
import domain.blocks.operators.DivideOperator;
import domain.blocks.operators.MaxOperator;
import domain.blocks.operators.MinOperator;
import domain.blocks.operators.ModulusOperator;
import domain.blocks.operators.MultiplyOperator;
import domain.blocks.operators.RandomOperator;
import domain.blocks.operators.SubstractOperator;
import domain.values.NumberLiteral;
import domain.values.StringLiteral;

public class OperatorsTest {
	
	private final NumberLiteral<Integer> NUMBER1 = new NumberLiteral<>(1);
	private final NumberLiteral<Double> NUMBERD1 = new NumberLiteral<>(1.0);
	private final NumberLiteral<Float> NUMBERF1 = new NumberLiteral<>(1.0f);
	private final NumberLiteral<Integer> NUMBER2 = new NumberLiteral<>(2);
	private final NumberLiteral<Integer> NUMBER3 = new NumberLiteral<>(3);
	private final NumberLiteral<Double> NUMBERD3 = new NumberLiteral<>(3.0);
	private final NumberLiteral<Float> NUMBERF3 = new NumberLiteral<>(3.0f);
	@Test
	public void addTest() {
		assertEquals(new AddOperator(NUMBER1, NUMBER1).value(), 2l);
		assertTrue(new EqualsBlock(NUMBER2, new AddOperator(NUMBER1, NUMBER1)).value());
		assertTrue(new EqualsBlock(NUMBER2, new AddOperator(NUMBER1, NUMBERD1)).value());
		assertTrue(new BiggerOrEqualThanBlock(new AddOperator(NUMBERD1, NUMBERD1), NUMBER1).value());
	
		assertEquals(new AppendOperator().setValues(new StringLiteral("a"), new StringLiteral("b")).value(), "ab");
	
		assertEquals(new EqualsBlock(NUMBER2, new AddOperator(NUMBER1, NUMBER1)).getCode(), "2 == 1 + 1");
	}
	
	@Test
	public void divideTest() {
		assertEquals(new DivideOperator(NUMBER2, NUMBER2).value(), 2l/2l);
		assertEquals(new DivideOperator(NUMBER2, NUMBER2).getCode(), "2 / 2");
	}
	
	@Test
	public void maxTest() {
		assertEquals(new MaxOperator(NUMBER2, NUMBER1).value(), Math.max(2l, 1l));
		assertEquals(new MaxOperator(NUMBER2, NUMBER1).getCode(), "Math.max(2, 1)");
	}
	
	@Test
	public void minTest() {
		assertEquals(new MinOperator(NUMBER2, NUMBER1).value(), Math.min(2l, 1l));
		assertEquals(new MinOperator(NUMBER2, NUMBER1).getCode(), "Math.min(2, 1)");
	}
	
	@Test
	public void modulusTest() {
		assertEquals(new ModulusOperator(NUMBER3, NUMBER2).value(), 3l%2l);
		assertEquals(new ModulusOperator(NUMBER3, NUMBER2).getCode(), "3 % 2");
	}
	
	@Test
	public void multiplyTest() {
		assertEquals(new MultiplyOperator(NUMBER3, NUMBER2).value(), 3l*2l);
		assertEquals(new MultiplyOperator(NUMBER3, NUMBER2).getCode(), "3 * 2");
	}
	
	@Test
	public void randomTest() {
		for(byte i = 0; i < 10; i++) {//To make sure most scenarios are covered with randomness
			assertTrue(new BiggerOrEqualThanBlock(new RandomOperator(NUMBER1, NUMBER3), NUMBER1).value());
			assertTrue(new SmallerOrEqualThanBlock(new RandomOperator(NUMBER1, NUMBER3), NUMBER3).value());
			
			assertTrue(new BiggerOrEqualThanBlock(new RandomOperator(NUMBERD1, NUMBERD3), NUMBER1).value());
			assertTrue(new SmallerOrEqualThanBlock(new RandomOperator(NUMBERD1, NUMBERD3), NUMBER3).value());
			
			assertTrue(new BiggerOrEqualThanBlock(new RandomOperator(NUMBERF1, NUMBERF3), NUMBER1).value());
			assertTrue(new SmallerOrEqualThanBlock(new RandomOperator(NUMBERF1, NUMBERF3), NUMBER3).value());
		}
		assertEquals(new RandomOperator(NUMBER1, NUMBER3).getCode(), "(1 + new Random().nextInt(3 - 1))");
		assertEquals(new RandomOperator(NUMBERD1, NUMBERD3).getCode(), "(1.0 + (new Random().nextDouble() % (3.0 - 1.0))");
		assertEquals(new RandomOperator(NUMBERD1, NUMBERF3).getCode(), "(1.0 + (new Random().nextDouble() % (3.0f - 1.0))");
		assertEquals(new RandomOperator(NUMBERF1, NUMBERF3).getCode(), "(1.0f + (new Random().nextFloat() % (3.0f - 1.0f))");
	}
	
	@Test
	public void substractTest() {
		assertEquals(new SubstractOperator(NUMBER1, NUMBER1).value(), 0l);
		assertTrue(new EqualsBlock(NUMBER1, new SubstractOperator(NUMBER2, NUMBER1)).value());
		assertTrue(new EqualsBlock(NUMBER1, new SubstractOperator(NUMBER2, NUMBERD1)).value());
		assertTrue(new SmallerThanBlock(new SubstractOperator(NUMBERD1, NUMBERD1), NUMBER1).value());
	
	
		assertEquals(new EqualsBlock(NUMBER2, new SubstractOperator(NUMBER1, NUMBER1)).getCode(), "2 == 1 - 1");
	}
}
