package ui.dialogs;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import domain.Sprite;
import ui.ImageFilter;
import ui.components.SpritePanel;
import ui.listeners.DoubleKeyListener;
import ui.listeners.NameKeyListener;

public class SpriteCreateDialog extends ScDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	public static void main(String[] args) {
		try {
			SpriteCreateDialog dialog = new SpriteCreateDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private BufferedImage img = Sprite.DEFAULT_TEXTURE;
	
	public SpriteCreateDialog() {
		setBounds(100, 100, 400, 180);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel imagePanel = new JPanel();
		imagePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPanel.add(imagePanel);
		imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JLabel lblNewLabel = new JLabel(new ImageIcon(img));
		imagePanel.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		contentPanel.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_name = new JPanel();
		panel.add(panel_name);
		panel_name.add(new JLabel("Set name"));
		textField = new JTextField();
		textField.setColumns(10);
		textField.addKeyListener(new NameKeyListener());
		panel_name.add(textField);
		
		JPanel panel_size = new JPanel();
		panel.add(panel_size);
		panel_size.add(new JLabel("Scale:"));
		JTextField zoomField = new JTextField(10);
		zoomField.setText("1");
		zoomField.addKeyListener(new DoubleKeyListener(zoomField));
		panel_size.add(zoomField);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JButton btnSetTexture = new JButton("Set texture");
		btnSetTexture.addActionListener((e) -> {
			JFileChooser fileChooser = new JFileChooser();
			 fileChooser.setAcceptAllFileFilterUsed(false);
			 fileChooser.addChoosableFileFilter(new ImageFilter());
			 int result = fileChooser.showOpenDialog(SpriteCreateDialog.this);
			 if (result == JFileChooser.APPROVE_OPTION) {
				 try {
					 BufferedImage temp = ImageIO.read(fileChooser.getSelectedFile());
					 img = temp;
					 if(temp.getHeight() < temp.getWidth())
						 lblNewLabel.setIcon(new ImageIcon(temp.getScaledInstance(190, (int)(temp.getHeight() * 190 / temp.getWidth()), result)));
					 else
						 lblNewLabel.setIcon(new ImageIcon(temp.getScaledInstance((int)(temp.getWidth() * lblNewLabel.getHeight() / temp.getHeight()), (int)(lblNewLabel.getHeight()), result)));
				 } catch(IOException ex) {
					 JOptionPane.showMessageDialog(null, "Error loading the image: " + ex.getMessage(), "Error on load", JOptionPane.ERROR_MESSAGE);
				 }
			 }
		});
		panel_1.add(btnSetTexture);
		JPanel buttonPane = new JPanel();
		panel.add(buttonPane);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton okButton = new JButton("OK");
		okButton.addActionListener((e) -> {
			if(textField.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "The sprite needs a name");
				return;
			}
			for(Sprite s : SpritePanel.getSprites())
				if(s.getName().equals(textField.getText())) {
					JOptionPane.showMessageDialog(null, "There's already an sprite with that name");
					return;
				}
			Sprite s = new Sprite(textField.getText(), img);
			s.getScale().setValue(Double.parseDouble(zoomField.getText()), true);
			SpritePanel.addSprite(s);
			dispose();
		});
		buttonPane.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> dispose());
		buttonPane.add(cancelButton);
	}
}
