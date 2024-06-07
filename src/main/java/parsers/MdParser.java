package parsers;

public class MdParser {
	
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
					if(!inTitle && (i == 0 || md.charAt(i - 1) != '\\')) {
						if(inString) {
							buffer.replace(i + offset, i + offset + 1, "<");
							buffer.insert(i + offset + 1, "/mark>");
							offset += 6;
						} else {
							buffer.replace(i + offset, i + offset + 1, "<");//<mark class="bg-dark text-white rounded-pill">
							buffer.insert(i + offset + 1, "mark class=\"bg-dark text-white rounded-pill\">");
							offset += 45;
						}
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
					else if(inTitle || inString) {
						buffer.replace(i + offset, i + offset + 1, "&");
						buffer.insert(i + offset + 1, "lt;");
						offset += 3;
					}
					break;
				
				case '>':
					if(inTitle || inString) {
						buffer.replace(i + offset, i + offset + 1, "&");
						buffer.insert(i + offset + 1, "gt;");
						offset += 3;
					}
					break;
				
				case '*':
					if(inTitle || inString) {
						buffer.replace(i + offset, i + offset + 1, "&");
						buffer.insert(i + offset + 1, "#42;");
						offset +=4;
					} else if(md.charAt(i + 1) == '*') {
						if(inBold) {
							inBold = false;
							buffer.replace(i + offset, i + offset + 2, "</");
							buffer.insert(i + offset + 2, "strong>");
							offset +=7;
						}else {
							inBold = true;
							buffer.replace(i + offset, i + offset + 2, "<s");
							buffer.insert(i + offset + 2, "trong>");
							offset +=6;
						}
					}
					break;
				
				case '_':
					if(inTitle) {
						buffer.replace(i + offset, i + offset + 1, "&");
						buffer.insert(i + offset + 1, "#95;");
						offset +=4;
					}
					break;
				case '|':
					if(inTitle) {
						buffer.replace(i + offset, i + offset + 1, "&");
						buffer.insert(i + offset + 1, "#124;");
						offset += 5;
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
		for(int i = 0; i < lines.length; i++)
			if(lines[i].startsWith("# "))
				lines[i] = "<div style=\"width: 50%\"><h1>" + lines[i].substring(2) + "</h1><hr></div>";
			else if(lines[i].startsWith("## "))
				lines[i] = "<div style=\"width: 50%\"><h2>" + lines[i].substring(3) + "</h2><hr></div>";
			else if(lines[i].startsWith("### "))
				lines[i] = "<div style=\"width: 50%\"><h3>" + lines[i].substring(4) + "</h3><hr></div>";
			else
				lines[i] = "<p>" + lines[i] + "</p>";
		
		
		
		return String.join("", lines);
	}
	
	public static void main(String[] args) {
		System.out.println(markdownToHTML(
				"In this tutorial you will learn about markdown's syntax in order to create better tutorials.\n"
						+ "## What's Markdown?\n"
						+ "Markdown it's a text modeling language focused on enhancing plain text for better reading. This tool can be related with HTML, which is another modeling language but based on web development.\n"
						+ "## Highlight keywords\n"
						+ "Markdown has multiple ways of highlighting keywords or code references.<br>"
						+ "The most normal ones are *cursive* where you can do it using `_word_` or `*word*`, **bold**, obtainable using `**word**` and ~crossed~ is obtainable using `~word~`.<br>"
						+ "Then, as you may have realized, we have the `codeblock`. This one can be archieved using \\` on each side of the word / text in order to highlight it.<br>"
						+ "The last way is using titles / subtitles, this can archieved using hastags, translating this into:<br>"
						+ "`# Title` <br>"
						+ "`## Subtitle` <br>"
						+ "`### Sub-subtitle`\n"
						+ "## Ordering text \n"
						+ "You may be tempted to use the enter key to change line, but the library used to render text it's not the best with that, therefore it's better practice to write `<br>`"));
	}
}
