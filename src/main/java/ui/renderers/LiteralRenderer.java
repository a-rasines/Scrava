package ui.renderers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import clickable.BlockClickable;
import clickable.LiteralClickable;
import domain.models.interfaces.Clickable;
import domain.models.interfaces.Clickable.Rect;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import domain.values.EnumLiteral;
import domain.values.NumberLiteral;
import ui.components.BlockPanel;

public class LiteralRenderer implements IRenderer {
	
	private static final long serialVersionUID = 4947025860653468394L;
	
	private transient final static Map<LiteralRenderable<?>, LiteralRenderer> RENDS_MAP = new HashMap<>();
	public float opacity = 1f;
	
	public static LiteralRenderer of(LiteralRenderable<?> block, String type, BlockClickable parent) {
		if(block instanceof EnumLiteral)
			return new LiteralRenderer(block, IRenderable.VARIABLE_ENUM, parent);
		return new LiteralRenderer(block, type, parent);
	}
	
	public static LiteralRenderer of(LiteralRenderable<?> block, Object type, BlockClickable parent) {
		if(block instanceof EnumLiteral)
			return new LiteralRenderer(block, IRenderable.VARIABLE_ENUM, parent);
		switch(type) {
			case Boolean b:
				return new LiteralRenderer(block, IRenderable.VARIABLE_BOOL, parent);
			case Number n:
				return new LiteralRenderer(block, IRenderable.VARIABLE_NUM, parent);
			case String s:
				return new LiteralRenderer(block, IRenderable.VARIABLE_STR, parent);
			default:
				return null;
		}
	}
	
	public static interface LiteralRenderable<T> extends IRenderable, Valuable<T> {
		
	}
	private final LiteralRenderable<?> block;
	private final LiteralClickable clickable;
	private String type;
	private transient BufferedImage rendered = null;
	
	private LiteralRenderer(LiteralRenderable<?> block, String type, BlockClickable parent) {
		this.block = block;
		if(!type.startsWith("{{"))
			type = "{{" + type;
		if(!type.endsWith("}}"))
			type += "}}";
		this.type = type;
		this.clickable = new LiteralClickable(this, (AbstractLiteral<?>)block, parent);
	}
	public transient static final BufferedImage STRING_VAR_START = IRenderer.getRes("textures/variable/literal/stringstart.svg");
	public transient static final BufferedImage STRING_VAR_END = IRenderer.getRes("textures/variable/literal/stringend.svg");
	public transient static final BufferedImage NUM_VAR_START = IRenderer.getRes("textures/variable/literal/numstart.svg");
	public transient static final BufferedImage NUM_VAR_END = IRenderer.getRes("textures/variable/literal/numend.svg");
	public transient static final BufferedImage BOOLEAN_VAR_START = IRenderer.getRes("textures/variable/literal/booleanstart.svg");
	public transient static final BufferedImage BOOLEAN_VAR_END = IRenderer.getRes("textures/variable/literal/booleanend.svg");
	public transient static final BufferedImage ENUM_VAR_START = IRenderer.getRes("textures/variable/literal/enumstart.svg");
	public transient static final BufferedImage ENUM_VAR_END = IRenderer.getRes("textures/variable/literal/enumend.svg");

	@Override
	public BufferedImage getRenderable() {
		if(rendered != null)
			return rendered;
		String value = (this.block instanceof EnumLiteral el)?el.name():this.block.value().toString();
		BufferedImage left;
		BufferedImage right;
		switch(type) {
		case IRenderable.VARIABLE_STR:
		case IRenderable.VARIABLE_ANY:
			left = STRING_VAR_START;
			right = STRING_VAR_END;
			break;
		case IRenderable.VARIABLE_BOOL:
			left = BOOLEAN_VAR_START;
			right = BOOLEAN_VAR_END;
			break;
		case IRenderable.VARIABLE_NUM:
			left = NUM_VAR_START;
			right = NUM_VAR_END;
			break;
		case IRenderable.VARIABLE_ENUM:
			left = ENUM_VAR_START;
			right = ENUM_VAR_END;
			
			break;
		default:
			left = STRING_VAR_START;
			right = STRING_VAR_END;	
	}
		rendered = new BufferedImage(left.getWidth() + right.getWidth() + value.length() * FONT_WIDTH, left.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = rendered.getGraphics();
		if(g instanceof Graphics2D g2d) {
			 g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	         g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	         g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		}
		background(
				rendered,
				left.getHeight(), 
				left.getWidth(), 
				left.getWidth() 
				+ value.length() * FONT_WIDTH 
		);
		g.drawImage(left, 0, 0, null);

		g.setFont(new Font( font.getName(), Font.PLAIN, 51 ));
		g.setColor(Color.black);
		background(rendered, left.getHeight(), left.getWidth()-1, value.length() * FONT_WIDTH + 2);
		if(this.block instanceof EnumLiteral)
			g.setColor(Color.white);
		g.drawString(value, left.getWidth(), left.getHeight()/2 + 20);
		
		g.drawImage(right, left.getWidth() + value.length() * FONT_WIDTH, 0, null);
		return rendered;
	}
	
	@Override
	public Translatable getBlock() {
		return block;
	}

	@Override
	public void update() {
		rendered = null;
		if(clickable.getParent() == null)
			BlockPanel.INSTANCE.repaint();
		else
			clickable.getParent().getRenderer().update();
		
	}

	@Override
	public List<IRenderer> getChildren() {
		return List.of(); //No children
	}
	
	@Override
	public Clickable getClickable() {
		return clickable;
	}

	public static void main(String[] args) {
		
	 	LiteralRenderer sampleImage = new LiteralRenderer(new NumberLiteral<Double>(1.21, null), IRenderable.VARIABLE_STR, new BlockClickable(null, null));
	 	sampleImage.opacity = 0.5f;
	 	
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);

            JPanel panel = new JPanel() {
				private static final long serialVersionUID = -94843536618228336L;

				@Override
                protected void paintComponent(Graphics g) {
					g.setColor(Color.black);
                    g.drawRect(0, 0, 500, 500);
                    g.drawImage(sampleImage.getRenderable(), 100, 100, this);
                }
            };
            frame.getContentPane().add(panel);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
	}

	@Override
	public void delete() {
		System.out.println("delete " + getBlock());
		RENDS_MAP.remove(this.block);
	}

	@Override
	public int getHeight() {
		return getRenderable().getHeight();
	}

	@Override
	public int getWidth() {
		return getRenderable().getWidth();
	}

	@Override
	public void patch(int x, int y, int h, int w, BufferedImage bi) {
		rendered = null;
		Rect r = clickable.getPosition();
		clickable.getParent().getRenderer().patch(r.x, r.y, r.y + r.h, r.w, getRenderable());
		
	}
}
