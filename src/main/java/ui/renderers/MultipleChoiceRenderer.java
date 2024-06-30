package ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.svg.SVGDocument;

import clickable.BlockClickable;
import clickable.CapsuleBlockClickable;
import clickable.InvocableClickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Translatable;
import domain.models.types.CapsuleBlock;
import domain.models.types.MultipleOptionCapsuleBlock;
import ui.components.BlockPanel;

public class MultipleChoiceRenderer implements CapsuleRenderer {

	private static final long serialVersionUID = 1971495825159916256L;
	
	private transient static final BufferedImage BODY_END_CONN    = IRenderer.getRes("textures/capsule/end_body_conn.svg");//under arm end
	private transient static final BufferedImage BODY_END         = IRenderer.getRes("textures/capsule/end_body.svg");//under arm end
	private transient static final BufferedImage HEAD_END         = IRenderer.getRes("textures/capsule/end.svg"); // right of head
	
	private int x;
	private int y;
	
	private MultipleOptionCapsuleBlock block;
	private CapsuleBlockClickable clickable;
	private List<BlockBundleRenderer> bundles = null;
	
	public MultipleChoiceRenderer(MultipleOptionCapsuleBlock b) {
		this(b, 0, 0);
	}
	public MultipleChoiceRenderer(MultipleOptionCapsuleBlock b, int x, int y) {
		this.block = b;
		this.x = x;
		this.y = y;
		this.clickable = new CapsuleBlockClickable(this, null);
		int by = 0;
		for(BlockBundleRenderer bbr: getBlockBundles()) {
			for(IRenderer ir : bbr.getChildren()) {
				if(ir.getClickable() instanceof BlockClickable)
					((BlockClickable) ir.getClickable()).setParent(this.getClickable());
				ir.getClickable().move(0, by);
			}
			by += bbr.getHeight();
		}
	}	
	
	private transient BufferedImage rendered = null;
	
	@Override
	public BufferedImage getRenderable() {
		if(getWidth() == 0 || getHeight() == 0)
			return rendered;
		if(rendered != null)
			return rendered;
		BufferedImage bi = new BufferedImage(getWidth(), getHeight() + (block.attachable()? InvocableBlockRenderer.CONNECTOR.getHeight():0), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = bi.getGraphics();
		if(g instanceof Graphics2D g2d) {
			 g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	         g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	         g2d.setStroke(new BasicStroke(2));
		}
		int y = 0;
		for(BlockBundleRenderer brr: getBlockBundles()) {
			g.drawImage(brr.getRenderable(), 0, y, null);
			brr.moveTo(0, y);
			if(BlockPanel.DEBUG_SHOW_HITBOXES) {
				Rect r = brr.getBlockBundlesSize().get(0);
				g.setColor(Color.cyan);
				g.drawRect(r.x, r.y, r.w, r.h);
				g.setColor(Color.ORANGE);
				for(InvocableBlock ib : brr.getBlocksOf(0)) {
					DragableRenderer dr = (DragableRenderer) ib.getRenderer();
					g.drawRect(dr.getX(), dr.getY(), dr.getWidth(), dr.getHeight());
				}
			}
				
			y += brr.getRenderable().getHeight();
		}
		g.drawImage(block.attachable()?BODY_END_CONN:BODY_END, 0, y, null);
		background(bi, BODY_END_CONN.getWidth(), getWidth() - BODY_END_CONN.getWidth() - HEAD_END.getWidth(), y, BODY_END.getHeight());
		g.drawImage(HEAD_END, bi.getWidth() - HEAD_END.getWidth(), y, null);
		
		
		
		rendered = bi;
		return bi;
	}
	
	public List<BlockBundleRenderer> getBlockBundles() {
		if(bundles != null)
			return bundles;
		bundles = new ArrayList<>(block.size());
		boolean isFirst = true;
		for(CapsuleBlock bl: block) {
			bundles.add(new BlockBundleRenderer(this, bl, isFirst));
			if(isFirst)
				isFirst = false;
		}
		return bundles;
		
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
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
	public Translatable getBlock() {
		return block;
	}

	@Override
	public void update() {
		rendered = null;
		for(BlockBundleRenderer b : bundles)
			b._update();
		if(clickable.getParent() == null)
			BlockPanel.INSTANCE.repaint();
		else
			clickable.getParent().getRenderer().update();
	}

	@Override
	public List<IRenderer> getChildren() {
		List<IRenderer> output = new ArrayList<>();
		for(BlockBundleRenderer bbr: getBlockBundles()) {
			output.addAll(bbr.getChildren());	
		}
		return output;
	}

	@Override
	public BlockClickable getClickable() {
		return clickable;
	}

	@Override
	public void delete() {
		System.out.println("delete " + getBlock());
		BlockPanel.INSTANCE.removeBlock(this);
		for(BlockBundleRenderer bbr: getBlockBundles())
			bbr.delete();
		InvocableClickable next = ((InvocableClickable)getClickable()).next();
		while(next != null) {
			next.delete();
			next = next.next();
		}
	}

	@Override
	public int getHeight() {
		int h = 0;
		for(BlockBundleRenderer bbr : getBlockBundles())
			h += bbr.getRenderable().getHeight();
		h += BODY_END.getHeight();
		return h;
	}

	@Override
	public int getWidth() {
		int w = 0;
		for(BlockBundleRenderer bbr : getBlockBundles())
			w = Math.max(w, bbr.getRenderable().getWidth());
		return w;
	}

	@Override
	public void patch(int x, int y, int h, int w, BufferedImage bi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Rect> getBlockBundlesSize() {
		List<Rect> r = new ArrayList<>(getBlockBundles().size());
		getBlockBundles().forEach(v-> r.add(v.getBlockBundlesSize().get(0)));
		return r;
	}
	
	@Override
	public void add(int bundle, int index, DragableRenderer block) {
		if(index < 0) {
			((MultipleOptionCapsuleBlock) getBlock()).get(bundle).add((InvocableBlock) block.getBlock());
		} else {
			((MultipleOptionCapsuleBlock) getBlock()).get(bundle).add(index, (InvocableBlock) block.getBlock());
		}
	}
	@Override
	public boolean contains(int bundle, DragableRenderer block) {
		return ((MultipleOptionCapsuleBlock) getBlock()).get(bundle).contains(block.getBlock());
	}
	
	@Override
	public DragableRenderer get(int bundle, int index) {
		return bundles.get(bundle).get(bundle, index);
	}
	
	@Override
	public List<InvocableBlock> getBlocksOf(int bundle) {
		return ((MultipleOptionCapsuleBlock) getBlock()).get(bundle);
	}
	
	@Override
	public int indexOf(int bundle, DragableRenderer block) {
		return  ((MultipleOptionCapsuleBlock) getBlock()).get(bundle).indexOf(block.getBlock());
	}
	
	@Override
	public boolean remove(int bundle, DragableRenderer block) {
		return  ((MultipleOptionCapsuleBlock) getBlock()).get(bundle).remove(block.getBlock());
	}
	@Override
	public int sizeOf(int bundle) {
		return  ((MultipleOptionCapsuleBlock) getBlock()).get(bundle).size();
	}
	
	@Override
	public int bundleCount() {
		return bundles.size();
	}
	@Override
	public SVGDocument getRenderableSVG() {
		// TODO Auto-generated method stub
		return null;
	}

}
