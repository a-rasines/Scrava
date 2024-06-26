package ui.windows;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import domain.AppCache;
import domain.AppCache.ProjectData;
import domain.Project;
import domain.Sprite;
import domain.blocks.event.EventThread;
import domain.blocks.event.OnKeyPressEventBlock;
import domain.blocks.event.OnStartEventBlock;
import domain.models.types.EventBlock;
import parsers.ProjectExporter;
import remote.ClientController;
import ui.EmptyLayout;
import ui.components.ActionPanel;
import ui.components.BlockPanel;
import ui.components.OnlineProjectsScrollPane;
import ui.components.SpritePanel;
import ui.dialogs.server.LoginDialog;
import ui.dialogs.server.TutorialDialog;

public class ProjectFrame extends JFrame implements WindowFocusListener {
	public static boolean isStarted = false;
//	public static void main(String[] args) {
////		for (long longVal = 4946144450195624L; longVal > 0; longVal >>= 5)
////			System.out.print((char) (((longVal & 31 | 64) % 95) + 32));
//		INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		INSTANCE.setVisible(true);
//	}
	
	public static final ProjectFrame INSTANCE = new ProjectFrame();
	private static final long serialVersionUID = -4157218152821931601L;
	private static final List<Supplier<Boolean>> activeTicks = new ArrayList<>();
	
	private static boolean isFocused = true;
	private static boolean isTick = false;
	
	public static boolean isTick() {
		return isTick;
	}
	
	public static boolean isFocus() {
		return isFocused;
	}
	
	private ProjectFrame() {
		JButton startButton = new JButton("Start");
		JButton tickButton = new JButton("Tick");
		JButton endButton = new JButton("End");
		
		JPanel layerPanel = new JPanel();
		layerPanel.setBorder(BorderFactory.createTitledBorder("Sprite Layer"));
		JButton upButton = new JButton("Up");
		JButton downButton = new JButton("Down");
		layerPanel.add(upButton);
		layerPanel.add(downButton);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(new EmptyLayout());
		setIconImage(ProjectSelectorFrame.ICON);
		add(BlockPanel.INSTANCE);
		add(ActionPanel.INSTANCE);
		add(SpritePanel.INSTANCE);
		add(startButton);
		add(tickButton);
		add(endButton);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, (int)screenSize.getWidth() - 50, (int)screenSize.getHeight() - 50);
		setMinimumSize(new Dimension(Math.max(700, (int)screenSize.getWidth() / 2), Math.max(500, (int)screenSize.getHeight() / 2)));
		
