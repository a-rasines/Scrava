package ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import clickable.BlockClickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import ui.components.BlockPanel;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.SimpleBlockRenderer.SimpleRenderable.BlockCategory;

public class SimpleBlockRenderer implements DragableRenderer{
	private static final long serialVersionUID = 2111348834794671783L;
	
	public static interface SimpleRenderable extends Translatable, IRenderable, VariableHolder{
		
		public String getTitle();
		
		/**
		 * Returns the block category for rendering
		 * @return
		 */
		public BlockCategory getCategory();
		/**
		 * This enum represents the different block texture groups
		 */
		public enum BlockCategory {
				OPERATOR(IRenderer.getRes("textures/operator/start.svg"), IRenderer.getRes("textures/operator/end.svg")),
				NUMBER_VARIABLE(IRenderer.getRes("textures/variable/numvarstart.svg"), IRenderer.getRes("textures/variable/numvarend.svg")),
				STRING_VARIABLE(IRenderer.getRes("textures/variable/stringvarstart.svg"), IRenderer.getRes("textures/variable/stringvarend.svg")),
				BOOLEAN_VARIABLE(IRenderer.getRes("textures/variable/booleanvarstart.svg"), IRenderer.getRes("textures/variable/booleanvarend.svg")),
				CONDITIONAL(IRenderer.getRes("textures/operator/conditional_start.svg"), IRenderer.getRes("textures/operator/conditional_end.svg")),
				STRING_OPERATOR(IRenderer.getRes("textures/operator/string_operator.svg"), IRenderer.getRes("textures/operator/string_operator_end.svg"))
				;
			
			/**
			 * Left end of the block
			 */
			public final BufferedImage start;
			/**
			 * Right end of the block
			 */
			public final BufferedImage end;
			BlockCategory(BufferedImage start, BufferedImage end){
				this.start = start;
				this.end = end;
			}
		}
	}
	
	
	private int x;
	private int y;
	protected final SimpleRenderable block;
	protected transient BufferedImage rendred = null;
	protected final BlockClickable clickable;
	
	private synchronized BufferedImage rendered(BufferedImage set, boolean w) {
		if(w) rendred = set;
		return rendred;
	}
	
	public SimpleBlockRenderer(SimpleRenderable block) {
		this.block = block;
		this.x = 0;
		this.y = 0;
		this.clickable = new BlockClickable(this, null);
	}
	
	public SimpleBlockRenderer(SimpleRenderable block, int x, int y) {
		this.block = block;
		this.x = x;
		this.y = y;
		this.clickable = new BlockClickable(this, null);
	}
	
	private transient static final int FONT_WIDTH = 25;
	
	@Override
	public BufferedImage getRenderable() {
		if(rendered(null, false) != null)
			return rendered(null, false);
		
		BufferedImage start = block.getCategory().start;
		BufferedImage text = renderText(block.getTitle(), start.getWidth(), getHeight());
		BufferedImage end = block.getCategory().end;
		
		BufferedImage rendered = new BufferedImage(start.getWidth() + text.getWidth() + end.getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = rendered.getGraphics();
		if(g instanceof Graphics2D g2d) {
			 g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	         g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	         g2d.setStroke(new BasicStroke(2));
		}
		
		g.drawImage(start.getScaledInstance(start.getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH), 0, 0, null);
		
		background(rendered,getHeight(),start.getWidth(),text.getWidth());
		g.drawImage(text, start.getWidth(), 0, null);
		
		g.drawImage(end.getScaledInstance(start.getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH), start.getWidth() + text.getWidth(), 0, null);
		rendered(rendered, true);
		return rendered;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void moveTo(int x, int y) {
		System.out.println(block.toString().replaceAll(".*\\.", "") + " x0:" + this.x + " y0:" + this.y + " xf:" + x + " yf:" + y);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	@Override
	public Translatable getBlock() {
		return block;
	}

	@Override
	public void update() {
		rendered(null, true);
		if(clickable.getParent() == null)
			BlockPanel.INSTANCE.repaint();
		else
			clickable.getParent().getRenderer().update();
	}

	
	@Override
	public List<IRenderer> getChildren() {
		List<IRenderer> output = new LinkedList<>();
		Valuable<?>[] v = block.getAllVariables();
		for(int i = 0; i < v.length; i++) {
			IRenderer rend;
			rend = v[i].getRenderer();
			if(rend.getClickable() instanceof BlockClickable bl)
				bl.setParent(this.getClickable());
			
			output.add(rend);
		}
		return output;
	}
	@Override
	public int getHeight() {
		if(getChildren().size() == 0)
			return block.getCategory().start.getHeight();
		else {
			int h = 0;
			for (IRenderer ir : getChildren())
				h = Math.max(h, ir.getRenderable().getHeight());
			return (int) Math.round(h * SUBSCALE);
		}
	}
	
	@Override
	public int getWidth() {
		BlockCategory bc = block.getCategory();
		int w = bc.start.getWidth() 
				   + bc.end.getWidth() 
				   + block.getTitle().replaceAll("\\{\\{.*?}}", "").length() * FONT_WIDTH;
		for(IRenderer rend : getChildren())
			w += rend.getRenderable().getWidth();
		return w;
		
	}

	@Override
	public BlockClickable getClickable() {
		return clickable;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof IRenderer && getBlock().equals(((IRenderer)obj).getBlock());
	}

	@Override
	public void delete() {
		System.out.println("delete " + getBlock());
		BlockPanel.INSTANCE.removeBlock(this);
		for(IRenderer rend : getChildren())
			rend.delete();
		
	}
	@Override
	public void patch(int x, int y, int h, int w, BufferedImage bi) {
		BufferedImage rend =  rendered(null, false);
		System.out.println("x:"+x+" h:"+h+" w:"+w);
		background(rend, h, x, w);
		Graphics2D g = (Graphics2D) rend.getGraphics();
		g.drawImage(bi, x, y, null);
		g.setColor(Color.green);
		g.setStroke(new BasicStroke(2));
		g.drawRect(x + 1, y + 1, w - 2, h - 2);
		rendered(rend, true);
		if(clickable.getParent() != null) {
			Rect r = clickable.getPosition();
			clickable.getParent().getRenderer().patch(r.x, r.y, r.h + r.y, r.w, getRenderable());
		} else
			BlockPanel.INSTANCE.repaint();
	}
}
