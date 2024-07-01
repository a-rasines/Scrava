package ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.w3c.dom.svg.SVGDocument;

import clickable.BlockClickable;
import clickable.InvocableClickable;
import domain.blocks.invocable.movement.MoveBlock;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import ui.components.BlockPanel;
import ui.renderers.IRenderer.DragableRenderer;

public class InvocableBlockRenderer implements DragableRenderer {
	
	private static final long serialVersionUID = -1092485586518514545L;
	
	public static interface InvocableBlockRenderable extends Translatable, IRenderable, VariableHolder, InvocableBlock{

		/**
		 * Returns the visual title of the block
		 * @return
		 */
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
				MOVEMENT(0xff16cbff),
				VARIABLE(0xffe97d00),
				CONTROL(0xffffda22),
				LOOKS(0xffdf61c5)
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
	
	private transient static final BufferedImage LEFT_TOP = IRenderer.getRes("textures/function/left_top.svg");
	private transient static final BufferedImage LEFT_BOTTOM = IRenderer.getRes("textures/function/left_bottom.svg");
	private transient static final BufferedImage LEFT_BOTTOM_C = IRenderer.getRes("textures/function/left_bottom_c.svg");
	private transient static final BufferedImage RIGHT = IRenderer.getRes("textures/function/right.svg");
	public transient static final BufferedImage CONNECTOR = IRenderer.getRes("textures/function/connector.svg");
	
	
	private int x;
	private int y;
	protected final InvocableBlockRenderable block;
	protected transient BufferedImage rendred = null;
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
		if(g instanceof Graphics2D g2d) {
			 g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	         g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(new Font( font.getName(), Font.PLAIN, 55 ));
		
		g.drawImage(replaceColor(clone(LEFT_TOP), block.getCategory().color), 0, 0, null);
		verticalBackground(rendered, 0, LEFT_TOP.getWidth(), LEFT_TOP.getHeight(), getHeight() - LEFT_TOP.getHeight() - LEFT_BOTTOM.getHeight());
		g.drawImage(replaceColor(clone(LEFT_BOTTOM_C), block.getCategory().color), 1, getHeight() - LEFT_BOTTOM.getHeight(), null);
		
		BufferedImage text = renderText(title, LEFT_TOP.getWidth(), getHeight());
		background(rendered, getHeight(), LEFT_TOP.getWidth(), text.getWidth());
		g.drawImage(text, LEFT_TOP.getWidth(), 0, null);	
		
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
			rend = v[i].getRenderer();
			if(rend.getClickable() instanceof BlockClickable bl)
				bl.setParent(this.getClickable());
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
		System.out.println("delete " + getBlock());
		BlockPanel.INSTANCE.removeBlock(this);
		for(IRenderer rend : getChildren()) {
			rend.delete();
		}
		InvocableClickable next = ((InvocableClickable)getClickable()).next();
		while(next != null) {
			next.delete();
			next = next.next();
		}
	}
	
	@Override
	public void patch(int x, int y, int h, int w, BufferedImage newI) {
		BufferedImage rend =  rendered(null, false);
		System.out.println("x:"+x+" h:"+h+" w:"+w);
		background(rend, h, x, w);
		Graphics2D g = (Graphics2D) rend.getGraphics();
		g.drawImage(newI, x, y, null);
		if(BlockPanel.DEBUG_SHOW_HITBOXES) {
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(2));
			g.drawRect(x + 1, y + 1, w - 2, h - 2);
		}
		rendered(rend, true);
		if(clickable.getParent() != null) {
			Rect r = clickable.getPosition();
			clickable.getParent().getRenderer().patch(r.x, r.y, r.h + r.y, r.w, rend);	
		} else
			BlockPanel.INSTANCE.repaint();
	}

	@Override
	public SVGDocument getRenderableSVG() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {		
	 	InvocableBlockRenderer ibr = new MoveBlock(null).getRenderer();
	 	
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);

            JPanel panel = new JPanel() {
				private static final long serialVersionUID = -94843536618228336L;

				@Override
                protected void paintComponent(Graphics g) {
					g.setColor(Color.black);
                    g.drawRect(0, 0, 500, 500);
                    g.drawImage(ibr.getRenderable(), 100, 100, this);
                }
            };
            frame.getContentPane().add(panel);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
	}
}

