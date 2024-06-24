package domain;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import domain.blocks.event.EventThread;
import domain.blocks.event.KeyEventBlock;
import domain.blocks.event.OnStartEventBlock;
import domain.models.types.EventBlock;
import domain.values.StaticVariable;
import ui.components.ActionPanel;
import ui.components.SpritePanel;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;

/**
 * This class represents the Sprite types inside the simulation
 */
public class Sprite extends AbstractSprite {
	private static final long serialVersionUID = 2195406778691654466L;
	
	private String name;
	private Project p;
	private StaticVariable<Long> xPos;
	private StaticVariable<Long> yPos;
	private StaticVariable<Double> scale;
	private StaticVariable<Double> rotation;
	private final List<DragableRenderer> blocks = new LinkedList<>();
	private transient Map<Class<? extends EventBlock>, List<EventBlock>> eventMap = null;
	public static final BufferedImage DEFAULT_TEXTURE = IRenderer.getRes("textures/sprite/def.svg");
	private transient List<BufferedImage> textures = new ArrayList<>();
	private int selectedTexture = 0;
	
	public Sprite() {
		this("Sprite", DEFAULT_TEXTURE);
	}
	
	public Sprite(String name, BufferedImage texture) {
		this(Project.getActiveProject(), name, texture);
	}
	
	public Sprite(Project p, String name, BufferedImage texture) {
		eventMap = new HashMap<>();
		textures.add(texture);
		this.name = name;
		this.p = p;
		scale = StaticVariable.createVariable(this, "scale", 1., true);
		xPos = StaticVariable.createVariable(this, "x", 0l, true);
		yPos = StaticVariable.createVariable(this, "y", 0l, true);
		rotation = StaticVariable.createVariable(this, "rotation", 0., true);
	}
	//																													EVENTS

	public void registerEvent(EventBlock event) {
		eventMap.putIfAbsent(event.getClass(), new LinkedList<>());
		eventMap.get(event.getClass()).add(event);
	}
	
	public void deleteEvent(EventBlock event) {
		event.getRenderer().delete();
	}
	
	public void _deleteEvent(EventBlock event) {
		eventMap.get(event.getClass()).remove(event);
	}
	
	public void runEvent(Class<? extends EventBlock> type) {
		if(eventMap.containsKey(type))
			for(EventBlock eb : eventMap.get(type))
				new EventThread(eb::invoke).start();
	}
	
	public List<EventBlock> getEvents(Class<? extends EventBlock> type) {
		eventMap.putIfAbsent(type, new LinkedList<>());
		return eventMap.get(type);
	}
	
	public void onStart() {
		for(EventBlock eb : eventMap.get(OnStartEventBlock.class)) {
			new EventThread(() -> eb.invoke()).start();
		}
	}
	
	public void runKeyEvent(Class<? extends KeyEventBlock> ev, int key) {
		eventMap.putIfAbsent(ev, new ArrayList<>());
		for(EventBlock keb : eventMap.get(ev))
			((KeyEventBlock)keb).invoke(key);
		
	}
	
	public void reset() {
		xPos.reset();
		yPos.reset();
		for(List<EventBlock> l : eventMap.values())
			for(EventBlock eb : l)
				eb.reset();
	}
	
	//																											TEXTURE
	private transient Image resized = null;
	private transient double res_sc = 0;
	private transient int ap_w = 0;
	public Image getRendered() {
		if(resized != null && res_sc == scale.value() && ActionPanel.INSTANCE.getWidth() == ap_w)
			return resized;
		else {
			res_sc = scale.value();
			ap_w = ActionPanel.INSTANCE.getWidth();
			BufferedImage fullSize = textures.get(selectedTexture);
			if(ap_w == 0) return fullSize;
			return (resized = fullSize.getScaledInstance(
					(int)(fullSize.getWidth() * scale.value() * ap_w / 1000), 
					(int)(fullSize.getHeight() * scale.value() * ap_w / 1000), 
					BufferedImage.SCALE_FAST));
		}
	}
	
	//																											GETTERS / SETTERS
	
	public Project getProject() {
		return p;
	}

	public void setSelectedTexture(int st) {
		if(st >= 0) {
			this.selectedTexture = st % textures.size();
			this.resized = null;
			ActionPanel.INSTANCE.repaint();
		}
	}
	
	
	public int getSelectedTexture() {
		return selectedTexture;
	}
	
	public void setSelectedTexture(BufferedImage bi) {
		this.selectedTexture = getTextures().indexOf(bi);
		ActionPanel.INSTANCE.repaint();
	}
	
	public List<BufferedImage> getTextures() {
		return textures;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	/**
	 * Gets the Sprite's unique name
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**	
	 * Gets the x position handler of the sprite
	 * @return The {@link domain.values.Variable Variable} corresponding to the X position
	 */
	public StaticVariable<Long> getX() {
		return xPos;
	}
	
	/**
	 * Gets the y position handler of the sprite
	 * @return The {@link domain.values.Variable Variable} corresponding to the Y position
	 */
	public StaticVariable<Long> getY() {
		return yPos;
	}
	
	public StaticVariable<Double> getScale() {
		return scale;
	}
	
	public List<DragableRenderer> getBlocks() {
		return blocks;
	}
	
	public StaticVariable<Double> getRotation() {
		return rotation;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        List<byte[]> images = new LinkedList<>();
        for(BufferedImage bi : textures) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "png", baos);
			images.add(baos.toByteArray());
		}
        oos.writeObject(images);
    }

    @SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        List<byte[]> images = (List<byte[]>) ois.readObject();
        this.textures = new ArrayList<>();
        for(byte[] b : images) {
        	ByteArrayInputStream bais = new ByteArrayInputStream(b);
            textures.add(ImageIO.read(bais));
        }
        eventMap = new HashMap<>();
		for(DragableRenderer dr : blocks)
			if(dr.getBlock() instanceof EventBlock eb) {
				eventMap.putIfAbsent(eb.getClass(), new LinkedList<>());
				eventMap.get(eb.getClass()).add(eb);
			}
    }
    
    public void delete() {
    	Project.getActiveProject().deleteSprite(this);
    	SpritePanel.deleteSprite(this);
    }
    
    @Override
    public String toString() {
    	return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
