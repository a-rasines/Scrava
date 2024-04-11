package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import debug.DebugOut;
import domain.blocks.conditional.bool.AndBlock;
import domain.blocks.container.IfElseBlock;
import domain.blocks.movement.MoveBlock;
import domain.blocks.movement.MoveToBlock;
import domain.blocks.movement.MoveXBlock;
import domain.blocks.operators.AddOperator;
import domain.blocks.operators.AppendOperator;
import domain.clickable.BlockClickable;
import domain.models.interfaces.Clickable.Rect;
import domain.values.NumberLiteral;
import domain.values.StringLiteral;
import domain.values.Variable;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.IRenderer.IRenderable;
import ui.renderers.InvocableBlockRenderer;

public class BlockPanel extends JPanel{
	
	public static final boolean DEBUG_SHOW_HITBOXES = true;
	public static final boolean DEBUG_SHOW_HASHES = true;
	public static final List<String> DEBUG_MUTED_FUNCTIONS = List.of(
		//block interaction
		//"addBlock",
		"getBlockBundleIndex",
		"moveTo",
		"removeBlock",
		"removeChild",
		
		//event
		"mouseDragged",
		//"onClick",
		"onHover",
		//"onHoverEnd",
		
		//paint
		"paintComponent",
		"patch",
		"renderText"
	);
	
	
	private static final long serialVersionUID = 8172972493584077329L;
	private int x = 0;
	private int y = 0;
	private int cx = 0;
	private int cy = 0;
	private final List<DragableRenderer> blocks = new LinkedList<>();
	private BlockClickable clicked = null;
	private BlockClickable hovered = null;
	public static BlockPanel INSTANCE = new BlockPanel();
	
	public void setClicked(BlockClickable cl) {
		System.out.println("clicked set to " + cl.getBlock().toString().replaceAll(".*\\.", ""));
		clicked = cl;
	}
	
	public static void main(String[] args) {
		DebugOut.setup();
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
	
	private BlockPanel() {
		super();
		setIgnoreRepaint(true);
		addMouseListener(new MouseController());
		addMouseMotionListener(new MouseMotionController());
	}
	@Override
	public void repaint() {
		if(getGraphics() != null)
			paintComponent(getGraphics());
		else super.repaint();
	}
	@Override
	protected void paintComponent(Graphics g) {
		try {
			BufferedImage temp = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			Graphics g0 = temp.getGraphics();
			g0.setColor(Color.white);
			g0.fillRect(0, 0, this.getWidth(), this.getHeight());
			List<DragableRenderer> _temp = new ArrayList<>(blocks);
			Collections.reverse(_temp);
			((Graphics2D)g0).setStroke(new BasicStroke(5));
			System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ Start repaint");
			for (DragableRenderer b : _temp) {
				System.out.println(b.getBlock().toString().replaceAll(".*\\.", ""));
				if(blocks.contains(b)) {
					g0.drawImage(b.getRenderable(), x + b.getX(), y + b.getY(), null);
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
			g.drawImage(temp, 0, 0, null);
		} catch(ArrayIndexOutOfBoundsException e) {
			//Can't find the problem and a frame skipped is no big deal
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private class MouseController extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			clickPosition = e.getPoint().getLocation();
			clickPosition.translate(-x, -y);
			System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
			System.out.println(x + " " + y);
			mouse = e.getPoint();
				
			for(DragableRenderer dr : blocks) {
				System.out.println(dr.getBlock().toString().replaceAll(".*\\.", "") + " x:" + mouse.x + " y:" + mouse.y);
				Rect r = dr.getClickable().getPosition();
				if(mouse.x > r.x + x && mouse.x < r.x + x + r.w && mouse.y > r.y + y && mouse.y < r.y + r.h + y) {
					System.out.println("in");
					clicked = dr.getClickable();
					break;
				}
				System.out.println("not");
			}
			if(clicked != null) {
				System.out.println(clicked.getClass().getSimpleName() + " " + clicked.getBlock().toString().replaceAll(".*\\.", "") + 
						" px:" + mouse.x + " py:" + mouse.y + 
						" xf:" + (mouse.x - clicked.getPosition().x) + " yf:" + (mouse.x - clicked.getPosition().x));
				clicked.onClick(mouse.x - (clicked.getPosition().x + x), mouse.y - (clicked.getPosition().y + y));
				paintComponent(getGraphics());
			} else {
				cx = mouse.x - x;
				cy = mouse.y - y;
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
			mouse = e.getPoint();
			if(clicked != null) {
				clicked.onDrag(mouse.x - x, mouse.y - y);
				paintComponent(getGraphics());
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
				x = mouse.x - cx;
				y = mouse.y - cy;
				paintComponent(getGraphics());
			}
			FlashThread.INSTANCE.setHovered(null);
			if(hovered != null) {
				hovered.onHoverEnd(false, clicked);
				hovered = null;
			}
			
		}
	}
}
