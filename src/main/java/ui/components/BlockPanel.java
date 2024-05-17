package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import clickable.BlockClickable;
import clickable.InvocableClickable;
import domain.Sprite;
import domain.models.interfaces.Clickable.Rect;
import ui.EmptyLayout;
import ui.FlashThread;
import ui.VariableCreator;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.IRenderer.IRenderable;

public class BlockPanel extends JLayeredPane{
	
//	static {
//		DebugOut.setup();
//	}
//	
//	public static void main(String[] args) {
//		JFrame p = new JFrame();
//		BlockPanel bp = BlockPanel.INSTANCE;
//		
//		p.setSize(1000, 1000);
//		p.add(bp, BorderLayout.CENTER);
//		p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		p.setVisible(true);
//	}
	
	public static final boolean DEBUG_SHOW_HITBOXES = false;
	public static final boolean DEBUG_SHOW_HASHES = false;	
	
	private static final long serialVersionUID = 8172972493584077329L;
	
	//Position
	private int x = 0;
	private int y = 0;
	
	//Position of mouse on screen when dragging panel (clicked == null)
	private int cx = 0;
	private int cy = 0;
	
	//Zoom of the blocks
	public double zoom = 0.5;
	
	public final JButton BOTON_VARIABLES = new JButton("Create new Variable");
	
	private Sprite actualSprite = SpritePanel.getSprite();
	
	public Sprite getSprite() {
		return actualSprite;
	}
	
	
	private final List<DragableRenderer> blocks = new LinkedList<>();
	private BlockClickable clicked = null;
	private BlockClickable hovered = null;
	private JLabel clickedLabel = new JLabel("");
	public static BlockPanel INSTANCE = new BlockPanel();
	
	public void changeSprite() {
		actualSprite = SpritePanel.getSprite();
		//TODO remove all blocks from here and add the ones from the new Sprite
	}
	
	/**
	 * Marks the clickable for the mouse events
	 * @param cl
	 */
	public void setClicked(BlockClickable cl) {
		if(cl != null)
			System.out.println("clicked set to " + cl.getBlock().toString().replaceAll(".*\\.", ""));
		else
			System.out.println("clicked cleared");
		clicked = cl;
	}
	
	/**
	 * Returns the clickable that interacts with the mouse events
	 * @return
	 */
	public BlockClickable getClicked() {
		return clicked;
	}
	
	/**
	 * Returns the virtual translation of the camera
	 * @return
	 */
	public Point getTraslation() {
		return new Point(x, y);
	}
	
	/**
	 * Adds a block to the top priority layer of the window
	 * @param b
	 */
	public void addBlock(IRenderable b) {
		System.out.println(""+b);
		addBlock(IRenderer.getDragableRendererOf(b));
	}
	
	/**
	 * Adds a block to the top priority layer of the window
	 * @param b
	 */
	public void addBlock(DragableRenderer b) {
		System.out.println("adding " + b.getBlock().toString().replaceAll(".*\\.", ""));
		if(!blocks.contains(b))
			blocks.add(0, b); //Last to spawn first to grab

		System.out.println("Hola");// @EnekoHernando wanted to take part in the debug process 
		System.out.println("newSize:" + blocks.size());
	}
	
	/**
	 * Adds a block to the top priority layer of the window
	 * @param b
	 */
	public void removeBlock(DragableRenderer b) {
		System.out.println("removing " + b.getBlock().toString().replaceAll(".*\\.", ""));
		System.out.println("removed:"+ blocks.remove(b) +" newSize:" + blocks.size());
	}
	private Point mouse;
	private Rect hpoint = null;
	public Point clickPosition;	
	
	//▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
	//▓							Constructor									▓
	//▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
	
