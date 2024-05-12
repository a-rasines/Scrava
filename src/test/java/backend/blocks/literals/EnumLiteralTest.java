package backend.blocks.literals;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import domain.values.EnumLiteral;

public class EnumLiteralTest {
	
	public static enum LiteralEnum {
		VALUE1,
		VALUE2,
		VALUE3,
		VALUE4
	}
	private static HashMap<String, String> pseudoEnum = new HashMap<>() {
		
		private static final long serialVersionUID = 7635490782683897369L;

		{
			for(LiteralEnum le : LiteralEnum.values())
				put(le.name(), le.name());
		}
	};
	private static EnumLiteral<LiteralEnum> enumLiteral = new EnumLiteral<>(LiteralEnum.VALUE1);
	
	private static EnumLiteral<String> mapLiteral = new EnumLiteral<>(pseudoEnum);
	
	@Before
	public void checkValue() {
		assertEquals(enumLiteral.value(), LiteralEnum.VALUE1);
		assertEquals(mapLiteral.value(), LiteralEnum.VALUE1.name());
	}
	
	@Test
	public void checkChangeValue() {
		enumLiteral.setValue(LiteralEnum.VALUE2.name());
		mapLiteral.setValue(LiteralEnum.VALUE2.name());
		assertEquals(enumLiteral.value(), LiteralEnum.VALUE2);
		assertEquals(mapLiteral.value(), LiteralEnum.VALUE2.name());
	}
	
	@Test
	public void checkGetValues() {
		assertArrayEquals(enumLiteral.possibleValues(), LiteralEnum.values());
		assertArrayEquals(mapLiteral.possibleValues(), pseudoEnum.values().toArray());
		
	}

}
