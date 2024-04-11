package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ValueSelector extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ValueSelector dialog = new ValueSelector(new JTextField());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ValueSelector(final JTextField textField) {
		setBounds(100, 100, 410, 107);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
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
