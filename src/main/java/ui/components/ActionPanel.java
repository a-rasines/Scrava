package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
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
		System.out.println("Update");
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		for(Sprite s : SpritePanel.getSprites()) {
			AffineTransform transform = new AffineTransform();
			Image i = s.getRendered();
			transform.translate(
				(int)((s.getX().value() - i.getWidth(null)/2) * getWidth() / 1000), 
				(int)((s.getY().value() - i.getHeight(null)/2) * getWidth() / 1000)
			);
			System.out.println("Translation: x:"+transform.getTranslateX() + " y:" + transform.getTranslateY());
			transform.rotate(Math.toRadians(s.getRotation().value()), i.getWidth(null) / 2, i.getHeight(null) / 2);
			((Graphics2D)g).drawImage(i,transform, null);
//			g.drawImage(
//					i.getScaledInstance(
//							i.getWidth(null) * getWidth() / 1000, 
//							i.getHeight(null) * getWidth() / 1000, 
//							BufferedImage.SCALE_FAST), 
//					(int)((s.getX().value() - i.getWidth(null)/2) * getWidth() / 1000), 
//					(int)((s.getY().value() - i.getHeight(null)/2) * getWidth() / 1000),
//					null);
		}
	}
}
