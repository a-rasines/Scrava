package ui.renderers;

import java.util.List;

import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Clickable.Rect;
import ui.renderers.IRenderer.DragableRenderer;

public interface CapsuleRenderer extends DragableRenderer{

	/**
	 * Returns the rect list corresponding to the block bundles inside the block.
	 * @return
	 */
	public List<Rect> getBlockBundlesSize();


	public void add(int bundle, int index, DragableRenderer block);
	
	public boolean contains(int bundle, DragableRenderer block);
	
	public DragableRenderer get(int bundle, int index);
	
	public List<InvocableBlock> getBlocksOf(int bundle);
	
	public int indexOf(int bundle, DragableRenderer block);
	
	public boolean remove(int bundle, DragableRenderer block);
	
	public int sizeOf(int bundle);
	
	public int bundleCount();

	
}
