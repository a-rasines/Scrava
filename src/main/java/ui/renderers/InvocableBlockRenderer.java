package ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.blocks.movement.MoveBlock;
import domain.clickable.BlockClickable;
import domain.clickable.InvocableClickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import domain.values.AbstractLiteral;
import domain.values.NumberLiteral;
import ui.BlockPanel;
import ui.renderers.IRenderer.DragableRenderer;

public class InvocableBlockRenderer implements DragableRenderer {
	public static interface InvocableBlockRenderable extends Translatable, IRenderable, VariableHolder, InvocableBlock{
		@Override
		public default Constructor<InvocableBlockRenderer> getRenderer() throws NoSuchMethodException, SecurityException {
			return InvocableBlockRenderer.class.getConstructor(InvocableBlockRenderable.class);
		}
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
				MOVEMENT(0xff16cbff)
				;			
			/**
			 * Left end of the block
			 */
			public final int color;
			BlockCategory(int color){
				this.color = color;
			}
		}
	}
	
	private static final BufferedImage LEFT_TOP = IRenderer.getRes("textures/function/left_top.svg");
	private static final BufferedImage LEFT_BOTTOM = IRenderer.getRes("textures/function/left_bottom.svg");
	private static final BufferedImage LEFT_BOTTOM_C = IRenderer.getRes("textures/function/left_bottom_c.svg");
	private static final BufferedImage RIGHT = IRenderer.getRes("textures/function/right.svg");
	public static final BufferedImage CONNECTOR = IRenderer.getRes("textures/function/connector.svg");
	
	
	private int x;
	private int y;
	protected final InvocableBlockRenderable block;
	protected BufferedImage rendred = null;
	protected final InvocableClickable clickable;
	
	private synchronized BufferedImage rendered(BufferedImage set, boolean w) {
		if(w) rendred = set;
		return rendred;
	}
	
	public InvocableBlockRenderer(InvocableBlockRenderable block) {
		this.block = block;
		this.x = 0;
		this.y = 0;
		this.clickable = new InvocableClickable(this, null);
	}
	
	public InvocableBlockRenderer(InvocableBlockRenderable block, int x, int y) {
		this.block = block;
		this.x = x;
		this.y = y;
		this.clickable = new InvocableClickable(this, null);
	}
	
	@Override
	public BufferedImage getRenderable() {
		int height = getHeight();
		int width = getWidth();
		if(rendered(null, false) != null)
			return rendered(null, false);
		
		String title = block.getTitle();
		if(BlockPanel.DEBUG_SHOW_HASHES)
			title = block.toString().split("@")[1] + title;
		//String[] parts = title.split("\\{\\{");
		
		BufferedImage rendered = new BufferedImage(width, height + CONNECTOR.getHeight(), BufferedImage.TYPE_INT_ARGB); //Thread safe
		Graphics g = rendered.getGraphics();
		g.setFont(new Font( font.getName(), Font.PLAIN, 55 ));
		
		g.drawImage(replaceColor(clone(LEFT_TOP), block.getCategory().color), 0, 0, null);
		verticalBackground(rendered, 0, LEFT_TOP.getWidth(), LEFT_TOP.getHeight(), getHeight() - LEFT_TOP.getHeight() - LEFT_BOTTOM.getHeight());
		g.drawImage(replaceColor(clone(LEFT_BOTTOM_C), block.getCategory().color), 1, getHeight() - LEFT_BOTTOM.getHeight(), null);
		
		BufferedImage text = renderText(title, LEFT_TOP.getWidth(), getHeight());
		background(rendered, getHeight(), LEFT_TOP.getWidth(), text.getWidth());
		g.drawImage(text, LEFT_TOP.getWidth(), 0, null);
		
//		int len = 0;
//		int vari = 0;
//		for(String part : parts) {
//		
//			if(part.split(" ")[0].contains("}}")) {
//				String[] divided = part.split("}}");
//				IRenderer rend = getChildren().get(vari++);
//				BufferedImage subblock = rend.getRenderable();
//				try {
//					background(rendered, getHeight(), LEFT_TOP.getWidth() + len, subblock.getWidth());
//				} catch(ArrayIndexOutOfBoundsException e) {
//					System.out.println("part:"+part + " block:" +rend.getBlock().toString().replaceAll(".*\\.", "") + " w:" + subblock.getWidth());
//					throw e;
//				}
//				rend.getClickable().setPosition(LEFT_TOP.getWidth() + len - 1, (int)((getHeight()- subblock.getHeight())/2));
//				g.drawImage(subblock, LEFT_TOP.getWidth() + len - 1, (int)((getHeight()- subblock.getHeight())/2) , null);
//				if(BlockPanel.DEBUG_SHOW_HITBOXES) {
//					g.setColor(Color.green);
//					((Graphics2D)g).setStroke(new BasicStroke(2));
//					Rect r = rend.getClickable().getPosition();
//					g.drawRect(r.x + 1, r.y + 1, r.w - 2, r.h - 2);
//					g.setColor(Color.white);
//				}
//					
//				len += subblock.getWidth();
//			
//				if(divided.length == 2) {
//					background(rendered, getHeight(), LEFT_TOP.getWidth() + len, divided[1].length() * FONT_WIDTH);
//					g.drawString(divided[1], LEFT_TOP.getWidth() + len, getHeight()/2 + 20);
//					len += divided[1].length() * FONT_WIDTH;
//				}
//				
//			} else {
//				try {
//				background(rendered, getHeight(), LEFT_TOP.getWidth() + len, part.length() * FONT_WIDTH);
//				} catch(ArrayIndexOutOfBoundsException e) {
//					System.out.println("part:"+part);
//					throw e;
//				}
//				g.drawString(part, LEFT_TOP.getWidth() + len, getHeight()/2  + 20);
//				len += part.length() * FONT_WIDTH;
//			}
//					
//		}
		
		g.drawImage(replaceColor(clone(RIGHT), block.getCategory().color).getScaledInstance(RIGHT.getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH), LEFT_TOP.getWidth() + text.getWidth(), 0, null);
		return rendered(rendered, true);
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
		height = -1;
		width = -1;
		rendered(null, true);
		clickable.update();
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
			String[] vars = block.getTitle().split("\\{\\{");
			if((rend = IRenderer.getDragableRendererOf(v[i]))==null)
				rend = LiteralRenderer.of((AbstractLiteral<?>)v[i], vars[i+1].split("}}")[0], getClickable());
			else {
				((BlockClickable) rend.getClickable()).setParent(this.getClickable());
			}
			
			output.add(rend);
		}
		return output;
	}
	int height = -1;
	public int getHeight() {
		if(height == -1) {
			if(getChildren().size() == 0)
				height =  RIGHT.getHeight();
			else {
				int h = 0;
				for (IRenderer ir : getChildren()) {
					h = Math.max(h, ir.getRenderable().getHeight());
				}
				height = (int) Math.round(h * SUBSCALE);
			}
			
		}
		return height;
	}
	
	int width = -1;
	public int getWidth() {
		if(width == -1) {
			width = LEFT_BOTTOM.getWidth() 
					   + RIGHT.getWidth() 
					   + (block.getTitle().replaceAll("\\{\\{.*?}}", "") + (BlockPanel.DEBUG_SHOW_HASHES ? block.toString().split("@")[1] : "")).length() * FONT_WIDTH;
			for(IRenderer rend : getChildren())
				width += rend.getRenderable().getWidth();
		}
		return width;
		
	}

	@Override
	public InvocableClickable getClickable() {
		return clickable;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof IRenderer && getBlock().equals(((IRenderer)obj).getBlock());
	}

	@Override
	public void delete() {
		this.clickable.delete();
		for(IRenderer rend : getChildren())
			rend.delete();
		DRAG_RENDS.remove((IRenderable)this.getBlock());
		
	}
