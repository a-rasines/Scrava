package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import domain.Sprite;
import ui.EmptyLayout;
import ui.dialogs.SpriteCreateDialog;
import ui.dialogs.SpriteEditDialog;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;

public class SpritePanel extends JLayeredPane implements ComponentListener {

	private static final long serialVersionUID = 5118042588735984151L;
	
	public static final SpritePanel INSTANCE = new SpritePanel();
	
	private SpriteList sl;
	private DefaultListModel<Sprite> sprites;
	private JScrollPane spritePane;
	private JLabel addButton;
	
	public static void addSprite(Sprite s) {
		System.out.println("add " + s.toString());
		INSTANCE.sprites.addElement(s);
		if(INSTANCE.sl.getSelectedIndex() == -1)
			INSTANCE.sl.setSelectedIndex(0);
		INSTANCE.sl.setVisibleRowCount(INSTANCE.sprites.size()%3==0?INSTANCE.sprites.size()/3:INSTANCE.sprites.size()/3 + 1);
		System.out.println(INSTANCE.sprites.size());
		BlockPanel.INSTANCE.changeSprite();
		ActionPanel.INSTANCE.repaint();
	}
	
	public static void clearSprites() {
		INSTANCE.sl.setSelectedIndex(-1);
		INSTANCE.sprites.clear();
	}
	
	public static  Sprite getSprite() {
		return INSTANCE.sl.getSelectedValue();
	}
	public static List<Sprite> getSprites() {
		Sprite[] s = new Sprite[INSTANCE.sprites.size()];
		INSTANCE.sprites.copyInto(s);
		return Arrays.asList(s);
	}
	
	public static void deleteSprite(Sprite s) {
		INSTANCE.sl.setSelectedIndex(0);
		INSTANCE.sprites.removeElement(s);
		INSTANCE.sl.setVisibleRowCount(INSTANCE.sprites.size()%3==0?INSTANCE.sprites.size()/3:INSTANCE.sprites.size()/3 + 1);
		BlockPanel.INSTANCE.changeSprite();
		ActionPanel.INSTANCE.repaint();
	}
	
	private int selectedIndex = -1;
	
	private SpritePanel() {
		sprites = new DefaultListModel<Sprite>();
		sl = new SpriteList();
		spritePane = new JScrollPane(sl);
		setLayout(new EmptyLayout());
		addComponentListener(this);
		add(spritePane, JLayeredPane.DEFAULT_LAYER);
		addButton = new JLabel(new ImageIcon(IRenderer.getRes("textures/ui/add.svg")));
		addButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!SpriteCreateDialog.isAlreadyOpen())
					new SpriteCreateDialog().setVisible(true);
			}
			
		});
		add(addButton, JLayeredPane.PALETTE_LAYER);
	}
	

	@Override
	public void componentResized(ComponentEvent e) {
		spritePane.setBounds(200, 0, getWidth() - 200, getHeight());
		sl.setBounds(spritePane.getBounds());
		addButton.setBounds(getWidth() - 25, getHeight() - 25, 25, 25);
	}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}

	private class SpriteList extends JList<Sprite> implements ListCellRenderer<Sprite>, MouseListener {
		private static final long serialVersionUID = -431526313947694850L;
		
		public SpriteList() {
			super(sprites);
			setLayoutOrientation(JList.HORIZONTAL_WRAP);
			setVisibleRowCount(1);
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setCellRenderer(this);
			addListSelectionListener((e) -> {
				if(getSelectedIndex() == -1 && sprites.size() > 0) setSelectedIndex(selectedIndex);
				else if(sprites.size() > 0) {
					BlockPanel.INSTANCE.changeSprite();
					BlockSelectorPanel.INSTANCE.update();
					selectedIndex = getSelectedIndex();
				}
			});
			addMouseListener(this);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Sprite> list, Sprite value, int index,
				boolean isSelected, boolean cellHasFocus) {
			BufferedImage out = new BufferedImage(120, 140, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = out.getGraphics();
			if(isSelected) {
				g.setColor(getSelectionBackground());
				g.fillRect(0, 0, 120, 140);
			}
			try {
				g.drawImage(value.getRendered().getScaledInstance(100, 100, BufferedImage.SCALE_FAST), 10, 10, null);
			} catch(NullPointerException e) {}
			g.setFont(new Font( DragableRenderer.font.getName(), Font.PLAIN, 20 ));
			g.setColor(Color.black);
			g.drawString(value.getName().length() > 10 ? value.getName().substring(0, 7) + "..." : value.getName(), 10, 130);
			return new JLabel(new ImageIcon(out));
		}
		private int slIndex = 0;
		@Override
		public void mouseClicked(MouseEvent e) {
			int newIndex = locationToIndex(e.getPoint());
			if(newIndex == slIndex) { 
				if(!SpriteEditDialog.isAlreadyOpen())
						new SpriteEditDialog(getSelectedValue()).setVisible(true);
			} else
				slIndex = newIndex; 
		}
		
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}	
		
	}
}
