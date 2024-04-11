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
	 
	
}
