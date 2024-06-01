package ui.windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import debug.DebugOut;
import ui.components.OnlineProjectsScrollPane;
import ui.components.UserPanel;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;

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
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Local Projects", null, scrollPane, null);
		
		tabbedPane.addTab("Online Projects", null, OnlineProjectsScrollPane.INSTANCE, null);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.setEnabled(false);
		panel.add(btnOpen);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		panel.add(btnDelete);
		
		JButton btnImport = new JButton("Import");
		panel.add(btnImport);
		
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

}
