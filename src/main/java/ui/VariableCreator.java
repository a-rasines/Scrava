package ui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VariableCreator extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameField;
	private JTextField valueField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VariableCreator frame = new VariableCreator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VariableCreator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 180);
		setResizable(false);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel namePanel = new JPanel();
		contentPane.add(namePanel);
		namePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel newNameLbl = new JLabel("Variable's name:");
		namePanel.add(newNameLbl);
		
		nameField = new JTextField();
		nameField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() != 32 && e.getKeyChar() != 95 && (e.getKeyChar() < 65 || e.getKeyChar() > 90 && e.getKeyChar() < 97 || e.getKeyChar() > 122))
					e.consume();
			}
		});
		namePanel.add(nameField);
		nameField.setColumns(10);
		
		JCheckBox globalVariable = new JCheckBox("Is global?");
		namePanel.add(globalVariable);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel valueTypeLbl = new JLabel("Value type:");
		panel.add(valueTypeLbl);
		
		JComboBox<VariableType> valueType = new JComboBox<>(VariableType.values());
		valueType.setSelectedIndex(1);
		panel.add(valueType);
		
		JLabel lblNewLabel = new JLabel("Initial value:");
		panel.add(lblNewLabel);
		
		valueField = new JTextField();
		panel.add(valueField);
		valueField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JLabel valuesLbl = new JLabel("Value range:");
		panel_2.add(valuesLbl);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		valueType.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				VariableType value = ((VariableType)valueType.getSelectedItem());
				if(value.desc != "" && !value.name().endsWith("DIVIDER"))
					valuesLbl.setText("Value range: " + value.desc);
				else
					valuesLbl.setText("Value range:");
				
			}
		});
		
		JButton createButton = new JButton("Create");
		createButton.addActionListener((v) -> {
			
		});
		panel_1.add(createButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((v) -> this.dispose());
		panel_1.add(cancelButton);
	}
	private static enum VariableType {
		_GENERAL_DIVIDER("GENERAL"),
		TEXT(""),
		NUMBER(Long.MIN_VALUE + " -> " + Long.MAX_VALUE),
		DECIMAL_NUMBER(Double.MIN_VALUE + " -> " + Double.MAX_VALUE),
		_STR_DIVIDER("TEXT"),
		STRING(""),
		_INT_DIVIDER("INTEGERS"),
		LONG(Long.MIN_VALUE + " -> " + Long.MAX_VALUE),
		INT(Integer.MIN_VALUE + " -> " + Integer.MAX_VALUE),
		SHORT(Short.MIN_VALUE + " -> " + Short.MAX_VALUE),
		BYTE(Byte.MIN_VALUE + " -> " + Byte.MAX_VALUE),
		_DEC_DIVIDER("DECIMALS"),
		DOUBLE(Double.MIN_VALUE + " -> " + Double.MAX_VALUE),
		FLOAT(Float.MIN_VALUE + " -> " + Float.MAX_VALUE);
		
		private final String desc;
		VariableType(String desc){
			this.desc = desc;
		}
		
		public String toString() {
			if(name().endsWith("_DIVIDER"))
				return "----"+desc+"----";
			return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
		}
		
	}

}
