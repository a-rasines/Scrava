package ui;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {
	private static final Set<String> supportedSuffixes = new HashSet<>(Arrays.asList(ImageIO.getReaderFileSuffixes()));
	@Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String fileName = file.getName().toLowerCase();
        for (String suffix : supportedSuffixes) {
            if (fileName.endsWith("." + suffix)) {
                return true;
            }
        }
        return false;
    }
	@Override
    public String getDescription() {
        return "Image Files (" + String.join(", ", supportedSuffixes) + ")";
    }
}