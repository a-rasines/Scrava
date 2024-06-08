package domain.blocks.movement;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.values.NumberHelper;

public class MoveBlock extends CoordinateBlock{
	private static final long serialVersionUID = 633807958335477657L;
	
	@Override
	public Translatable create(Sprite s) {
		return new MoveBlock(s);
	}
	
	public MoveBlock(Sprite s) {
		super(s);
	}
	
	public MoveBlock(Sprite s, Valuable<? extends Number> x, Valuable<? extends Number> y) {
		super(s, x, y);
	}

	@Override
	public String getTitle() {
		return "Move x:"+VARIABLE_NUM+" y:"+VARIABLE_NUM;
	}

	@Override
	public String getCode() {
		return "this.moveTo(this.getX()" + (this.getX() == null? "":" + " + this.getX().getCode()) + ", this.getY()" + (this.getY() == null? "":" + " + this.getY().getCode()) + ");";
	}

	@Override
	public void invoke() {
		if(this.getX()!=null)this.getSprite().getX().setValue(this.getSprite().getX().value() + NumberHelper.castTo(this.getX().value(), Long.class));
		if(this.getY()!=null)this.getSprite().getY().setValue(this.getSprite().getY().value() + NumberHelper.castTo(this.getY().value(), Long.class));
		
	}


}
