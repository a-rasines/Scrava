package ui.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.text.html.HTMLEditorKit;

import parsers.MdParser;
import remote.ClientController.Tutorial;

public class TutorialWindow extends JFrame{

	private static final long serialVersionUID = 6700899765898668273L;
	
	public TutorialWindow(Tutorial t) {
		JPanel parent = new JPanel(new BorderLayout());
		setContentPane(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(500, 500));
		parent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JEditorPane jep = new JEditorPane();
		jep.setEditorKit(new HTMLEditorKit());
		jep.setEditable(false);
		jep.setContentType("text/html");							
		jep.setText(  "<html>"
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
					+ "</html>");
		System.out.println(jep.getText());
		jep.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(jep, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		new TutorialWindow(new Tutorial("Starting in Markdown", 
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
				+ "You may be tempted to use the enter key to change line, but the library used to render text it's not the best with that, therefore it's better practice to write `<br>`")).setVisible(true);
	}

}
