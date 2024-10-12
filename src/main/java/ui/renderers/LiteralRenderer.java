package ui.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import clickable.BlockClickable;
import clickable.LiteralClickable;
import domain.models.interfaces.Clickable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import domain.values.AbstractLiteral;
import domain.values.BooleanLiteral;
import domain.values.EnumLiteral;
import parsers.SVGReader;
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
	private boolean updateSVG = false;
	private transient Element element = null;
	private transient SVGConfig config = null;
	private transient BridgeContext ctx = null;
	private transient GraphicsNode gn = null;
	
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
			SVGDocument doc = (SVGDocument) getRenderableSVG().getOwnerDocument();
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
		if(clickable.getParent() != null)
			clickable.getParent().getRenderer().update();
		getRenderableSVG();
		
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
		if(getClickable().getParent() != null)
			((VariableHolder)getClickable().getParent().getBlock()).removeVariable(getBlock());
		element.getParentNode().removeChild(element);
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
	public LiteralRenderer toDocument(SVGDocument doc) {
		if(element == null) getRenderableSVG();
		ctx = SVGReader.build(doc);
		element = (Element)doc.importNode(element, true);
		doc.getDocumentElement().appendChild(element);
		return this;
	}
	
	
	private transient boolean isRect = false;
	protected void generateSVG() {
		////////////////////////////////////////////////////////////////
		//							INITIAL CONFIG
		////////////////////////////////////////////////////////////////
		config = SVGConfig.getConfig(type);
		SVGDocument document = config.document();
		ctx = SVGReader.build(document);
		element = setupSVG(document);
		int blockHashCode = getBlock().hashCode();
		
		
		////////////////////////////////////////////////////////////////
		//							INSERT CONTENT
		////////////////////////////////////////////////////////////////
		String value = type.equals(IRenderable.VARIABLE_ENUM)? ((EnumLiteral<?>)getBlock()).name() : getBlock().value().toString();
		Element elem = document.getElementById("resize_path");
		Element end = null;
		isRect = elem == null;
		
		////////////////////////////////////////////////////////////////
		//							ADAPT SIZE OF CONTAINER
		////////////////////////////////////////////////////////////////
		if(isRect) {
		
			elem = document.getElementById("resize_rect");
			elem.setAttribute("id", blockHashCode + "_resize_rect");
			elem.setAttributeNS(null, "width", config.wOffset() + value.length() * FONT_WIDTH_SVG + "");
			end = document.getElementById("end");
			if(end != null)
				end.setAttribute("id", blockHashCode + "_end");
		
		} else {
			
			elem.setAttribute("id", blockHashCode + "_resize_path");
			String[] commands = elem.getAttribute("d").split(" ");
			StringBuilder newD = new StringBuilder();
			String lastCommand = "";
			for (String command : commands) {
				if (command.matches("[MLHVCSQTAZmlhvcsqtaz]")) {
					lastCommand = command;
					newD.append(command).append(" ");
				} else {
					if(lastCommand.equals("h"))
						newD.append(value.length() * FONT_WIDTH_SVG);
					else if (lastCommand.toLowerCase().equals("m")) {
						lastCommand = "";
						newD.append(config.wOffset() + ",0");
					} else
						newD.append(command);
					newD.append(" ");
				}
			}
			elem.setAttribute("d", newD.toString().trim());
		}

		////////////////////////////////////////////////////////////////
		// INSERT TEXT
		////////////////////////////////////////////////////////////////

		Element textElement;
		textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");

		textElement.setAttributeNS(null, "x", "" + config.textXOffset());
		textElement.setAttributeNS(null, "y", String.valueOf(17));
		textElement.setAttributeNS(null, "font-size", String.valueOf(16));
		textElement.setAttributeNS(null, "font-weight", "bold");
		textElement.setAttributeNS(null, "font-family", "monospace");
		textElement.setAttribute("id", blockHashCode + "_text");
		if (type.equals(IRenderable.VARIABLE_ENUM))
			textElement.setAttributeNS(null, "fill", "white");
		textElement.setTextContent(value);

		element.appendChild(textElement);
		Rectangle2D bb = ctx.getGraphicsNode(elem).getBounds();
		if (end != null)
			end.setAttribute("transform", "translate(" + (bb.getWidth() - config.endOffset()) + ", 0)");
		element.setAttributeNS(null, "width", "" + (bb.getWidth() + 1.25));
		element.setAttributeNS(null, "height", "" + bb.getHeight());
		element.setAttributeNS(null, "viewBox", "0 0 " + bb.getWidth() + " " + bb.getHeight());
	}
	
	protected void updateSVG() {
		System.out.println("Update SVG");
		String value = type.equals(IRenderable.VARIABLE_ENUM)? ((EnumLiteral<?>)getBlock()).name() : getBlock().value().toString();
		Element elem;
		Element end = null;
		Document doc = element.getOwnerDocument();
		int blockHashCode = getBlock().hashCode();
		
		if(isRect) {
			System.out.println(blockHashCode + "_resize_rect");
			System.out.println(doc.toString());
			elem = doc.getElementById(blockHashCode + "_resize_rect");
			elem.setAttributeNS(null, "width", config.wOffset() + value.length() * FONT_WIDTH_SVG + "");
			end = doc.getElementById(blockHashCode + "_end");
		
		} else {
			
			elem = doc.getElementById(blockHashCode + "_resize_path");
			String[] commands = elem.getAttribute("d").split(" ");
			StringBuilder newD = new StringBuilder();
			String lastCommand = "";
			for (String command : commands) {
				if (command.matches("[MLHVCSQTAZmlhvcsqtaz]")) {
					lastCommand = command;
					newD.append(command).append(" ");
				} else {
					if(lastCommand.equals("h"))
						newD.append(value.length() * FONT_WIDTH_SVG);
					else if (lastCommand.toLowerCase().equals("m")) {
						lastCommand = "";
						newD.append(config.wOffset() + ",0");
					} else
						newD.append(command);
					newD.append(" ");
				}
			}
			elem.setAttribute("d", newD.toString().trim());
		}
		doc.getElementById(blockHashCode + "_text").setTextContent(value);
		if(ctx.getGraphicsNode(elem) == null)
			ctx = SVGReader.build(doc);
		Rectangle2D bb = ctx.getGraphicsNode(elem).getBounds();
		if (end != null)
			end.setAttribute("transform", "translate(" + (bb.getWidth() - config.endOffset()) + ", 0)");
		element.setAttributeNS(null, "width", "" + (bb.getWidth() + 1.25));
		element.setAttributeNS(null, "height", "" + bb.getHeight());
		element.setAttributeNS(null, "viewBox", "0 0 " + bb.getWidth() + " " + bb.getHeight());
	}
	
	@Override
	public Element getRenderableSVG() {
		if(updateSVG) {
			updateSVG();
		} else if (element == null)
			generateSVG();
		return element;
		
//		if(element == null || updateSVG) {
//			needsUpdate = false;
//			updateSVG = false;
//			boolean isNewDocument = false;
//			if(element == null) {
//				config = SVGConfig.getConfig(type);
//				SVGDocument document = config.document();
//				ctx = SVGReader.build(document);
//				isNewDocument = true;
//			}
//			SVGOMSVGElement root = (SVGOMSVGElement)document.getDocumentElement();
//			String value = getBlock().value().toString();
//			if(type.equals(IRenderable.VARIABLE_ENUM))
//				value = ((EnumLiteral<?>)getBlock()).name();
//			SVGPathElement path = (SVGPathElement) document.getElementById("resize_path");
//			SVGOMRectElement rect = null;
//			Element end = null;
//			if(path != null) {
//				String[] commands = path.getAttribute("d").split(" ");
//		        StringBuilder newD = new StringBuilder();
//		        String lastCommand = "";
//		        for (String command : commands) {
//		            if (command.matches("[MLHVCSQTAZmlhvcsqtaz]")) {
//		            	lastCommand = command;
//		                newD.append(command).append(" ");
//		            } else {
//		            	if(lastCommand.equals("h"))
//		            		newD.append(value.length() * FONT_WIDTH_SVG);
//		            	else if(lastCommand.toLowerCase().equals("m")) {
//		            		lastCommand = "";
//		            		newD.append(config.wOffset() + ",0");
//		            	} else
//		            		newD.append(command);
//		            	newD.append(" ");
//		            }
//		        }
//		        path.setAttribute("d", newD.toString().trim());
//			} else {
//				rect = (SVGOMRectElement)document.getElementById("resize_rect");
//				rect.setAttributeNS(null, "width", config.wOffset() + value.length() * FONT_WIDTH_SVG + "");
//				end = document.getElementById("end");
//			}
//	        Element textElement;
//	        if(isNewDocument) {
//		        textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
//		        
//		        textElement.setAttributeNS(null, "x", ""+config.textXOffset());
//		        textElement.setAttributeNS(null, "y", String.valueOf(17));
//		        textElement.setAttributeNS(null, "font-size", String.valueOf(16));
//		        textElement.setAttributeNS(null, "font-weight", "bold");
//		        textElement.setAttributeNS(null, "font-family", "monospace");
//		        if(type.equals(IRenderable.VARIABLE_ENUM))
//		        	textElement.setAttributeNS(null, "fill", "white");	
//		        textElement.setTextContent(value);
//		
//		        root.appendChild(textElement);
//			} else {
//				textElement = (Element) document.getElementsByTagName("text").item(0);
//				textElement.setAttributeNS(null, "x", ""+config.textXOffset());
//				textElement.setTextContent(value);
//			}
//	        Rectangle2D bb = ctx.getGraphicsNode(path == null?rect:path).getBounds();
//	        if(end != null)
//				end.setAttribute("transform", "translate("+ (bb.getWidth() - config.endOffset()) + ", 0)");
//			root.setAttributeNS(null, "width", ""  + (bb.getWidth() + 1.25));
//			root.setAttributeNS(null, "height", "" + bb.getHeight());
//			root.setAttributeNS(null, "viewBox", "0 0 " + bb.getWidth() + " " + bb.getHeight());
//
//		}
//		return document;
	}
}
