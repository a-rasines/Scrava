package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import domain.Sprite;

public class ActionPanel extends JPanel{
	public static final ActionPanel INSTANCE = new ActionPanel();
	private static final long serialVersionUID = 3270465978058475360L;

	private ActionPanel() {
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		for(Sprite s : ProjectFrame.getSprites()) {
			g.drawImage(
					s.getRendered().getScaledInstance(
							s.getRendered().getWidth() * getWidth() / 1000, 
							s.getRendered().getHeight() * getWidth() / 1000, 
							BufferedImage.SCALE_FAST), 
					(int)((s.getX().value() - s.getRendered().getWidth()/2) * getWidth() / 1000), 
					(int)((s.getY().value() - s.getRendered().getHeight()/2) * getWidth() / 1000),
					null);
		}
	}
}
