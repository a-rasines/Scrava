package parsers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import domain.values.BooleanLiteral;
import ui.renderers.LiteralRenderer;

public class SVGReader {

	public static SVGDocument readSVG(String path) throws IOException, URISyntaxException {
		String uri = ClassLoader.getSystemClassLoader().getResource(path).toURI().toString();
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        System.out.println(uri);
        return (SVGDocument) factory.createDocument(uri);
	}
	public static void main(String[] args) throws URISyntaxException, IOException {
//        SVGDocument document = readSVG("textures/sprite/def.svg");
        JFrame frame = new JFrame("SVG Display");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

//        JSVGCanvas canvas = new JSVGCanvas();
//        frame.getContentPane().add(canvas, BorderLayout.CENTER);
//        canvas.setSVGDocument(document);
//        LiteralRenderer lr = (LiteralRenderer) new NumberLiteral<Integer>(123450, null).getRenderer();
        LiteralRenderer lr = (LiteralRenderer) new BooleanLiteral(true, null).getRenderer();
//        LiteralRenderer lr = (LiteralRenderer) new StringLiteral("123456789987654321", null).getRenderer();
//        LiteralRenderer lr = (LiteralRenderer) new EnumLiteral<Integer>(KeyEventBlock.KEY_MAP, null).getRenderer();
        BufferedImage bi = lr.getRenderable();
        Graphics g = bi.getGraphics();
        g.setColor(Color.red);
        g.drawRect(0, 0, bi.getWidth(), bi.getHeight());
        g.drawLine(bi.getWidth()/2, 0, bi.getWidth()/2, bi.getHeight());
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
