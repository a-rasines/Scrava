package ui.domain;

import java.util.List;
import java.util.function.Supplier;

import domain.blocks.capsule.IfBlock;
import domain.blocks.capsule.IfElseBlock;
import domain.blocks.capsule.RepeatBlock;
import domain.blocks.capsule.WhileBlock;
import domain.blocks.conditional.BiggerOrEqualThanBlock;
import domain.blocks.conditional.BiggerThanBlock;
import domain.blocks.conditional.EqualsBlock;
import domain.blocks.conditional.SmallerOrEqualThanBlock;
import domain.blocks.conditional.SmallerThanBlock;
import domain.blocks.conditional.bool.AndBlock;
import domain.blocks.conditional.bool.OrBlock;
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
import domain.values.IVariable;
import ui.renderers.IRenderer;

public enum BlockSection {
	MOVEMENT(0xff16cbff, new IRenderer[] {
		new MoveBlock(null).getRenderer(),
		new MoveXBlock(null).getRenderer(),
		new MoveYBlock(null).getRenderer(),
					
		new MoveToBlock(null).getRenderer(),
		new MoveXToBlock(null).getRenderer(),
		new MoveYToBlock(null).getRenderer(),
			
	}),
	CONTROL(0xffffda22, new IRenderer[] {
		new IfBlock().getRenderer(),
		new IfElseBlock().getRenderer(),
		new WhileBlock().getRenderer(),
		new RepeatBlock().getRenderer(),
	}),
	
	EVENT(0xffe97d00, new IRenderer[] {
		new OnStartEventBlock().getRenderer(),
		new OnKeyPressEventBlock().getRenderer()
	}),
	
	VARIABLE(0xffe97d00, () -> {
		List<IVariable<?>> variables = IVariable.getVisibleVariables();
		IRenderer[] output = new IRenderer[variables.size() + 1];
		output[0] = new SetValueBlock(null).getRenderer();
		for(int i = 0; i < variables.size(); i++)
			output[i + 1] = variables.get(i).getRenderer();
		return output;
	}),
	OPERATOR(0xff4ca742, new IRenderer[] {
		new BiggerOrEqualThanBlock().getRenderer(),
		new BiggerThanBlock().getRenderer(),
		new EqualsBlock().getRenderer(),
		new SmallerThanBlock().getRenderer(),
		new SmallerOrEqualThanBlock().getRenderer(),
		
		new AndBlock().getRenderer(),
		new OrBlock().getRenderer(),
		
		new AppendOperator().getRenderer(),
		new RandomOperator().getRenderer(),
		
		new AddOperator().getRenderer(),
		new SubstractOperator().getRenderer(),
		new MultiplyOperator().getRenderer(),
		new DivideOperator().getRenderer(),
		new ModulusOperator().getRenderer(),
		
		new MaxOperator().getRenderer(),
		new MinOperator().getRenderer(),
		
		new StringToDecimalNumberParser().getRenderer(),
		new StringToIntegerNumberParser().getRenderer()
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
