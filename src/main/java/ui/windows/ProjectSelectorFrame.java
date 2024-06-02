package ui.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import debug.DebugOut;
import domain.AppCache;
import domain.AppCache.ProjectData;
import remote.ClientController;
import domain.Project;
import ui.components.OnlineProjectsScrollPane;
import ui.components.UserPanel;

public class ProjectSelectorFrame extends JFrame implements WindowFocusListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public static final ProjectSelectorFrame INSTANCE = new ProjectSelectorFrame();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DebugOut.setup();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					INSTANCE.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProjectSelectorFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		addWindowFocusListener(this);
		
		contentPane.add(UserPanel.INSTANCE, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		DefaultListModel<ProjectData> plm = new DefaultListModel<>();
		for(ProjectData pd : AppCache.getInstance().importedProjects)
			plm.addElement(pd);
		JList<ProjectData> projectList = new JList<>(plm);
		JScrollPane scrollPane = new JScrollPane(projectList);
		tabbedPane.addTab("Local Projects", null, scrollPane, null);
		
		tabbedPane.addTab("Online Projects", null, OnlineProjectsScrollPane.INSTANCE, null);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(e -> {
			if(tabbedPane.getSelectedIndex() == 0) {
				if(projectList.getSelectedIndex() != -1) {
					Project.readProject(projectList.getSelectedValue().file());
					ProjectFrame.INSTANCE.setVisible(true);
					setVisible(false);
				}
			} else {
				if (OnlineProjectsScrollPane.INSTANCE.getSelectedIndex() != -1) {
					ClientController.INSTANCE.getProject(OnlineProjectsScrollPane.INSTANCE.getSelectedValue().id());
					ProjectFrame.INSTANCE.setVisible(true);
					setVisible(false);
				}
			}
		});
		panel.add(btnOpen);
		
		JButton btnDelete = new JButton("Delete");
		panel.add(btnDelete);
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ProjectSelectorFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
            	if(Project.readProject(fileChooser.getSelectedFile())) {
            		ProjectData pd = new ProjectData(Project.getActiveProject().name, fileChooser.getSelectedFile());
            		AppCache.getInstance().importedProjects.add(pd);
            		plm.addElement(pd);
            		AppCache.save();
            		ProjectFrame.INSTANCE.setVisible(true);
            		setVisible(false);
            	}
            	
            }
		});
		panel.add(btnImport);
		
		tabbedPane.addChangeListener(e -> {
			btnImport.setEnabled(tabbedPane.getSelectedIndex() == 0);
			
			JButton btnNew = new JButton("New");
			panel.add(btnNew);
		});
	}
	
	private static boolean isFocused = true;
	
	public static boolean isFocus() {
		return isFocused;
	}
	
	@Override
	public void windowGainedFocus(WindowEvent e) {
		isFocused = true;		
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		isFocused = false;
	}

}
