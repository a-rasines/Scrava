package ui.components;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import clickable.BlockClickable;
import debug.DebugOut;
import domain.blocks.conditional.bool.AndBlock;
import domain.blocks.container.IfElseBlock;
import domain.blocks.movement.MoveBlock;
import domain.blocks.movement.MoveToBlock;
import domain.blocks.movement.MoveXBlock;
import domain.blocks.operators.AddOperator;
import domain.blocks.operators.AppendOperator;
import domain.models.interfaces.Clickable.Rect;
import domain.values.NumberLiteral;
import domain.values.StringLiteral;
import domain.values.Variable;
import ui.FlashThread;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.IRenderer.IRenderable;
import ui.renderers.InvocableBlockRenderer;

public class BlockPanel extends JLayeredPane{
	
	static {
		DebugOut.setup();
	}
	public static void main(String[] args) {
		JFrame p = new JFrame();
		BlockPanel bp = BlockPanel.INSTANCE;
		bp.addBlock(new AppendOperator().setValues(new AddOperator<Double>(new NumberLiteral<Double>(0.0), new NumberLiteral<Double>(0.0)), new StringLiteral("Test")));
		bp.addBlock(new MoveBlock(null, new NumberLiteral<Integer>(0), new NumberLiteral<Integer>(0)));
		bp.addBlock(new IfElseBlock());
		bp.addBlock(new AndBlock());
		bp.addBlock(Variable.createGlobalVariable("Test", 0));
		InvocableBlockRenderer mtb = IRenderer.getDragableRendererOf(new MoveToBlock(null, new NumberLiteral<Integer>(0), new NumberLiteral<Integer>(0)));
		InvocableBlockRenderer mb = IRenderer.getDragableRendererOf(new MoveBlock(null, new NumberLiteral<Integer>(0), new NumberLiteral<Integer>(0)));
		InvocableBlockRenderer mxb = IRenderer.getDragableRendererOf(new MoveXBlock(null, new NumberLiteral<Integer>(0)));
		mtb.getClickable()
		   .setNext(mb.getClickable())
		   .setNext(mxb.getClickable());
		
		bp.addBlock(mtb);
		bp.addBlock(mb);
		bp.addBlock(mxb);
		
		p.setSize(1000, 1000);
		p.add(bp, BorderLayout.CENTER);
		p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.setVisible(true);
	}
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
	
	
	private final List<DragableRenderer> blocks = new LinkedList<>();
	private BlockClickable clicked = null;
	private BlockClickable hovered = null;
	private JLabel clickedLabel = new JLabel("");
	public static BlockPanel INSTANCE = new BlockPanel();
	
	public void setClicked(BlockClickable cl) {
		System.out.println("clicked set to " + cl.getBlock().toString().replaceAll(".*\\.", ""));
		clicked = cl;
	}
	
	public void addBlock(IRenderable b) {
		System.out.println(""+b);
		addBlock(IRenderer.getDragableRendererOf(b));
	}
	public void addBlock(DragableRenderer b) {
		System.out.println("adding " + b.getBlock().toString().replaceAll(".*\\.", ""));
		if(!blocks.contains(b))
			blocks.add(0, b); //Last to spawn first to grab
		System.out.println("newSize:" + blocks.size());
	}
	
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
		//Empty layout to not mess with custom render system
		setLayout(new LayoutManager() {public void addLayoutComponent(String name, Component comp) {} public void removeLayoutComponent(Component comp) {} public Dimension preferredLayoutSize(Container parent) {	return null; } public Dimension minimumLayoutSize(Container parent) { return null; } public void layoutContainer(Container parent) {}});
		add(SectionList.INSTANCE, JLayeredPane.PALETTE_LAYER);
		add(clickedLabel, JLayeredPane.MODAL_LAYER);
		
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
			clicked = null;
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
