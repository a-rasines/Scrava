package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
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
		for(Sprite s : SpritePanel.getSprites()) {
			Image i = s.getRendered();
			g.drawImage(
					i.getScaledInstance(
							i.getWidth(null) * getWidth() / 1000, 
							i.getHeight(null) * getWidth() / 1000, 
							BufferedImage.SCALE_FAST), 
					(int)((s.getX().value() - i.getWidth(null)/2) * getWidth() / 1000), 
					(int)((s.getY().value() - i.getHeight(null)/2) * getWidth() / 1000),
					null);
		}
	}
}
