package ui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Function;

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
				if(e.getKeyChar() != ' ' && e.getKeyChar() != '_' && (e.getKeyChar() < 'A' || e.getKeyChar() > 'Z' && e.getKeyChar() < 'a' || e.getKeyChar() > 'z'))
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
		valueField.addKeyListener(new KeyAdapter() {
		
			@Override
			public void keyTyped(KeyEvent e) {
				Function<Character, Boolean> filter = ((VariableType)valueType.getSelectedItem()).keyFilter;
				if(filter != STRING_FILTER && e.getKeyChar() == '.' && valueField.getText().contains(".") || !filter.apply(e.getKeyChar()))
					e.consume();
			}
			
		});
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
				
				char[] actualValue = valueField.getText().toCharArray();
				for(char c : actualValue)
					if(!value.keyFilter.apply(c))
						valueField.setText("");
				if(value.keyFilter != STRING_FILTER && valueField.getText().split("\\.").length > 2)
					valueField.setText("");
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
	private static final Function<Character, Boolean> STRING_FILTER = (v)->true;
	private static final Function<Character, Boolean> DIVIDER_FILTER = (v)->true;
	private static final Function<Character, Boolean> INTEGER_FILTER = (v)->v >= '0' && v <= '9';
	private static final Function<Character, Boolean> DECIMAL_FILTER = (v)->v >= '0' && v <= '9' || v == '.';
	private static enum VariableType {
		_GENERAL_DIVIDER("GENERAL", DIVIDER_FILTER),
		TEXT("", STRING_FILTER),
		NUMBER(Long.MIN_VALUE + " -> " + Long.MAX_VALUE, INTEGER_FILTER),
		DECIMAL_NUMBER(Double.MIN_VALUE + " -> " + Double.MAX_VALUE, DECIMAL_FILTER),
		_STR_DIVIDER("TEXT", DIVIDER_FILTER),
		STRING("", STRING_FILTER),
		_INT_DIVIDER("INTEGERS", DIVIDER_FILTER),
		LONG(Long.MIN_VALUE + " -> " + Long.MAX_VALUE, INTEGER_FILTER),
		INT(Integer.MIN_VALUE + " -> " + Integer.MAX_VALUE, INTEGER_FILTER),
		SHORT(Short.MIN_VALUE + " -> " + Short.MAX_VALUE, INTEGER_FILTER),
		BYTE(Byte.MIN_VALUE + " -> " + Byte.MAX_VALUE, INTEGER_FILTER),
		_DEC_DIVIDER("DECIMALS", DIVIDER_FILTER),
		DOUBLE(Double.MIN_VALUE + " -> " + Double.MAX_VALUE, DECIMAL_FILTER),
		FLOAT(Float.MIN_VALUE + " -> " + Float.MAX_VALUE, DECIMAL_FILTER);
		
		private final String desc;
		public final Function<Character, Boolean> keyFilter;
		VariableType(String desc, Function<Character, Boolean> keyFilter){
			this.desc = desc;
			this.keyFilter = keyFilter;
		}
		
		public String toString() {
			if(name().endsWith("_DIVIDER"))
				return "----"+desc+"----";
			return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
		}
		
	}

}
