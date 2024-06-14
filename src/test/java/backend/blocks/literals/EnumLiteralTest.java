package backend.blocks.literals;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.values.EnumLiteral;
import domain.values.EnumLiteral.EnumCapable;

public class EnumLiteralTest {
	
	public static enum LiteralEnum implements EnumCapable<LiteralEnum>{
		VALUE1,
		VALUE2,
		VALUE3,
		VALUE4;

		@Override
		public LiteralEnum valueof(String value) {
			return LiteralEnum.valueOf(value);
		}

		@Override
		public String[] names() {
			LiteralEnum[] values = LiteralEnum.values();
			String[] s = new String[values.length];
			for(int i = 0; i < values.length; i++)
				s[i] = values[i].name();
				
			return s;
		}


		@Override
		public List<LiteralEnum> getValues() {
			return List.of(values());
		}
	}
	private static HashMap<String, String> pseudoEnum = new HashMap<>() {
		
		private static final long serialVersionUID = 7635490782683897369L;

		{
			for(LiteralEnum le : LiteralEnum.values())
				put(le.name(), le.name());
		}
	};
	private static EnumLiteral<LiteralEnum> enumLiteral = new EnumLiteral<>(LiteralEnum.VALUE1, null);
	
	private static EnumLiteral<String> mapLiteral = new EnumLiteral<>(pseudoEnum, null);
	
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
		assertEquals(enumLiteral.possibleValues(), LiteralEnum.values());
		assertEquals(mapLiteral.possibleValues(), pseudoEnum.values().toArray());
		
	}

}
