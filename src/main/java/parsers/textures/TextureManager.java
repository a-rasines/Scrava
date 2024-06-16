package parsers.textures;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextureManager {
	public static class ImageData {
		public final BufferedImage image;
		public File file = null;
		
		public ImageData(BufferedImage image) {
			this.image = image;
		}
	}
	
	public static class TextureTreeNode {
		private Point position = null;
		public final Integer color;
		private Object node;
		public TextureTreeNode(int color, ImageData node) {
			this.color = color;
			this.node = node;
		}	
		
		@SuppressWarnings("unchecked")
		public void addNode(ImageData bi) {
			
			if(node instanceof ImageData _bi) {
				Point point = getFirstUnequalPixel(_bi.image, bi.image);
				if(point.x == -1) return;
				position = point;
				node = new ArrayList<TextureTreeNode>();
				for(ImageData img : new ImageData[] {bi, _bi})
					((List<TextureTreeNode>)node).add(new TextureTreeNode(point.x >= img.image.getWidth() || point.y >= img.image.getHeight()? null : img.image.getRGB(point.x, point.y), img));
			} else {
				for(TextureTreeNode ttn : (List<TextureTreeNode>)node)
					if(((position.x >= bi.image.getWidth()) || position.y >= bi.image.getHeight()) && color == null || ttn.color == bi.image.getRGB(position.x, position.y)) {
						ttn.addNode(bi);
						return;
					}
				((List<TextureTreeNode>)node).add(new TextureTreeNode(bi.image.getRGB(position.x, position.y), bi));
			}
		}
		
		public Point getPosition() {
			return position;
		}
		
		@SuppressWarnings("unchecked")
		public ImageData getImageData(BufferedImage bi) {
			if(node instanceof ImageData id)
				return id;
			for(TextureTreeNode ttn : (List<TextureTreeNode>)node)
				if(bi.getRGB(position.x, position.y) == ttn.color)
					return ttn.getImageData(bi);
			return null;
		}
		
		private Point getFirstUnequalPixel(BufferedImage a, BufferedImage b) {
			for(int y = 0; y < Math.max(a.getHeight(), b.getHeight()); y++) {
				if(y >= a.getHeight() || y >= b.getHeight())
					return new Point(0, y);
				for(int x = 0; x < Math.max(a.getWidth(), b.getWidth()); x++) {
					if(x >= a.getWidth() || x >= b.getWidth() || a.getRGB(x, y) != b.getRGB(x, y))
						return new Point(x, y);
				}
			}
			return new Point(-1, -1);
		}
		
	}
	
	List<TextureTreeNode> firstNode = new ArrayList<>();
	public ImageData addImage(BufferedImage image) {
		for(TextureTreeNode node : firstNode)
			if(node.color == image.getRGB(0, 0)) {
				node.addNode(new ImageData(image));
				return node.getImageData(image);
			}
		ImageData id = new ImageData(image);
		firstNode.add(new TextureTreeNode(image.getRGB(0, 0), id));
		return id;
		
	}
}
