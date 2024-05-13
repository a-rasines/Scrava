package ui.components;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import debug.DebugOut;
import domain.Sprite;
import domain.blocks.event.OnKeyPressEventBlock;
import domain.blocks.event.OnStartEventBlock;
import ui.EmptyLayout;

public class ProjectFrame extends JFrame implements WindowFocusListener {
	public static boolean isStarted = false;
	static {
		DebugOut.setup();
	}
	
	public static void main(String[] args) {
//		for (long longVal = 4946144450195624L; longVal > 0; longVal >>= 5)
//			System.out.print((char) (((longVal & 31 | 64) % 95) + 32));
		INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		INSTANCE.setVisible(true);
	}
	
	public static final ProjectFrame INSTANCE = new ProjectFrame();
	private static final long serialVersionUID = -4157218152821931601L;

	public String projectName;
	private static List<Sprite> projectSprites;
	private static boolean isFocused = true;
	
	private ProjectFrame() {
		JButton startButton = new JButton("Start");
		JButton tickButton = new JButton("Tick");
		JButton endButton = new JButton("End");
		projectName = "Untitled";
		projectSprites = new ArrayList<>();
		projectSprites.add(new Sprite());
		setLayout(new EmptyLayout());
		add(BlockPanel.INSTANCE);
		add(ActionPanel.INSTANCE);
		add(startButton);
		add(tickButton);
		add(endButton);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		setMinimumSize(new Dimension(Math.max(700, (int)screenSize.getWidth() / 2), Math.max(500, (int)screenSize.getHeight() / 2)));
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				BlockPanel.INSTANCE.setBounds((int) (getWidth()*2/5)+10, 0, (int) (getWidth()*3/5) - 25, getHeight());
				int w = (int) (getWidth()*2/5);
				ActionPanel.INSTANCE.setBounds(0, 0, w, w * 2 / 3);
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
							System.out.println("Pressed");
							for(Sprite s : getSprites())
								s.runKeyEvent(OnKeyPressEventBlock.class, e.getKeyCode());
							repaint();
							break;
					}
				return false;
			}
        	
        });
        addWindowFocusListener(this);
		
		startButton.addActionListener((e) -> {
			if(isStarted)
				for(Sprite s : getSprites())
					s.reset();
			for(Sprite s : getSprites())
				s.runEvent(OnStartEventBlock.class);
			ActionPanel.INSTANCE.repaint();
			isStarted = true;
		});
		endButton.addActionListener((e) -> {
			for(Sprite s : getSprites())
				s.reset();
			isStarted = false;
		});
	}
	
	public void loadProject(List<Sprite> newSprites) {
		
	}
	
	public static  Sprite getDefSprite() {
		return projectSprites.get(0);
	}
	public static List<Sprite> getSprites() {
		return new ArrayList<>(projectSprites);
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
