package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import domain.values.EnumLiteral;
import domain.values.EnumLiteral.EnumCapable;
import domain.values.Variable;
import ui.components.ActionPanel;
import ui.components.BlockPanel;
import ui.components.SpritePanel;
import ui.renderers.IRenderer.IRenderable;
import ui.windows.ProjectFrame;

public class Project implements Serializable {
	
	private static final long serialVersionUID = -7008939450955709656L;
	private static Project active = null;
	public transient File file;
	public int id = -1;
	public String name;
	
	public static Project getActiveProject() {
		return active;
	}
	
	public static void setProject(Project p) {
		System.out.println(p);
		active = p;
		SpritePanel.clearSprites();
		for(Sprite s : p.getSprites())
			if(s != null)
				SpritePanel.addSprite(s);
		BlockPanel.INSTANCE.changeSprite();
		ActionPanel.INSTANCE.repaint();
	}
	
	public Project(String name) {
		if(active == null)
			active = this;
		this.name = name;
		registerSprite(new Sprite());
	}
	
	/**
	 * In this list all the variables get stored sorted by Sprite
	 * 
	 * null Sprite = global variable
	 */
	private HashMap<Sprite, HashMap<String, Variable<?>>> variables = new HashMap<>();
	
	{
		variables.put(null, new HashMap<>());
	}
	
	public void registerSprite(Sprite s) {
		variables.putIfAbsent(s, new HashMap<>());
	}
	
	public void deleteSprite(Sprite s) {
		variables.remove(s);
	}
	
	public Set<Sprite> getSprites() {
		return variables.keySet();
	}
	
	public void registerVariable(Sprite s, String name, Variable<?> v) {
		variables.get(s).putIfAbsent(name, v);
	}
	
	public void removeVariable(Sprite s, String name) {
		variables.get(s).remove(name);
	}
	
	public Variable<?> getVariable(Sprite s, String name) {
		return variables.get(s).get(name);
	}
	public Map<String, Variable<?>> getVariablesOf(Sprite s) {
		return variables.get(s);
	}
	
	public void save() {
		if(file == null) {
			JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
            	String res = JOptionPane.showInputDialog("Set file name:");
            	if(res.length() > 0)
            		file = new File(fileChooser.getSelectedFile().getAbsolutePath() + "/" + res + ".scrv");
            } else return;
		}
		save(file);
	}
	
	public void save(File f) {
		try {
			file = f;
			ProjectFrame.INSTANCE.reset();
			FileOutputStream fileOut = new FileOutputStream(f);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(this);
	        out.close();
	        fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	private final transient VariableEnumCapable ENUM_INSTANCE = new VariableEnumCapable();
	private class VariableEnumCapable implements EnumCapable<Variable<?>> {
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
	public static void readProject(File f) {
		try {
			FileInputStream fileIn = new FileInputStream(f);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        Project temp = (Project) in.readObject();
	        in.close();
	        fileIn.close();
	        
	        // ONLY if temp is read correctly changes are applied
	        
	        temp.file = f;
	        setProject(temp);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "The project could no tbe loaded: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ClassNotFoundException e) { 
			//(really) unlike
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an {@link domain.values.EnumLiteral EnumLiteral} version of the variables' map
	 * @return
	 */
	public EnumLiteral<Variable<?>> getVariablesEnumLiteral(IRenderable parent) {
		
		return new EnumLiteral<>(ENUM_INSTANCE, parent);
	}
}
