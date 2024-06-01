package ui.components;

import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import remote.ClientController;
import server.ScravaProto.ClientData;
import server.ScravaProto.ObjectDescriptor;

public class OnlineProjectsScrollPane extends JScrollPane {
	private static final long serialVersionUID = 3252837874139006516L;
	
	public static OnlineProjectsScrollPane INSTANCE = new OnlineProjectsScrollPane();
	
	private OnlineProjectsScrollPane() {
		regenerate();
	}
	
	private JList<Project> list = null;
	private record Project(String name, int id) { @Override public final String toString() { return name; }	};
	public void regenerate() {
		ClientData user = ClientController.INSTANCE.getUser();
		if(user == null)
			setViewportView(new JLabel("You need to log in / sign up to upload and download projects from the cloud"));
		else {
			DefaultListModel<Project> lm = new DefaultListModel<>();
			int count;
			int offset = 0;
			do {
				count = 0;
				Iterator<ObjectDescriptor> ls = ClientController.INSTANCE.getUserProjects(offset);
				while(ls.hasNext()) {
					ObjectDescriptor actual = ls.next();
					lm.addElement(new Project(actual.getName(), actual.getId()));
					count++;
				}
				offset += 30;
			
			} while (count == 30);
			list = new JList<>(lm);
			setViewportView(list);
			
		}
		
	}

}
