package parsers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGRect;

import debug.DebugOut;
import domain.values.NumberLiteral;
import ui.renderers.IRenderer;

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
	 * @param doc
	 */
	public static BridgeContext build(Document doc) {
		BridgeContext context = new BridgeContext(new UserAgentAdapter());
		context.setDynamicState(BridgeContext.DYNAMIC);
		try {
			new GVTBuilder().build(context, doc);
		} catch(Exception e) {
			try {
				System.out.println(documentToString(doc));
			} catch (TransformerException e1) {
				e1.printStackTrace();
			}
			throw e;
		}
		return context;
	}
	
	public static String documentToString(Document doc) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
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
        NumberLiteral<Integer> nl = new NumberLiteral<Integer>(123450, null);
        IRenderer r = nl.getRenderer();
//        IRenderer r = new BooleanLiteral(true, null).getRenderer();
//        IRenderer r = new StringLiteral("123456789987654321", null).getRenderer();
//        IRenderer r = new EnumLiteral<Integer>(KeyEventBlock.KEY_MAP, null).getRenderer();

//        IRenderer r = new AppendOperator().getRenderer();
//        AddOperator ao = new AddOperator();
//        NumberLiteral<Double> nl = new NumberLiteral<Double>(0., ao);
//        ao.setLeft(nl);
//        AddOperator pao = new AddOperator();
//        pao.setLeft(ao);
//        
//        IRenderer r = pao.getRenderer();
        
//        nl.setValue(10000., true);
        
//        IRenderer r = new RandomOperator().getRenderer();
//        IRenderer r = new OrBlock(new AndBlock(new OrBlock(), new BooleanLiteral(true, null)), new BooleanLiteral(true, null)).getRenderer();
//        IRenderer r = new OrBlock().getRenderer();
        SVGDocument doc = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        Node n = r.toDocument(doc).getRenderableSVG();
        doc.getDocumentElement().appendChild(n);
        System.out.println(doc.hashCode());
        try (FileWriter writer = new FileWriter(new File(doc.hashCode() + ".svg"))) {
            DOMUtilities.writeDocument(doc, writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        nl.setValue(220, true);
        System.out.println(doc.hashCode());
        try (FileWriter writer = new FileWriter(new File(doc.hashCode() + ".svg"))) {
            DOMUtilities.writeDocument(doc, writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        BufferedImage bi = SVGReader.toBufferedImage(doc);
        Graphics g = bi.getGraphics();
        g.setColor(Color.blue);
        build(doc);
        SVGRect bbox = ((SVGOMSVGElement) doc.getDocumentElement()).getBBox();
        g.drawRect((int)bbox.getX(), (int)bbox.getY(), (int)bbox.getWidth(), (int)bbox.getHeight());
        g.drawLine((int)(bbox.getWidth()/2), 0, (int)(bbox.getWidth()/2), (int)bbox.getHeight());
        JLabel label = new JLabel(new ImageIcon(bi)); 
        frame.add(label, BorderLayout.CENTER);
        frame.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
//        		nl.setValue(new Random().nextDouble(), true);
        		SVGDocument doc = (SVGDocument)r.getRenderableSVG().getOwnerDocument();
        		BufferedImage bi = SVGReader.toBufferedImage(doc);
                Graphics g = bi.getGraphics();
                g.setColor(Color.blue);
                build(doc);
                SVGRect bbox = ((SVGOMSVGElement) doc.getDocumentElement()).getBBox();
                g.drawRect((int)bbox.getX(), (int)bbox.getY(), (int)bbox.getWidth(), (int)bbox.getHeight());
                g.drawLine((int)(bbox.getWidth()/2), 0, (int)(bbox.getWidth()/2), (int)bbox.getHeight());
        		label.setIcon(new ImageIcon(bi));
        	}
		});
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
	
	public static Rectangle2D getBoundingBox(Element svgElement) {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = -Float.MIN_VALUE + 1;
        float maxY = -Float.MIN_VALUE + 1;

        NodeList childNodes = svgElement.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            System.out.println(node.getClass());
            if (node instanceof SVGLocatable graphicsElement) {
                SVGRect bbox = graphicsElement.getBBox();
                if(bbox == null) continue;

                float x = bbox.getX();
                float y = bbox.getY();
                float width = bbox.getWidth();
                float height = bbox.getHeight();
                System.out.println("x:" + x + " y:" + y + " w:" + width + " h:" + height);
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x + width);
                maxY = Math.max(maxY, y + height);
            }
        }
        System.out.println("minX: " + minX + " minY:" + minY + " maxX:" + maxX + " maxY:" + maxY);
        if (minX == Float.MAX_VALUE || minY == Float.MAX_VALUE || maxX == -Float.MIN_VALUE || maxY == -Float.MIN_VALUE) {
        	SVGRect bb = ((SVGLocatable)svgElement).getBBox();
            return  new Rectangle2D.Float(bb.getX(), bb.getY(), bb.getWidth(), bb.getHeight());
        } else {
            return new Rectangle2D.Float(minX, minY, maxX - minX, maxY - minY);
        }
    }
}
