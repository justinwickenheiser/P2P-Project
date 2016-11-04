import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class HostGUI extends Frame {
    
    public JPanel top;
    public JPanel center;
    public JPanel bottom;
    public JLabel sHostname;
    public JLabel port;
    public JLabel username;
    public JLabel hostname;
    public JLabel speed;
    public JLabel keyword;
    public JLabel command;
    public JButton connect;
    public JButton search;
    public JButton go;
    public JTextField sHostnameInput;
    public JTextField portInput;
    public JTextField usernameInput;
    public JTextField hostnameInput;
    public JTextField keywordInput;
    public JTextField commandInput;
    public JTextArea ftpCommands;
    public JTextArea keywordResults;
    public JComboBox selectSpeed;
    
    public String[] speeds = { "Ethernet", "Wifi", "T1", "T3" };
	
	public HostGUI (String title) {
		super (title);

        //Connect Panel - Top
		top  = new JPanel();
		add(top, BorderLayout.NORTH);
		top.setBorder(BorderFactory.createTitledBorder("Connect"));
        top.setPreferredSize(new Dimension(700, 140));
        
        //First Row of Panel
        sHostname = new JLabel("Server Hostname:");
        sHostnameInput = new JTextField(20);
        port = new JLabel("Port:");
        portInput = new JTextField(10);
        
        //Button + Action Listener
        connect = new JButton("Connect");
        
        top.add(sHostname);
        top.add(sHostnameInput);
        top.add(port);
        top.add(portInput);
        top.add(connect);
        
        //Second Row of Panel
        username = new JLabel("Username:");
        usernameInput = new JTextField(20);
        hostname = new JLabel("Hostname:");
        hostnameInput = new JTextField(20);
        speed = new JLabel("Speed:");
        selectSpeed = new JComboBox(speeds);
        
        top.add(username);
        top.add(usernameInput);
        top.add(hostname);
        top.add(hostnameInput);
        top.add(speed);
        top.add(selectSpeed);

		//Search Panel - Center
		center = new JPanel();
		add(center, BorderLayout.CENTER);
		center.setBorder(BorderFactory.createTitledBorder("Search"));
        center.setPreferredSize(new Dimension(700, 500));
        keyword = new JLabel("Keyword:");
        keywordInput = new JTextField(20);
        keywordResults = new JTextArea();
        keywordResults.setPreferredSize(new Dimension(500, 150));
        keywordResults.setEditable(false);
        
        //Button + Action Listener
        search = new JButton("Search");
        
        center.add(keyword);
        center.add(keywordInput);
        center.add(search);
        center.add(keywordResults);
		
        //FTP Panel - Bottom
		bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);
		bottom.setBorder(BorderFactory.createTitledBorder("FTP"));
        command = new JLabel("Enter Command:");
        commandInput = new JTextField(20);
        
        //Button + Action Listener
        go = new JButton("Go");

        ftpCommands = new JTextArea();
        ftpCommands.setPreferredSize(new Dimension(200, 150));
        ftpCommands.setEditable(false);
        
        bottom.add(command);
        bottom.add(commandInput);
        bottom.add(go);
        bottom.add(ftpCommands);
        
        setPreferredSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}

	public static void main(String[] args) {
        new HostGUI("NAPSTER FRIENDS");
	}
}
