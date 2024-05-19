package domain.blocks.event;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import domain.models.types.EventBlock;
import domain.values.EnumLiteral;

public abstract class KeyEventBlock extends EventBlock {
	private static final long serialVersionUID = -4600101075423209197L;
	
	private final static Map<String, Integer> KEY_MAP = new TreeMap<>();
	protected final EnumLiteral<Integer> KEY = new EnumLiteral<>(KEY_MAP, this);
	
	static {
		for(Field f : KeyEvent.class.getDeclaredFields())
			if(f.getName().startsWith("VK"))
				try {
					KEY_MAP.put(f.getName().substring(3), f.getInt(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public void invoke(int key) {
		if(KEY.value() == key)
			invoke();
	}

}
