package ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.svg.SVGDocument;

import clickable.BlockClickable;
import clickable.CapsuleBlockClickable;
import clickable.InvocableClickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.types.CapsuleBlock;
import domain.models.types.EventBlock;
import ui.components.BlockPanel;
import ui.components.SpritePanel;

public class CapsuleBlockRenderer implements CapsuleRenderer{
	
	private static final long serialVersionUID = -4123702565115474473L;
	
	private int x;
	private int y;
	
	private transient static final BufferedImage ARM_BLOCK        = IRenderer.getRes("textures/capsule/arm_block.svg"); //left of block
	private transient static final BufferedImage ARM_END          = IRenderer.getRes("textures/capsule/arm_end.svg"); //between arm block & body end
	private transient static final BufferedImage BODY_END_CONN    = IRenderer.getRes("textures/capsule/end_body_conn.svg");//under arm end
	private transient static final BufferedImage BODY_END         = IRenderer.getRes("textures/capsule/end_body.svg");//under arm end
	private transient static final BufferedImage HEAD_END         = IRenderer.getRes("textures/capsule/end.svg"); // right of head
	//private static final BufferedImage MIDDLE_BODY    = IRenderer.getRes("textures/capsule/middle_body.svg"); N/A
	private transient static final BufferedImage ARM_START        = IRenderer.getRes("textures/capsule/start_arm.svg"); //Under head
	private transient static final BufferedImage BODY_START_CONN  = IRenderer.getRes("textures/capsule/start_body_conn.svg");//left top of head
	private transient static final BufferedImage BODY_START       = IRenderer.getRes("textures/capsule/start_body.svg");//left top of head
	
	// HEAD = BODY_START + SimpleRenderer + HEAD_END
	private CapsuleBlock block;
	private Rect blockRect = null;
	
	private final CapsuleBlockClickable clickable;
	private transient BufferedImage rendered = null;
	
	public CapsuleBlockRenderer(CapsuleBlock b) {
		this.block = b;
		this.x = 0;
		this.y = 0;
		this.clickable = new CapsuleBlockClickable(this, null);
	}
	public CapsuleBlockRenderer(CapsuleBlock b, int x, int y) {
		this.block = b;
		this.x = x;
		this.y = y;
		this.clickable = new CapsuleBlockClickable(this, null);	
		}
	
