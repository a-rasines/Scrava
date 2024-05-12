package domain.blocks.movement;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.FunctionBlock;
import domain.values.AbstractLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public abstract class CoordinateBlock extends FunctionBlock{

	private static final long serialVersionUID = 3637013013393291141L;
	
	private Valuable<? extends Number> x;
	private Valuable<? extends Number> y;
	private Valuable<? extends Number> defX;
	private Valuable<? extends Number> defY;
	
	protected CoordinateBlock(Sprite s) {
		super(s);
		this.x = AbstractLiteral.getDefault(0);
		this.y = AbstractLiteral.getDefault(0);;
		this.defX = this.x;
		this.defY = this.y;
	}
	
	protected CoordinateBlock(Sprite s, Valuable<? extends Number> x, Valuable<? extends Number> y) {
		super(s);
		this.x = x;
		this.y = y;
		this.defX = x instanceof AbstractLiteral ? x : AbstractLiteral.getDefault(0);
		this.defY = y instanceof AbstractLiteral ? y : AbstractLiteral.getDefault(0);
	}

	public Valuable<? extends Number> getX() {
		return x;
	}

	public void setX(Valuable<? extends Number> x) {
		this.x = x;
	}

	public Valuable<? extends Number> getY() {
		return y;
	}

	public void setY(Valuable<? extends Number> y) {
		this.y = y;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable<?>[] {x, y};
	}
	
	@Override
	public Valuable<? extends Number> getVariableAt(int q) {
		return q==0?x:y;
	}
	@Override
	public void getImports(Set<String> imports) {
		x.getImports(imports);
		y.getImports(imports);
	}
	

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.MOVEMENT;
	}
	
	@Override
	public LiteralRenderable<? extends Number> removeVariable(Valuable<?> v) {
		if(x.equals(v)) {
			x = defX;
			return (LiteralRenderable<? extends Number>) x;
		} else if(y.equals(v)) {
			y = defY;
			return (LiteralRenderable<? extends Number>)y;
		}
		return null;
		
	}
	
	@Override
	public void removeVariableAt(int i) {
		if(i == 0)
			x = defX;
		else
			y = defY;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		if(v.value() instanceof Number)
			if(i == 0)
				x = (Valuable<? extends Number>) v;
			else
				y = (Valuable<? extends Number>) v;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		if(x.equals(old)) {
			x = (Valuable<? extends Number>) newValue;
		} else if(y.equals(old)) {
			y = (Valuable<? extends Number>) newValue;
		}
		
	}
	@Override
	public void reset() {
		x.reset();
		y.reset();
	}
	
	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof Number;
	}

}
