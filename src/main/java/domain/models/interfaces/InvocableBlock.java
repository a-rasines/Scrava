package domain.models.interfaces;

import ui.renderers.IRenderer.IRenderable;

/**
 * Represents a function block 
 */
public interface InvocableBlock extends IRenderable, Translatable {
	
	/**
	 * This function runs the functionality of the block when it's reached
	 */
	public void invoke();
	
	/**
	 * This function steps over the program
	 * @return true if the block has finished and the parent can step into the next
	 */
	public boolean tick();
	
	/**
	 * Resets the tick pointer and ticks through the first element
	 */
	public void firstTick();
	 
	
}
