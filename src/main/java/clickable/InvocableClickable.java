package clickable;

import ui.components.BlockPanel;
import ui.renderers.IRenderer.DragableRenderer;

public class InvocableClickable extends BlockClickable{

	public InvocableClickable(DragableRenderer renderer, BlockClickable parent) {
		super(renderer, parent);
	}
	
	private InvocableClickable next = null;
	private InvocableClickable prev = null;
	
	@Override
	public void setParent(BlockClickable c) {
		super.setParent(c);
		if(next != null) {
			next.setParent(c);
		}
	}
	
	public InvocableClickable setNext(InvocableClickable next) {
		if(next == this) return this;
		System.out.println(getBlock().toString().replaceAll(".*\\.", "") + " > " + (next==null?"null":next.getBlock().toString().replaceAll(".*\\.", "")));
		this.next = next;
		if(this.next == null) return this.next;//For some reason without the 'this' it's dead code
		next.setPosition(getRenderer().getX(), getRenderer().getY() + getRenderer().getHeight());
		next.prev = this;
		if(getParent() == null)
			updateHierarchy();
		return next;
	}
	
	public InvocableClickable next() {
		return next;
	}

	@Override
	public void setPosition(int x, int y) {
		System.out.println(toString() + " x:" + getRenderer().getX() + " y:" + y + " h:" + getRenderer().getHeight() + " next:" + (next==null?"null":next.toString()));
		super.setPosition(x, y);
		if(next != null)
			next.setPosition(getRenderer().getX(), y + getRenderer().getHeight());
	}
	
	private void updateHierarchy() {
		if(getParent() != null)
			return;
		if(prev != null) 
			prev.updateHierarchy();
		else
			_updateHierarchy();
	}
	
	private void _updateHierarchy() {
		BlockPanel.INSTANCE.removeBlock(getRenderer());
		if(next != null)
			next._updateHierarchy();
		BlockPanel.INSTANCE.addBlock(getRenderer());
		
	}
	@Override
	public void onClick(int x, int y) {
		System.out.println("cx:" + x + 
						   " cy:" + y + 
						   " x:" + getRenderer().getX() + 
						   " y:" + getRenderer().getY() + 
						   " w:" + getRenderer().getWidth() +
						   " h:" + getRenderer().getHeight());
		if(between(x, 0, getRenderer().getWidth()) && between(y, 0, getRenderer().getHeight())) { //Inside block
			System.out.println("inside block");
			InvocableClickable parent = (InvocableClickable)getParent();
			super.onClick(x, y);
			if(parent != null && clicked == null) {
				if(next!=null)BlockPanel.INSTANCE.addBlock(next.getRenderer());
				parent.getRenderer().update();
				System.out.println();
			}
			if(clicked == null) {
				if(prev != null)
					prev.setNext(null);
				prev = null;
				updateHierarchy();
			} else {
				getRenderer().update();
			}
			
		} else if(next != null) { //Inside connector
			System.out.println(this.getBlock().toString().replaceAll(".*\\.", "") + " -> " + next.getBlock().toString().replaceAll(".*\\.", ""));
			next.onClick(x, y - getRenderer().getHeight());
		}
	}
	
	protected boolean append = false;
	@Override
	public void onHover(int x, int y, BlockClickable clicked) {
		System.out.println(this.getBlock().toString().replaceAll(".*\\.", "") + 
				" hx:" + x + 
				" hy:" + y + 
				" x:" + getRenderer().getX() + 
				" y:" + getRenderer().getY() +
				" w:" + getRenderer().getWidth() +
				" h:" + getRenderer().getHeight());
		append = !(between(x, 0, getRenderer().getWidth()) && between(y, 0, getRenderer().getHeight()));
		System.out.println("append block? " + append);
		if(!append)
			super.onHover(x, y, clicked);
		else if(next != null)
			next.onHover(x, y - getRenderer().getHeight(), clicked);
	}
	
	@Override
	public void onClickEnd() {
		super.onClickEnd();
	}
	
	public void update() {
		if(this.next != null)
			next.setPosition(getRenderer().getX(), getRenderer().getY() + getRenderer().getHeight());
	}
	
	@Override
	public void onDrag(int x, int y) {
		super.onDrag(x, y);
		if(next != null && super.clicked == null) {
			next.onDrag0(x - cx , y - cy + getRenderer().getHeight());
		}
		
		
	}
	protected void onDrag0(int x, int y) {
		super.onDrag0(x, y);
		if(next != null)
			next.onDrag0(x, y  + getRenderer().getHeight());
		
	}
	
	@Override
	public void onHoverEnd(boolean click, BlockClickable clicked) {
		System.out.println("ended " + this.getBlock().toString().replaceAll(".*\\.", "") + " append=" + append);
		if(click && append && clicked instanceof InvocableClickable) { //next
			System.out.println("append " + toString() + " -> " + clicked.getBlock().toString().replaceAll(".*\\.", ""));
			
			InvocableClickable next = this.next;
			setNext((InvocableClickable) clicked);
			if(next != null) {
				InvocableClickable clickedNext = ((InvocableClickable) clicked).next();
				if(clickedNext != null) {
					while(clickedNext.next() != null)
						clickedNext = clickedNext.next();
					clickedNext.setNext(next);
				} else
					((InvocableClickable) clicked).setNext(next);
			}
			BlockPanel.INSTANCE.repaint();
		} else { //inside
			System.out.println(append);
			if(!append)
				super.onHoverEnd(click, clicked);
			else if(next != null)
				next.onHoverEnd(click, clicked);
			append = false;
		}
		
	}
	
	@Override
	public String toString() {
		return getBlock().getClass().getSimpleName() + "@" + Integer.toHexString(getBlock().hashCode());
	}


}
