package ui.domain;

import domain.blocks.container.IfBlock;
import domain.blocks.container.IfElseBlock;
import domain.blocks.container.WhileBlock;
import domain.blocks.event.OnStartEventBlock;
import domain.blocks.movement.MoveBlock;
import domain.blocks.movement.MoveToBlock;
import domain.blocks.movement.MoveXBlock;
import domain.blocks.movement.MoveXToBlock;
import domain.blocks.movement.MoveYBlock;
import domain.blocks.movement.MoveYToBlock;
import ui.renderers.IRenderer;

public enum BlockSection {
	MOVEMENT(0xff16cbff, new IRenderer[] {
			IRenderer.getDetachedDragableRendererOf(new MoveBlock(null)),
			IRenderer.getDetachedDragableRendererOf(new MoveXBlock(null)),
			IRenderer.getDetachedDragableRendererOf(new MoveYBlock(null)),
			
			IRenderer.getDetachedDragableRendererOf(new MoveToBlock(null)),
			IRenderer.getDetachedDragableRendererOf(new MoveXToBlock(null)),
			IRenderer.getDetachedDragableRendererOf(new MoveYToBlock(null)),
			
	}),
	CONTROL(0xffffda22, new IRenderer[] {
			IRenderer.getDetachedDragableRendererOf(new IfBlock()),
			IRenderer.getDetachedDragableRendererOf(new IfElseBlock()),
			IRenderer.getDetachedDragableRendererOf(new WhileBlock()),
	}),
	
	EVENT(0xffe97d00, new IRenderer[] {
			IRenderer.getDetachedDragableRendererOf(new OnStartEventBlock())
	})
	
	;
	
	
	public final int color;
	public final IRenderer[] blocks;
	public final int totalY;
	BlockSection(int color, IRenderer[] blocks) {
		this.color = color;
		this.blocks = blocks;
		int temp = 0;
		for(IRenderer block: blocks)
			temp += block.getRenderable().getHeight();
		this.totalY = temp;
	}
	
	@Override
	public String toString() {
		String spr = super.toString();
		return spr.charAt(0) + spr.substring(1).toLowerCase().replaceAll("_", " ");
	}
}
