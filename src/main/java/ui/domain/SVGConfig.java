package ui.domain;

import java.util.Map;

import org.w3c.dom.svg.SVGDocument;

import parsers.SVGReader;
import ui.renderers.IRenderer.IRenderable;

public record SVGConfig(SVGDocument document, double wOffset, double textXOffset, double endOffset) {
	
	private static final Map<String, SVGConfig> CONFIG_MAP;
	
	static {
		SVGDocument numberDoc = null;
		SVGDocument stringDoc = null;
		SVGDocument booleanDoc = null;
		SVGDocument enumDoc = null;
		try { numberDoc = SVGReader.readSVG("textures/variable/literal/num.svg"); } catch(Exception e) {e.printStackTrace();}
		try { stringDoc = SVGReader.readSVG("textures/variable/literal/string.svg"); } catch(Exception e) {e.printStackTrace();}
		try { booleanDoc = SVGReader.readSVG("textures/variable/literal/boolean.svg"); } catch(Exception e) {e.printStackTrace();}
		try { enumDoc = SVGReader.readSVG("textures/variable/literal/enum.svg"); } catch(Exception e) {e.printStackTrace();}
		CONFIG_MAP = Map.of(
			IRenderable.VARIABLE_NUM, new SVGConfig(numberDoc, 24, 13, 0),
			IRenderable.VARIABLE_BOOL, new SVGConfig(booleanDoc, 8.75, 9, 0),
			IRenderable.VARIABLE_STR, new SVGConfig(stringDoc, 12, 5, 0),
			IRenderable.VARIABLE_ENUM, new SVGConfig(enumDoc, 25, 5, 12.35)
		);
	}
	
	public static SVGConfig getConfig(String config) {
		return CONFIG_MAP.get(config);
	}
	
	public SVGDocument document() {
		return SVGReader.clone(document);
	}
	
}