	private BlockPanel() {
		super();
		setMinimumSize(new Dimension(400, 100));
		clickedLabel.setOpaque(false);
		clickedLabel.setDoubleBuffered(true);
		setLayout(new EmptyLayout());
		add(SectionList.INSTANCE, JLayeredPane.DEFAULT_LAYER);
		add(BOTON_VARIABLES, JLayeredPane.PALETTE_LAYER);
		add(clickedLabel, JLayeredPane.MODAL_LAYER);
		
		BOTON_VARIABLES.addActionListener((v)-> new VariableCreator().setVisible(true));
		BOTON_VARIABLES.setVisible(false);
	
		setDoubleBuffered(false);
		setIgnoreRepaint(true);
		addMouseListener(new MouseController());
		addMouseMotionListener(new MouseMotionController());
		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.getPoint().x > 2 * getWidth() / 3 - 10) // List / section selected
					return;
				zoom = Math.max(0.25, Math.min(zoom - e.getWheelRotation() * 0.05, 1));
				repaint();
			}
			
		});
		repaint();
	}
	
	/**
	 * Updates the visuals of the window.
	 * 
	 * Better use {@link ui.components.BlockPanel#repaint() INSTANCE.repaint()}
	 */
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	BufferedImage rend = null;
	@Override
	public void repaint() {
		rend = null;
		super.repaint();
	}
	
	private void adjustSizes() {
		SectionList.INSTANCE.setBounds(getWidth() - 25, 0, 25, getHeight());
		BOTON_VARIABLES.setBounds(getWidth() * 2 / 3, getHeight() - 70, getWidth() / 3 - 30, 50);
	}
	
	private Dimension d = null;
	private static final ImageIcon EMPTY_CLICKED = new ImageIcon(new BufferedImage(1,1, BufferedImage.TYPE_4BYTE_ABGR));
	@Override
	protected void paintComponent(Graphics g) {
		if(d == null || d.getHeight() != getHeight() || d.getWidth() != getWidth()) adjustSizes();
		if(clicked == null)
			clickedLabel.setIcon(EMPTY_CLICKED);
		else {
			BufferedImage clk = clicked.getRenderer().getRenderable();
			if(clicked instanceof InvocableClickable) {
				InvocableClickable next = (InvocableClickable) clicked;
				int y = 0;
				int w = 0;
				while(next != null) {
					y += next.getRenderer().getHeight();
					w = Math.max(w, next.getRenderer().getWidth());
					next = next.next();
				}
				next = (InvocableClickable) clicked;
				clk = new BufferedImage(w, y, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics g0 = clk.getGraphics();
				y = 0;
				while(next != null) {
					g0.drawImage(next.getRenderer().getRenderable(), 0, y, null);
					y += next.getRenderer().getHeight();
					next = next.next();
				}
			}
			Rect pos = clicked.getPosition();
			clickedLabel.setIcon(
					new ImageIcon(
							clk.getScaledInstance(
								(int)(clk.getWidth() * zoom), 
								(int)(clk.getHeight() * zoom), 
								BufferedImage.SCALE_FAST)
							)
					);
			clickedLabel.setBounds(
					(int)((pos.x + x) * zoom), //X 
					(int)((pos.y + y) * zoom), //Y
					(int)(clk.getWidth() * zoom), //W
					(int)(clk.getHeight() * zoom) ); //H
		}
		if(rend == null)
			try {
				rend = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
				Graphics g0 = rend.getGraphics();
				g0.setColor(Color.white);
				g0.fillRect(0, 0, this.getWidth(), this.getHeight());
				List<DragableRenderer> _temp = new ArrayList<>(blocks);
				Collections.reverse(_temp);
				((Graphics2D)g0).setStroke(new BasicStroke((int)Math.ceil(5 * zoom)));
				System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ Start repaint");
				for (DragableRenderer b : _temp) {
					System.out.println(b.getBlock().toString().replaceAll(".*\\.", ""));
					if(blocks.contains(b)) {
						g0.drawImage(b.getRenderable().getScaledInstance((int)(b.getRenderable().getWidth() * zoom), (int)(b.getRenderable().getHeight() * zoom), BufferedImage.SCALE_FAST), (int)((x + b.getX()) * zoom), (int)((y + b.getY()) * zoom), null);
						if(DEBUG_SHOW_HITBOXES) {
							g0.setColor(Color.green);
							Rect hb = b.getClickable().getPosition();
							g0.drawRect(hb.x + x, hb.y + y, hb.w, hb.h);
							g0.setColor(Color.red);
							g0.drawRect(b.getX() + x, b.getY() + y, b.getWidth(), b.getHeight());
						}
					}
				}
				if(DEBUG_SHOW_HITBOXES) {
					if(mouse != null)
						g0.drawOval(mouse.x - 1, mouse.y - 1, 2, 2);
					if(hpoint != null) {
						g0.setColor(Color.blue);
						g0.drawOval(hpoint.x - 1 + x, hpoint.y - 1 + y, 2, 2);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				//Can't find the problem and a frame skipped is no big deal
			} catch(Exception e) {
				e.printStackTrace();
			}
		g.drawImage(rend, 0, 0, null);
		BlockSelectorPanel.INSTANCE.paintComponent(g);
	}
	private class MouseController extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getPoint().x > 2 * getWidth() / 3 - 10) // List / section selected
				return;
			clickPosition = e.getPoint().getLocation();
			clickPosition.x /= zoom;
			clickPosition.y /= zoom;
			clickPosition.translate(-x, -y);
			System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
			mouse = e.getPoint().getLocation();
				
			for(DragableRenderer dr : blocks) {
				System.out.println(dr.getBlock().toString().replaceAll(".*\\.", "") + " x:" + mouse.x + " y:" + mouse.y);
				Rect r = dr.getClickable().getPosition();
				if(mouse.x > (r.x + x) * zoom && mouse.x < (r.x + x + r.w) * zoom && mouse.y > (r.y + y) * zoom && mouse.y < (r.y + r.h + y) * zoom) {
					System.out.println("in");
					clicked = dr.getClickable();
					break;
				}
				System.out.println("not");
			}
			if(clicked != null) {
				System.out.println(clicked.getClass().getSimpleName() + " " + clicked.getBlock().toString().replaceAll(".*\\.", "") + 
						" px:" + clickPosition.x + " py:" + clickPosition.y + 
						" xf:" + (clickPosition.x - clicked.getPosition().x) + " yf:" + (clickPosition.x - clicked.getPosition().x));
				clicked.onClick(clickPosition.x - clicked.getPosition().x, clickPosition.y - clicked.getPosition().y);//(int)(mouse.x - (clicked.getPosition().x + x) * zoom), (int)(mouse.y - (clicked.getPosition().y + y) * zoom));
				repaint();
			} else {
				cx = (int) (mouse.x / zoom - x);
				cy = (int) (mouse.y / zoom - y);
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			hpoint= null;
			FlashThread.INSTANCE.setHovered(null);
			if(clicked != null)
				clicked.onClickEnd();
			if(hovered != null)
				hovered.onHoverEnd(true, clicked);
			hovered = null;
		}
	}
	private class MouseMotionController extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			Point clickPosition = e.getPoint().getLocation();
			clickPosition.x /= zoom;
			clickPosition.y /= zoom;
			clickPosition.translate(-x, -y);
			mouse = e.getPoint();
			if(clicked != null) {
				clicked.onDrag(clickPosition.x, clickPosition.y);
				repaint();
				hpoint = clicked.getPosition();
				hpoint.x += hpoint.w/2;
				hpoint.y += 20;
				for(DragableRenderer dr : blocks) {
					if(dr.getClickable().equals(clicked)) continue;
					Rect r = dr.getClickable().getPosition();
					if(hpoint.x > r.x && hpoint.x < r.x + r.w && hpoint.y > r.y && hpoint.y < r.y + r.h) {
						if(hovered != null && !hovered.equals(dr.getClickable())) {
							System.out.println("hover changed " + hovered.getBlock().toString().replaceAll(".*\\.", "") + " -> "+ dr.getBlock().toString().replaceAll(".*\\.", ""));
							hovered.onHoverEnd(false, clicked);
						}
						hovered = dr.getClickable();
						hovered.onHover(hpoint.x - hovered.getPosition().x, hpoint.y - hovered.getPosition().y, clicked);
						return;
					}
					
				}
			} else {
				x = (int) ((mouse.x / zoom - cx));
				y = (int) ((mouse.y / zoom - cy));
				repaint();
			}
			FlashThread.INSTANCE.setHovered(null);
			if(hovered != null) {
				hovered.onHoverEnd(false, clicked);
				hovered = null;
			}
			
		}
	}
}
