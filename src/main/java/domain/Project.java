package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import domain.AppCache.ProjectData;
import domain.values.DinamicVariable;
import domain.values.EnumLiteral;
import domain.values.EnumLiteral.EnumCapable;
import domain.values.IVariable;
import domain.values.StaticVariable;
import domain.values.dinamicVariable.SystemTimeMillisVariable;
import remote.ClientController;
import ui.components.ActionPanel;
import ui.components.BlockPanel;
import ui.components.SpritePanel;
import ui.renderers.IRenderer.IRenderable;
import ui.windows.ProjectFrame;

public class Project implements Serializable {
	
	/**
	 * Here the variables get their dinamic and other transient variables
	 * @param s
	 */
	public void insertVariables(Sprite s) {
		
	}
	
	/**
	 * Here the dinamic and other transient global variables are inserted
	 */
	public void insertGlobalVariables() {
		registerVariable(null, "epoch time (ms)", new SystemTimeMillisVariable());
	}
	
	private static final long serialVersionUID = -7008939450955709656L;
	private static Project active = null;
	public transient File file;
	public int id = -1;
	public String name;
	
	public static Project getActiveProject() {
		return active;
	}
	
	public static void setProject(Project p) {
		active = p;
		SpritePanel.clearSprites();
		System.out.println("Sprite count:" + (p.getSprites().size() - 1));
		for(Sprite s : p.getSprites())
			if(s != null)
				SpritePanel.addSprite(s);
		BlockPanel.INSTANCE.changeSprite();
		ActionPanel.INSTANCE.repaint();
	}
	
	public static void newProject(String name) {
		SpritePanel.clearSprites();
		active = new Project(name);
		System.out.println("Sprite count:" + (active.getSprites().size() - 1));
		ActionPanel.INSTANCE.repaint();
	}
	
	public Project(String name) {
		if(active == null)
			active = this;
		this.name = name;
		registerSprite(null);
		insertGlobalVariables();
		new Sprite();
	}
	
	/**
	 * In this list all the variables get stored sorted by Sprite
	 * 
	 * null Sprite = global variable
	 */
	private HashMap<Sprite, HashMap<String, IVariable<?>>> variables = new HashMap<>();
	
	{
		variables.put(null, new HashMap<>());
	}
	
	public synchronized void registerSprite(Sprite s) {
		if(variables.putIfAbsent(s, new HashMap<>()) == null && s != null) {
			if(active == this)
				SpritePanel.addSprite(s);
			insertVariables(s);
		}
		
	}
	
	public void deleteSprite(Sprite s) {
		variables.remove(s);
	}
	
	public Set<Sprite> getSprites() {
		return variables.keySet();
	}
	
	public void registerVariable(Sprite s, String name, IVariable<?> v) {
		if(variables.get(s) == null)
			registerSprite(s);
		variables.get(s).putIfAbsent(name, v);
	}
	
	public void removeVariable(Sprite s, String name) {
		variables.get(s).remove(name);
	}
	
	public IVariable<?> getVariable(Sprite s, String name) {
		return variables.get(s).get(name);
	}
	public Map<String, IVariable<?>> getVariablesOf(Sprite s) {
		return variables.get(s);
	}
	
	public void save() {
		if(file == null && id == -1) {
			JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
            	String res = JOptionPane.showInputDialog("Set file name:");
            	if(res.length() > 0) {
            		file = new File(fileChooser.getSelectedFile().getAbsolutePath() + "/" + res + ".scrv");
            		AppCache.getInstance().importedProjects.add(new ProjectData(name, file));
            		AppCache.save();
            	}
            } else return;
		}
		save(file);
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		for(Map<String, IVariable<?>> s : variables.values())
			for(IVariable<?> v : new ArrayList<>(s.values()))
				if(v instanceof DinamicVariable<?> dv)
					variables.get(dv.getSprite()).remove(dv.getName());
			
