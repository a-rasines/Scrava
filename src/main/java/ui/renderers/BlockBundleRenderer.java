package ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import clickable.BlockClickable;
import clickable.CapsuleBlockClickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.CapsuleBlock;
import domain.values.AbstractLiteral;
import ui.components.BlockPanel;

public class BlockBundleRenderer implements CapsuleRenderer{
	
	private int x;
	private int y;
	
	private static final BufferedImage ARM_BLOCK        = IRenderer.getRes("textures/capsule/arm_block.svg"); //left of block
	private static final BufferedImage ARM_END          = IRenderer.getRes("textures/capsule/arm_end.svg"); //between arm block & body end
	//private static final BufferedImage BODY_END_CONN    = IRenderer.getRes("textures/capsule/end_body_conn.svg");//under arm end
	//private static final BufferedImage BODY_END         = IRenderer.getRes("textures/capsule/end_body.svg");//under arm end
	private static final BufferedImage HEAD_END         = IRenderer.getRes("textures/capsule/end.svg"); // right of head
	private static final BufferedImage MIDDLE_BODY    = IRenderer.getRes("textures/capsule/middle_body.svg");
	private static final BufferedImage ARM_START        = IRenderer.getRes("textures/capsule/start_arm.svg"); //Under head
	private static final BufferedImage BODY_START_CONN  = IRenderer.getRes("textures/capsule/start_body_conn.svg");//left top of head
	private static final BufferedImage BODY_START       = IRenderer.getRes("textures/capsule/start_body.svg");//left top of head
	
	// HEAD = BODY_START + SimpleRenderer + HEAD_END
	private CapsuleBlock block;
	private CapsuleBlockClickable clickable;
	private DragableRenderer parent;
	private Rect blockRect = null;
	private boolean isFirst;
	
	public BlockBundleRenderer(DragableRenderer parent, CapsuleBlock b, boolean isFirst) {
		this.parent = parent;
		this.block = b;
		this.x = 0;
		this.y = 0;
		this.isFirst = isFirst;
		this.clickable = new CapsuleBlockClickable(this, null);
	}
	public BlockBundleRenderer(CapsuleBlock b, boolean isFirst, int x, int y) {
		this.block = b;
		this.x = x;
		this.y = y;
		this.isFirst = isFirst;
		this.clickable = new CapsuleBlockClickable(this, null);	
		}
	
	private BufferedImage rendered = null;
	
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
		g.setFont(new Font( font.getName(), Font.PLAIN, 55 ));
		blockRect = new Rect(ARM_BLOCK.getWidth() - 4, 0, 0, bi.getWidth() - ARM_BLOCK.getWidth() + 4);
		
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		// ▓                                 ▓
		// ▓               HEAD              ▓
		// ▓								 ▓
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		
		if(isFirst)
			g.drawImage(block.attachable()? BODY_START_CONN:BODY_START, 0, 0, null);
		else
			g.drawImage(MIDDLE_BODY, 0, 0, null);
		verticalBackground(bi, 0, BODY_START_CONN.getWidth(), BODY_START_CONN.getHeight(), getTitleHeight() - BODY_START_CONN.getHeight());
		
		String title = block.getTitle();
		if(BlockPanel.DEBUG_SHOW_HASHES)
			title = block.toString().split("@")[1] + title;
		BufferedImage text = renderText(title, BODY_START_CONN.getWidth(), getTitleHeight());
		for(IRenderer ir : getChildren())
			ir.getClickable().move(x, y);
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
		blockRect.y += h;
		for(InvocableBlock ib : block) {
			DragableRenderer rend = IRenderer.getDragableRendererOf((IRenderable) ib);
//			verticalBackground(bi, 0, ARM_BLOCK.getWidth(), h/* == getTitleHeight()?h+ARM_START.getHeight():h*/, rend.getHeight());
//			leftClone(bi, 0, Math.max(getTitleHeight() + ARM_START.getHeight(), h), 10, ARM_BLOCK.getHeight(), ARM_BLOCK.getWidth());
			rend.getClickable().setPosition(ARM_BLOCK.getWidth() - 4 + x, h + y);
			rend.getClickable().setParent(this.getClickable());
			if(BlockPanel.DEBUG_SHOW_HITBOXES) {
				Rect hb = rend.getClickable().getPosition();
				((Graphics2D)g).setStroke(new BasicStroke(2));
				g.setColor(Color.green);
				g.drawRect(hb.x, hb.y, hb.w, hb.h);
			}
			g.drawImage(rend.getRenderable(), ARM_BLOCK.getWidth() - 4, h, null);
			h += rend.getHeight();
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
		rendered = bi;
		if(BlockPanel.DEBUG_SHOW_HITBOXES) {
			g.setColor(Color.red);
			g.drawOval(blockRect.x - 2, blockRect.y - 2, 4, 4);
			g.drawRect(blockRect.x, blockRect.y, blockRect.w, blockRect.h);
			g.setColor(Color.blue);
			g.drawOval(blockRect.x + blockRect.w - 2, blockRect.y + blockRect.h - 2, 4, 4);
			g.drawRect(blockRect.x, blockRect.y + blockRect.h, blockRect.w, InvocableBlockRenderer.CONNECTOR.getHeight());
		}
		blockRect.y += y;
		blockRect.x += x;
		return bi;
	}

