package domain;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import domain.blocks.event.KeyEventBlock;
import domain.blocks.event.OnStartEventBlock;
import domain.models.types.EventBlock;
import domain.values.Variable;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.DragableRenderer;

/**
 * This class represents the Sprite types inside the simulation
 */
public class Sprite implements Serializable{
	private static final long serialVersionUID = 2195406778691654466L;
	
	private String name;
	private Variable<Long> xPos = Variable.createVariable(this, "x", 0l, true);
	private Variable<Long> yPos = Variable.createVariable(this, "y", 0l, true);
	private final List<DragableRenderer> blocks = new LinkedList<>();
	private transient Map<Class<? extends EventBlock>, List<EventBlock>> eventMap = null;
	private static final BufferedImage DEFAULT_TEXTURE = IRenderer.getRes("textures/sprite/def.svg");
	private List<BufferedImage> textures = new ArrayList<>();
	private int selectedTexture = 0;
	
	public Sprite() {
		eventMap = new HashMap<>();
		Variable.registerSprite(this);
		textures.add(DEFAULT_TEXTURE);
		name = "Sprite";
	}
	
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        eventMap = new HashMap<>();
		for(DragableRenderer dr : blocks)
			if(dr.getBlock() instanceof EventBlock eb) {
				eventMap.putIfAbsent(eb.getClass(), new LinkedList<>());
				eventMap.get(eb.getClass()).add(eb);
			}
    }
	
	public void registerEvent(EventBlock event) {
		eventMap.putIfAbsent(event.getClass(), new LinkedList<>());
		eventMap.get(event.getClass()).add(event);
	}
	
	public List<EventBlock> getEvents(Class<? extends EventBlock> type) {
		eventMap.putIfAbsent(type, new LinkedList<>());
		return eventMap.get(type);
	}
	
	public void reset() {
		xPos.reset();
		yPos.reset();
		for(List<EventBlock> l : eventMap.values())
			for(EventBlock eb : l)
				eb.reset();
	}
	
	public void deleteEvent(EventBlock event) {
		eventMap.get(event.getClass()).remove(event);
		event.getRenderer().delete();
	}
	
	public void runEvent(Class<? extends EventBlock> type) {
		if(eventMap.containsKey(type))
			for(EventBlock eb : eventMap.get(type))
				eb.invoke();
	}
	
	public BufferedImage getRendered() {
		return textures.get(selectedTexture);
	}
	
	public void onStart() {
		for(EventBlock eb : eventMap.get(OnStartEventBlock.class)) {
			new Thread(() -> eb.invoke()).start();
		}
	}
	
	public void runKeyEvent(Class<? extends KeyEventBlock> ev, int key) {
		eventMap.putIfAbsent(ev, new ArrayList<>());
		for(EventBlock keb : eventMap.get(ev))
			((KeyEventBlock)keb).invoke(key);
		
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
	public Variable<Long> getX() {
		return xPos;
	}
	
	/**
	 * Gets the y position handler of the sprite
	 * @return The {@link domain.values.Variable Variable} corresponding to the Y position
	 */
	public Variable<Long> getY() {
		return yPos;
	}
	
	public List<DragableRenderer> getBlocks() {
		return blocks;
	}
}
