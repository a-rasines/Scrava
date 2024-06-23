package domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import domain.values.DinamicVariable;
import domain.values.IVariable;

public abstract class AbstractSprite implements Comparable<AbstractSprite>, Serializable{
	private static final long serialVersionUID = -2611052011981081031L;
	private int position = -1;
	
	private Map<String, IVariable<?>> variableMap = new HashMap<>();
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public int compareTo(AbstractSprite o) {
		return position - o.position;
	}
	
	public void registerVariable(String name, IVariable<?> v) {
		variableMap.putIfAbsent(name, v);
	}
	
	public void removeVariable(String name) {
		variableMap.remove(name);
	}
	
	public IVariable<?> getVariable(String name) {
		return variableMap.get(name);
	}
	public Map<String, IVariable<?>> getVariables() {
		return new HashMap<>(variableMap);
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		for(IVariable<?> v : new ArrayList<>(variableMap.values()))
			if(v instanceof DinamicVariable<?> dv)
				variableMap.remove(dv.getName());
			
        oos.defaultWriteObject();
    }
	
	private void readObject(ObjectInputStream ois)  throws IOException, ClassNotFoundException  {
		ois.defaultReadObject();
		if(this instanceof Sprite s)
			Project.insertVariables(s);
			
    }
}
