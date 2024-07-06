package ui.domain;

import org.w3c.dom.svg.SVGDocument;

import parsers.SVGReader;

public record SVGConfig(SVGDocument document, double wOffset, double textXOffset, double endOffset) {
	
	public SVGDocument document() {
		return SVGReader.clone(document);
	}
	
}
