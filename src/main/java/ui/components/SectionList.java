package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ui.domain.BlockSection;
import ui.renderers.IRenderer;
import ui.windows.ProjectFrame;

public class SectionList extends JList<BlockSection>{
	
	public static SectionList INSTANCE = new SectionList();
	
	private static final BufferedImage START = IRenderer.getRes("textures/section/start.svg");
	private static final BufferedImage END = IRenderer.getRes("textures/section/end.svg");
	private static final long serialVersionUID = -2265088593094415303L;
	
	private DefaultListModel<BlockSection> dlm = new DefaultListModel<>();
	
	private SectionList() {
		setCellRenderer(new CellRenderer());
		setModel(dlm);
		try {
		for(BlockSection section : BlockSection.values())
			dlm.addElement(section);
		} catch(Error e) {
			e.printStackTrace();
		}
		addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(ProjectFrame.isFocus() && getSelectedIndex() > -1)
					BlockSelectorPanel.INSTANCE.setPage(getSelectedValue());
			}
		});
	}
	
	private class CellRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = -1502694783643837727L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			String text = ((BlockSection)value).toString();
			int clr = ((BlockSection)value).color;
			BufferedImage textR = new BufferedImage(START.getWidth(), text.length() * 25 + 20, BufferedImage.TYPE_INT_ARGB);
			Graphics2D textG = (Graphics2D) textR.getGraphics();
			textG.setFont(new Font( IRenderer.font.getName(), Font.PLAIN, 50 ));
			textG.setColor(Color.white);
			textG.setTransform(AffineTransform.getQuadrantRotateInstance(1));
			textG.drawString(text, 10, -10);
			
			BufferedImage rendered = new BufferedImage(START.getWidth(), START.getHeight() + textR.getHeight() + END.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics gr = rendered.getGraphics();
			gr.drawImage(START, 0, 0, null);
			for(int x = 0; x < rendered.getWidth(); x++) {
				int color = rendered.getRGB(x, START.getHeight() - 1);
				for(int y = START.getHeight(); y < START.getHeight() + textR.getHeight(); y++)
					rendered.setRGB(x, y, color);
			}
			gr.drawImage(textR, 0, START.getHeight(), null);
			gr.drawImage(END, 0, START.getHeight() + textR.getHeight(), null);
			for(int x = 0; x < rendered.getWidth(); x++)
				for(int y = 0; y < rendered.getHeight(); y++) {
					int argb = rendered.getRGB(x, y);
					byte a = (byte)((argb & 0xff000000) / 0x1000000);
					byte r = (byte)((argb & 0xff0000) / 0x10000);
					byte g = (byte)((argb & 0xff00) / 0x100);
					byte b = (byte)(argb & 0xff);
					if(a != ((byte)0xff) || r == g && b == r)continue;
					rendered.setRGB(x, y, clr);
					
				}
				
			return new JLabel(new ImageIcon(rendered.getScaledInstance(25, rendered.getHeight() * 25 / rendered.getWidth(), BufferedImage.SCALE_SMOOTH)));
		}
		
	}
	
	public static void main(String[] args) {
		String text = "Movement";
		int clr = 0xcccccccc;
		BufferedImage textR = new BufferedImage(START.getWidth(), text.length() * 25 + 20, BufferedImage.TYPE_INT_ARGB);
		Graphics2D textG = (Graphics2D) textR.getGraphics();
		textG.setFont(new Font( IRenderer.font.getName(), Font.PLAIN, 50 ));
		textG.setColor(Color.white);
		textG.setTransform(AffineTransform.getQuadrantRotateInstance(1));
		textG.drawString(text, 10, -10);
		
		BufferedImage rendered = new BufferedImage(START.getWidth(), START.getHeight() + textR.getHeight() + END.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics gr = rendered.getGraphics();
		gr.drawImage(START, 0, 0, null);
		for(int x = 0; x < rendered.getWidth(); x++) {
			int color = rendered.getRGB(x, START.getHeight() - 1);
			for(int y = START.getHeight(); y < START.getHeight() + textR.getHeight(); y++)
				rendered.setRGB(x, y, color);
		}
		gr.drawImage(textR, 0, START.getHeight(), null);
		gr.drawImage(END, 0, START.getHeight() + textR.getHeight(), null);
		for(int x = 0; x < rendered.getWidth(); x++)
			for(int y = 0; y < rendered.getHeight(); y++) {
				int argb = rendered.getRGB(x, y);
				byte a = (byte)((argb & 0xff000000) / 0x1000000);
				byte r = (byte)((argb & 0xff0000) / 0x10000);
				byte g = (byte)((argb & 0xff00) / 0x100);
				byte b = (byte)(argb & 0xff);
				if(a != ((byte)0xff) || r == g && b == r)continue;
				rendered.setRGB(x, y, clr);
				
			}
			
		
		SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 1000);

            frame.getContentPane().add(new JLabel(new ImageIcon(rendered)));

            frame.setVisible(true);
        });
		
	}
	
}
