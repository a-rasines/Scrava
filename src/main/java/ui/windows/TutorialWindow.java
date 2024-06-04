package ui.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.github.rjeschke.txtmark.Processor;

import remote.ClientController.Tutorial;

public class TutorialWindow extends JFrame{

	private static final long serialVersionUID = 6700899765898668273L;
	
	public TutorialWindow(Tutorial t) {
		JPanel parent = new JPanel(new BorderLayout());
		setContentPane(parent);
		setMinimumSize(new Dimension(200, 200));
		parent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JEditorPane jep = new JEditorPane();
		jep.setEditable(false);
		jep.setContentType("text/html");							
		jep.setText(  "<html>"
						+ "<head>"
							+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">"
						+ "</head>"
						+ "<body>"
							+ "<nav class=\"navbar navbar-light bg-light\">"
								+ "<a class=\"navbar-brand\" href=\"#\">"
									+ t.title()
								+ "</a>"
							+ "</nav>"
						+ "<br>" 
						+ t.content()
						+ "</body>"
					+ "</html>");
		jep.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(jep, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		new TutorialWindow(new Tutorial(Processor.process("#Test"), Processor.process("This is a `test`"))).setVisible(true);;
	}

}
