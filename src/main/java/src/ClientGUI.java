package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientGUI extends JFrame {

    public static final String IP = "127.0.0.1";
    public static final String PORT = "8180";
    public static final String LOGIN = "Admin";
    public static final String PASSWORD = "123456";

    public static final String TITLE = "Chat client";
    public static final int WIDTH = 500;
    public static final int HEIGHT = 400;

    public JTextArea textArea;
    private ServerWindow serverWindow;
    private boolean connected = false;
    JTextField ip, port, login, message;
    JPasswordField password;
    JButton btnLogin, btnSend;
    Label label;

    ClientGUI(ServerWindow serverWindow){
        this.serverWindow = serverWindow;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);

        add(createMainPanel(), BorderLayout.NORTH);
        add(createChatPanel());
        add(createChatButton(), BorderLayout.SOUTH);
        setVisible(true);
    }

    public void append(String text){
        textArea.append(text + "\n");
    }

    public void disconnected(){
        connected = false;
        textArea.selectAll();
        textArea.replaceSelection("");
    }

    Component createMainPanel(){
        JPanel panel = new JPanel(new GridLayout(2, 1));

        panel.add(createLogPanel());
        panel.add(createClientPanel());

        return panel;
    }

    Component createChatPanel(){
        JPanel panel = new JPanel(new GridLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);

        panel.add(textArea);

        return panel;
    }

    Component createLogPanel(){
        JPanel panel = new JPanel(new GridLayout(1, 3));

        ip = new JTextField(IP);
        port = new JTextField(PORT);
        label = new Label();

        panel.add(ip);
        panel.add(port);
        panel.add(label);

        return panel;
    }

    Component createClientPanel(){
        JPanel panel = new JPanel(new GridLayout(1, 3));

        login = new JTextField(LOGIN);
        password = new JPasswordField(PASSWORD);
        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!connected){
                    connected = serverWindow.connected(ClientGUI.this);
                }
            }
        });

        panel.add(login);
        panel.add(password);
        panel.add(btnLogin);

        return panel;
    }

    Component createChatButton(){
        JPanel panel = new JPanel(new GridLayout(1, 2));

        message = new JTextField();
        btnSend = new JButton("Send");

        btnSend.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connected) {
                    serverWindow.message(message.getText());
                }
            }
        });

        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if (connected) {
                        serverWindow.message(message.getText());
                    }
                }
            }
        });

        panel.add(message);
        panel.add(btnSend);

        return panel;
    }

}
