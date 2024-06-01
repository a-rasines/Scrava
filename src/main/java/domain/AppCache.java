package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import server.ScravaProto.ClientData;

public class AppCache implements Serializable{

	private static final long serialVersionUID = -1026876039808009222L;
	
	private static transient AppCache instance;
	
	public static AppCache getInstance() {
		return instance;
	}
	
	public ClientData user = null;
	
	public static record ProjectData(String name, File file) implements Serializable {
		@Override
		public final boolean equals(Object arg0) {
			return arg0 instanceof ProjectData pd && file.equals(pd.file);
		}
	}
	
	public Set<ProjectData> importedProjects;
	
	private AppCache() {
		importedProjects = new HashSet<>();
	}
	
	public static void load() {
		try {
			FileInputStream fileIn = new FileInputStream(new File("config.cache"));
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        instance = (AppCache) in.readObject();
	        in.close();
	        fileIn.close();
		} catch (IOException e) {
			instance = new AppCache();
		} catch (ClassNotFoundException e) { 
			//(really) unlike
			e.printStackTrace();
		}
	}
	
	public static void save() {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(new File("config.cache"));
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(instance);
	        out.close();
	        fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
