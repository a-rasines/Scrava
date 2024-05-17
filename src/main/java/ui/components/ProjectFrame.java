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
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JFrame;

import debug.DebugOut;
import domain.Sprite;
import domain.blocks.event.OnKeyPressEventBlock;
import domain.blocks.event.OnStartEventBlock;
import domain.models.types.EventBlock;
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
	private static final List<Supplier<Boolean>> activeTicks = new ArrayList<>();
	
	public String projectName;
	private static boolean isFocused = true;
	private static boolean isTick = false;
	
	public static boolean isTick() {
		return isTick;
	}
	
	private ProjectFrame() {
		JButton startButton = new JButton("Start");
		JButton tickButton = new JButton("Tick");
		JButton endButton = new JButton("End");
		projectName = "Untitled";
		setLayout(new EmptyLayout());
		add(BlockPanel.INSTANCE);
		add(ActionPanel.INSTANCE);
		add(SpritePanel.INSTANCE);
		SpritePanel.addSprite(new Sprite());
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
				SpritePanel.INSTANCE.setBounds(0, 30 + w * 2 / 3, w, getHeight() - w * 2 / 3 - 30);
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
							for(Sprite s : SpritePanel.getSprites())
								s.runKeyEvent(OnKeyPressEventBlock.class, e.getKeyCode());
							repaint();
							break;
					}
				return false;
			}
        	
        });
        addWindowFocusListener(this);
		
		startButton.addActionListener((e) -> {
			isTick = false;
			activeTicks.clear();
			List<Sprite> l = SpritePanel.getSprites();
			if(isStarted)
				for(Sprite s : l)
					s.reset();
			for(Sprite s : l)
				s.runEvent(OnStartEventBlock.class);
			ActionPanel.INSTANCE.repaint();
			isStarted = true;
		});
		tickButton.addActionListener((e) -> {
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
		});
		endButton.addActionListener((e) -> {
			activeTicks.clear();
			for(Sprite s : SpritePanel.getSprites())
				s.reset();
			isStarted = false;
		});
	}
	
	public void loadProject(List<Sprite> newSprites) {
		
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
