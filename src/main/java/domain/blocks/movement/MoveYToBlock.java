package domain.blocks.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import domain.values.NumberLiteral;

public class MoveYToBlock extends MoveToBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	@Override
	public MoveYToBlock create(Sprite s) {
		return new MoveYToBlock(s);
	}
	
	public MoveYToBlock(Sprite s, Valuable<? extends Number> y) {
		super(s, new NumberLiteral<Byte>((byte)0), y);
	}
	
	public MoveYToBlock(Sprite s) {
		super(s, new NumberLiteral<Byte>((byte)0), AbstractLiteral.getDefault(0));
	}
	
	@Override
	public String getTitle() {
		return "Move to y: " + VARIABLE_NUM;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {super.getAllVariables()[1]};
	}

}
