package ui.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.apache.batik.anim.dom.SVGOMRectElement;
import org.apache.batik.anim.dom.SVGOMSVGElement;
import org.apache.batik.bridge.BridgeContext;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPathElement;

import clickable.BlockClickable;
import clickable.LiteralClickable;
import domain.models.interfaces.Clickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import domain.values.BooleanLiteral;
import domain.values.EnumLiteral;
import parsers.SVGReader;
import ui.components.BlockPanel;
import ui.domain.SVGConfig;

public class LiteralRenderer implements IRenderer {
	
	private static final long serialVersionUID = 4947025860653468394L;
	
	public float opacity = 1f;
	
	public static LiteralRenderer of(LiteralRenderable<?> block, String type, BlockClickable parent) {
		if(block instanceof EnumLiteral)
			return new LiteralRenderer(block, IRenderable.VARIABLE_ENUM, parent);
		return new LiteralRenderer(block, type, parent);
	}
	
	public static LiteralRenderer of(LiteralRenderable<?> block, Object type, BlockClickable parent) {
		if(block instanceof EnumLiteral)
			return new LiteralRenderer(block, IRenderable.VARIABLE_ENUM, parent);
		switch(type) {
			case Boolean b:
				return new LiteralRenderer(block, IRenderable.VARIABLE_BOOL, parent);
			case Number n:
				return new LiteralRenderer(block, IRenderable.VARIABLE_NUM, parent);
			case String s:
				return new LiteralRenderer(block, IRenderable.VARIABLE_STR, parent);
			default:
				return null;
		}
	}
	
	public static interface LiteralRenderable<T> extends IRenderable, Valuable<T> {
		
	}
	private final LiteralRenderable<?> block;
	private final LiteralClickable clickable;
	private String type;
	private transient BufferedImage rendered = null;
	private boolean updateSVG = true;
	private transient SVGDocument document = null;
	private transient SVGConfig config;
	private transient BridgeContext ctx;
	
	private LiteralRenderer(LiteralRenderable<?> block, String type, BlockClickable parent) {
		this.block = block;
		if(!type.startsWith("{{"))
			type = "{{" + type;
		if(!type.endsWith("}}"))
			type += "}}";
		this.type = type;
		this.clickable = new LiteralClickable(this, (AbstractLiteral<?>)block, parent);
	}

	@Override
	public BufferedImage getRenderable() {
		if(rendered == null) {
			SVGDocument doc = getRenderableSVG();
			BufferedImage bi = SVGReader.toBufferedImage(doc);
			return rendered = bi;
		}else
			return rendered;
	}
	
	@Override
	public LiteralRenderable<?> getBlock() {
		return block;
	}

	@Override
	public void update() {
		rendered = null;
		updateSVG = true;
		if(clickable.getParent() == null)
			BlockPanel.INSTANCE.repaint();
		else
			clickable.getParent().getRenderer().update();
		
	}

	@Override
	public List<IRenderer> getChildren() {
		return List.of(); //No children
	}
	
	@Override
	public Clickable getClickable() {
		return clickable;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
//  	SVGDocument document = readSVG("textures/sprite/def.svg");
	  JFrame frame = new JFrame("SVG Display");
	  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	  frame.setSize(800, 600);

//	  JSVGCanvas canvas = new JSVGCanvas();
//	  frame.getContentPane().add(canvas, BorderLayout.CENTER);
//	  canvas.setSVGDocument(document);
//	  LiteralRenderer lr = (LiteralRenderer) new NumberLiteral<Integer>(123450, null).getRenderer();
	  LiteralRenderer lr = (LiteralRenderer) new BooleanLiteral(true, null).getRenderer();
	//  LiteralRenderer lr = (LiteralRenderer) new StringLiteral("123456789987654321", null).getRenderer();
	//  LiteralRenderer lr = (LiteralRenderer) new EnumLiteral<Integer>(KeyEventBlock.KEY_MAP, null).getRenderer();
	  BufferedImage bi = lr.getRenderable();
	  Graphics g = bi.getGraphics();
	  g.setColor(Color.red);
	  g.drawRect(0, 0, bi.getWidth(), bi.getHeight());
	  g.drawLine(bi.getWidth()/2, 0, bi.getWidth()/2, bi.getHeight());
	  frame.add(new JLabel(new ImageIcon(bi)), BorderLayout.CENTER);
	  
	
	  frame.setVisible(true);
	}

	@Override
	public void delete() {
		System.out.println("delete " + getBlock());
	}