	@Override
	public BufferedImage getRenderable() {
		
		if(rendered != null)
			return rendered;
		
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		// ▓                                 ▓
		// ▓              SETUP              ▓
		// ▓								 ▓
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		
		BufferedImage bi = new BufferedImage(getWidth(), getHeight() + InvocableBlockRenderer.CONNECTOR.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		if(g instanceof Graphics2D g2d) {
			 g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	         g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(new Font( font.getName(), Font.PLAIN, 55 ));
		blockRect = new Rect(ARM_BLOCK.getWidth() - 4, 0, 0, bi.getWidth() - ARM_BLOCK.getWidth() + 4);
		
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		// ▓                                 ▓
		// ▓               HEAD              ▓
		// ▓								 ▓
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		
		g.drawImage(block.attachable()? BODY_START_CONN:BODY_START, 0, 0, null);
		verticalBackground(bi, 0, BODY_START_CONN.getWidth(), BODY_START_CONN.getHeight(), getTitleHeight() - BODY_START_CONN.getHeight());
		
		String title = block.getTitle();
		if(BlockPanel.DEBUG_SHOW_HASHES)
			title = block.toString().split("@")[1] + title;
		BufferedImage text = renderText(title, BODY_START_CONN.getWidth(), getTitleHeight());
		background(bi, getTitleHeight(), BODY_START_CONN.getWidth(), text.getWidth());
		g.drawImage(text, BODY_START_CONN.getWidth(), 0, null);
		
		g.drawImage(ARM_START, -3/*I'm not gonna fight with Inkscape over this*/, getTitleHeight() - 3, null);
		g.drawImage(HEAD_END.getScaledInstance(HEAD_END.getWidth(), getTitleHeight(), BufferedImage.SCALE_SMOOTH), BODY_START_CONN.getWidth() + text.getWidth(), 0, null);
		verticalClone(bi, ARM_START.getWidth() - 5, 0, getTitleWidth() - ARM_START.getWidth() - HEAD_END.getWidth() + 7, 12, getTitleHeight());
		
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		// ▓                                 ▓
		// ▓               BODY              ▓
		// ▓								 ▓
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		
		int h = getTitleHeight();
		blockRect.y = h;
		if(block.size() > 0) {
			DragableRenderer rend = (DragableRenderer) block.get(0).getRenderer();
			rend.getClickable().setPosition(ARM_BLOCK.getWidth() - 4, h);
			rend.getClickable().setParent(this.getClickable());
			for(InvocableBlock ib : block) {
				rend = (DragableRenderer) ib.getRenderer();
				if(BlockPanel.DEBUG_SHOW_HITBOXES) {
					Rect hb = rend.getClickable().getPosition();
					((Graphics2D)g).setStroke(new BasicStroke(2));
					g.setColor(Color.green);
					g.drawRect(hb.x, hb.y, hb.w, hb.h);
				}
				g.drawImage(rend.getRenderable(), ARM_BLOCK.getWidth() - 4, h, null);
				h += rend.getHeight();
			}
		}
		g.drawImage(ARM_BLOCK.getScaledInstance(ARM_BLOCK.getWidth(), h - getTitleHeight() - ARM_START.getHeight() + 3, BufferedImage.SCALE_FAST), -1, ARM_START.getHeight() + getTitleHeight() - 3, null);
		
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		// ▓                                 ▓
		// ▓               END               ▓
		// ▓								 ▓
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		blockRect.h = h - blockRect.y;
		g.drawImage(ARM_END, 0, h, null);
		h+= ARM_END.getHeight() - 5;
		g.drawImage(block.attachable()?BODY_END_CONN:BODY_END, 0, h, null);
		background(bi, BODY_END_CONN.getWidth(), getWidth() - BODY_END_CONN.getWidth() - HEAD_END.getWidth(), h, block.attachable()?BODY_END_CONN.getHeight():BODY_END.getHeight());
		g.drawImage(HEAD_END, getWidth() - HEAD_END.getWidth(), h, null);
		rendered = bi;
		if(BlockPanel.DEBUG_SHOW_HITBOXES) {
			g.setColor(Color.red);
			g.drawRect(blockRect.x, blockRect.y, blockRect.w, blockRect.h);
			g.setColor(Color.blue);
			g.drawRect(blockRect.x, blockRect.y + blockRect.h, blockRect.w, InvocableBlockRenderer.CONNECTOR.getHeight());
		}
		
		return bi;
	}

	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public List<Rect> getBlockBundlesSize() {
		if(blockRect == null)
			getRenderable();
		return List.of(blockRect);
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}

	@Override
	public CapsuleBlock getBlock() {
		return block;
	}

	@Override
	public void update() {
		this.rendered = null;
		getClickable().update();
		if(getClickable().getParent() == null)
			BlockPanel.INSTANCE.repaint();
		else
			getClickable().getParent().getRenderer().update();
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
	public CapsuleBlockClickable getClickable() {
		return clickable;
	}

	@Override
	public void delete() {
		System.out.println("delete " + getBlock());
		BlockPanel.INSTANCE.removeBlock(this);
		if(getBlock() instanceof EventBlock eb)
			SpritePanel.getSprite()._deleteEvent(eb);
		for(IRenderer rend : getChildren())
			rend.delete();
		InvocableClickable next = ((InvocableClickable)getClickable()).next();
		while(next != null) {
			next.delete();
			next = next.next();
		}
		
	}
	
	public int getTitleHeight() {
		if(getChildren().size() == 0)
			return HEAD_END.getHeight();
		else {
			int h = 0;
			for (IRenderer ir : getChildren()) {
				h = Math.max(h, ir.getRenderable().getHeight());
			}
			return (int) Math.round(h * SUBSCALE);
			
		}
	}

	@Override
	public int getHeight() {
		int h = 0;
		for (InvocableBlock ib : block)
			h += ib.getRenderer().getHeight();
		return getTitleHeight() + ARM_END.getHeight() + (block.attachable()?BODY_END_CONN.getHeight():BODY_END.getHeight()) + h - InvocableBlockRenderer.CONNECTOR.getHeight();
	}

	@Override
	public int getWidth() {
		int w = 0;
		for(InvocableBlock ib: block)
			w = Math.max(w, ib.getRenderer().getWidth());
		
		return Math.max(w + ARM_BLOCK.getWidth(), getTitleWidth());
	}
	
	public int getTitleWidth() {
		int width = BODY_START_CONN.getWidth() 
				   + HEAD_END.getWidth() 
				   + block.getTitle().replaceAll("\\{\\{.*?}}", "").length() * FONT_WIDTH
				   + (BlockPanel.DEBUG_SHOW_HASHES? block.toString().split("@")[1].length() * FONT_WIDTH : 0);
		for(IRenderer rend : getChildren())
			width += rend.getRenderable().getWidth();
		return width;
	}

	@Override
	public void patch(int x, int y, int h, int w, BufferedImage newI) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void add(int bundle, int index, DragableRenderer block) {
		System.out.println("adding block");
		if(index < 0 || index == ((CapsuleBlock) getBlock()).size()) {
			((CapsuleBlock) getBlock()).add((InvocableBlock) block.getBlock());
		} else {
			((CapsuleBlock) getBlock()).add(index, (InvocableBlock) block.getBlock());
		}
	}
	@Override
	public boolean contains(int bundle, DragableRenderer block) {
		return ((CapsuleBlock) getBlock()).contains(block.getBlock());
	}
	
	@Override
	public DragableRenderer get(int bundle, int index) {
		return (DragableRenderer) getBlock().get(index).getRenderer();
	}
	
	@Override
	public List<InvocableBlock> getBlocksOf(int bundle) {
		return getBlock();
	}
	
	@Override
	public int indexOf(int bundle, DragableRenderer block) {
		return  ((CapsuleBlock) getBlock()).indexOf(block.getBlock());
	}
	
	@Override
	public boolean remove(int bundle, DragableRenderer block) {
		return  ((CapsuleBlock) getBlock()).remove(block.getBlock());
	}
	@Override
	public int sizeOf(int bundle) {
		return  ((CapsuleBlock) getBlock()).size();
	}
	
	@Override
	public int bundleCount() {
		return 1;
	}
	@Override
	public SVGDocument getRenderableSVG() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
