package ui.domain;

import java.util.List;
import java.util.function.Supplier;

import domain.blocks.capsule.*;
import domain.blocks.conditional.*;
import domain.blocks.conditional.bool.*;
import domain.blocks.event.*;
import domain.blocks.movement.*;
import domain.blocks.operators.*;
import domain.blocks.operators.parser.*;
import domain.values.Variable;
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
		List<Variable<?>> variables = Variable.getVisibleVariables();
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
