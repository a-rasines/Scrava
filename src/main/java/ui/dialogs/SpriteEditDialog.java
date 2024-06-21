package ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import domain.Sprite;
import ui.ImageFilter;
import ui.components.ActionPanel;
import ui.components.SpritePanel;
import ui.listeners.DoubleKeyListener;
import ui.listeners.NameKeyListener;

public class SpriteEditDialog extends ScDialog implements ListCellRenderer<BufferedImage> {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SpriteEditDialog dialog = new SpriteEditDialog(new Sprite());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SpriteEditDialog(Sprite s) {
		DefaultListModel<BufferedImage> dlm = new DefaultListModel<>();
		dlm.addAll(s.getTextures());
		JList<BufferedImage> list = new JList<>(dlm);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(1);
		list.setCellRenderer(this);
		list.setSelectedIndex(s.getSelectedTexture());
		
		//setResizable(false);
		JLabel selectedTextureLbl = new JLabel(new ImageIcon(s.getRendered()));
		
		setBounds(100, 100, 570, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(1, 0));
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				panel.add(panel_1);
				panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				panel_1.add(selectedTextureLbl);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				panel_1.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					{
						JLabel lblNewLabel = new JLabel("Name:");
						panel_2.add(lblNewLabel);
					}
					{
						textField = new JTextField();
						panel_2.add(textField);
						
						textField.setText(s.getName());
						textField.addKeyListener(new NameKeyListener());
						textField.setColumns(20);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					{
						JLabel lblNewLabel_1 = new JLabel("Scale:");
						panel_2.add(lblNewLabel_1);
					}
					{
						textField_1 = new JTextField();
						textField_1.setText(s.getScale().value().toString());
						textField_1.addKeyListener(new DoubleKeyListener(textField_1));
						list.addListSelectionListener((e) -> {
							BufferedImage temp = list.getSelectedValue();
							if(temp == null) {
								list.setSelectedIndex(0);
								return;
							}
							double scale = 1;
							try {
								scale = Double.parseDouble(textField_1.getText());
							} catch (Exception _e) {}
							selectedTextureLbl.setIcon(new ImageIcon(temp.getScaledInstance((int)(temp.getWidth() * scale), (int)(temp.getHeight() * scale), Image.SCALE_FAST)));
								
						});
						panel_2.add(textField_1);
						textField_1.setColumns(10);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					{
						JButton addTextureButton = new JButton("New Texture");
						addTextureButton.addActionListener((e) -> {
							JFileChooser fileChooser = new JFileChooser();
							 fileChooser.setAcceptAllFileFilterUsed(false);
							 fileChooser.addChoosableFileFilter(new ImageFilter());
							 int result = fileChooser.showOpenDialog(SpriteEditDialog.this);
							 if (result == JFileChooser.APPROVE_OPTION) {
								 try {
									 BufferedImage temp = ImageIO.read(fileChooser.getSelectedFile());
									 dlm.addElement(temp);
								 } catch(IOException ex) {
									 JOptionPane.showMessageDialog(null, "Error loading the image: " + ex.getMessage(), "Error on load", JOptionPane.ERROR_MESSAGE);
								 }
							 }
						});
						panel_2.add(addTextureButton);
					}
					{
						JButton btnRemoveTexture = new JButton("Remove Texture");
						btnRemoveTexture.addActionListener((e) -> {
							if(dlm.size() > 1 && list.getSelectedIndex() != -1) {
								BufferedImage bi = list.getSelectedValue();
								dlm.remove(list.getSelectedIndex());
								s.setSelectedTexture(0);
								s.getTextures().remove(bi);
							}
						});
						panel_2.add(btnRemoveTexture);
					}
				}
			}
		}
		{
			
			JScrollPane scrollPane = new JScrollPane(list);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			contentPanel.add(scrollPane);			
			scrollPane.setBorder(new TitledBorder(null, "Textures", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener((e) -> {
					if(textField.getText().length() == 0) {
						JOptionPane.showMessageDialog(null, "The sprite needs a name");
						return;
					}
					
					if(SpriteCreateDialog.RESERVED_KEYWORDS.contains(textField.getText())) {
						JOptionPane.showMessageDialog(null, "The name is a reserved keyword in Java, please select another");
						return;
					}
					
					if(textField.getText().charAt(0) >= '0' && textField.getText().charAt(0) <= '9') {
						JOptionPane.showMessageDialog(null, "The name cannot start with a number");
						return;
					}
					
					if(textField.getText().matches("_*")) {
						JOptionPane.showMessageDialog(null, "Name must contain at least one alphabetic letter");
						return;
					}
					
					for(Sprite _s : SpritePanel.getSprites())
						if(_s.getName().equals(textField.getText()) && _s != s) {
							JOptionPane.showMessageDialog(null, "There's already an sprite with that name");
							return;
						}
					s.setName(textField.getText());
					
					try {
						s.getScale().setValue(Double.parseDouble(textField_1.getText()), true);
					} catch(NumberFormatException _e) {}
					
					List<BufferedImage> textures = s.getTextures();
					textures.clear();
					Enumeration<BufferedImage> el = dlm.elements();
					while(el.hasMoreElements())
						textures.add(el.nextElement());
					s.setSelectedTexture(list.getSelectedIndex());
					ActionPanel.INSTANCE.repaint();
					dispose();
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton deleteButton = new JButton("Delete");
				deleteButton.setEnabled(SpritePanel.getSprites().size() > 1);
				deleteButton.addActionListener((e) -> {
					s.delete();
					dispose();
				});
				buttonPane.add(deleteButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(e -> dispose());
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends BufferedImage> list, BufferedImage value, int index,
		boolean isSelected, boolean cellHasFocus) {
		Image bi;
		double scale = 90. / value.getHeight();
		int width = (int)(value.getWidth() * scale);
		if(isSelected) {
			bi = new BufferedImage(width, 100, BufferedImage.TYPE_3BYTE_BGR);
		 	Graphics g = bi.getGraphics();
			g.setColor(UIManager.getColor("Tree.selectionBackground"));
			g.fillRect(0, 0, width, 100);
			g.drawImage(value.getScaledInstance(width, (int)(scale * value.getHeight()), Image.SCALE_FAST), 0, 5, null);
		}
		else
			bi = value.getScaledInstance((int)(scale * value.getWidth()), (int)(scale * value.getHeight()), Image.SCALE_FAST);
		return new JLabel(new ImageIcon(bi));
	}

}
