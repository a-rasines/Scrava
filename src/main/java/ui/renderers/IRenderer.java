package ui.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.anim.dom.SVGOMSVGElement;
import org.apache.batik.anim.dom.SVGOMTextElement;
import org.apache.batik.bridge.BridgeContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGRect;

import clickable.BlockClickable;
import domain.models.interfaces.Clickable;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import parsers.SVGReader;

public interface IRenderer extends Serializable {
	public static final int FONT_WIDTH = 28;
	public static final int FONT_WIDTH_SVG = 10;
	
	public static interface IRenderable extends Serializable, Translatable {
		
		public static final String VARIABLE_ANY = "{{var}}";
		public static final String VARIABLE_NUM = "{{numvar}}";
		public static final String VARIABLE_STR = "{{strvar}}";
		public static final String VARIABLE_BOOL = "{{bvar}}";
		public static final String VARIABLE_ENUM = "{{evar}}";
		
		public IRenderer getRenderer();
		
	}
	
	public static Font font = initFont();
	private static Font initFont() {
		 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    try (InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/monofonto rg.otf")) {
	            Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
	            ge.registerFont(font);
	            return font;
	        } catch (FontFormatException | IOException ex) {
	            ex.printStackTrace();
	            return null;
	        }
	}
	
	/**
	 * Gets the {@link java.awt.image.BufferedImage BufferedImage} of the texture in that relative path from the resources' folder
	 * @param res path to the texture
	 * @return the texture as a BufferedImage
	 */
	public static BufferedImage getRes(String res) {
		try {
			return ImageIO.read(ClassLoader.getSystemClassLoader().getResource(res));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public default void background(BufferedImage rendered, int height, int start, int width) {
		for(int y = 0; y < height; y++) {
			int color;
			try {
				color = rendered.getRGB(start - 1, y);
			} catch(ArrayIndexOutOfBoundsException e) {
				throw e;
			}
			for(int x = 0; x < width; x++)
				rendered.setRGB(x + start, y, color);
				
		} 
	}
	public default void background(BufferedImage rendered, int startX, int width, int startY, int height) {
		for(int y = startY; y < height + startY; y++) {
			int color;
			try {
				color = rendered.getRGB(startX - 1, y);
			} catch(ArrayIndexOutOfBoundsException e) {
				throw e;
			}
			for(int x = 0; x < width; x++)
				rendered.setRGB(x + startX, y, color);
				
		} 
	}
	
	public default void verticalBackground(BufferedImage rendered, int startX, int width, int startY, int height) {
		for(int x = startX; x < startX + width; x++) {
			int color;
			try {
				color = rendered.getRGB(x, startY - 1);
			} catch(ArrayIndexOutOfBoundsException e) {
				throw e;
			}
			for(int y = startY; y < startY + height; y++) {
				rendered.setRGB(x, y, color);
			}
		} 
	}
	/**
	 * Clones the top part to the bottom part in vertical mirror
	 * @param rendered image where to paint the mirrored clone
	 * @param startX x where the original starts (top)
	 * @param startY y where the original starts (top)
	 * @param width width of the mirror
	 * @param height height of the mirror
	 * @param objHeight y where the mirrored part starts (bottom)
	 */
	public default void verticalClone(BufferedImage rendered, int startX, int startY, int width, int height, int objHeight) {
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				try {
					rendered.setRGB(startX + x, objHeight - startY - y, rendered.getRGB(startX + x, startY + y));
				} catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("start    " + startX + " " + startY + 
									   "\noffset     " + x + " " + y + 
									   "\nrendered " + rendered.getWidth() + " " + objHeight +
									   "\nsize     " + width + " " + height + 
									   "\nset      " + (rendered.getWidth() - startX - x - 1) + " " + (rendered.getHeight() - startY - y - 1) + 
									   "\nget      " + (startX + x) + " " + (startY + y));
				}
				
	}
	public default void leftClone(BufferedImage rendered, int startX, int startY, int width, int height, int objWidth) {
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				try {
					rendered.setRGB(objWidth - startX - x - 1, startY + y, rendered.getRGB(startX + x, startY + y));
				} catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("start    " + startX + " " + startY + 
									   "\noffset     " + x + " " + y + 
									   "\nrendered " + objWidth + " " + rendered.getHeight() +
									   "\nsize     " + width + " " + height + 
									   "\nset      " + (rendered.getWidth() - startX - x - 1) + " " + (rendered.getHeight() - startY - y - 1) + 
									   "\nget      " + (startX + x) + " " + (startY + y));
				}
				
	}
	
