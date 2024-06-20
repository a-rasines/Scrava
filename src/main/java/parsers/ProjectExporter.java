package parsers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import domain.Project;
import domain.Sprite;
import domain.models.types.EventBlock;
import domain.values.IVariable;
import domain.values.StaticVariable;
import parsers.textures.TextureManager;
import parsers.textures.TextureManager.ImageData;
import ui.renderers.IRenderer.DragableRenderer;

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
	
	public static void exportTo(File folder) throws IOException{
		
		folder.mkdir();
		copyRes("generate/root/gen.clazzpath", folder.getAbsolutePath() + "/.classpath");
		try(InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("generate/root/gen.pro");
			FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + "/.project")) {
			String project = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			fos.write(project.replace("{{ProjectName}}", Project.getActiveProject().name).getBytes());
		}
		copyRes("generate/root/manifest.txt", folder.getAbsolutePath() + "/MANIFEST.MF");
		try(InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("generate/root/compile.btch");
				FileOutputStream win_fos = new FileOutputStream(folder.getAbsolutePath() + "/compile.bat");
				FileOutputStream linux_fos = new FileOutputStream(folder.getAbsolutePath() + "/compile.sh");) {
				String project = new String(is.readAllBytes(), StandardCharsets.UTF_8).replace("{{ProjectName}}", Project.getActiveProject().name);
				win_fos.write(project.getBytes());
				linux_fos.write(project.getBytes());
			}
		
		File srcFolder = new File(folder.getAbsolutePath() + "/src");
		srcFolder.mkdir();
		
		File resourcesFolder = new File(folder.getAbsolutePath() + "/resources");
		resourcesFolder.mkdir();
					
		new File(srcFolder.getAbsolutePath() + "/base").mkdir();
		try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("generate/base/GraphicsPanel.jv");
			FileOutputStream fos = new FileOutputStream(srcFolder.getAbsolutePath() + "/base/GraphicsPanel.java")){
			String graphicsPanel = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			is.close();
			for (Sprite s : Project.getActiveProject().getSprites())
				if(s != null)
					graphicsPanel = graphicsPanel.replace("{{SpriteConstructors}}", "new generated."+ s.getName() + "();\n\t\t\t{{SpriteConstructors}}");
			graphicsPanel = graphicsPanel.replace("\n\t\t\t{{SpriteConstructors}}", "");
			fos.write(graphicsPanel.getBytes());
		}
		copyRes("generate/base/Sprite.jv", srcFolder.getAbsolutePath() + "/base/Sprite.java");
		copyRes("generate/base/EventSystem.jv", srcFolder.getAbsolutePath() + "/base/EventSystem.java");
		
		new File(srcFolder.getAbsolutePath() + "/generated").mkdir();
		try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("generate/generated/GeneratedSprite.jv")){
			String spriteBase = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			is.close();
			TextureManager tm = new TextureManager();
			for (Sprite s : Project.getActiveProject().getSprites()) {
				prepareSprite(tm, s, srcFolder, resourcesFolder, spriteBase);
			}
		}
		
		try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("generate/generated/GlobalVariables.jv");
			 FileOutputStream fos = new FileOutputStream(srcFolder.getAbsolutePath() + "/generated/GlobalVariables.java")){
			String globalVariables = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			is.close();
			Set<String> imports = new HashSet<>();
			for (IVariable<?> v : Project.getActiveProject().getVariablesOf(null).values()) {
				v.getImports(imports);
				if(v instanceof StaticVariable<?> sv)
					globalVariables.replace("{{Variables}}", sv.getInitialization() + "\n\t{{Variables}}");
			}
			globalVariables = globalVariables.replace("\n\t{{Variables}}", "");
			for(String inport : imports)
				globalVariables = globalVariables.replace("{{Imports}}", inport + "\n{{Imports}}");
			globalVariables = globalVariables.replace("{{Imports}}", "");
		}
		System.out.println(folder.getAbsolutePath()+"\\compile.bat");
		if(System.getProperty("os.name").indexOf("win") > -1)
			Runtime.getRuntime().exec(new String[] {"cmd", "/c", folder.getAbsolutePath() + "\\compile.bat"});
		else
			Runtime.getRuntime().exec(new String[] {folder.getAbsolutePath() + "\\compile.sh"});
	}
	private static void prepareSprite(TextureManager tm, Sprite s, File srcFolder, File resourcesFolder, String sprite) throws IOException {
		if(s == null) return;
		sprite = sprite.replaceAll("\\{\\{SpriteName}}", s.getName());
		
		for(IVariable<?> v : Project.getActiveProject().getVariablesOf(s).values()) {
			if(v.getName().equals("scale") && v instanceof StaticVariable<?> sv)
				sprite = sprite.replaceAll("\\{\\{Scale}}", ""+sv.initialValue());
			else if(!v.getName().equals("x") && !v.getName().equals("y")  && v instanceof StaticVariable<?> sv)
				sprite = sprite.replace("{{Variables}}", sv.getInitialization() + "\n\t{{Variables}}");
		}
		sprite = sprite.replace("\t{{Variables}}", "");
		for(BufferedImage bi : s.getTextures()) {
			ImageData im = tm.addImage(bi);
			if(im.file == null) {
				File f = new File(resourcesFolder.getAbsolutePath() + "/" + Integer.toHexString(bi.hashCode()) + ".png");
				ImageIO.write(bi, "png", f);
				im.file = f;
			}
			sprite = sprite.replace("{{Textures}}", "importTexture(\"" + im.file.getPath().replace(resourcesFolder.getAbsolutePath() + "\\", "").replace(resourcesFolder.getAbsolutePath(), "")+ "\"),\n					  {{Textures}}");
		}
		sprite = sprite.replace(",\n					  {{Textures}}", "");
		
		Set<String> inports = new HashSet<>();
		for(DragableRenderer e : s.getBlocks())
			if(e.getBlock() instanceof EventBlock eb) {
				eb.getImports(inports);
				sprite = sprite.replace("{{Events}}", eb.getCode().replace("\n", "\n\t") + "\n\n\t{{Events}}");
			}
		sprite = sprite.replace("{{Events}}", "");
		for(String inport : inports)
			sprite = sprite.replace("{{Imports}}", inport + "\n{{Imports}}");
		sprite = sprite.replace("{{Imports}}", "");
		FileOutputStream fos = new FileOutputStream(srcFolder.getAbsolutePath() + "/generated/"+s.getName()+".java");
		fos.write(sprite.getBytes());
		fos.close();
	}
}
