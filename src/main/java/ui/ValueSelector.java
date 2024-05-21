package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ui.components.ProjectFrame;

public class ValueSelector extends JDialog implements WindowFocusListener, WindowListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	public static final int STRING = 1;
	public static final int DECIMAL_NUMBERS = 2;
	public static final int INTEGER_NUMBERS = 4;
	
	private static boolean isAlreadyOpen = false;
	public static boolean isAlreadyOpen() {
		return isAlreadyOpen;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ValueSelector(JComboBox comboBox) {
		isAlreadyOpen = true;
		Object def = comboBox.getSelectedItem();
		setBounds(100, 100, 300, 120);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Select new value:");
		contentPanel.add(lblNewLabel);
		contentPanel.add(comboBox);
		comboBox.setPrototypeDisplayValue("This is 21 characters");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener((e)-> dispose());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> {
			comboBox.setSelectedItem(def);
			dispose();
		});
		buttonPane.add(cancelButton);
		addWindowFocusListener(this);
		addWindowListener(this);
	}
	
	/**
	 * Create the dialog.
	 */
	public ValueSelector(final JTextField textField, int allowedValues) {
		isAlreadyOpen = true;
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
		addWindowFocusListener(this);
		addWindowListener(this);
	}
	
	@Override
	public void windowLostFocus(WindowEvent e) {
		if(!ProjectFrame.isFocus())requestFocus();	
	}

	@Override
	public void windowClosing(WindowEvent e) {
		isAlreadyOpen = false;
	}
	
	@Override
	public void dispose() {
		windowClosing(null);
		super.dispose();
	}
	
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowGainedFocus(WindowEvent e) {}
	@Override public void windowOpened(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}

}
