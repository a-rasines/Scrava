package domain;

import domain.values.Variable;

/**
 * This class represents the Sprite types inside the simulation
 */
public class Sprite {
	private String name;
	private Variable<Long> xPos = Variable.createVariable(this, "x", 0l, true);
	private Variable<Long> yPos = Variable.createVariable(this, "y", 0l, true);
	
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
