import java.awt.event.*;

public class ButtonListener implements ActionListener {
    
    Host host;
    HostGUI gui;
    FTPClient client;
    int action;

    public ButtonListener (Host host, HostGUI gui, FTPClient client, int groupNumber) {
        this.host = host;
        this.gui = gui;
        this.client = client;
        this.action = groupNumber;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (action) {
            case 1:
                String cmd = gui.usernameInput.getText() + " " + gui.hostnameInput.getText() + " " + gui.selectSpeed.getSelectedItem().toString();
                int port = Integer.parseInt(gui.portInput.getText());
                host.setGroupOne(gui.sHostnameInput.getText(), port, cmd);

                break;
            case 2:

                host.setGroupTwo(gui.keywordInput.getText());
            
                break;
            case 3:
                try {
                    client = new FTPClient(gui.commandInput.getText());
                } catch (Exception ex) {
                    System.out.println("Could not instantiate FTPClient.");
                    System.out.println(ex);
                }
                
            /*
                String cmd = commandInput.getText() + "\n";
                String text = ftpCommands.getText();
                
                //Run FTP Client functions
                
                ftpCommands.setText(text + cmd);
                
                System.out.println("\nGo Button clicked...");
                System.out.println("FTP Command: " + cmd);
            */
                break;
            default:
                break;
        }
    }
}