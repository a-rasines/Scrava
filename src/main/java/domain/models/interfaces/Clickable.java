package domain.models.interfaces;

import domain.clickable.BlockClickable;
import ui.renderers.IRenderer;

public interface Clickable {

	public static class Rect {
		@Override
		public String toString() {
			return "Rect [x=" + x + ", y=" + y + ", h=" + h + ", w=" + w + "]";
		}
		public int x;
		public int y;
		public int h;
		public int w;
		public Rect(int x, int y, int h, int w) {
			super();
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
		}
		
		
	}
	
	/**
	 * Runs whenever a click has started inside this object's {@link domain.models.interfaces.Clickable.Rect Rect} obtained with the {@link domain.models.interfaces.Clickable#getPosition() getPosition()} function
	 * @param x relative x position of the click
	 * @param y relative y position of the click
	 */
	public void onClick(int x, int y);
	
	/**
	 * Since the moment the object it's clicked until it's released, every time the mouse moves this function triggers
	 * @param x relative new x position of the mouse
	 * @param y relative new y position of the mouse
	 */
	public void onDrag(int x, int y);
	
	/**
	 * Like the {@link domain.models.interfaces.Clickable#onDrag(int, int) onDrag()} function but the clicked block it's different to this one
	 * @param x relative new x position of the mouse
	 * @param y relative new y position of the mouse
	 */
	public void onHover(int x, int y, BlockClickable clicked);
	
	/**
	 * Whenever another object it's dragged outside this one or the mouse is released when another object it's clicked this function is invoked 
	 */
	public void onHoverEnd(boolean click, BlockClickable clicked);
	
	/**
	 * When this object ends being clicked this function it's triggered
	 */
	public void onClickEnd();
	
	/**
	 * Sets the absolute position of the hitbox (not the size)
	 * @param x new X position
	 * @param y new Y position
	 */
	public void setPosition(int x, int y);
	
	public void move(int x, int y);
	
	/**
	 * Returns the {@link domain.models.interfaces.Clickable.Rect Rect} that represents the hitbox of this clickable.
	 * @return
	 */
	public Rect getPosition();
	
	/**
	 * Returns the object higher in the hierarchy of this object, can be null
	 * @return
	 */
	public Clickable getParent();
	
	/**
	 * Get's the block this hitbox belongs to
	 * @return
	 */
	public Translatable getBlock();
	
	/**
	 * Get's the renderer of the block
	 * @return
	 */
	public IRenderer getRenderer();
	
	/**
	 * Removes the hitbox of the block. To remove the block as a whole, you have to use {@link ui.renderers.IRenderer#delete() IRenderer#delete()}.
	 */
	public void delete();
}
