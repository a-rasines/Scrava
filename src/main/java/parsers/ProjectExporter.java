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
import domain.values.Variable;
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
			for (Sprite s : Project.getActiveProject().getSprites()) {
				prepareSprite(s, srcFolder, resourcesFolder, spriteBase);
			}
		}
		
	}
	private static void prepareSprite(Sprite s, File srcFolder, File resourcesFolder, String sprite) throws IOException {
		if(s == null) return;
		sprite = sprite.replaceAll("\\{\\{SpriteName}}", s.getName());
		
		for(Variable<?> v : Project.getActiveProject().getVariablesOf(s).values()) {
			if(v.name.equals("scale"))
				sprite = sprite.replaceAll("\\{\\{Scale}}", ""+v.initialValue());
			else if(!v.name.equals("x") && !v.name.equals("y"))
				sprite = sprite.replace("{{Variables}}", v.getInitialization() + "\n\t{{Variables}}");
		}
		sprite = sprite.replace("{{Variables}}", "");
		for(BufferedImage bi : s.getTextures()) {
			ImageIO.write(bi, "png", new File(resourcesFolder.getAbsolutePath() + "/" + Integer.toHexString(bi.hashCode()) + ".png"));
			sprite = sprite.replace("{{Textures}}", "importTexture(\"" + Integer.toHexString(bi.hashCode()) + ".png" + "\"),\n					  {{Textures}}");
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
