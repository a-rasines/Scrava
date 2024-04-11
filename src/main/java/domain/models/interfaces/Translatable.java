package domain.models.interfaces;

import java.io.Serializable;
import java.util.Set;

/**
 * This interface represents a block that can be turned into a piece of code
 */
public interface Translatable extends Serializable{
	/**
	 * This function returns the generated code
	 * @return
	 */
	public String getCode();

	public void getImports(Set<String> imports);
}
