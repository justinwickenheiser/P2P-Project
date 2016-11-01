import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class HostGUI extends Frame { 
	
	public ChatFrame (String title) {
		super (title);
		
		JPanel top  = new JPanel();
		frame.getContentPane().add(top, BorderLayout.NORTH);
		top.setBorder(BorderFactory.createTitledBorder("Connect"));
		
		JPanel center = new JPanel();
		frame.getContentPane().add(center, BorderLayout.CENTER);
		center.setBorder(BorderFactory.createTitledBorder("Search"));
		
		JPanel bottom = new JPanel();
		frame.getContentPane().add(bottom, BorderLayout.SOUTH);
		bottom.setBorder(BorderFactory.createTitledBorder("FTP"));
		
		pack (); 
	    show (); 
	    input.requestFocus ();
	}

	public static void main(String[] args) {
		new HostGUI("NAPSTER FRIENDS");	
	}
}