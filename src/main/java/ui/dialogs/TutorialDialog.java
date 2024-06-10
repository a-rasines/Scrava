package ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import parsers.MdParser;
import remote.ClientController;
import remote.ClientController.Tutorial;
import server.ScravaProto.ObjectDescriptor;
import server.ScravaProto.Query;

public class TutorialDialog extends ScDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private DefaultListModel<IdObject> dlm;
	private JList<IdObject> list;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TutorialDialog dialog = new TutorialDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static record IdObject(int id, String name) {
		@Override
		public final String toString() {
			return name;
		}
	}

	/**
	 * Create the dialog.
	 */
	public TutorialDialog() {
		System.out.println("init");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			
			dlm = new DefaultListModel<>();
			contentPanel.setLayout(new BorderLayout(0, 0));
			list = new JList<>(dlm);
			refreshList(null);
			JScrollPane scrollPane = new JScrollPane(list);
			contentPanel.add(scrollPane);
			{
				JPanel panel = new JPanel();
				scrollPane.setColumnHeaderView(panel);
				{
					JLabel lblNewLabel = new JLabel("Search:");
					panel.add(lblNewLabel);
				}
				{
					textField = new JTextField();
					panel.add(textField);
					textField.setColumns(24);
				}
				{
					JButton searchButton = new JButton("Search");
					searchButton.addActionListener(this::refreshList);
					panel.add(searchButton);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton openButton = new JButton("Open");
				openButton.addActionListener(this::open);
				buttonPane.add(openButton);
				getRootPane().setDefaultButton(openButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void refreshList(ActionEvent e) {
		dlm.clear();
		System.out.println("Loading");
		new Thread(() -> {
			Iterator<ObjectDescriptor> iter = ClientController.INSTANCE.getTutorialList(Query.newBuilder().setQuery("name LIKE '%" + textField.getText().replace("'", "''") + "%'").build());
			while(iter.hasNext()) {
				ObjectDescriptor th = iter.next();
				dlm.addElement(new IdObject(th.getId(), th.getName()));
			}
		}).start();
	}
	
	private void open(ActionEvent e) {
		if(list.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(null, "Select a tutorial to open");
			return;
		}
		IdObject selected = list.getSelectedValue();
		File temp = new File("tt_" + selected.id + ".html");
		try (FileOutputStream fos = new FileOutputStream(temp)) {
			temp.createNewFile();
			temp.deleteOnExit();
			Tutorial so = ClientController.INSTANCE.getTutorial(selected.id);
			System.out.println(so.content());
			fos.write(MdParser.tutorialToHTML(so).getBytes());
			Desktop.getDesktop().browse(temp.toURI());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Something went wrong: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			return;
		}
		
	}

}
