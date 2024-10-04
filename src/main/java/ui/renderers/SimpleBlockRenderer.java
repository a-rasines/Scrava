package ui.renderers;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import org.apache.batik.anim.dom.SVGOMGElement;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.dom.AbstractElement;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

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
		if(rendered(null, false) == null) {
			getRenderableSVG();
			Element root = element.getOwnerDocument().getDocumentElement();
			root.setAttributeNS(null, "width", root.getAttribute("width") + "mm");
			root.setAttributeNS(null, "height", root.getAttribute("height") + "mm");
			rendered(SVGReader.toBufferedImage(element.getOwnerDocument()), true);
			
		}
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
			gn = ctx.getGraphicsNode(element);
		}
		return gn.getBounds().getHeight();
	}

	@Override
	public double getWidth() {
		if(gn == null) {
			getRenderableSVG();
			gn = ctx.getGraphicsNode(element);
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
												 .replace("stroke:none", "") + ";stroke:#ffffff;stroke-opacity:1;stroke-dasharray:none");
				fillColor(e);
			}
	}
	
	private transient SVGConfig config = null;
	private transient Element element = null;
	private transient BridgeContext ctx = null;
	private transient GraphicsNode gn = null;
	private boolean updateSVG = false;
	
	@Override
	public SimpleBlockRenderer toDocument(SVGDocument doc) {
		if(element == null) getRenderableSVG();
		element = (Element)doc.importNode(element, true);
		ctx = SVGReader.build(doc);
		return this;
	}
	
	private void generateSVG() {
		config = switch(getBlock().value()) {
			case Number n -> SVGConfig.getConfig(IRenderable.VARIABLE_NUM);
			case Boolean b -> SVGConfig.getConfig(IRenderable.VARIABLE_BOOL);
			default -> SVGConfig.getConfig(IRenderable.VARIABLE_STR);
		};
		SVGDocument document = config.document();
		element = setupSVG(document);
		fillColor(element);
		Element contentElement = document.createElementNS("http://www.w3.org/2000/svg", "g");
		contentElement.setAttribute("id", getBlock().hashCode() + "_content_group");
		element.appendChild(contentElement);
		addText(getBlock().getTitle(), (AbstractElement) contentElement);
		Element background = document.getElementById("resize_rect");
		background.setAttribute("width", contentElement.getAttribute("width"));
		background.setAttribute("height", contentElement.getAttribute("height"));
		background.setAttribute("id", getBlock().hashCode() + "_resize_rect");
	}
	private void updateSVG() {
		updateSVG = false;
		SVGOMGElement contentElement = (SVGOMGElement) element.getOwnerDocument().getElementById(getBlock().hashCode() + "_content_group");
		assemble(contentElement, contentElement.getBBox().getHeight());
	}
	
	@Override
	public Element getRenderableSVG() {
		if(element == null)
			generateSVG();
		else if(updateSVG)
			updateSVG();
		return element;
	}
}
