package domain.blocks.invocable.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.NumberHelper;

public class MoveXToBlock extends CoordinateBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	@Override
	public MoveXToBlock create(Sprite s) {
		return new MoveXToBlock(s);
	}
	
	public MoveXToBlock(Sprite s, Valuable<? extends Number> x) {
		super(s, x, null);
	}
	
	public MoveXToBlock(Sprite s) {
		super(s);
	}

	@Override
	public String getTitle() {
		return "Move to x: " + VARIABLE_NUM;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {super.getAllVariables()[0]};
	}
	
	@Override
	public void invoke() {
		this.getSprite().getX().setValue(NumberHelper.castTo(getX().value(), Long.class));
	}

	@Override
	public String getCode() {
		return "this.moveTo("+(getX().value() instanceof Integer?getX().getCode():"(int)"+getX().getCode())+", this.getY());";
	}

}
