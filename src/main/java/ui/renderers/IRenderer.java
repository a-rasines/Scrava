package ui.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.imageio.ImageIO;

import org.w3c.dom.svg.SVGDocument;

import clickable.BlockClickable;
import domain.models.interfaces.Clickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.Translatable;
import ui.components.BlockPanel;

public interface IRenderer extends Serializable {
	public static final int FONT_WIDTH = 28;
	
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
	
	public int getHeight();
	
	public int getWidth();
	
	/**
	 * Replaces the pixels from x to x + w
	 * @param x first x of the patch
	 * @param y first y of the new image
	 * @param h height of the patch
	 * @param w width of the patch
	 * @param newI new image to put on the 
	 */
	public void patch(int x, int y, int h, int w, BufferedImage newI);
	
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
		
		public default BufferedImage renderText(String text, int startX, int height) {
			int w = text.replaceAll("\\{\\{.*?}}", "").length() * FONT_WIDTH;
			for(IRenderer rend : getChildren())
				w += rend.getRenderable().getWidth();
			BufferedImage rendered = new BufferedImage(w, height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = rendered.getGraphics();
			g.setFont(new Font( font.getName(), Font.PLAIN, 55 ));
			String[] parts = text.split("\\{\\{");
			int len = 0;
			int vari = 0;
			System.out.println(text);
			for(String part : parts) {		
				if(part.split(" ")[0].contains("}}")) {
					String[] divided = part.split("}}");
					IRenderer rend = getChildren().get(vari++);
					BufferedImage subblock = rend.getRenderable();
					rend.getClickable().setPosition(startX + len - 1, (int)((height- subblock.getHeight())/2));
					g.drawImage(subblock, len - 1, (int)((height- subblock.getHeight())/2) , null);
					if(BlockPanel.DEBUG_SHOW_HITBOXES) {
						g.setColor(Color.green);
						((Graphics2D)g).setStroke(new BasicStroke(2));
						Rect r = rend.getClickable().getPosition();
						g.drawRect(r.x + 1 - startX, r.y + 1, r.w - 2, r.h - 2);
						g.setColor(Color.white);
					}
					len += subblock.getWidth();
				
					if(divided.length == 2) {
						g.drawString(divided[1], len, height/2 + 20);
						len += divided[1].length() * FONT_WIDTH;
					}
					
				} else {
					g.drawString(part, len, height/2  + 20);
					len += part.length() * FONT_WIDTH;
				}
						
			}
			return rendered;
		}
		
	}
}
