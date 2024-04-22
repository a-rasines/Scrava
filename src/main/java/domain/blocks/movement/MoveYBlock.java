package domain.blocks.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import domain.values.NumberLiteral;

public class MoveYBlock extends MoveBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	@Override
	public MoveYBlock create(Sprite s) {
		return new MoveYBlock(s);
	}
	
	public MoveYBlock(Sprite s, Valuable<? extends Number> y) {
		super(s, new NumberLiteral<Byte>((byte)0), y);
	}
	
	public MoveYBlock(Sprite s) {
		super(s, new NumberLiteral<Byte>((byte)0), AbstractLiteral.getDefault(0));
	}
	@Override
	public String getTitle() {
		return "Move y: " + VARIABLE_NUM;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {super.getAllVariables()[1]};
	}
	
	

}

