package ui.dialogs.server;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import domain.AppCache;
import parsers.MdParser;
import remote.ClientController;
import remote.ClientController.IdObject;
import remote.ClientController.Tutorial;
import server.ScravaProto.ObjectDescriptor;
import server.ScravaProto.Query;
import ui.dialogs.ScDialog;

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
					refreshList(null);
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
				JButton uploadButton = new JButton("Upload");
				uploadButton.addActionListener(this::upload);
				buttonPane.add(uploadButton);
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
		new Thread(() -> {
			Iterator<ObjectDescriptor> iter = ClientController.INSTANCE.getTutorialList(Query.newBuilder().setQuery("name LIKE '%" + textField.getText().replace("'", "''") + "%' OR content like '%" + textField.getText().replace("'", "''") + "%'").build());
			if(iter == null) {
				JOptionPane.showMessageDialog(null, "Unable to connect to server", "Conection error", JOptionPane.ERROR_MESSAGE);
				dispose();
				return;
			}
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
		File temp = new File("tt_" + selected.id() + ".html");
		try (FileOutputStream fos = new FileOutputStream(temp)) {
			temp.createNewFile();
			temp.deleteOnExit();
			Tutorial so = ClientController.INSTANCE.getTutorial(selected.id());
			System.out.println(so.content());
			byte[] out = MdParser.tutorialToHTML(so).getBytes();
			System.out.println(new String(out, StandardCharsets.UTF_8));
			fos.write(out);
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
	
	private void upload(ActionEvent e) {
		
		if(AppCache.getInstance().user == null) {
			JOptionPane.showMessageDialog(null, "Connect to your account to use this feature");
			return;
		}
		
		List<IdObject> tutorials = ClientController.INSTANCE.getUserTutorials();
		if(tutorials == null) {
			JOptionPane.showMessageDialog(null, "Unable to connect to server", "Conection error", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		} 		
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".md") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "HTML files";
			}
			 
		 });
		 int result = fileChooser.showOpenDialog(this);
		 if (result == JFileChooser.APPROVE_OPTION) {
			String content = "";
			try (FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile())) {
				content = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
			} catch (IOException _e) {
				_e.printStackTrace();
			}	
			tutorials.add(0, new IdObject(-1, "-- New --"));
	        JComboBox<IdObject> comboBox = new JComboBox<>();
	        tutorials.forEach(comboBox::addItem);

	        JPanel panel = new JPanel();
	        panel.add(comboBox);

	        result = JOptionPane.showConfirmDialog(null, panel, 
	                "Select the tutorial to replace:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
	        	String origName = switch(((IdObject)comboBox.getSelectedItem()).id()) {
		        	case -1 -> "";
		        	default -> ((IdObject)comboBox.getSelectedItem()).name();			        	
	        	};
	        	JTextField field = new JTextField(origName, 20);
	        	panel = new JPanel();
		        panel.add(field);

		        result = JOptionPane.showConfirmDialog(null, panel, 
		                "Set the new name", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        if(result == JOptionPane.OK_OPTION) {
		        	ClientController.INSTANCE.saveTutorial(((IdObject)comboBox.getSelectedItem()).id(), field.getText(), content);
		        	refreshList(null);
		        }
	        	
	        }
		 }
	}

}
