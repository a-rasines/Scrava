package domain.models.interfaces;

import ui.renderers.IRenderer.IRenderable;

/**
 * Blocks that can be used to define the value of a variable
 * @param <T>
 */
public interface Valuable<T> extends Translatable, IRenderable{
	
	/**
	 * Get's the result value of the block
	 * @return the result of the block
	 */
	public T value();
}
