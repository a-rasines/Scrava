package ui.dialogs.server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import io.grpc.StatusRuntimeException;
import remote.ClientController;
import ui.components.OnlineProjectsScrollPane;
import ui.components.UserPanel;
import ui.dialogs.ScDialog;

public class LoginDialog extends ScDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginDialog dialog = new LoginDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LoginDialog() {
		setBounds(100, 100, 330, 140);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel);
			{
				JLabel lblNewLabel = new JLabel("Username:");
				panel.add(lblNewLabel);
			}
			{
				textField = new JTextField();
				panel.add(textField);
				textField.setColumns(20);
			}
		}
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel);
			{
				JLabel lblNewLabel_1 = new JLabel("Password:");
				panel.add(lblNewLabel_1);
			}
			{
				passwordField = new JPasswordField();
				passwordField.setColumns(20);
				panel.add(passwordField);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton loginButton = new JButton("Login");
				loginButton.addActionListener((e)-> {
					if(textField.getText().length() == 0 || passwordField.getPassword().length == 0)
						JOptionPane.showMessageDialog(null, "Must fill user and password");
					else 
						try {
							if(ClientController.INSTANCE.login(textField.getText(), String.copyValueOf(passwordField.getPassword())) == null) {
								JOptionPane.showMessageDialog(null, "Unable to connect to server", "Conection error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							UserPanel.INSTANCE.regenerate();
							OnlineProjectsScrollPane.INSTANCE.regenerate();
							dispose();
						} catch(StatusRuntimeException _e) {
							_e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Error logging in:" + _e.getMessage().substring(_e.getMessage().indexOf(":") + 1), "Unexpected error", JOptionPane.ERROR_MESSAGE);
						}
				});
				buttonPane.add(loginButton);
				getRootPane().setDefaultButton(loginButton);
			}
			{
				JButton registerButton = new JButton("Register");
				registerButton.addActionListener((e)-> {
					if(textField.getText().length() == 0 || passwordField.getPassword().length == 0)
						JOptionPane.showMessageDialog(null, "Must fill user and password");
					else if (passwordField.getPassword().length < 8 || passwordField.getPassword().length > 256)
						JOptionPane.showMessageDialog(null, "Password must be between 8 and 256 characters long");
					else 
						try {
							if(ClientController.INSTANCE.register(textField.getText(), String.copyValueOf(passwordField.getPassword())) == null) {
								JOptionPane.showMessageDialog(null, "Unable to connect to server", "Conection error", JOptionPane.ERROR_MESSAGE);
								return;
							}
							UserPanel.INSTANCE.regenerate();
							OnlineProjectsScrollPane.INSTANCE.regenerate();
							dispose();
						} catch(StatusRuntimeException _e) {
							JOptionPane.showMessageDialog(null, "Error registering:" + _e.getMessage().substring(_e.getMessage().indexOf(":") + 1));
						}
				});
				buttonPane.add(registerButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(e->dispose());
				buttonPane.add(cancelButton);
			}
		}
	}

}