	@Override
	public int getX() {
		return parent.getX() + x;//x + parent.getX();
	}
	
	@Override
	public List<Rect> getBlockBundlesSize() {
		if(blockRect == null)
			getRenderable();
		return List.of(blockRect);
	}

	@Override
	public int getY() {
		return y + parent.getY();
	}

	@Override
	public void moveTo(int x, int y) {
		blockRect.y -= this.y;
		blockRect.x -= this.x;
		blockRect.y += y;
		blockRect.x += x;
		for(InvocableBlock ib : block) {
			IRenderer.getDragableRendererOf((IRenderable) ib).move(x - this.x, y - this.y);
		}
		this.x = x;
		this.y = y;
	}

	@Override
	public void move(int x, int y) {
		blockRect.y += y;
		blockRect.x += x;
		this.x += x;
		this.y += y;
	}

	@Override
	public Translatable getBlock() {
		return block;
	}

	@Override
	public void update() {
			parent.update();
	}
	
	public void _update() {
		this.rendered = null;
		clickable.update();
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

	@Override
	public BlockClickable getClickable() {
		return clickable;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
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
			h += IRenderer.getDragableRendererOf((IRenderable)ib).getHeight();
		return getTitleHeight() + ARM_END.getHeight() + h - InvocableBlockRenderer.CONNECTOR.getHeight();
	}

	@Override
	public int getWidth() {
		int w = 0;
		for(InvocableBlock ib: block)
			w = Math.max(w, IRenderer.getDragableRendererOf((IRenderable) ib).getWidth());
		
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
		if(index < 0) {
			((CapsuleBlock) getBlock()).add((InvocableBlock) block.getBlock());
		} else {
			((CapsuleBlock) getBlock()).add(index, (InvocableBlock) block.getBlock());
		}
		rendered = null;
		blockRect = null;
	}
	@Override
	public boolean contains(int bundle, DragableRenderer block) {
		return ((CapsuleBlock) getBlock()).contains(block.getBlock());
	}
	
	@Override
	public DragableRenderer get(int bundle, int index) {
		return IRenderer.getDragableRendererOf((IRenderable) ((CapsuleBlock) getBlock()).get(index));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InvocableBlock> getBlocksOf(int bundle) {
		return (List<InvocableBlock>) getBlock();
	}
	
	@Override
	public int indexOf(int bundle, DragableRenderer block) {
		return  ((CapsuleBlock) getBlock()).indexOf(block.getBlock());
	}
	
	@Override
	public boolean remove(int bundle, DragableRenderer block) {
		boolean res = ((CapsuleBlock) getBlock()).remove(block.getBlock());
		rendered = null;
		blockRect = null;
		return res;
	}
	@Override
	public int sizeOf(int bundle) {
		return  ((CapsuleBlock) getBlock()).size();
	}
	@Override
	public int bundleCount() {
		return 1;
	}
	
//	public static void main(String[] args) {
//		
//		BlockBundleRenderer sampleImage = new BlockBundleRenderer(new IfBlock(
//																		new BooleanLiteral(true), 
//																			new MoveBlock(null), 
//																			new MoveBlock(null),
//																			new MoveBlock(null),
//																			new MoveBlock(null)) {
//			private static final long serialVersionUID = -4006721083467801784L;
//
//			public boolean attachable() {
//				return false;
//			}
//		}, false);
//	 	
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Image Display");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1000, 1000);
//
//            JPanel panel = new JPanel() {
//				private static final long serialVersionUID = -94843536618228336L;
//
//				@Override
//                protected void paintComponent(Graphics g) {
//                    g.drawImage(sampleImage.getRenderable(), 100, 100, this);
//                }
//            };
//            frame.getContentPane().add(panel);
//
//            frame.setVisible(true);
//        });
//	}

}
