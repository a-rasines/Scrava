package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import domain.Project;
import domain.Sprite;
import domain.values.Variable;

public class ProjectExporter {

	private static void copyRes(String orig, String dest) {
		try {
			new File(dest).createNewFile();
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(orig);
			FileOutputStream fos = new FileOutputStream(dest);
			fos.write(is.readAllBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportTo(File folder) {
		folder.mkdir();
		new File(folder.getAbsolutePath() + "/base").mkdir();
		copyRes("generate/base/GraphicsPanel.jv", folder.getAbsolutePath() + "/base/GraphicsPanel.java");
		copyRes("generate/base/Sprite.jv", folder.getAbsolutePath() + "/base/Sprite.java");
		copyRes("generate/base/EventSystem.jv", folder.getAbsolutePath() + "/base/EventSystem.java");
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("generate/generated/GeneratedSprite.jv");
		new File(folder.getAbsolutePath() + "/generated").mkdir();
		try {
			String spriteBase = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			for (Sprite s : Project.getActiveProject().getSprites()) {
				if(s == null) continue;
				String sprite = spriteBase;
				sprite = sprite.replaceAll("\\{\\{SpriteName}}", s.getName());
				for(Variable<?> v : Project.getActiveProject().getVariablesOf(s).values()) {
					if(v.name.equals("scale"))
						sprite = sprite.replaceAll("\\{\\{Scale}}", ""+v.initialValue());
					else if(!v.name.equals("x") && !v.name.equals("y"))
						sprite = sprite.replace("{{Variables}}", v.getInitialization() + "\n\t{{Variables}}");
				}
				sprite = sprite.replace("{{Variables}}", "");
				System.out.println(sprite);
				FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + "/generated/"+s.getName()+".java");
				fos.write(sprite.getBytes());
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
	}
}
