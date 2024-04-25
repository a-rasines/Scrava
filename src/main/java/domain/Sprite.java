package domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import domain.blocks.event.OnStartEventBlock;
import domain.models.types.EventBlock;
import domain.values.Variable;

/**
 * This class represents the Sprite types inside the simulation
 */
public class Sprite {
	private String name;
	private Variable<Long> xPos = Variable.createVariable(this, "x", 0l, true);
	private Variable<Long> yPos = Variable.createVariable(this, "y", 0l, true);
	private Map<Class<? extends EventBlock>, List<EventBlock>> eventMap = new HashMap<>();
	
	
	public void registerEvent(EventBlock event) {
		eventMap.putIfAbsent(event.getClass(), new LinkedList<>());
		eventMap.get(event.getClass()).add(event);
	}
	
	public void onStart() {
		for(EventBlock eb : eventMap.get(OnStartEventBlock.class)) {
			new Thread(() -> eb.invoke()).start();
		}
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
}
