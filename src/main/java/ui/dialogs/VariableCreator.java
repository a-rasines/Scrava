package ui.dialogs;

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.Project;
import domain.values.StaticVariable;
import ui.components.BlockSelectorPanel;
import ui.components.SpritePanel;
import ui.listeners.NumberKeyListener;

public class VariableCreator extends ScDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameField;
	private static JTextField valueField = new JTextField();
	private static JCheckBox booleanBox = new JCheckBox();
	private static JComboBox<VariableType> valueType = new JComboBox<>(VariableType.values());
	static {
		valueField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				Function<Character, Boolean> filter = ((VariableType)valueType.getSelectedItem()).keyFilter;
				if(!filter.apply(e.getKeyChar()))
					e.consume();
			}
			
		});
	}

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
				if(e.getKeyChar() != '_' && (e.getKeyChar() < 'A' || e.getKeyChar() > 'Z' && e.getKeyChar() < 'a' || e.getKeyChar() > 'z'))
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
		
		valueType.setSelectedIndex(1);
		panel.add(valueType);
		
		JLabel lblNewLabel = new JLabel("Initial value:");
		panel.add(lblNewLabel);
		
		panel.add(valueField);
		valueField.setColumns(10);
		
		panel.add(booleanBox);
		
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
				valueField.setVisible(value != VariableType.BOOLEAN);
				booleanBox.setVisible(value == VariableType.BOOLEAN);
				if(value.desc != "" && !value.name().endsWith("DIVIDER"))
					valuesLbl.setText("Value range: " + value.desc);
				else
					valuesLbl.setText("Value range:");
				
				char[] actualValue = valueField.getText().toCharArray();
				for(char c : actualValue)
					if(!value.keyFilter.apply(c))
						valueField.setText("");
				if(value.keyFilter != Filter.STRING_FILTER.keyFilter && valueField.getText().split("\\.").length > 2)
					valueField.setText("");
			}
		});
		
		JButton createButton = new JButton("Create");
		createButton.addActionListener((v) -> {
			if(nameField.getText().length() == 0){
				JOptionPane.showMessageDialog(null, "Variable needs a name");
				return;
			}
			if( valueField.isVisible()								&& 
				valueField.getText().length() == 0 					&& 
				valueType.getSelectedItem() != VariableType.STRING 	&& 
				valueType.getSelectedItem() != VariableType.TEXT) {
				JOptionPane.showMessageDialog(null, "Variable needs a value");
				return;
			}
			if(globalVariable.isSelected()) {
				if(Project.getActiveProject().getGlobalVariables().get(nameField.getText()) != null) {
					JOptionPane.showMessageDialog(null, "Variable with that name already exists");
					return;
				}else {
					StaticVariable.createGlobalVariable(nameField.getText(), ((VariableType)valueType.getSelectedItem()).parser.apply(valueField.getText()));
					BlockSelectorPanel.INSTANCE.update();
					dispose();
				}
			} else if(SpritePanel.getSprite().getVariable(nameField.getText()) != null) {
				JOptionPane.showMessageDialog(null, "Variable with that name already exists");
				return;
			}else {
				if(valueField.isVisible())
					StaticVariable.createVariable(SpritePanel.getSprite(), nameField.getText(), ((VariableType)valueType.getSelectedItem()).parser.apply(valueField.getText()));
				else
					StaticVariable.createVariable(SpritePanel.getSprite(), nameField.getText(), booleanBox.isSelected());
				BlockSelectorPanel.INSTANCE.update();
				dispose();
			}			
		});
		panel_1.add(createButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((v) -> this.dispose());
		panel_1.add(cancelButton);
	}
	private static final Function<String, String> EMPTY_PARSER = (v)->v;
	private static enum Filter {
		STRING_FILTER((v)->true),
		DIVIDER_FILTER((v)->true),
		INTEGER_FILTER(new NumberKeyListener(valueField, false)::check),
		DECIMAL_FILTER(new NumberKeyListener(valueField, true)::check),		
		;
		
		
		public final Function<Character, Boolean> keyFilter;
		
		Filter(Function<Character, Boolean> keyFilter) {
			this.keyFilter = keyFilter;
		}
	}
	
	private static enum VariableType {
		
		// TYPE                    VAL RANGE / DESC                           FILTER          PARSER
		_GENERAL_DIVIDER("GENERAL",                                      Filter.DIVIDER_FILTER, EMPTY_PARSER),
		
		TEXT(            "'' -> Your computer's ram",                    Filter.STRING_FILTER,  EMPTY_PARSER),
		NUMBER(          Long.MIN_VALUE + " -> " + Long.MAX_VALUE,       Filter.INTEGER_FILTER, Long::parseLong),
		DECIMAL_NUMBER(  Double.MIN_VALUE + " -> " + Double.MAX_VALUE,   Filter.DECIMAL_FILTER, Double::parseDouble),
		BOOLEAN(		 "true or false",								 Filter.STRING_FILTER,  null),
		
		_STR_DIVIDER(    "TEXT",                                         Filter.DIVIDER_FILTER, EMPTY_PARSER),
		
		STRING(          "'' -> Your computer's ram",                    Filter.STRING_FILTER,  EMPTY_PARSER),
		
		_INT_DIVIDER(    "INTEGERS",                                     Filter.DIVIDER_FILTER, EMPTY_PARSER),
		
		LONG(            Long.MIN_VALUE + " -> " + Long.MAX_VALUE,       Filter.INTEGER_FILTER, Long::parseLong),
		INT(             Integer.MIN_VALUE + " -> " + Integer.MAX_VALUE, Filter.INTEGER_FILTER, Integer::parseInt),
		SHORT(           Short.MIN_VALUE + " -> " + Short.MAX_VALUE,     Filter.INTEGER_FILTER, Short::parseShort),
		BYTE(            Byte.MIN_VALUE + " -> " + Byte.MAX_VALUE,       Filter.INTEGER_FILTER, Byte::parseByte),
		
		_DEC_DIVIDER(    "DECIMALS",                                     Filter.DIVIDER_FILTER, EMPTY_PARSER),
		
		DOUBLE(          Double.MIN_VALUE + " -> " + Double.MAX_VALUE,   Filter.DECIMAL_FILTER, Double::parseDouble),
		FLOAT(           Float.MIN_VALUE + " -> " + Float.MAX_VALUE,     Filter.DECIMAL_FILTER, Float::parseFloat);
		
		
		/**
		 * This is a free string to be used with the value.
		 * 
		 * For all values ending in DIVIDER is the group over which it is.
		 * 
		 * For all the rest of values, it's the range.
		 */
		private final String desc;
		/**
		 * This is a filter to make parseable strings
		 * @param char character to check
		 * @return true if that character can be transformed by the parser
		 */
		public final Function<Character, Boolean> keyFilter;
		/**
		 * This is an indirect call to the parser of the class.
		 * @return the input string in the value's format
		 */
		public final Function<String, ? extends Object> parser;
		VariableType(String desc, Filter keyFilter, Function<String, ? extends Object> parser){
			this.desc = desc;
			this.keyFilter = keyFilter.keyFilter;
			this.parser = parser;
		}
		
		public String toString() {
			if(name().endsWith("_DIVIDER"))
				return "----"+desc+"----";
			return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
		}
		
	}

}
