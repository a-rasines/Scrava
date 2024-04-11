package domain.blocks.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.NumberHelper;

public class MoveBlock extends CoordinateBlock{
	private static final long serialVersionUID = 633807958335477657L;
	
	public MoveBlock(Sprite s, Valuable<? extends Number> x, Valuable<? extends Number> y) {
		super(s, x, y);
	}
	
	public MoveBlock(Sprite s) {
		super(s);
	}


	@Override
	public String getTitle() {
		return "Move x:"+VARIABLE_NUM+" y:"+VARIABLE_NUM;
	}

	@Override
	public String getCode() {
		return "this.moveTo(this.x + "+this.getX().getCode()+", this.y + "+this.getY().getCode()+");";
	}

	@Override
	public void invoke() {
		this.getSprite().getX().setValue(this.getSprite().getX().value() + NumberHelper.castTo(this.getX().value(), Long.class));
		this.getSprite().getY().setValue(this.getSprite().getY().value() + NumberHelper.castTo(this.getY().value(), Long.class));
		
	}


}
