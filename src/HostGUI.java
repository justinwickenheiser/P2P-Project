import java.awt.*;

import javax.swing.*;


public class HostGUI extends Frame {
    
    private JPanel top;
    private JPanel center;
    private JPanel bottom;
    private JLabel sHostname;
    private JLabel port;
    private JLabel username;
    private JLabel hostname;
    private JLabel speed;
    private JLabel keyword;
    private JLabel command;
    private JButton connect;
    private JButton search;
    private JButton go;
    private JTextField sHostnameInput;
    private JTextField portInput;
    private JTextField usernameInput;
    private JTextField hostnameInput;
    private JTextField keywordInput;
    private JTextArea ftpCommands;
    private JComboBox selectSpeed;
    
    private String[] speeds = { "Ethernet", "Wifi", "T1", "T3" };
    
    
	
	public HostGUI (String title) {
		super (title);
        
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        

        //Connect Panel - Top
		top  = new JPanel();
		add(top, BorderLayout.NORTH);
		top.setBorder(BorderFactory.createTitledBorder("Connect"));
        top.setPreferredSize(new Dimension(700, 140));
        
        FlowLayout topPanel = new FlowLayout();
        top.setLayout(topPanel);
        
        //First Row of Panel
        sHostname = new JLabel("Server Hostname:");
        sHostnameInput = new JTextField(20);
        //sHostnameInput.addActionListener();
        
        port = new JLabel("Port:");
        portInput = new JTextField(10);
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
        sHostnameInput = new JTextField(20);
        speed = new JLabel("Speed:");
        selectSpeed = new JComboBox(speeds);
        
        top.add(username);
        top.add(usernameInput);
        top.add(hostname);
        top.add(sHostnameInput);
        top.add(speed);
        top.add(selectSpeed);

		//Search Panel - Center
		center = new JPanel();
		add(center, BorderLayout.CENTER);
		center.setBorder(BorderFactory.createTitledBorder("Search"));
        keyword = new JLabel("Keyword:");
        keywordInput = new JTextField(20);
        search = new JButton("Search");
        
        center.add(keyword);
        center.add(keywordInput);
        center.add(search);
		
        //FTP Panel - Bottom
		bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);
		bottom.setBorder(BorderFactory.createTitledBorder("FTP"));
        command = new JLabel("Enter Command:");
        go = new JButton("Go");
        ftpCommands = new JTextArea("FTP Commands...Go Here");
        ftpCommands.setEditable(false);
        
        bottom.add(command);
        bottom.add(go);
        bottom.add(ftpCommands);
        
		
        pack ();
        show ();
        //input.requestFocus ();
	}

	public static void main(String[] args) {
        new HostGUI("NAPSTER FRIENDS");
	}
}
