package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import domain.models.interfaces.Clickable;
import ui.domain.BlockSection;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.IRenderer.IRenderable;

public class BlockSelectorPanel implements MouseListener, MouseWheelListener {
	
	public static final BlockSelectorPanel INSTANCE = new BlockSelectorPanel(); 
	
	private Color background;
	private BufferedImage rendered = null;
	private int y = 0;
	private BlockSection selected;
	private BlockSelectorPanel() {
		BlockPanel.INSTANCE.addMouseListener(this);
		BlockPanel.INSTANCE.addMouseWheelListener(this);
		setPage(BlockSection.values()[0]);		
	}
	public void setPage(BlockSection page) {
		int r = (page.color & 0xff0000) + 0x330000;
		if((r & 0xff000000) != 0)
			r = 0xff0000;
		int g = (page.color & 0xff00) + 0x3300;
		if((g & 0xff0000) != 0)
			g = 0xff00;
		int b = (page.color & 0xff) + 0x33;
		if((b & 0xff00) != 0)
			b = 0xff;
		this.selected = page;
		background = new Color(0xff000000 | r | g | b);
		this.rendered = null;
		BlockPanel.INSTANCE.repaint();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point click = e.getPoint();
		int w = BlockPanel.INSTANCE.getWidth();
		System.out.println("registered click");
		if(click.x < (2 * w / 3 - 10) || click.x > w - 10) return;
		System.out.println("verified click");
		int y = -this.y + 10;
		for(IRenderer block: selected.blocks) {	
			if(y + block.getRenderable().getHeight() / 2 < 0) {
				y += block.getRenderable().getHeight() / 2;
				continue;
			}
			if(y < click.y && BlockPanel.INSTANCE.zoom * block.getRenderable().getHeight() + y > click.y) {
				DragableRenderer dr = IRenderer.getDragableRendererOf((IRenderable) block.getBlock().create(BlockPanel.INSTANCE.getSprite()));
				Point tras = BlockPanel.INSTANCE.getTraslation();
				dr.moveTo((int)(2*BlockPanel.INSTANCE.getWidth()/(3*BlockPanel.INSTANCE.zoom) + tras.x), (int)(click.y/BlockPanel.INSTANCE.zoom + tras.y));
				BlockPanel.INSTANCE.addBlock(dr);
				BlockPanel.INSTANCE.setClicked(dr.getClickable());
				BlockPanel.INSTANCE.repaint();
				break;
				
			}
			
			//
			
			y += block.getRenderable().getHeight() / 2;
			if(y > rendered.getHeight())break;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(BlockPanel.INSTANCE.getClicked() != null) {
			Clickable clicked = BlockPanel.INSTANCE.getClicked();
			if(BlockPanel.INSTANCE.zoom * (clicked.getPosition().x + clicked.getPosition().w / 2) > 2 * BlockPanel.INSTANCE.getWidth() / 3 - 10) { 
				clicked.delete();
				BlockPanel.INSTANCE.repaint();
			}
		}
		BlockPanel.INSTANCE.setClicked(null);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point click = e.getPoint();
		int w = BlockPanel.INSTANCE.getWidth();
		if(click.x < (2 * w / 3 - 10) || click.x > w - 10) return;
		
		int y = (int) Math.max(0, Math.min(this.y + e.getWheelRotation() * 32, selected.totalY / 2 - BlockPanel.INSTANCE.getHeight()));
		if(y != this.y) {
			this.y = y;
			this.rendered = null;
			BlockPanel.INSTANCE.repaint();
		}
	}
	
	public void paintComponent(Graphics g) {
		int w = BlockPanel.INSTANCE.getWidth();
		if(rendered == null || rendered.getWidth() != w / 3 || rendered.getHeight() != BlockPanel.INSTANCE.getHeight()) {
			rendered = new BufferedImage(w / 3, BlockPanel.INSTANCE.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g0 = rendered.getGraphics();
			g0.setColor(background);
			g0.fillRect(0, 0, w / 3, BlockPanel.INSTANCE.getHeight());
			int y = -this.y + 10;
			for(IRenderer block: selected.blocks) {	
				if(y + block.getRenderable().getHeight() / 2 < 0) {
					y += block.getRenderable().getHeight() / 2;
					continue;
				}
				g0.drawImage(block.getRenderable().getScaledInstance(block.getRenderable().getWidth() / 2, block.getRenderable().getHeight() / 2, BufferedImage.SCALE_FAST), 0, y, null);
				y += block.getRenderable().getHeight() / 2;
				if(y > rendered.getHeight())break;
			}
		}
		g.drawImage(rendered, 2 * w / 3 - 10, 0, null);
		
	}
	
	// Empty functions

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

}
