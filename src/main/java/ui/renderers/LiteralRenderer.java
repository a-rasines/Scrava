package ui.renderers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import clickable.BlockClickable;
import clickable.LiteralClickable;
import domain.models.interfaces.Clickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import domain.values.EnumLiteral;
import parsers.SVGReader;
import ui.components.BlockPanel;

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
	private SVGDocument document = null;
	private SVGConfig config;
	
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
		if(rendered == null)
			return rendered = SVGReader.toBufferedImage(getRenderableSVG());
		else
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

//	public static void main(String[] args) {
//		
//	 	LiteralRenderer sampleImage = new LiteralRenderer(new NumberLiteral<Double>(1.21, null), IRenderable.VARIABLE_STR, new BlockClickable(null, null));
//	 	sampleImage.opacity = 0.5f;
//	 	
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Image Display");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(500, 500);
//
//            JPanel panel = new JPanel() {
//				private static final long serialVersionUID = -94843536618228336L;
//
//				@Override
//                protected void paintComponent(Graphics g) {
//					g.setColor(Color.black);
//                    g.drawRect(0, 0, 500, 500);
//                    g.drawImage(sampleImage.getRenderable(), 100, 100, this);
//                }
//            };
//            frame.getContentPane().add(panel);
//
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//	}

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
	private record SVGConfig(SVGDocument document, double wOffset, double textXOffset, double endOffset) {
		
		public SVGDocument document() {
			return SVGReader.clone(document);
		}
		
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
			IRenderable.VARIABLE_BOOL, new SVGConfig(booleanDoc, 9.2229801, 7, 0),
			IRenderable.VARIABLE_STR, new SVGConfig(stringDoc, 12, 5, 0),
			IRenderable.VARIABLE_ENUM, new SVGConfig(enumDoc, 25, 5, 12.35)
		);
	}
	
	public static final int FONT_WIDTH_PATH = 7;
	public static final double FONT_WIDTH_RECT = 7.75;
	@Override
	public SVGDocument getRenderableSVG() {
		if(document == null || updateSVG) {
			updateSVG = false;
			boolean isNewDocument = false;
			if(document == null) {
				config = CONFIG_MAP.get(type);
				document = config.document();
				isNewDocument = true;
			}
			Element root = document.getDocumentElement();
			String value = getBlock().value().toString();
			if(type.equals(IRenderable.VARIABLE_ENUM))
				value = ((EnumLiteral<?>)getBlock()).name();
			String height = root.getAttribute("height");
			double width;
			if(document.getElementsByTagName("rect").item(0) != null)
				width = (2 + config.wOffset() + value.length() * FONT_WIDTH_RECT);
			else
				width = (2 + config.wOffset() * 2 + value.length() * FONT_WIDTH_PATH);
			root.setAttribute("width", ""+width+"mm");
			root.setAttributeNS(null, "height", height);
			System.out.println("0 0 " + width + " " + height.replaceAll("[^0-9.]", ""));
			root.setAttributeNS(null, "viewBox", "0 0 " + width + " " + height.replaceAll("[^0-9.]", ""));
			
			Element path = (Element) document.getElementById("resize_path");
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
		            		newD.append(value.length() * FONT_WIDTH_PATH);
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
				Element rect = document.getElementById("resize_rect");
				rect.setAttributeNS(null, "width", config.wOffset() + value.length() * FONT_WIDTH_RECT + "");
				Element end = document.getElementById("end");
				if(end != null)
					end.setAttribute("transform", "translate("+ (width - config.endOffset()) + ", 0)");
			}
	        Element textElement;
	        if(isNewDocument) {
		        textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
		        
		        textElement.setAttributeNS(null, "x", ""+config.textXOffset());
		        textElement.setAttributeNS(null, "y", String.valueOf(17));
		        textElement.setAttributeNS(null, "font-size", String.valueOf(16));
		        textElement.setAttributeNS(null, "font-family", "monofonto");
		        if(type.equals(IRenderable.VARIABLE_ENUM))
		        	textElement.setAttributeNS(null, "fill", "white");	
		        textElement.setTextContent(value);
		
		        root.appendChild(textElement);
			} else {
				textElement = (Element) document.getElementsByTagName("text").item(0);
				textElement.setAttributeNS(null, "x", ""+config.textXOffset());
				textElement.setTextContent(value);
			}
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
