package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProjectExporter {

	private static void copyRes(String orig, String dest) {
		try {
			new File(dest).createNewFile();
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(orig);
			FileOutputStream fos = new FileOutputStream(dest);
			fos.write(is.readAllBytes());
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
	}
}