        oos.defaultWriteObject();
    }
	
	private void readObject(ObjectInputStream ois)  throws IOException, ClassNotFoundException  {
		ois.defaultReadObject();
		for (Sprite s : variables.keySet()) {
			if(s != null)
				insertVariables(s);
		}
		insertGlobalVariables();
			
    }
	
	public void save(File f) {
		if(id != -1)
			ClientController.INSTANCE.saveProject(this);
		try {
			file = f;
			System.out.println(variables.keySet().size());
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
	
	@SuppressWarnings("rawtypes")
	private transient VariableEnumCapable<IVariable> ENUM_INSTANCE;
	@SuppressWarnings("rawtypes")
	private transient VariableEnumCapable<StaticVariable> STATIC_ENUM_INSTANCE;
	@SuppressWarnings("rawtypes")
	private transient VariableEnumCapable<DinamicVariable> DINAMIC_ENUM_INSTANCE;
	
	@SuppressWarnings("rawtypes")
	private class VariableEnumCapable<T extends IVariable> implements EnumCapable<T> {
		private static final long serialVersionUID = -3026004184272864437L;
		private Class<T> clazz;
		
		public VariableEnumCapable(Class<T> clazz) {
			this.clazz = clazz;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T valueof(String value) {
			T v = (T)variables.get(null).get(value);
			if(v == null)
				v = (T)variables.get(SpritePanel.getSprite()).get(value);
			return v;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<T> getValues() {
			List<T> var = new LinkedList<>();
			Iterator<IVariable<?>> it = variables.get(null).values().iterator();
			for(int i = 0; i < variables.get(null).size(); i++) {
				IVariable<?> next = it.next();
				if(clazz.isInstance(next))
					var.add((T)next);
			}
			it = variables.get(SpritePanel.getSprite()).values().iterator();
			for(int i = variables.get(null).size(); i < variables.get(null).size() + variables.get(SpritePanel.getSprite()).size(); i++) {
				IVariable<?> next = it.next();
				if(clazz.isInstance(next))
					var.add((T)next);
			}
			return var;
		}

		@Override
		public String[] names() {
			HashSet<String> s = new HashSet<>();
			for(Entry<String, IVariable<?>> e : variables.get(null).entrySet())
				if(clazz.isInstance(e.getValue()))
					s.add(e.getKey());
			for(Entry<String, IVariable<?>> e : variables.get(SpritePanel.getSprite()).entrySet())
				if(clazz.isInstance(e.getValue()))
					s.add(e.getKey());
			return s.toArray(new String[s.size()]);
		}
		
	}
	public static boolean readProject(File f) throws FileNotFoundException{
		try {
			FileInputStream fileIn = new FileInputStream(f);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        Project temp = (Project) in.readObject();
	        in.close();
	        fileIn.close();
	        
	        // ONLY if temp is read correctly changes are applied
	        
	        temp.file = f;
	        setProject(temp);
	        return true;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "The project could not be loaded: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ClassNotFoundException e) { 
			//(really) unlike
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Returns an {@link domain.values.EnumLiteral EnumLiteral} version of the variables' map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public EnumLiteral<IVariable> getVariablesEnumLiteral(IRenderable parent) {
		if(ENUM_INSTANCE == null) {
			ENUM_INSTANCE = new VariableEnumCapable<>(IVariable.class);
			STATIC_ENUM_INSTANCE = new VariableEnumCapable<StaticVariable>(StaticVariable.class);
			DINAMIC_ENUM_INSTANCE = new VariableEnumCapable<DinamicVariable>(DinamicVariable.class);
		}
		return new EnumLiteral<IVariable>(ENUM_INSTANCE, parent);
	}
	
	/**
	 * Returns an {@link domain.values.EnumLiteral EnumLiteral} version of the variables' map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public EnumLiteral<StaticVariable> getStaticVariablesEnumLiteral(IRenderable parent) {
		if(STATIC_ENUM_INSTANCE == null) {
			ENUM_INSTANCE = new VariableEnumCapable<>(IVariable.class);
			STATIC_ENUM_INSTANCE = new VariableEnumCapable<StaticVariable>(StaticVariable.class);
			DINAMIC_ENUM_INSTANCE = new VariableEnumCapable<DinamicVariable>(DinamicVariable.class);
		}
		return new EnumLiteral<StaticVariable>(STATIC_ENUM_INSTANCE, parent);
	}
	
	/**
	 * Returns an {@link domain.values.EnumLiteral EnumLiteral} version of the variables' map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public EnumLiteral<DinamicVariable> getDinamicVariablesEnumLiteral(IRenderable parent) {
		if(DINAMIC_ENUM_INSTANCE == null) {
			ENUM_INSTANCE = new VariableEnumCapable<>(IVariable.class);
			STATIC_ENUM_INSTANCE = new VariableEnumCapable<StaticVariable>(StaticVariable.class);
			DINAMIC_ENUM_INSTANCE = new VariableEnumCapable<DinamicVariable>(DinamicVariable.class);
		}
		return new EnumLiteral<DinamicVariable>(DINAMIC_ENUM_INSTANCE, parent);
	}
	
	
}
