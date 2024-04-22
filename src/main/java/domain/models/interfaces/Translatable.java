package domain.models.interfaces;

import java.io.Serializable;
import java.util.Set;

import domain.Sprite;

/**
 * This interface represents a block that can be turned into a piece of code
 */
public interface Translatable extends Serializable{
	/**
	 * This function returns the generated code
	 * @return
	 */
	public String getCode();

	/**
	 * Adds the imports needed to make the class work
	 * @param imports
	 */
	public void getImports(Set<String> imports);
	
	/**
	 * Creates a new empty instance of the block
	 * @param s
	 */
	public Translatable create(Sprite s);
}
