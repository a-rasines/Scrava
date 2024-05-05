package ui.components;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import debug.DebugOut;
import ui.EmptyLayout;

public class ProjectFrame extends JFrame {
	private static final long serialVersionUID = -4157218152821931601L;
	static {
		DebugOut.setup();
	}
	
	public static void main(String[] args) {
//		for (long longVal = 4946144450195624L; longVal > 0; longVal >>= 5)
//			System.out.print((char) (((longVal & 31 | 64) % 95) + 32));
		JFrame p = new ProjectFrame();
		p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.setVisible(true);
	}
	
	public ProjectFrame() {
		setLayout(new EmptyLayout());
		add(BlockPanel.INSTANCE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				BlockPanel.INSTANCE.setBounds((int) (getWidth()*2/5)+20, 0, (int) (getWidth()*3/5) - 40, getHeight());
			}
		});
	}
	
	@Override
	public void setSize(Dimension d) {}

}
