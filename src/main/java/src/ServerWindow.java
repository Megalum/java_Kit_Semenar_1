package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final String TITLE = "Chat server";
    public static final String NAME_LOG = "./src/main/java/src/log.txt";

    boolean serverStatus = false;
    List<ClientGUI> clientGUIList;
    JTextArea textArea;
    JButton btnStart,btnStop;

    ServerWindow(){
        clientGUIList = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle(TITLE);

        add(createMainPanel());
        add(createButtonControl(), BorderLayout.SOUTH);

        setVisible(true);
    }

    Component createMainPanel(){
        JPanel panel = new JPanel(new GridLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);

        panel.add(textArea);

        return panel;
    }

    Component createButtonControl(){
        JPanel panel = new JPanel(new GridLayout(1, 2));

        btnStop = new JButton("Stop");
        btnStart = new JButton("Start");

        btnStart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!serverStatus) {
                    serverStatus = true;

                    System.out.println("Server up");
                    String mes = readLog();
                    appendMes(mes);

                }
                else {
                    System.out.println("Server already launched...");
                }
            }
        });

        btnStop.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverStatus) {
                    serverStatus = false;
                    int i = clientGUIList.size() - 1;
                    while (i != -1){
                        disconnected(clientGUIList.get(i));
                        i--;
                    }
                    System.out.println("Server down");

                    textArea.selectAll();
                    textArea.replaceSelection("");

                }
                else {
                    System.out.println("Server already lying down...");
                }
            }
        });

        panel.add(btnStart);
        panel.add(btnStop);

        return panel;
    }

    public boolean connected(ClientGUI clientGUI){
        if (serverStatus) {
            clientGUIList.add(clientGUI);
            clientGUIList.get(clientGUIList.size() - 1).append(textArea.getText());
            return true;
        }
        return false;
    }

    public void disconnected(ClientGUI clientGUI){
        clientGUIList.remove(clientGUI);
        clientGUI.disconnected();
    }

    public void message(String text){
        if (serverStatus){
            appendMes(text);
            writeLog(text);
            clientMessage(text);
        }
    }

    public void appendMes(String text){
        textArea.append(text + "\n");
    }

    public String readLog(){
        StringBuilder sb = new StringBuilder();

        try (FileReader reader = new FileReader(NAME_LOG);){
            int c;
            while ((c = reader.read()) != -1){
                sb.append((char) c);
            }
            sb.delete(sb.length()-1, sb.length());
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeLog(String text){
        try (FileWriter writer = new FileWriter(NAME_LOG, true)){
            writer.write(text);
            writer.write("\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clientMessage(String text){
        for (ClientGUI clientGUI : clientGUIList) {
            clientGUI.append(text);
        }
    }


}