	@Override
	public int getHeight() {
		return getRenderable().getHeight();
	}

	@Override
	public int getWidth() {
		return getRenderable().getWidth();
	}

	@Override
	public void patch(int x, int y, int h, int w, BufferedImage bi) {
		rendered = null;
		Rect r = clickable.getPosition();
		clickable.getParent().getRenderer().patch(r.x, r.y, r.y + r.h, r.w, getRenderable());
		
	}
	
	private static final Map<String, SVGConfig> CONFIG_MAP;
	
	static {
		SVGDocument numberDoc = null;
		SVGDocument stringDoc = null;
		SVGDocument booleanDoc = null;
		SVGDocument enumDoc = null;
		try { numberDoc = SVGReader.readSVG("textures/variable/literal/num.svg"); } catch(Exception e) {e.printStackTrace();}
		try { stringDoc = SVGReader.readSVG("textures/variable/literal/string.svg"); } catch(Exception e) {e.printStackTrace();}
		try { booleanDoc = SVGReader.readSVG("textures/variable/literal/boolean.svg"); } catch(Exception e) {e.printStackTrace();}
		try { enumDoc = SVGReader.readSVG("textures/variable/literal/enum.svg"); } catch(Exception e) {e.printStackTrace();}
		CONFIG_MAP = Map.of(
			IRenderable.VARIABLE_NUM, new SVGConfig(numberDoc, 24, 13, 0),
			IRenderable.VARIABLE_BOOL, new SVGConfig(booleanDoc, 8.75, 9, 0),
			IRenderable.VARIABLE_STR, new SVGConfig(stringDoc, 12, 5, 0),
			IRenderable.VARIABLE_ENUM, new SVGConfig(enumDoc, 25, 5, 12.35)
		);
	}
	
	@Override
	public SVGDocument getRenderableSVG() {
		if(document == null || updateSVG) {
			updateSVG = false;
			boolean isNewDocument = false;
			if(document == null) {
				config = CONFIG_MAP.get(type);
				document = config.document();
				ctx = SVGReader.build(document);
				isNewDocument = true;
			}
			SVGOMSVGElement root = (SVGOMSVGElement)document.getDocumentElement();
			String value = getBlock().value().toString();
			if(type.equals(IRenderable.VARIABLE_ENUM))
				value = ((EnumLiteral<?>)getBlock()).name();
			
			SVGPathElement path = (SVGPathElement) document.getElementById("resize_path");
			SVGOMRectElement rect = null;
			Element end = null;
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
		            		newD.append(value.length() * FONT_WIDTH_SVG);
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
				rect.setAttributeNS(null, "width", config.wOffset() + value.length() * FONT_WIDTH_SVG + "");
				end = document.getElementById("end");
			}
	        Element textElement;
	        if(isNewDocument) {
		        textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
		        
		        textElement.setAttributeNS(null, "x", ""+config.textXOffset());
		        textElement.setAttributeNS(null, "y", String.valueOf(17));
		        textElement.setAttributeNS(null, "font-size", String.valueOf(16));
		        textElement.setAttributeNS(null, "font-family", "monospace");
		        if(type.equals(IRenderable.VARIABLE_ENUM))
		        	textElement.setAttributeNS(null, "fill", "white");	
		        textElement.setTextContent(value);
		
		        root.appendChild(textElement);
			} else {
				textElement = (Element) document.getElementsByTagName("text").item(0);
				textElement.setAttributeNS(null, "x", ""+config.textXOffset());
				textElement.setTextContent(value);
			}
	        Rectangle2D bb = ctx.getGraphicsNode(path == null?rect:path).getBounds();
	        if(end != null)
				end.setAttribute("transform", "translate("+ (bb.getWidth() - config.endOffset()) + ", 0)");
			root.setAttributeNS(null, "width", ""  + bb.getWidth());
			root.setAttributeNS(null, "height", "" + bb.getHeight());
			root.setAttributeNS(null, "viewBox", "0 0 " + bb.getWidth() + " " + bb.getHeight());
//			root.removeAttribute("width");
//			root.removeAttribute("height");
//			root.removeAttribute("viewBox");
			
//	        if(type.equals(IRenderable.VARIABLE_ENUM)) {
//		        System.out.println(document.hashCode());
//		        try (FileWriter writer = new FileWriter(new File(document.hashCode() + ".svg"))) {
//		            DOMUtilities.writeDocument(document, writer);
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
//	        }
		}
		return document;
	}
}
