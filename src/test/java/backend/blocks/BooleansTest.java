package backend.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import domain.blocks.conditional.bool.AndBlock;
import domain.blocks.conditional.bool.OrBlock;
import domain.values.BooleanLiteral;

public class BooleansTest {
	
	private final BooleanLiteral TRUE = new BooleanLiteral(true, null);
	private final BooleanLiteral FALSE = new BooleanLiteral(false, null);
	
	@Test
	public void andTest() {
		assertTrue(new AndBlock(TRUE, TRUE).value());
		
		assertFalse(new AndBlock(TRUE, FALSE).value());
		assertFalse(new AndBlock(FALSE, TRUE).value());
		
		assertFalse(new AndBlock(FALSE, FALSE).value());
		
		assertEquals(new AndBlock(TRUE, TRUE).getCode(), "true && true");
	}
	
	@Test
	public void orTest() {
		assertTrue(new OrBlock(TRUE, TRUE).value());
		
		assertTrue(new OrBlock(TRUE, FALSE).value());
		assertTrue(new OrBlock(FALSE, TRUE).value());
		
		assertFalse(new OrBlock(FALSE, FALSE).value());
		
		System.out.println(new OrBlock(TRUE, TRUE).getCode());
		assertEquals(new OrBlock(TRUE, TRUE).getCode(), "true || true");
	}
}
