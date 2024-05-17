package domain.models.interfaces;

import java.util.function.Supplier;

import ui.renderers.IRenderer.IRenderable;

/**
 * Represents a function block 
 */
public interface InvocableBlock extends IRenderable, Translatable {
	
	public static final Supplier<Boolean> SKIP_TICK = () -> true;

	/**
	 * This function runs the functionality of the block when it's reached
	 */
	public void invoke();
	
	/**
	 * Returns a supplier that runs one operation per execution.
	 * @return a {@link java.util.function.Supplier Supplier} that returns true when finished
	 */
	public Supplier<Boolean> getTick();
	 
	
}