public static void main(String[] args) {
		
	 	InvocableBlockRenderer sampleImage = (InvocableBlockRenderer) IRenderer.getDragableRendererOf(new MoveBlock(null, new NumberLiteral<Integer>(0), new NumberLiteral<Integer>(0))); 	
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 500);

            JPanel panel = new JPanel() {
				private static final long serialVersionUID = -94843536618228336L;

				@Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawRect(0, 0, 500, 500);
                    g.drawImage(sampleImage.getRenderable(), 100, 100, this);
                }
            };
            frame.getContentPane().add(panel);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
	}
	@Override
	public void patch(int x, int y, int h, int w, BufferedImage newI) {
		BufferedImage rend =  rendered(null, false);
		System.out.println("x:"+x+" h:"+h+" w:"+w);
		background(rend, h, x, w);
		Graphics2D g = (Graphics2D) rend.getGraphics();
		g.drawImage(newI, x, y, null);
		g.setColor(Color.green);
		g.setStroke(new BasicStroke(2));
		g.drawRect(x + 1, y + 1, w - 2, h - 2);
		rendered(rend, true);
		if(clickable.getParent() != null) {
			Rect r = clickable.getPosition();
			clickable.getParent().getRenderer().patch(r.x, r.y, r.h + r.y, r.w, rend);	
		} else
			BlockPanel.INSTANCE.repaint();
	}
}

