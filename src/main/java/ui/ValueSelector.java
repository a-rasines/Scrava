package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ValueSelector extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ValueSelector dialog = new ValueSelector(new JTextField(24), STRING + INTEGER_NUMBERS + DECIMAL_NUMBERS);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final int STRING = 1;
	public static final int DECIMAL_NUMBERS = 2;
	public static final int INTEGER_NUMBERS = 4;

	/**
	 * Create the dialog.
	 */
	public ValueSelector(final JTextField textField, int allowedValues) {
		setBounds(100, 100, 410, 107);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
//		JComboBox<Class<?>> comboBox = new JComboBox<Class<?>>();
//		comboBox.setRenderer(new ListCellRenderer<>() {
//
//			@Override
//			public Component getListCellRendererComponent(JList<? extends Class<?>> list, Class<?> value, int index,
//					boolean isSelected, boolean cellHasFocus) {
//				return new JLabel(value.getSimpleName());
//			}
//			
//		});
//		contentPanel.add(comboBox);
//		if((allowedValues & STRING) != 0)
//			comboBox.addItem(String.class);
//		if((allowedValues & INTEGER_NUMBERS) != 0) {
//			comboBox.addItem(Long.class);
//			comboBox.addItem(Integer.class);
//			comboBox.addItem(Short.class);
//			comboBox.addItem(Byte.class);
//		}
//		if((allowedValues & DECIMAL_NUMBERS) != 0) {
//			comboBox.addItem(Float.class);
//			comboBox.addItem(Double.class);
//		} TODO add
		JLabel lblNewLabel = new JLabel("Select new value:");
		contentPanel.add(lblNewLabel);
		contentPanel.add(textField);
		textField.setColumns(24);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener((e)->dispose());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		String defaultValue = textField.getText();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> {
			textField.setText(defaultValue);
			dispose();
		});
		buttonPane.add(cancelButton);
		textField.selectAll();
	}

}
