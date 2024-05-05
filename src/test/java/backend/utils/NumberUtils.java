package backend.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import domain.values.NumberHelper;

public class NumberUtils {

	//LONG
	
	@Test
	public void longToLong() {
		assertEquals((Long)0l, NumberHelper.castTo(0l, Long.class));
	}
	
	@Test
	public void intToLong() {
		assertEquals((Long)0l, NumberHelper.castTo(0, Long.class));
	}
	
	@Test
	public void shortToLong() {
		assertEquals((Long)0l, NumberHelper.castTo((Short)(short)0, Long.class));
	}
	@Test
	public void byteToLong() {
		assertEquals((Long)0l, NumberHelper.castTo((Byte)(byte)0, Long.class));
	}
	@Test
	public void floatToLong() {
		assertEquals((Long)0l, NumberHelper.castTo(0f, Long.class));
	}
	@Test
	public void doubleToLong() {
		assertEquals((Long)0l, NumberHelper.castTo(0., Long.class));
	}
	
	//INTEGER
	
	@Test
	public void longToInteger() {
		assertEquals((Integer)0, NumberHelper.castTo(0l, Integer.class));
	}
	
	@Test
	public void intToInteger() {
		assertEquals((Integer)0, NumberHelper.castTo(0, Integer.class));
	}
	
	@Test
	public void shortToInteger() {
		assertEquals((Integer)0, NumberHelper.castTo((Short)(short)0, Integer.class));
	}
	@Test
	public void byteToInteger() {
		assertEquals((Integer)0, NumberHelper.castTo((Byte)(byte)0, Integer.class));
	}
	@Test
	public void floatToInteger() {
		assertEquals((Integer)0, NumberHelper.castTo(0f, Integer.class));
	}
	@Test
	public void doubleToInteger() {
		assertEquals((Integer)0, NumberHelper.castTo(0., Integer.class));
	}
	
	//SHORT
	
	@Test
	public void longToShort() {
		assertEquals((Short)(short)0, NumberHelper.castTo(0l, Short.class));
	}
	
	@Test
	public void intToShort() {
		assertEquals((Short)(short)0, NumberHelper.castTo(0, Short.class));
	}
	
	@Test
	public void shortToShort() {
		assertEquals((Short)(short)0, NumberHelper.castTo((Short)(short)0, Short.class));
	}
	@Test
	public void byteToShort() {
		assertEquals((Short)(short)0, NumberHelper.castTo((Byte)(byte)0, Short.class));
	}
	@Test
	public void floatToShort() {
		assertEquals((Short)(short)0, NumberHelper.castTo(0f, Short.class));
	}
	@Test
	public void doubleToShort() {
		assertEquals((Short)(short)0, NumberHelper.castTo(0., Short.class));
	}
	
	//BYTE
	
	@Test
	public void longToByte() {
		assertEquals((Byte)(byte)0, NumberHelper.castTo(0l, Byte.class));
	}
	
	@Test
	public void intToByte() {
		assertEquals((Byte)(byte)0, NumberHelper.castTo(0, Byte.class));
	}
	
	@Test
	public void shortToByte() {
		assertEquals((Byte)(byte)0, NumberHelper.castTo((Short)(short)0, Byte.class));
	}
	@Test
	public void byteToByte() {
		assertEquals((Byte)(byte)0, NumberHelper.castTo((Byte)(byte)0, Byte.class));
	}
	@Test
	public void floatToByte() {
		assertEquals((Byte)(byte)0, NumberHelper.castTo(0f, Byte.class));
	}
	@Test
	public void doubleToByte() {
		assertEquals((Byte)(byte)0, NumberHelper.castTo(0., Byte.class));
	}
	
	//FLOAT
	
	@Test
	public void longToFloat() {
		assertEquals((Float)0f, NumberHelper.castTo(0l, Float.class));
	}
	
	@Test
	public void intToFloat() {
		assertEquals((Float)0f, NumberHelper.castTo(0, Float.class));
	}
	
	@Test
	public void shortToFloat() {
		assertEquals((Float)0f, NumberHelper.castTo((Short)(short)0, Float.class));
	}
	@Test
	public void byteToFloat() {
		assertEquals((Float)0f, NumberHelper.castTo((Byte)(byte)0, Float.class));
	}
	@Test
	public void floatToFloat() {
		assertEquals((Float)0f, NumberHelper.castTo(0f, Float.class));
	}
	@Test
	public void doubleToFloat() {
		assertEquals((Float)0f, NumberHelper.castTo(0., Float.class));
	}
	
	//DOUBLE
	
	@Test
	public void longToDouble() {
		assertEquals((Double)0., NumberHelper.castTo(0l, Double.class));
	}
	
	@Test
	public void intToDouble() {
		assertEquals((Double)0., NumberHelper.castTo(0, Double.class));
	}
	
	@Test
	public void shortToDouble() {
		assertEquals((Double)0., NumberHelper.castTo((Short)(short)0, Double.class));
	}
	@Test
	public void byteToDouble() {
		assertEquals((Double)0., NumberHelper.castTo((Byte)(byte)0, Double.class));
	}
	@Test
	public void floatToDouble() {
		assertEquals((Double)0., NumberHelper.castTo(0f, Double.class));
	}
	@Test
	public void doubleToDouble() {
		assertEquals((Double)0., NumberHelper.castTo(0., Double.class));
	}
	
}
