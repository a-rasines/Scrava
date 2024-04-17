package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import ui.domain.BlockSection;
import ui.renderers.IRenderer;

public class BlockList extends JList<IRenderer>{
	
	public static BlockList INSTANCE = new BlockList();
	
	private static final long serialVersionUID = 7433870091568658074L;
	
	private DefaultListModel<IRenderer> dlm = new DefaultListModel<>();
	
	private BlockList() {
		setCellRenderer(new CellRenderer());
		setModel(dlm);
		setPage(BlockSection.values()[0]);
	}
	
	public void setPage(BlockSection selected) {
		int r = (selected.color & 0xff0000) + 0x330000;
		if((r & 0xff000000) != 0)
			r = 0xff0000;
		int g = (selected.color & 0xff00) + 0x3300;
		if((g & 0xff0000) != 0)
			g = 0xff00;
		int b = (selected.color & 0xff) + 0x33;
		if((b & 0xff00) != 0)
			b = 0xff;
		setBackground(new Color(0xff000000 | r | g | b));
		dlm.removeAllElements();
		for(IRenderer ir : selected.blocks)
			dlm.addElement(ir);
	}
	
	private class CellRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = -1502694783643837727L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel lbl = new JLabel("");
			try {
			BufferedImage original = ((IRenderer)value).getRenderable();
			lbl.setIcon(new ImageIcon(original.getScaledInstance((int)(original.getWidth() * 0.5), (int)(original.getHeight() * 0.5), BufferedImage.SCALE_FAST)));
			return lbl;
			} catch(Exception e) {
				System.out.println(((IRenderer)value).getBlock());
				e.printStackTrace();
				throw e;
			}
			
		}
	}

}
