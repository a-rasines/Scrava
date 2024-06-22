package ui.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.FileNotFoundException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import domain.AppCache;
import domain.AppCache.ProjectData;
import domain.Project;
import remote.ClientController;
import ui.components.OnlineProjectsScrollPane;
import ui.components.UserPanel;
import ui.renderers.IRenderer;

public class ProjectSelectorFrame extends JFrame implements WindowFocusListener, ListCellRenderer<ProjectData> {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public static final Image ICON = IRenderer.getRes("textures/icon.svg");
	
	public static final ProjectSelectorFrame INSTANCE = new ProjectSelectorFrame();

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		DebugOut.setup();
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					INSTANCE.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public DefaultListModel<ProjectData> plm = new DefaultListModel<>();
	
	/**
	 * Create the frame.
	 */
	private ProjectSelectorFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Project selector - Scrava");
		setIconImage(ICON);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		addWindowFocusListener(this);
		
		contentPane.add(UserPanel.INSTANCE, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		for(ProjectData pd : AppCache.getInstance().importedProjects)
			plm.addElement(pd);
		JList<ProjectData> projectList = new JList<>(plm);
		projectList.setFixedCellHeight(50);
		projectList.setCellRenderer(this);
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
					try {
						Project.readProject(projectList.getSelectedValue().file());
					} catch (FileNotFoundException _e) {
						JOptionPane.showMessageDialog(null, "The project has been moved / deleted");
						AppCache.getInstance().importedProjects.remove(projectList.getSelectedValue());
						AppCache.save();
						plm.removeElement(projectList.getSelectedValue());
						return;
					}
					setVisible(false);
					ProjectFrame.INSTANCE.setVisible(true);
				}
			} else {
				if (OnlineProjectsScrollPane.INSTANCE.getSelectedIndex() != -1) {
					Project.setProject(ClientController.INSTANCE.getProject(OnlineProjectsScrollPane.INSTANCE.getSelectedValue().id()));
					setVisible(false);
					ProjectFrame.INSTANCE.setVisible(true);
				}
			}
		});
		panel.add(btnOpen);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(e -> {
			if(tabbedPane.getSelectedIndex() == 0) {
				if(projectList.getSelectedIndex() != -1) {
					ProjectData pd = projectList.getSelectedValue();
					switch(JOptionPane.showConfirmDialog(null, "Delete also from storage?")) {
						case JOptionPane.OK_OPTION:
							pd.file().delete();
						case JOptionPane.NO_OPTION:
							AppCache.getInstance().importedProjects.remove(pd);
							AppCache.save();
							plm.removeElement(pd);
					}
				}
			} else {
				
			}
		});
		panel.add(btnDelete);
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ProjectSelectorFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
            	try {
					if(Project.readProject(fileChooser.getSelectedFile())) {
						ProjectData pd = new ProjectData(Project.getActiveProject().name, fileChooser.getSelectedFile());
						AppCache.getInstance().importedProjects.add(pd);
						plm.addElement(pd);
						AppCache.save();
						ProjectFrame.INSTANCE.setVisible(true);
						setVisible(false);
					}
				} catch (FileNotFoundException e1) { //Unlike
					e1.printStackTrace();
				}
            	
            }
		});
		panel.add(btnImport);
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(e -> {
			String name = "";
			while(name.length() == 0) {
				name = JOptionPane.showInputDialog("Set project's name", "New Project");
				if(name == null) return;
			}
			Project.newProject(name);
			ProjectFrame.INSTANCE.setVisible(true);
			setVisible(false);
		});
		panel.add(btnNew);
		
		tabbedPane.addChangeListener(e -> {
			btnImport.setEnabled(tabbedPane.getSelectedIndex() == 0);
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

	@Override
	public Component getListCellRendererComponent(JList<? extends ProjectData> list, ProjectData value, int index,
			boolean isSelected, boolean cellHasFocus) {
		JPanel output = new JPanel(new BorderLayout());
		if(isSelected)
			output.setBackground(list.getSelectionBackground());
		else
			output.setBackground(Color.white);
		JLabel titleLabel = new JLabel(value.name());
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        output.add(titleLabel, BorderLayout.NORTH);
        
        JLabel subtitleLabel = new JLabel(value.file().getAbsolutePath());
        subtitleLabel.setFont(new Font("Dialog", Font.ITALIC, 10));
        output.add(subtitleLabel, BorderLayout.CENTER);
        
        JPanel linePanel = new JPanel() {
			private static final long serialVersionUID = 5654765985053899746L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawLine(0, 0, getWidth(), 0);
            }
        };
        if(isSelected)
        	linePanel.setBackground(list.getSelectionBackground());
		else
			linePanel.setBackground(Color.white);
        linePanel.setPreferredSize(new Dimension(1, 1));
        output.add(linePanel, BorderLayout.SOUTH);
		
		return output;
	}

}
