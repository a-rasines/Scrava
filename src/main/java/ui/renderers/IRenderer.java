package ui.renderers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.batik.anim.dom.SVGOMRectElement;
import org.apache.batik.anim.dom.SVGOMSVGElement;
import org.apache.batik.anim.dom.SVGOMTextElement;
import org.apache.batik.dom.AbstractElement;
import org.w3c.dom.Document;
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
	
	public default BufferedImage clone(BufferedImage original) {
		return new BufferedImage(original.getColorModel(), original.copyData(null), original.getColorModel().isAlphaPremultiplied(), null);
	}
	
	/**
	 * Returns the visual representation of the renderable as a BufferedImage
	 * @return
	 */
	public BufferedImage getRenderable();
	
	/**
	 * Returns the visual representation of the renderable using an SVG document
	 * @return
	 */
	public Element getRenderableSVG();
	
	/**
	 * Moves the SVG renderable (got using {@link IRenderer#getRenderableSVG()}) to the {@link org.w3c.dom.svg.SVGDocument SVGDocument} specified. <br>
	 * <b>Important:</b> It does not append it to the document, it only registers the document as the new owner
	 * @param doc the document the element will be part of
	 * @return itself
	 */
	public IRenderer toDocument(SVGDocument doc);
	
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
		
		public default void addText(String text, AbstractElement parent) {
	        List<Element> elements = new LinkedList<>();
	        String[] parts = text.split("\\{\\{");
			int vari = 0;
			double h = 0;
			for(String part : parts) {
				if(part.split(" ")[0].contains("}}")) {
					String[] divided = part.split("}}");
					IRenderer rend = getChildren().get(vari++);
//					SVGOMGElement newChild = (SVGOMGElement)parent.getOwnerDocument().createElementNS("http://www.w3.org/2000/svg", "g");
					IRenderer.insertBlockInsideElement(parent, rend);
//					parent.appendChild(newChild);
					if(parent instanceof SVGLocatable l) {
						if (l.getBBox() == null)
							SVGReader.build(parent.getOwnerDocument());
						h = Math.max(h, l.getBBox().getHeight());
					}
					if(divided.length > 1 && divided[1].strip().length() > 0) {
						SVGOMTextElement textElement = IRenderer.textOf(parent.getOwnerDocument(), divided[1]);
						parent.appendChild(textElement);
				        elements.add(textElement);

					}
				} else if(!part.strip().equals("")){
					SVGOMTextElement textElement = IRenderer.textOf(parent.getOwnerDocument(), part);
					textElement.setAttributeNS(null, "dx", "10");
			        parent.appendChild(textElement);
			        elements.add(textElement);
				}
			}
			if(parent instanceof SVGLocatable l) {
				h = Math.max(h, l.getBBox().getHeight());
			}
			assemble(parent, h);
		}
	}
	
	/**
	 * Corrects the position of the elements in the root element by putting them in horizontal layout
	 * @param root the parent node
	 * @param h the height of the block (to center them vertically)
	 */
	public default void assemble(AbstractElement root, double h) {
		float len = 0;
		int child = 0;
		Element background = null;
		for(Element e = root.getFirstElementChild(); e != null; e = (Element)e.getNextSibling()) {
			System.out.println(e);
			if(e.getAttribute("id").startsWith("resize_")) {
				background = e;
				continue;
			}
			if(e instanceof SVGLocatable ge) {
				SVGRect bb = ge.getBBox();
				if(bb == null)
					continue;
				double w = bb.getWidth();
				double x0 = 0;
				if(e instanceof SVGOMTextElement te) {
					w = FONT_WIDTH_SVG *  te.getTextContent().length();
					x0 = (te.getTextContent().length() - te.getTextContent().stripLeading().length()) * FONT_WIDTH_SVG;
				} else if(e.getAttribute("id").strip().endsWith("_root")){
					Valuable<?> ch = ((VariableHolder)getBlock()).getVariableAt(child);
					e.setAttributeNS(null, "block", String.valueOf(ch.hashCode()));
					ch.getRenderer().getClickable().setPosition((int)bb.getX(), (int)bb.getY());
					child++;
				}
				String[] transform = e.getAttribute("transform").split(" ");
				transform: {
					for(int i = 0; i < transform.length; i++)
						if(transform[i].startsWith("translate")) {
							if(!(e instanceof SVGOMTextElement))
								transform[i] = "translate(" + (x0 + len) + ","+ String.valueOf((h - bb.getHeight()) / 2) + ")";
							else
								transform[i] = "translate(" + (x0 + len) + "," + String.valueOf(h / 2) + ")";
							e.setAttribute("transform", String.join(" ", transform));
							break transform;
						}
					if(!(e instanceof SVGOMTextElement))
						e.setAttribute("transform", String.join(" ", transform) + "translate(" + (x0 + len) + ","+ String.valueOf((h - bb.getHeight()) / 2) + ")");
					else
						e.setAttribute("transform", String.join(" ", transform) + "translate(" + (x0 + len) + "," + String.valueOf(h / 2) + ")");
				}
				len += w;
				if(e instanceof SVGLocatable l)
					h = Math.max(h, l.getBBox().getHeight());
				
			}
		}
		background.setAttributeNS(null, "width", String.valueOf(Math.round((len + 7.5) * 100) / 100));
		background.setAttributeNS(null, "height", h + "");
	}
	
	public static void insertBlockInsideElement(Element parent, IRenderer block) {
		Node nn = parent.getOwnerDocument().importNode(block.getRenderableSVG(), true);
		parent.appendChild(nn);
	}
	
	public default Element setupSVG(Document doc) {
		SVGOMSVGElement root = (SVGOMSVGElement)doc.getDocumentElement();
		Element element = doc.createElementNS("http://www.w3.org/2000/svg", "g");
		System.out.println(element);
		element.setAttribute("id", getBlock().hashCode() + "_root");
		doc.importNode(element, true);
		NodeList nl = root.getChildNodes();
		
		for(;nl.getLength() > 0;)
			element.appendChild(nl.item(0));
		root.appendChild(element);
		
		return element;
	}
	
	private static SVGOMTextElement textOf(Document doc, String str) {
		SVGOMTextElement textElement = (SVGOMTextElement) doc.createElementNS("http://www.w3.org/2000/svg", "text");

        textElement.setAttributeNS(null, "font-size", String.valueOf(16));
        textElement.setAttributeNS(null, "textAnchor", "middle");
        textElement.setAttributeNS(null, "dy", "0.3em");
        textElement.setAttributeNS(null, "style", "fill:white; position:relative");
        textElement.setAttributeNS(null, "font-family", "monospace");
        textElement.setAttributeNS(null, "font-weight", "bold");
        textElement.setTextContent(str);
        return textElement;
	}
}