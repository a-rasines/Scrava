package domain.blocks.operators;

import java.util.Random;
import java.util.Set;

import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberHelper;

public class RandomOperator extends OperatorBlock<Number, Number>{
	
	private static final long serialVersionUID = 2400959582373684302L;

	public RandomOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		Number lv = left.value();
		Number rv = right.value();
		if (lv instanceof Float && rv instanceof Float)
			return NumberHelper.castTo(lv, Float.class) + (new Random().nextFloat() % (NumberHelper.castTo(rv, Float.class) - NumberHelper.castTo(lv, Float.class)));
		else if(lv instanceof Double || rv instanceof Double || lv instanceof Float || rv instanceof Float)
			return NumberHelper.castTo(lv, Double.class) + (new Random().nextDouble() % (NumberHelper.castTo(rv, Double.class) - NumberHelper.castTo(lv, Double.class)));
		else if(lv instanceof Long || rv instanceof Long)
			return NumberHelper.castTo(lv, Long.class) + (new Random().nextLong() % (NumberHelper.castTo(rv, Long.class) - NumberHelper.castTo(lv, Long.class)));
		else
			return NumberHelper.castTo(lv, Integer.class) + new Random().nextInt(NumberHelper.castTo(rv, Integer.class) - NumberHelper.castTo(lv, Integer.class));
	}	

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		Number lv = left.value();
		Number rv = right.value();
		String lc = left.getCode();
		String rc = right.getCode();
		if (lv instanceof Float && rv instanceof Float)
			return "(" + lc + " + (new Random().nextFloat() % (" + rc + " - " + lc + "))";
		else if(lv instanceof Double || rv instanceof Double || lv instanceof Float || rv instanceof Float)
			return "(" + lc + " + (new Random().nextDouble() % (" + rc + " - " + lc + "))";
		else if(lv instanceof Long || rv instanceof Long)
			return "(" + lc + " + (new Random().nextLong() % (" + rc + " - " + lc + "))";
		else
			return "(" + lc + " + new Random().nextInt("+ rc+" - " + lc + "))";
	}
	
	@Override
	public void getImports(Set<String> imports) {
		super.getImports(imports);
		imports.add("java.util.Random");
	}


	@Override
	public String getTitle() {
		return "Random number between " + VARIABLE_NUM + " and " + VARIABLE_NUM;
	}
}