		addComponentListener(new ComponentAdapter() {
			private final int DOWN_OFFSET = 60;
			@Override
			public void componentResized(ComponentEvent e) {
				BlockPanel.INSTANCE.setBounds((int) (getWidth()*2/5)+10, 0, (int) (getWidth()*3/5) - 25, getHeight() - DOWN_OFFSET);
				int w = (int) (getWidth()*2/5);
				ActionPanel.INSTANCE.setBounds(0, 0, w, w * 2 / 3);
				SpritePanel.INSTANCE.setBounds(0, 30 + w * 2 / 3, w, getHeight() - w * 2 / 3 - 35 - DOWN_OFFSET);
				startButton.setBounds(0, w * 2/3, 100, 20);
				tickButton.setBounds(100, w * 2/3, 100, 20);
				endButton.setBounds(200, w * 2/3, 100, 20);
			}
		});
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if(isStarted && isFocused)
					switch(e.getID()) {
						case KeyEvent.KEY_RELEASED:
							System.out.println("Released");
							break;
							
						case KeyEvent.KEY_PRESSED:
							System.out.println("Pressed " + e.getKeyChar());
							for(Sprite s : SpritePanel.getSprites())
								s.runKeyEvent(OnKeyPressEventBlock.class, e.getKeyCode());
							repaint();
							break;
					}
				return false;
			}
        	
        });
        addWindowFocusListener(this);
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		setVisible(false);
        		ProjectSelectorFrame.INSTANCE.setVisible(true);
        	}
        });
		
		startButton.addActionListener(this::start);
		tickButton.addActionListener(this::tick);
		endButton.addActionListener(this::reset);
		
		upButton.addActionListener((e) -> {
			
		});
		
		generateJMenu();
	}
	
	private void start(ActionEvent e) {
		isTick = false;
		activeTicks.clear();
		List<Sprite> l = SpritePanel.getSprites();
		EventThread.killThreads();
		if(isStarted)
			for(Sprite s : l)
				s.reset();
		for(Sprite s : l)
			s.runEvent(OnStartEventBlock.class);
		System.out.println("start");
		ActionPanel.INSTANCE.repaint();
		isStarted = true;
	}
	
	private void tick(ActionEvent e) {
		EventThread.killThreads();
		isTick = true;
		if(isStarted == false) {
			System.out.println("Collecting ticks");
			for(Sprite s : SpritePanel.getSprites())
				for(EventBlock eb : s.getEvents(OnStartEventBlock.class))
					activeTicks.add(eb.getTick());
			isStarted = true;
		}
		System.out.println("Running ticks");
		for(Supplier<Boolean> s : new ArrayList<>(activeTicks))
			if(s.get())
				activeTicks.remove(s);
		ActionPanel.INSTANCE.repaint();
	}
	
	private void reset(ActionEvent e) {
		reset();
	}
	
	public void reset() {
		EventThread.killThreads();
		activeTicks.clear();
		for(Sprite s : SpritePanel.getSprites())
			s.reset();
		isStarted = false;
		ActionPanel.INSTANCE.repaint();
	}
	
	private void generateJMenu() {
		JMenuBar menuBar = new JMenuBar();									this.setJMenuBar(menuBar);
        	JMenu archiveMenu = new JMenu("Archive");							menuBar.add(archiveMenu);
        		JMenuItem openMenuItem = new JMenuItem("Open");						archiveMenu.add(openMenuItem);
        		JMenuItem saveMenuItem = new JMenuItem("Save"); 					archiveMenu.add(saveMenuItem);
        		JMenu saveAsMenu = new JMenu("Save as");							archiveMenu.add(saveAsMenu);
        			JMenuItem toFileMenuItem = new JMenuItem("To File");				saveAsMenu.add(toFileMenuItem);
        			JMenuItem toServerMenuItem = new JMenuItem("To Server");			saveAsMenu.add(toServerMenuItem);
        		JMenuItem exportMenuItem = new JMenuItem("Export");					archiveMenu.add(exportMenuItem);
        		JMenuItem exitMenuItem = new JMenuItem("Exit");						archiveMenu.add(exitMenuItem);
    		JMenuItem resourcesMenu = new JMenuItem("Resources");						menuBar.add(resourcesMenu);
    		
    	openMenuItem.addActionListener(e -> {
    		ProjectSelectorFrame.INSTANCE.setVisible(true);
    		ProjectSelectorFrame.INSTANCE.repaint();
    		setVisible(false);
    	});
    	
    	exportMenuItem.addActionListener((e) -> {
    		JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(ProjectFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
            	try {
					ProjectExporter.exportTo(new File(fileChooser.getSelectedFile().getAbsolutePath() + "/" + Project.getActiveProject().name));
					JOptionPane.showMessageDialog(null, "Project succesfully exported");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(this, "Something went wrong exporting the project: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            }
    	});
    	
    	saveMenuItem.addActionListener((e) -> {
    		reset();
    		Project.getActiveProject().save();
    	});
    	
    	toFileMenuItem.addActionListener((e) -> {
    		reset();
    		JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".scrv") || pathname.isDirectory();
				}

				@Override
				public String getDescription() {
					return null;
				}
    			
    		});
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
            	if(fileChooser.getSelectedFile().getAbsolutePath().endsWith(".scrv"))
            		Project.getActiveProject().save(new File(fileChooser.getSelectedFile().getAbsolutePath()));
            	else
            		Project.getActiveProject().save(new File(fileChooser.getSelectedFile().getAbsolutePath() + ".scrv"));
            	ProjectData thls = new ProjectData(Project.getActiveProject().name, Project.getActiveProject().file);
            	ProjectSelectorFrame.INSTANCE.plm.addElement(thls);
            	AppCache.getInstance().importedProjects.add(thls);
            	AppCache.save();
            }
    	});
    	
    	toServerMenuItem.addActionListener((e) -> {
    		reset();
    		if(AppCache.getInstance().user == null) {
    			if(!LoginDialog.isAlreadyOpen())
    				new LoginDialog().setVisible(true);
    		} else {
    			if(ClientController.INSTANCE.saveProject(Project.getActiveProject())) {
    				JOptionPane.showMessageDialog(null, "Poject succesfully uploaded");
    				OnlineProjectsScrollPane.INSTANCE.addElement(new OnlineProjectsScrollPane.Project(Project.getActiveProject().name, Project.getActiveProject().id));
    			}
    			else
    				JOptionPane.showMessageDialog(null, "Unable to connect to server", "Conection error", JOptionPane.ERROR_MESSAGE);
    		}
    	});
    	resourcesMenu.addActionListener((e) -> {
    		System.out.println(TutorialDialog.isAlreadyOpen());
    		if(!TutorialDialog.isAlreadyOpen())
    			new TutorialDialog().setVisible(true);
    	});
    		
	}
	@Override
	public void setSize(Dimension d) {}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		isFocused = true;		
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		isFocused = false;
	}

}
