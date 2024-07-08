package ui.renderers;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import org.apache.batik.anim.dom.SVGOMRectElement;
import org.apache.batik.anim.dom.SVGOMSVGElement;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPathElement;

import clickable.BlockClickable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import parsers.SVGReader;
import ui.components.BlockPanel;
import ui.domain.SVGConfig;
import ui.renderers.IRenderer.DragableRenderer;

public class SimpleBlockRenderer implements DragableRenderer{
	private static final long serialVersionUID = 2111348834794671783L;
	
	public static interface SimpleRenderable<R> extends IRenderable, VariableHolder, Valuable<R> {
		
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
				OPERATOR("#4ca742"),
				VARIABLE("#e97d00"),
				;
			
			/**
			 * color to fill the block with
			 */
			public final String color;
			BlockCategory(String color){
				this.color = color;
			}
		}
	}
	
	
	private int x;
	private int y;
	protected final SimpleRenderable<?> block;
	protected transient BufferedImage rendred = null;
	protected final BlockClickable clickable;
	
	private synchronized BufferedImage rendered(BufferedImage set, boolean w) {
		if(w) rendred = set;
		return rendred;
	}
	
	public SimpleBlockRenderer(SimpleRenderable<?> block) {
		this.block = block;
		this.x = 0;
		this.y = 0;
		this.clickable = new BlockClickable(this, null);
	}
	
	public SimpleBlockRenderer(SimpleRenderable<?> block, int x, int y) {
		this.block = block;
		this.x = x;
		this.y = y;
		this.clickable = new BlockClickable(this, null);
	}
	
	@Override
	public BufferedImage getRenderable() {
		if(rendered(null, false) == null)
			rendered(SVGReader.toBufferedImage(getRenderableSVG()), true);
		return rendered(null, false);
//		if(rendered(null, false) != null)
//			return rendered(null, false);
//		
//		BufferedImage start = block.getCategory().start;
//		BufferedImage text = renderText(block.getTitle(), start.getWidth(), getHeight());
//		BufferedImage end = block.getCategory().end;
//		
//		BufferedImage rendered = new BufferedImage(start.getWidth() + text.getWidth() + end.getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
//		Graphics g = rendered.getGraphics();
//		if(g instanceof Graphics2D g2d) {
//			 g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//	         g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//	         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//	         g2d.setStroke(new BasicStroke(2));
//		}
//		
//		g.drawImage(start.getScaledInstance(start.getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH), 0, 0, null);
//		
//		background(rendered,getHeight(),start.getWidth(),text.getWidth());
//		g.drawImage(text, start.getWidth(), 0, null);
//		
//		g.drawImage(end.getScaledInstance(start.getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH), start.getWidth() + text.getWidth(), 0, null);
//		rendered(rendered, true);
//		return rendered;
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
	public SimpleRenderable<?> getBlock() {
		return block;
	}

	@Override
	public void update() {
		rendered(null, true);
		updateSVG = true;
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
	public double getHeight() {
		if(gn == null) {
			getRenderableSVG();
			gn = ctx.getGraphicsNode(document.getDocumentElement());
		}
		return gn.getBounds().getHeight();
	}

	@Override
	public double getWidth() {
		if(gn == null) {
			getRenderableSVG();
			gn = ctx.getGraphicsNode(document.getDocumentElement());
		}
		return gn.getBounds().getWidth();
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
//	@Override
//	public void patch(int x, int y, int h, int w, BufferedImage bi) {
//		BufferedImage rend =  rendered(null, false);
//		System.out.println("x:"+x+" h:"+h+" w:"+w);
//		background(rend, h, x, w);
//		Graphics2D g = (Graphics2D) rend.getGraphics();
//		g.drawImage(bi, x, y, null);
//		g.setColor(Color.green);
//		g.setStroke(new BasicStroke(2));
//		g.drawRect(x + 1, y + 1, w - 2, h - 2);
//		rendered(rend, true);
//		if(clickable.getParent() != null) {
//			Rect r = clickable.getPosition();
//			clickable.getParent().getRenderer().patch(r.x, r.y, r.h + r.y, r.w, getRenderable());
//		} else
//			BlockPanel.INSTANCE.repaint();
//	}
	
	private void fillColor(Element parent) {
		NodeList nl = parent.getChildNodes();
		String style = null;
		for(int i = 0; i < nl.getLength(); i++)
			if(nl.item(i) instanceof Element e) {
				if((style = e.getAttribute("style")) != null)
					e.setAttribute("style", style.replace("fill:#ffffff", "fill:" + getBlock().getCategory().color)
												 .replace("stroke:none", "stroke:#ffffff;stroke-opacity:1;stroke-dasharray:none"));
				fillColor(e);
			}
	}
	
	private transient SVGConfig config = null;
	private transient SVGDocument document = null;
	private transient BridgeContext ctx = null;
	private transient GraphicsNode gn = null;
	private boolean updateSVG = true;
	
	@Override
	public SVGDocument getRenderableSVG() {
		if(config == null) {
			config = switch(getBlock().value()) {
				case Number n -> SVGConfig.getConfig(IRenderable.VARIABLE_NUM);
				case Boolean b -> SVGConfig.getConfig(IRenderable.VARIABLE_BOOL);
				default -> SVGConfig.getConfig(IRenderable.VARIABLE_STR);
			};
			document = config.document();
			fillColor(document.getDocumentElement());
			SVGOMSVGElement root = (SVGOMSVGElement)document.getDocumentElement();
			
			SVGDocument text = addText(getBlock().getTitle());
			double textWidth = SVGReader.build(text).getGraphicsNode(text.getDocumentElement()).getBounds().getWidth();
			
			SVGPathElement path = (SVGPathElement) document.getElementById("resize_path");
			SVGOMRectElement rect = null;
			if(path != null) {
				String[] commands = path.getAttribute("d").split(" ");
		        StringBuilder newD = new StringBuilder();
		        String lastCommand = "";
		        for (String command : commands) {
		            if (command.matches("[MLHVCSQTAZmlhvcsqtaz]")) {
		            	lastCommand = command;
		                newD.append(command).append(" ");
		            } else {
		            	if(lastCommand.equals("h"))
		            		newD.append(textWidth);
		            	else if(lastCommand.toLowerCase().equals("m")) {
		            		lastCommand = "";
		            		newD.append(config.wOffset() + ",0");
		            	} else
		            		newD.append(command);
		            	newD.append(" ");
		            }
		        }
		        path.setAttribute("d", newD.toString().trim());
			} else {
				rect = (SVGOMRectElement)document.getElementById("resize_rect");
				rect.setAttributeNS(null, "width", config.wOffset() + textWidth+ "");
			}
			Node child = document.importNode(text.getDocumentElement(), true);
			((Element)child).setAttributeNS(null, "x", "0");
			root.appendChild(child);
			ctx = SVGReader.build(document);
			Rectangle2D bb = ctx.getGraphicsNode(path == null?rect:path).getBounds();
			root.setAttributeNS(null, "width", ""  + bb.getWidth());
			root.setAttributeNS(null, "height", "" + bb.getHeight());
			root.setAttributeNS(null, "viewBox", "0 0 " + (bb.getWidth() + 1) + " " + bb.getHeight());
		} else if(updateSVG) {
			
		}
		
		return document;
	}
}