	public default BufferedImage replaceColor(BufferedImage bi, Color newColor) {
		return replaceColor(bi, newColor.getRGB());
	}
	public default BufferedImage replaceColor(BufferedImage bi, int newColor) {
		for(int x = 0; x < bi.getWidth(); x++)
			for(int y = 0; y < bi.getHeight(); y++) {
				if(bi.getRGB(x, y) != 0xffffffff && (bi.getRGB(x, y) & 0xff000000) == 0xff000000)
					bi.setRGB(x, y, newColor);
			}
		return bi;
	}
	
	public default BufferedImage clone(BufferedImage original) {
		return new BufferedImage(original.getColorModel(), original.copyData(null), original.getColorModel().isAlphaPremultiplied(), null);
	}
	
	/**
	 * Returns the visual representation of the renderable made by hand
	 * @return
	 */
	public BufferedImage getRenderable();
	
	/**
	 * Returns the visual representation of the renderable using an SVG document
	 * @return
	 */
	public SVGDocument getRenderableSVG();
	
	/**
	 * Returns the rendered block
	 * @return
	 */
	public Translatable getBlock();
	/**
	 * Updates the visual representation to keep up with changes
	 */
	public void update();
	
	public double getHeight();
	
	public double getWidth();
	
	/**
	 * Replaces the pixels from x to x + w
	 * @param x first x of the patch
	 * @param y first y of the new image
	 * @param h height of the patch
	 * @param w width of the patch
	 * @param newI new image to put on the 
	 */
//	public void patch(int x, int y, int h, int w, BufferedImage newI);
	
	/**
	 * Returns the tree of renderables nested inside 
	 * @return
	 */
	public List<IRenderer> getChildren();
	
	public Clickable getClickable();
	
	public void delete();
	
	public static interface DragableRenderer extends IRenderer{
		
		public static final double SUBSCALE = 1.25;
		/**
		 * Gets the local x position of the rendered object
		 * @return
		 */
		public int getX();
		
		/**
		 * Gets the global x position of the rendered object
		 * @return
		 */
		public default int getGlobalX() {
			if(getClickable().getParent() == null) {
				return getX();
			} else {
				return getX() + ((DragableRenderer)getClickable().getParent().getRenderer()).getGlobalX();
			}
		}
		
		/**
		 * Gets the local y position of the rendered object
		 * @return
		 */
		public int getY();
		
		/**
		 * Gets the global y position of the rendered object
		 * @return
		 */
		public default int getGlobalY() {
			if(getClickable().getParent() == null) {
				return getY();
			} else {
				return getY() + ((DragableRenderer)getClickable().getParent().getRenderer()).getGlobalY();
			}
		}
		
		/**
		 * Changes the X and Y values of the object
		 * @param x
		 * @param y
		 */
		public void moveTo(int x, int y);
		/**
		 * Shifts the values of X and Y of the object
		 * @param x
		 * @param y
		 */
		public void move(int x, int y);
		
		@Override
		public BlockClickable getClickable();
		
