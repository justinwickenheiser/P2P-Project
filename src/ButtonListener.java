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

                // clear old search results
                gui.keywordResults.setText("");
                // get new search results
                host.setGroupTwo(gui.keywordInput.getText());
            
                break;
            case 3:
                try {
                    String[] arguments = gui.commandInput.getText().split(" ");
                    if (arguments[0].toLowerCase().equals("connect")) {
                        client = new FTPClient(gui.commandInput.getText());
                    } else {
                        client.runCommand(gui.commandInput.getText());
                    }

                    String enteredCmd = gui.commandInput.getText() + "\n";
                    String text = gui.ftpCommands.getText();

                    // Appends new command
                    gui.ftpCommands.setText(text + enteredCmd);

                    // clear command input
                    gui.commandInput.setText("");

                } catch (Exception ex) {
                    System.out.println("Could not instantiate FTPClient.");
                    System.out.println(ex);
                }
                break;
            default:
                break;
        }
    }
}