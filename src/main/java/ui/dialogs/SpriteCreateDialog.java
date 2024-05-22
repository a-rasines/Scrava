package ui.dialogs;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import javax.swing.filechooser.FileFilter;

import domain.Sprite;
import ui.components.SpritePanel;

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
		setBounds(100, 100, 400, 150);
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
		JLabel lblNewLabel_1 = new JLabel("Set name");
		panel_name.add(lblNewLabel_1);
		textField = new JTextField();
		textField.setColumns(10);
		textField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z' || e.getKeyChar() >= 'A' && e.getKeyChar() <= 'Z' || e.getKeyChar() == '_');
				else e.consume();
			}
			
		});
		panel_name.add(textField);
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
			SpritePanel.addSprite(new Sprite(textField.getText(), img));
			dispose();
		});
		buttonPane.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> dispose());
		buttonPane.add(cancelButton);
	}
	private static class ImageFilter extends FileFilter {
		private static final Set<String> supportedSuffixes = new HashSet<>(Arrays.asList(ImageIO.getReaderFileSuffixes()));
		@Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            String fileName = file.getName().toLowerCase();
            for (String suffix : supportedSuffixes) {
                if (fileName.endsWith("." + suffix)) {
                    return true;
                }
            }
            return false;
        }
		@Override
        public String getDescription() {
            return "Image Files (" + String.join(", ", supportedSuffixes) + ")";
        }
	}
}
