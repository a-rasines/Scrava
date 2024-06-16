package parsers;

import remote.ClientController.Tutorial;

public class MdParser {
	
	private static int replace(StringBuffer buffer, int origLen, String fnal, int offset) {
		buffer.replace(offset, offset + origLen, fnal.substring(0, origLen));
		buffer.insert(offset + origLen, fnal.substring(origLen));
		return fnal.length() - origLen;
	}
	
	public static String markdownToHTML(String md) {
		StringBuffer buffer = new StringBuffer(md);
		
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		// ▓								HIGHLIGHT/TITLE PARSER								   ▓
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		
		boolean inString = false;
		boolean inTitle = false;
		boolean inCrossed = false;
		byte inCursive = 0; //0 no, 1 *, 2 _
		boolean inBold = false;
		int offset = 0;
		for(int i = 0; i < md.length(); i++) {
			switch(md.charAt(i)) {
				
				case '`':
					if(md.charAt(i - 1) == '\\') {
						buffer.delete(i + offset - 1, i + offset);
						offset--;
					}
					if(!inTitle && (i == 0 || md.charAt(i - 1) != '\\')) {
						if(inString)
							offset += replace(buffer, 1, "</mark>", i + offset);
						else
							offset += replace(buffer, 1, "<mark class=\"bg-dark text-white rounded-pill\">", i + offset);
						inString = !inString;
					}
					break;
				
				case '#':
					inTitle = buffer.charAt(i + 1) == ' ' && !inString;
					break;
				
				case '\n':
					inTitle = false;
					break;
				
				case '<':
					if(inTitle && (buffer.substring(i, i+4).equals("<br>") || buffer.substring(i, i+5).equals("<\br>")))
						inTitle = false;
					else if(inTitle || inString || !(buffer.substring(i, i+4).equals("<br>") || buffer.substring(i, i+5).equals("<\br>")))
						offset += replace(buffer, 1, "&lt;", i + offset);
					break;
				
				case '>':
					if(inTitle || inString || !(buffer.substring(i - 3, i + 1).equals("<br>") || buffer.substring(i - 4, i + 1).equals("<\br>"))) 
						offset += replace(buffer, 1, "&gt;", i + offset);
					break;
				
				case '*':
					if(inTitle || inString)
						offset += replace(buffer, 1, "&#42;", i + offset);
					else if(md.charAt(i + 1) == '*') {
						if(inBold) {
							inBold = false;
							offset += replace(buffer, 1, "</strong>", i + offset);
						} else {
							inBold = true;
							offset += replace(buffer, 1, "<strong>", i + offset);
						}
					} else if(inCursive == 1) {
						offset += replace(buffer, 1, "</em>", i + offset);
						inCursive = 0;
					} else if (inCursive == 0){
						offset += replace(buffer, 1, "<em>", i + offset);
						inCursive = 1;
					}						
					break;
				case '_':
					if(inTitle || inString)
						offset += replace(buffer, 1, "&#95;", i + offset);
					else if(inCursive == 0) {
						offset += replace(buffer, 1, "<em>", i + offset);
						inCursive = 2;
					} else if (inCursive == 2) {
						offset += replace(buffer, 1, "</em>", i + offset);
						inCursive = 0;
					}
					break;
				case '|':
					if(inTitle || inString) {
						offset += replace(buffer, 1, "&#124;", i + offset);
					}
					break;
				case '~':
					if(inTitle || inString) {
						buffer.replace(i + offset, i + offset + 1, "&");
						buffer.insert(i + offset + 1, "#126;");
						offset += 5;
					} else if (inCrossed) {
						buffer.replace(i + offset, i + offset + 1, "<");
						buffer.insert(i + offset + 1, "/del>");
						offset += 5; 
						inCrossed = false;
					} else {
						buffer.replace(i + offset, i + offset + 1, "<");
						buffer.insert(i + offset + 1, "del>");
						offset += 4; 
						inCrossed = true;
					}
					break;
				default:
					
			}
		}
		
		
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		// ▓										TITLES										   ▓
		// ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
		
		String[] lines = buffer.toString().split("\n|<br>");
		int indentations = 0;
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].startsWith("# ")) {
				lines[i] = "<div style=\"width: 50%\"><h1>" + lines[i].substring(2) + "</h1><hr></div>";
				for(int j = 0; j < indentations; j++)
					lines[i - 1] += "</div>";
				indentations = 0;
			}
			else if(lines[i].startsWith("## ")) {
				lines[i] = "<div style=\"width: 50%\"><h2>" + lines[i].substring(3) + "</h2><hr></div>";
				for(int j = 0; j < indentations; j++)
					lines[i - 1] += "</div>";
				indentations = 0;
			} else if(lines[i].startsWith("### ")) {
				lines[i] = "<div style=\"width: 50%\"><h3>" + lines[i].substring(4) + "</h3><hr></div>";
				for(int j = 0; j < indentations; j++)
					lines[i - 1] += "</div>";
				indentations = 0;
			} else if(lines[i].startsWith("| ")) {
				int indt = 0;
				while(lines[i].substring(indt * 2).startsWith("| "))
					indt ++;
				System.out.println(indt + " " + indentations);
				if(indentations > indt)
					for(int j = 0; j < indentations - indt; j++)
						lines[i - 1] += "</div>";
				else if(indentations < indt)
					for(int j = 0; j < indt - indentations; j++)
						lines[i] = "<div class=\"vertical-bar\">" + lines[i].substring(indt * 2);
				indentations = indt;
			} else {
				lines[i] = "<p>" + lines[i] + "</p>";
				for(int j = 0; j < indentations; j++)
					lines[i - 1] += "</div>";
				indentations = 0;
			}
		}
		
		
		
		return String.join("", lines);
	}
	
	public static String tutorialToHTML(Tutorial t) {
		return "<html>"
				+ "<head>"
					+ "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\" crossorigin=\"anonymous\">"
					+"<style>"
						+ ".vertical-bar {"
							+ "border-left: 4px solid #000;"
							+ "padding-left: 10px;"
						+ "}"
					+"</style>"
				+ "</head>"
				+ "<body class=\"bg-secondary text-white\" style=\"margin-left:20px\">"
					+ "<div class=\"bg-dark text-white\" style=\"margin-left:-20px\">"
						+ "<h1 class=\"display-3\" style=\"margin-left:20px\">" + t.title() + "</h1>"
						+ "<hr>"
					+ "</div>"
					+ "<br>" 
					+ MdParser.markdownToHTML(t.content())
				+ "</body>"
			+ "</html>";
	}
}
