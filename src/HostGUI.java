import java.awt.*;
import java.awt.event.*;
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
    private JTextField commandInput;
    private JTextArea ftpCommands;
    private JComboBox selectSpeed;
    ActionClass actionEvent = new ActionClass();
    
    private String[] speeds = { "Ethernet", "Wifi", "T1", "T3" };
    
    
	
	public HostGUI (String title) {
		super (title);

        //Connect Panel - Top
		top  = new JPanel();
		add(top, BorderLayout.NORTH);
		top.setBorder(BorderFactory.createTitledBorder("Connect"));
        top.setPreferredSize(new Dimension(700, 140));
        
        //FlowLayout topPanel = new FlowLayout();
        //top.setLayout(topPanel);
        
        //First Row of Panel
        sHostname = new JLabel("Server Hostname:");
        sHostnameInput = new JTextField(20);
        port = new JLabel("Port:");
        portInput = new JTextField(10);
        
        //Button + Action Listener
        connect = new JButton("Connect");
        connect.addActionListener(actionEvent);
        connect.setActionCommand("1");
        
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
        keyword = new JLabel("Keyword:");
        keywordInput = new JTextField(20);
        
        //Button + Action Listener
        search = new JButton("Search");
        search.addActionListener(actionEvent);
        search.setActionCommand("2");
        
        center.add(keyword);
        center.add(keywordInput);
        center.add(search);
		
        //FTP Panel - Bottom
		bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);
		bottom.setBorder(BorderFactory.createTitledBorder("FTP"));
        command = new JLabel("Enter Command:");
        commandInput = new JTextField(20);
        
        //Button + Action Listener
        go = new JButton("Go");
        go.addActionListener(actionEvent);
        go.setActionCommand("3");

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
    
    class ActionClass implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int action = Integer.parseInt(e.getActionCommand());
            switch (action) {
                case 1:
                    String server = sHostnameInput.getText();
                    int port = Integer.parseInt(portInput.getText());
                    String user = usernameInput.getText();
                    String host = hostnameInput.getText();
                    String value = selectSpeed.getSelectedItem().toString();
                    
                    //Call functions from Host to Connect to Server
                    
                    System.out.println("\nConnect Button clicked...");
                    System.out.println("Server Hostname: " + server + "\tPort: " + port);
                    System.out.println("User: " + user + "\tHost: " + host + "\tSpeed: " + value);
                    break;
                case 2:
                    String kw = keywordInput.getText();
                    
                    //Run Function from Host to perfrom keyword search
                    
                    System.out.println("\nSearch Button clicked...");
                    System.out.println("Keyword: " + kw);
                    break;
                case 3:
                    String cmd = commandInput.getText() + "\n";
                    String text = ftpCommands.getText();
                    
                    //Run FTP Client functions
                    
                    ftpCommands.setText(text + cmd);
                    
                    System.out.println("\nGo Button clicked...");
                    System.out.println("FTP Command: " + cmd);
                    break;
                default:
                    break;
            }
        }
    }

	public static void main(String[] args) {
        new HostGUI("NAPSTER FRIENDS");
	}
}
