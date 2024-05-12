package ui.domain;

import java.util.List;
import java.util.function.Supplier;

import domain.blocks.conditional.BiggerOrEqualThanBlock;
import domain.blocks.conditional.BiggerThanBlock;
import domain.blocks.conditional.EqualsBlock;
import domain.blocks.conditional.SmallerOrEqualThanBlock;
import domain.blocks.conditional.SmallerThanBlock;
import domain.blocks.conditional.bool.AndBlock;
import domain.blocks.conditional.bool.OrBlock;
import domain.blocks.container.IfBlock;
import domain.blocks.container.IfElseBlock;
import domain.blocks.container.WhileBlock;
import domain.blocks.event.OnKeyPressEventBlock;
import domain.blocks.event.OnStartEventBlock;
import domain.blocks.movement.MoveBlock;
import domain.blocks.movement.MoveToBlock;
import domain.blocks.movement.MoveXBlock;
import domain.blocks.movement.MoveXToBlock;
import domain.blocks.movement.MoveYBlock;
import domain.blocks.movement.MoveYToBlock;
import domain.blocks.operators.AddOperator;
import domain.blocks.operators.AppendOperator;
import domain.blocks.operators.DivideOperator;
import domain.blocks.operators.MaxOperator;
import domain.blocks.operators.MinOperator;
import domain.blocks.operators.ModulusOperator;
import domain.blocks.operators.MultiplyOperator;
import domain.blocks.operators.RandomOperator;
import domain.blocks.operators.SetValueBlock;
import domain.blocks.operators.SubstractOperator;
import domain.blocks.operators.parser.StringToDecimalNumberParser;
import domain.blocks.operators.parser.StringToIntegerNumberParser;
import domain.values.Variable;
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
			IRenderer.getDetachedDragableRendererOf(new OnStartEventBlock()),
			IRenderer.getDetachedDragableRendererOf(new OnKeyPressEventBlock())
	}),
	
	VARIABLE(0xffe97d00, () -> {
		List<Variable<?>> variables = Variable.getVisibleVariables();
		IRenderer[] output = new IRenderer[variables.size() + 1];
		output[0] = IRenderer.getDetachedDragableRendererOf(new SetValueBlock(null));
		for(int i = 0; i < variables.size(); i++)
			output[i + 1] = IRenderer.getDetachedDragableRendererOf(variables.get(i));
		return output;
	}),
	OPERATOR(0xff4ca742, new IRenderer[] {
		IRenderer.getDetachedDragableRendererOf(new BiggerOrEqualThanBlock()),
		IRenderer.getDetachedDragableRendererOf(new BiggerThanBlock()),
		IRenderer.getDetachedDragableRendererOf(new EqualsBlock()),
		IRenderer.getDetachedDragableRendererOf(new SmallerThanBlock()),
		IRenderer.getDetachedDragableRendererOf(new SmallerOrEqualThanBlock()),
		
		IRenderer.getDetachedDragableRendererOf(new AndBlock()),
		IRenderer.getDetachedDragableRendererOf(new OrBlock()),
		
		IRenderer.getDetachedDragableRendererOf(new AppendOperator()),
		IRenderer.getDetachedDragableRendererOf(new RandomOperator()),
		
		IRenderer.getDetachedDragableRendererOf(new AddOperator()),
		IRenderer.getDetachedDragableRendererOf(new SubstractOperator()),
		IRenderer.getDetachedDragableRendererOf(new MultiplyOperator()),
		IRenderer.getDetachedDragableRendererOf(new DivideOperator()),
		IRenderer.getDetachedDragableRendererOf(new ModulusOperator()),
		
		IRenderer.getDetachedDragableRendererOf(new MaxOperator()),
		IRenderer.getDetachedDragableRendererOf(new MinOperator()),
		
		IRenderer.getDetachedDragableRendererOf(new StringToDecimalNumberParser()),
		IRenderer.getDetachedDragableRendererOf(new StringToIntegerNumberParser())
	}),
	;
	
	
	public final int color;
	private final IRenderer[] blocks;
	public int totalY;
	private final Supplier<IRenderer[]> blockGetter;
	BlockSection(int color, IRenderer[] blocks) {
		this.color = color;
		this.blocks = blocks;
		int temp = 0;
		for(IRenderer block: blocks)
			temp += block.getRenderable().getHeight();
		this.totalY = temp;
		this.blockGetter = this::_gb;
	}
	
	private IRenderer[] _gb() {
		return blocks;
	}
	
	BlockSection(int color, Supplier<IRenderer[]> dinList) {
		this.color = color;
		this.blockGetter = dinList;
		this.totalY = 0;
		this.blocks = new IRenderer[0];
	}
	public int getTotalY() {
		if(totalY != 0) return totalY;
		int temp = 0;
		for(IRenderer block: getBlocks())
			temp += block.getRenderable().getHeight();
		return temp;
	}
	
	public IRenderer[] getBlocks() {
		return blockGetter.get();
	}
	
	@Override
	public String toString() {
		String spr = super.toString();
		return spr.charAt(0) + spr.substring(1).toLowerCase().replaceAll("_", " ");
	}
}
