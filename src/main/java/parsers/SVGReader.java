package parsers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.anim.dom.SVGOMSVGElement;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGRect;

import debug.DebugOut;
import domain.blocks.operators.RandomOperator;
import ui.renderers.SimpleBlockRenderer;

public class SVGReader {

	public static SVGDocument readSVG(String path) throws IOException, URISyntaxException {
		String uri = ClassLoader.getSystemClassLoader().getResource(path).toURI().toString();
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
		SVGDocument document = (SVGDocument) factory.createDocument(uri);
        return document;
	}
	/**
	 * Builds the document to be able to extract metadata from elements
	 * @param document
	 */
	public static BridgeContext build(SVGDocument document) {
		BridgeContext context = new BridgeContext(new UserAgentAdapter());
		context.setDynamicState(BridgeContext.DYNAMIC);
		new GVTBuilder().build(context, document);
		return context;
	}
	public static void main(String[] args) throws URISyntaxException, IOException {
 //       SVGDocument document = new MoveXBlock(null).getRenderer().getRenderableSVG();
        DebugOut.setup();
		JFrame frame = new JFrame("SVG Display");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

//        JSVGCanvas canvas = new JSVGCanvas();
//        frame.getContentPane().add(canvas, BorderLayout.CENTER);
//        canvas.setSVGDocument(document);
//        LiteralRenderer r = (LiteralRenderer) new NumberLiteral<Integer>(123450, null).getRenderer();
//        LiteralRenderer r = (LiteralRenderer) new BooleanLiteral(true, null).getRenderer();
//        LiteralRenderer r = (LiteralRenderer) new StringLiteral("123456789987654321", null).getRenderer();
//        LiteralRenderer r = (LiteralRenderer) new EnumLiteral<Integer>(KeyEventBlock.KEY_MAP, null).getRenderer();

//        SimpleBlockRenderer r = (SimpleBlockRenderer) new AppendOperator().getRenderer();
//        SimpleBlockRenderer r = (SimpleBlockRenderer) new AddOperator().getRenderer();
        SimpleBlockRenderer r = (SimpleBlockRenderer) new RandomOperator().getRenderer();
        SVGDocument doc = r.getRenderableSVG();
        System.out.println(doc.hashCode());
        try (FileWriter writer = new FileWriter(new File(doc.hashCode() + ".svg"))) {
            DOMUtilities.writeDocument(doc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        BufferedImage bi = SVGReader.toBufferedImage(doc);
        Graphics g = bi.getGraphics();
        g.setColor(Color.blue);
        build(doc);
        SVGRect bbox = ((SVGOMSVGElement) doc.getDocumentElement()).getBBox();
       
        System.out.println(bbox.getX() + " " + bbox.getY() + " " + bbox.getWidth() + " " + bbox.getHeight());
        g.drawRect((int)bbox.getX(), (int)bbox.getY(), (int)bbox.getWidth(), (int)bbox.getHeight());
        g.drawLine((int)(bbox.getWidth()/2), 0, (int)(bbox.getWidth()/2), (int)bbox.getHeight());
        frame.add(new JLabel(new ImageIcon(bi)), BorderLayout.CENTER);
        frame.setVisible(true);
    }

	public static BufferedImage toBufferedImage(SVGDocument document) {
		BufferedImageTranscoder imageTranscoder = new BufferedImageTranscoder();
		try {
			TranscoderInput input = new TranscoderInput(document);
			imageTranscoder.transcode(input, null);
			return imageTranscoder.getBufferedImage();
		} catch (TranscoderException e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	private static class BoundingBox implements SVGRect {
//		private float x = Float.MAX_VALUE;
//		private float y = Float.MAX_VALUE;
//		
//		private float w = Float.MIN_VALUE;
//		private float h = Float.MIN_VALUE;
//		
//		
//
//		public BoundingBox(float x, float y, float w, float h) {
//			super();
//			this.x = x;
//			this.y = y;
//			this.w = w;
//			this.h = h;
//		}
//		@Override public float getHeight() { return h; }
//		@Override public float getWidth() { return w; }
//
//		@Override public float getX() {	return x; }
//		@Override public float getY() {	return y; }
//
//		@Override public void setHeight(float arg0) throws DOMException { h = arg0; }
//		@Override public void setWidth(float arg0) throws DOMException { w = arg0; }
//
//		@Override public void setX(float arg0) throws DOMException { x = arg0; }
//		@Override public void setY(float arg0) throws DOMException { y = arg0; }
//		
//		@Override public String toString() { return "BoundingBox [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]"; }
//	}

	private static class BufferedImageTranscoder extends ImageTranscoder {
		private BufferedImage bufferedImage;

		@Override
        public BufferedImage createImage(int width, int height) {
        	return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}

		@Override
		public void writeImage(BufferedImage img, TranscoderOutput output) throws TranscoderException {
			this.bufferedImage = img;
		}

		public BufferedImage getBufferedImage() {
			return bufferedImage;
		}
	}
	
	public static SVGDocument clone(SVGDocument orig) {
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
        String namespaceURI = SVGDOMImplementation.SVG_NAMESPACE_URI;
        SVGDocument clonedDocument = (SVGDocument) domImpl.createDocument(namespaceURI, "svg", null);
        
        Element originalRoot = orig.getDocumentElement();
        Element clonedRoot = clonedDocument.getDocumentElement();

        for (int i = 0; i < originalRoot.getAttributes().getLength(); i++) {
            Node attr = originalRoot.getAttributes().item(i);
            clonedRoot.setAttributeNS(attr.getNamespaceURI(), attr.getNodeName(), attr.getNodeValue());
        }

        for (Node child = originalRoot.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element) {
                Element clonedChild = (Element) clonedDocument.importNode(child, true);
                clonedRoot.appendChild(clonedChild);
            }
        }
        return clonedDocument;
	}
}
