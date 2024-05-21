package domain.values;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.EnumLiteral.EnumCapable;
import ui.components.BlockPanel;
import ui.components.ProjectFrame;
import ui.components.SpritePanel;
import ui.renderers.IRenderer.IRenderable;
import ui.renderers.LiteralRenderer.LiteralRenderable;
import ui.renderers.SimpleBlockRenderer;
import ui.renderers.SimpleBlockRenderer.SimpleRenderable;

/**
 * Represents a sprite or global variable
 * @param <T>
 */
public class Variable<T> extends AbstractLiteral<T> implements SimpleRenderable {

	private static final long serialVersionUID = 6146036704484981438L;
	/**
	 * In this list all the variables get stored sorted by Sprite
	 * 
	 * null Sprite = global variable
	 */
	private static HashMap<Sprite, HashMap<String, Variable<?>>> variables = new HashMap<>();
	
	static {
		variables.put(null, new HashMap<>());
	}
	
	public static void registerSprite(Sprite s) {
		variables.putIfAbsent(s, new HashMap<>());
	}
	
	public static void saveProject(File f) {
		try {
			ProjectFrame.INSTANCE.reset();
			FileOutputStream fileOut = new FileOutputStream(f);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(variables);
	        out.close();
	        fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	@SuppressWarnings("unchecked")
	public static void readProject(File f) {
		try {
			FileInputStream fileIn = new FileInputStream(f);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        var temp = (HashMap<Sprite, HashMap<String, Variable<?>>>) in.readObject();
	        in.close();
	        fileIn.close();
	        
	        // ONLY if temp is read correctly changes are applied
	        
	        variables = temp;
	        SpritePanel.clearSprites();
	        for(Sprite s : variables.keySet())
	        	if(s != null)
	        		SpritePanel.addSprite(s);
	        ProjectFrame.INSTANCE.repaint();
	        BlockPanel.INSTANCE.repaint();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "The project could no tbe loaded: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ClassNotFoundException e) { 
			//(really) unlike
			e.printStackTrace();
		}
	}
	
	@Override
	public AbstractLiteral<T> create(Sprite s) {
		return this;
	}
	
	 
	private static class VariableEnumCapable implements EnumCapable<Variable<?>> {
		private final static VariableEnumCapable INSTANCE = new VariableEnumCapable();
		private static final long serialVersionUID = -3026004184272864437L;

		@Override
		public Variable<?> valueof(String value) {
			Variable<?> v = variables.get(null).get(value);
			if(v == null)
				v = variables.get(SpritePanel.getSprite()).get(value);
			return v;
		}

		@Override
		public Variable<?>[] getValues() {
			System.out.println(variables);
			Variable<?>[] var = new Variable<?>[variables.get(null).size() + variables.get(SpritePanel.getSprite()).size()];
			Iterator<Variable<?>> it = variables.get(null).values().iterator();
			for(int i = 0; i < variables.get(null).size(); i++)
				var[i] = (Variable<?>) it.next();
			it = variables.get(SpritePanel.getSprite()).values().iterator();
			for(int i = variables.get(null).size(); i < variables.get(null).size() + variables.get(SpritePanel.getSprite()).size(); i++)
				var[i] = (Variable<?>) it.next();
			return var;
		}

		@Override
		public String[] names() {
			HashSet<String> s = new HashSet<>();
			s.addAll(variables.get(null).keySet());
			s.addAll(variables.get(SpritePanel.getSprite()).keySet());
			return s.toArray(new String[s.size()]);
		}
		
	}
	
	/**
	 * Returns an {@link domain.values.EnumLiteral EnumLiteral} version of the variables' map
	 * @return
	 */
	public static EnumLiteral<Variable<?>> getEnumLiteral(IRenderable parent) {
		
		return new EnumLiteral<>(VariableEnumCapable.INSTANCE, parent);
	}
	
	/**
	 * Creates a global variable of the desired type with the desired initial value
	 * @param <T> Type of the variable
	 * @param name Unique name of the variable
	 * @param value Initial value of the variable
	 * @return
	 */
	public static <T> Variable<T> createGlobalVariable(String name, T value) {
		return createVariable(null, name, value);
	}
	
	/**
	 * Create a local variable with the desired name and the default value
	 * @param <T> Type of the variable
	 * @param s Sprite where the variable is stored
	 * @param name name of the variable
	 * @param value initial value of the variable
	 * @return
	 */
	public static <T> Variable<T> createVariable(Sprite s, String name, T value) {
		return createVariable(s, name, value, false);
	}
	/**
	 * Create a local variable with the desired name and the default value with the option of creating it native (not deletable)
	 * @param <T> Type of the variable
	 * @param s Sprite where the variable is stored
	 * @param name name of the variable
	 * @param value initial value of the variable
	 * @param whether it must be inmune to variable deleting
	 * @return
	 */
	public static <T> Variable<T> createVariable(Sprite s, String name, T value, boolean nat) {
		Variable<T> var = new Variable<T>(name, value, s, nat);
		variables.putIfAbsent(s, new HashMap<>());
		variables.get(s).put(name, var);
		return var;
	}
	
	public static Variable<?> getGlobalVariable(String name) {
		return getVariable(null, name);
	}
	
	public static Variable<?> getVariable(Sprite s, String name) {
		return variables.get(s).get(name);
	}
	
	public static List<Variable<?>> getVisibleVariables() {
		List<Variable<?>> output = new ArrayList<>(variables.get(null).values());
		output.addAll(variables.get(SpritePanel.getSprite()).values());
		return output;
	}
	
	
	public final String name;
	private boolean nat;
	public final Sprite sprite;
	
	private Variable(String name, T value, Sprite sprite, boolean isNative) {
		super(value, null);
		this.name = name;
		this.sprite = sprite;
		this.nat = isNative;
	}
	@Override
	public SimpleBlockRenderer getRenderer() {
		return new SimpleBlockRenderer(this);
	}
	
	/**
	 * Native variables are those that are generated by the sprite itself and therefore not deletable
	 * @return
	 */
	public boolean isNative() {
		return nat;
	}
	
	public boolean isGlobal() {
		return variables.get(null).get(this.name) != null;
	}
	
	public Variable<?> setValue(T value) {
		this.value = value;
		return this;
	}
	
	public Class<?> getValueClass() {
		return value.getClass();
	}
	
	public String toString() {
		return (sprite==null?"GlobalVariables":sprite.getName()) +"."+name; 
	}
	@Override
	public T value() {
		return value;
	}
	@Override
	public String getCode() {
		return this.name;
	}
	
	/**
	 * Gets the line representing the definition of the variable
	 * e.g: String varname = "defaultValue";
	 * @return
	 */
	public String getDefinition() {
		return value.getClass().getName() + " " + name + " = " + ((initialValue() instanceof String)?'"'+initialValue().toString()+"'":initialValue())+";";
	}
	@Override
	public void getImports(Set<String> imports) {}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(String str) {
		if(initialValue() instanceof String)
			this.setValue((T)str, true);
		else if(initialValue() instanceof Number)
			this.setValue((T)NumberHelper.parse(str, (Class<? extends Number>) value.getClass()), true);
		else if(initialValue() instanceof Boolean)
			this.setValue((T)(Boolean)Boolean.parseBoolean(str), true);
	}
	@Override
	public Valuable<?> getVariableAt(int i) {return null;} // N/A
	public Valuable<?>[] getAllVariables() {return new Valuable[0];} // N/A
	public void setVariableAt(int i, Valuable<?> v) {} // N/A
	public void removeVariableAt(int i) {} // N/A
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {return null;} // N/A
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {} // N/A
	
	@Override
	public boolean isAplicable(Valuable<?> v) {
		return false;
	}
	@Override
	public String getTitle() {
		return name;
	}
	@Override
	public BlockCategory getCategory() {
		if(value() instanceof Number)
			return BlockCategory.NUMBER_VARIABLE;
		else if(value() instanceof Boolean)
			return BlockCategory.BOOLEAN_VARIABLE;
		else
			return BlockCategory.STRING_VARIABLE;
	}

}
