package domain.blocks.event;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.EventBlock;
import domain.values.EnumLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class OnKeyPressEventBlock extends EventBlock {

	private static final long serialVersionUID = 5804391234476797763L;
	private static Map<String, Integer> keyMap = new TreeMap<>();
	
	
	private EnumLiteral<Integer> variable = new EnumLiteral<>(keyMap);
	
	static {
		for(Field f : KeyEvent.class.getDeclaredFields())
			if(f.getName().startsWith("VK"))
				try {
					keyMap.put(f.getName().substring(3), f.getInt(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	@Override
	public Valuable<?> getVariableAt(int i) {
		return variable;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable<?>[] {variable};
	}

	@Override
	public void setVariableAt(int i, Valuable<?> v) {} // N/A

	@Override
	public void removeVariableAt(int i) {}  // N/A

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {	return null;}

	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {} 

	@Override
	public boolean isAplicable(Valuable<?> v) {	return false;}

	@Override
	public EventBlock newInstance(Sprite s) {
		return new OnKeyPressEventBlock();
	}

	@Override
	public String getTitle() {
		return "On " + VARIABLE_ENUM + " press";
	}

}
