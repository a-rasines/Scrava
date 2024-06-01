package ui.components;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import remote.ClientController;
import server.ScravaProto.ClientData;
import ui.dialogs.server.LoginDialog;
import java.awt.FlowLayout;

public class UserPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final UserPanel INSTANCE = new UserPanel();
	
	/**
	 * Create the panel.
	 */
	private UserPanel() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		regenerate();
	}
	
	public void regenerate() {
		removeAll();
		ClientData user = ClientController.INSTANCE.getUser();
		if(user != null) {
			JLabel lblNewLabel = new JLabel(user.getName());
			add(lblNewLabel);
		
			JButton btnNewButton = new JButton("Log off");
			btnNewButton.addActionListener(e->{
				ClientController.INSTANCE.logoff();
				OnlineProjectsScrollPane.INSTANCE.regenerate();
				regenerate();
			});
			add(btnNewButton);
		} else {
			JButton btnNewButton = new JButton("Log in / Register");
			btnNewButton.addActionListener(e->{
				if(!LoginDialog.isAlreadyOpen())
					new LoginDialog().setVisible(true);
			});
			add(btnNewButton);
		}
		revalidate();
	}

}
