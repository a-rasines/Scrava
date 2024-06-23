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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

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
import ui.windows.ProjectSelectorFrame;

public class Project implements Serializable {
	
	/**
	 * Here the variables get their dinamic and other transient variables
	 * @param s
	 */
	public static void insertVariables(Sprite s) {
		
	}
	
	/**
	 * Here the dinamic and other transient global variables are inserted
	 */
	public void insertGlobalVariables() {
		GLOBAL_VARIABLES.registerVariable("epoch time (ms)", new SystemTimeMillisVariable());
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
		ProjectFrame.INSTANCE.setTitle(p.name + " - Scrava");
		for(Sprite s : p.getSprites())
			SpritePanel.addSprite(s);
		BlockPanel.INSTANCE.changeSprite();
		ActionPanel.INSTANCE.repaint();
	}
	
	public static void newProject(String name) {
		SpritePanel.clearSprites();
		active = new Project(name);
		ProjectFrame.INSTANCE.setTitle(name + " - Scrava");
		System.out.println("Sprite count:" + (active.getSprites().size() - 1));
		ActionPanel.INSTANCE.repaint();
	}
	
	public Project(String name) {
		if(active == null)
			active = this;
		insertGlobalVariables();
		registerSprite(new Sprite());
		this.name = name;
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        insertGlobalVariables();
	}
	
	private void writeObject(ObjectOutputStream ois) throws IOException, ClassNotFoundException {
		for(Entry<String, IVariable<?>> v : GLOBAL_VARIABLES.getVariables().entrySet()) {
			if(v.getValue() instanceof DinamicVariable<?>) {
				GLOBAL_VARIABLES.removeVariable(v.getKey());
			}
		}
        ois.defaultWriteObject();
	}
	
	
	private class GlobalVariables extends AbstractSprite {
		private static final long serialVersionUID = -5163257882372575797L;
		
	}

	private final GlobalVariables GLOBAL_VARIABLES = new GlobalVariables();
	
	public Map<String, IVariable<?>> getGlobalVariables() {
		return GLOBAL_VARIABLES.getVariables();
	}
	
	public void registerGlobalVariable(String name, IVariable<?> value) {
		GLOBAL_VARIABLES.registerVariable(name, value);
	}
	
	/**
	 * In this list all the variables get stored sorted by Sprite
	 * 
	 * null Sprite = global variable
	 */
	private List<Sprite> sprites = new ArrayList<>();
	
	public synchronized void registerSprite(Sprite s) {
		if(sprites.contains(s))return;
		s.setPosition(sprites.size());
		if(sprites.add(s)) {
			if(active == this)
				SpritePanel.addSprite(s);
			insertVariables(s);
		}
	}
	
	public void deleteSprite(Sprite s) {
		sprites.remove(s);
	}
	
	public List<Sprite> getSprites() {
		return new ArrayList<>(sprites);
	}
	
	public void save() {
		if(file == null && id == -1) {
			JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".scrv") || pathname.isDirectory();
				}

				@Override
				public String getDescription() {
					return null;
				}
    			
    		});
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
            	if(fileChooser.getSelectedFile().getAbsolutePath().endsWith(".scrv"))
            		save(new File(fileChooser.getSelectedFile().getAbsolutePath()));
            	else
            		save(new File(fileChooser.getSelectedFile().getAbsolutePath() + ".scrv"));
            	ProjectData thls = new ProjectData(name, file);
            	ProjectSelectorFrame.INSTANCE.plm.addElement(thls);
            	AppCache.getInstance().importedProjects.add(thls);
            	AppCache.save();
            }
		}else if (file != null)
			save(file);
		else
			ClientController.INSTANCE.saveProject(this);
	}
	
	public void save(File f) {
		if(id != -1)
			ClientController.INSTANCE.saveProject(this);
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
			T v = (T)GLOBAL_VARIABLES.getVariable(value);
			if(v == null)
				v = (T)SpritePanel.getSprite().getVariable(value);
			return v;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<T> getValues() {
			List<T> var = new LinkedList<>();
			for(IVariable<?> v : GLOBAL_VARIABLES.getVariables().values()) {
				if(clazz.isInstance(v))
					var.add((T)v);
			}
			for(IVariable<?> v : SpritePanel.getSprite().getVariables().values()) {
				if(clazz.isInstance(v))
					var.add((T)v);
			}
			return var;
		}

		@Override
		public String[] names() {
			HashSet<String> s = new HashSet<>();
			for(Entry<String, IVariable<?>> e : SpritePanel.getSprite().getVariables().entrySet())
				if(clazz.isInstance(e.getValue()))
					s.add(e.getKey());
			for(Entry<String, IVariable<?>> e : SpritePanel.getSprite().getVariables().entrySet())
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