		public default SVGDocument addText(String text) {
	        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
	        SVGOMSVGElement root = (SVGOMSVGElement)document.getDocumentElement();
	        List<Element> elements = new LinkedList<>();
	        String[] parts = text.split("\\{\\{");
			int vari = 0;
			double h = 0;
			BridgeContext ctx = SVGReader.build(document);
			for(String part : parts) {
				if(part.split(" ")[0].contains("}}")) {
					String[] divided = part.split("}}");
					IRenderer rend = getChildren().get(vari++);
					SVGDocument doc = rend.getRenderableSVG();
					Element newChild = document.createElementNS("http://www.w3.org/2000/svg", "g");
					newChild.setAttribute("transform", doc.getDocumentElement().getAttribute("transform"));
					NodeList nl = document.importNode(doc.getDocumentElement(), true).getChildNodes();
					int len = nl.getLength(); //for some reason it decreases each iteration
					for(int i = 0; i < len; i++) {
						Node n = nl.item(0);
						newChild.appendChild(n);
					}
					//child.setAttribute("style", child.getAttribute("style") + ";position:relative");
					root.appendChild(newChild);
					h = Math.max(h, SVGReader.getBoundingBox(newChild).getHeight());
					if(divided.length > 1 && divided[1].strip().length() > 0) {
						SVGOMTextElement textElement = (SVGOMTextElement) document.createElementNS("http://www.w3.org/2000/svg", "text");
				        textElement.setAttributeNS(null, "textAnchor", "middle");
				        textElement.setAttributeNS(null, "dy", "0.3em");
				        textElement.setAttributeNS(null, "style", "fill:white; position:relative");
				        textElement.setAttributeNS(null, "font-weight", "bold");
				        textElement.setAttributeNS(null, "font-size", String.valueOf(16));
				        textElement.setAttributeNS(null, "font-family", "monospace");
				        textElement.setTextContent(divided[1]);
				        
				        root.appendChild(textElement);
				        elements.add(textElement);

					}
				} else if(!part.strip().equals("")){
					SVGOMTextElement textElement = (SVGOMTextElement) document.createElementNS("http://www.w3.org/2000/svg", "text");

			        textElement.setAttributeNS(null, "font-size", String.valueOf(16));
			        textElement.setAttributeNS(null, "textAnchor", "middle");
			        textElement.setAttributeNS(null, "dy", "0.3em");
			        textElement.setAttributeNS(null, "style", "fill:white; position:relative");
			        textElement.setAttributeNS(null, "font-family", "monospace");
			        textElement.setAttributeNS(null, "font-weight", "bold");
			        textElement.setAttributeNS(null, "dx", "10");
			        textElement.setTextContent(part);

			        root.appendChild(textElement);
			        elements.add(textElement);
				}
			}
			float len = 0;
			int child = 0;
			h = Math.max(h, SVGReader.getBoundingBox(root).getHeight());
			for(Element e = root.getFirstElementChild(); e != null; e = (Element)e.getNextSibling()) {
				if(e instanceof SVGLocatable ge) {
					SVGRect bb = ge.getBBox();
					if(bb == null) {
						System.out.println(ge.getClass());
						continue;
					}
					double w = bb.getWidth();
					double x0 = 0;
					if(e instanceof SVGOMTextElement te) {
						w = FONT_WIDTH_SVG *  te.getTextContent().length();
						x0 = (te.getTextContent().length() - te.getTextContent().stripLeading().length()) * FONT_WIDTH_SVG;
					} else {
						Valuable<?> ch = ((VariableHolder)getBlock()).getVariableAt(child);
						e.setAttributeNS(null, "id", getBlock().hashCode() + "_"+child);
						e.setAttributeNS(null, "block", ch.toString());
						ch.getRenderer().getClickable().setPosition((int)bb.getX(), (int)bb.getY());
						child++;
					}
					String[] transform = e.getAttribute("transform").split(" ");
					transform: {
						for(int i = 0; i < transform.length; i++)
							if(transform[i].startsWith("translate")) {
								if(!(e instanceof SVGOMTextElement))
									transform[i] = "translate(" + (x0 + len) + ", "+ String.valueOf((h - bb.getHeight()) / 2) + ")";
								else
									transform[i] = "translate(" + (x0 + len) + ", " + String.valueOf(h / 2) + ")";
								e.setAttribute("transform", String.join(" ", transform));
								break transform;
							}
						if(!(e instanceof SVGOMTextElement))
							e.setAttribute("transform", String.join(" ", transform) + "translate(" + (x0 + len) + ", "+ String.valueOf((h - bb.getHeight()) / 2) + ")");
						else
							e.setAttribute("transform", String.join(" ", transform) + "translate(" + (x0 + len) + ", " + String.valueOf(h / 2) + ")");
					}
					len += w;
					h = Math.max(h, ctx.getGraphicsNode(e).getBounds().getHeight());
				}
			}
			root.setAttributeNS(null, "width", Math.round((len + 1.75) * 100) / 100 + "");
			root.setAttributeNS(null, "height", h + "");
			root.setAttributeNS(null, "viewBox", "0 0 " + Math.round((len + 1.75) * 100) / 100 + " " + h);
			return document;
		}
	}
}