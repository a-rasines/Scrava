package clickable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domain.models.interfaces.Clickable;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import ui.components.BlockPanel;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.LiteralRenderer;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class BlockClickable implements Clickable{
	
	//protected final Set<Clickable> nestedClickables = new HashSet<>();
	protected Clickable clicked;
	protected Clickable hovered;
	private DragableRenderer renderer;
	private BlockClickable parent;
	
	public BlockClickable(DragableRenderer renderer, BlockClickable parent) {
		this.parent = parent;
		this.renderer = renderer;
	}
	
	public void removeChild(BlockClickable child) {
		System.out.println(getClass().getSimpleName() + " remove child");
		child.setParent(null);
		if(child.getBlock() instanceof Valuable vBlock) {
			VariableHolder vh = (VariableHolder)getBlock();
			LiteralRenderable<?> lr = vh.removeVariable(vBlock);
			LiteralRenderer.of(lr, lr.value(), this);
		}
	}
	
	public void removeParent() {
		
	}
	
	protected boolean between(int v, int min, int max) {
		return v <= max && v >= min;
	}
	
	protected int cx;
	protected int cy;
	
	@Override
	public void onClick(int x, int y) {
		this.cx = x;
		this.cy = y;
		System.out.println("Checking " + getNestedClickables().size() + " childs.");
		clicked = null;
		for(Clickable c : getNestedClickables()) {
			Rect rect = c.getPosition();
			System.out.println(renderer.getBlock().toString().replaceAll(".*\\.", "") + " -> " + c.getBlock().toString().replaceAll(".*\\.", "") + " x:" + x + " y:" + y + " rx:" + rect.x + " ry:" + rect.y);
			if(between(x, rect.x, rect.x + rect.w) && between(y, rect.y, rect.y + rect.h)) {
				System.out.println("inside " + c.getBlock().toString().replaceAll(".*\\.", ""));
				(clicked = c).onClick(x - rect.x, y - rect.y);
				return;
			}
		}
		if (parent == null)
			BlockPanel.INSTANCE.removeBlock(renderer);
		if(parent != null) {
			setPosition(BlockPanel.INSTANCE.clickPosition.x - cx, BlockPanel.INSTANCE.clickPosition.y - cy);
			BlockClickable parent = this.parent;
			parent.removeChild(this);
			parent.getRenderer().update();
		}
		BlockPanel.INSTANCE.addBlock(renderer);
		BlockPanel.INSTANCE.setClicked(this);
	}
	
	@Override
	public void onDrag(int x, int y) {
		if(clicked != null) {
			System.out.println(toString() + " -> " + clicked);
			clicked.onDrag(x, y);
			
			return;
		}
			renderer.moveTo(x - cx, y - cy);		
	}
	protected void onDrag0(int x, int y) {
		if(clicked != null) {
			System.out.println(toString() + " -> " + clicked);
			clicked.onDrag(x, y);
			
			return;
		}
		renderer.moveTo(x, y);
	}

	@Override
	public Rect getPosition() {
		BufferedImage bi = this.renderer.getRenderable();
		return new Rect(this.renderer.getX(), this.renderer.getY(), bi.getHeight(), bi.getWidth());
	}

	@Override
	public void onClickEnd() {
		if(clicked != null) {
			clicked.onClickEnd();
			return;
		}
		clicked = null;
		
	}

	@Override
	public Clickable getParent() {
		return this.parent;
	}
	
	public Clickable getClicked() {
		return this.clicked;
	}

	public void setParent(BlockClickable c) {
		this.parent = c;
	}

	@Override
	public void setPosition(int x, int y) {
		this.renderer.moveTo(x, y);
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof BlockClickable bc && renderer.equals(bc.renderer);
	}

	@Override
	public Translatable getBlock() {
		return renderer.getBlock();
	}

	@Override
	public DragableRenderer getRenderer() {
		return renderer;
	}
	
	public void replaceVariable(Clickable cl, BlockClickable newValue) {
		BlockPanel.INSTANCE.removeBlock(newValue.getRenderer());
		((VariableHolder)this.getBlock()).replaceVariable((Valuable<?>) cl.getBlock(), (Valuable<?>)newValue.getBlock());
		getRenderer().update();
	}

	@Override
	public void onHover(int x, int y, BlockClickable clicked) {
		System.out.println("checking " + getNestedClickables().size() + " childs.");
		
		//Check last hovered
		
		if(hovered != null) {
			Rect rect = hovered.getPosition();
			if(between(x, rect.x, rect.x + rect.w) && between(y, rect.y, rect.y + rect.h)) {
				System.out.println("hovered " + renderer.getBlock().toString().replaceAll(".*\\.", "") + " -> " + hovered.getBlock().toString().replaceAll(".*\\.", "") + " x:" + x + " y:" + y + " rx:" + rect.x + " ry:" + rect.y);
				hovered.onHover(x - rect.x, y - rect.y, clicked);
			} else {
				System.out.println("hoverChange old:" + renderer.getBlock().toString().replaceAll(".*\\.", "") + " -> " + hovered.getBlock().toString().replaceAll(".*\\.", "") + " x:" + x + " y:" + y + " rx:" + rect.x + " ry:" + rect.y);
				onHoverEnd(false, clicked);
			return;
			}
		}
		
		//find new hovered
		
		for(Clickable c : getNestedClickables()) {
			Rect rect = c.getPosition();
			System.out.println("checking " + renderer.getBlock().toString().replaceAll(".*\\.", "") + " -> " + c.getBlock().toString().replaceAll(".*\\.", "") + " x:" + x + " y:" + y + " rx:" + rect.x + " ry:" + rect.y + " w:" + rect.w + " h:" + rect.h);
			if(between(x, rect.x, rect.x + rect.w) && between(y, rect.y, rect.y + rect.h)) {
				System.out.println("inside");
				(hovered = c).onHover(x - rect.x, y - rect.y, clicked);
				return;
			}
		}
	}

	@Override
	public void onHoverEnd(boolean click, BlockClickable clicked) {
		if(this.hovered != null) {
			this.hovered.onHoverEnd(click, clicked);
			this.hovered = null;
		}
		
	}

	@Override
	public void delete() {
		getRenderer().delete();
		//TODO		
	}
	
	/**
	 * Sets the offset of where the click has happened
	 * @param x
	 * @param y
	 */
	public void setOffset(int x, int y) {
		this.cx = x;
		this.cy = y;
	}
	
	protected Collection<Clickable> getNestedClickables() {
		List<Clickable> lst = new ArrayList<>();
		for(IRenderer ir : getRenderer().getChildren())
			lst.add(ir.getClickable());
		return lst;
	}

	@Override
	public void move(int x, int y) {
		this.renderer.move(x, y);
		
	}
	
	
	@Override
	public String toString() {
		return getBlock().toString().replaceAll(".*\\.", "");
	}

}
