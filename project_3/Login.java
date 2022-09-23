import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loadButton;
    private JButton saveButton;
    private JPanel mainPanel;
    private JTextField userIDTF;
    private JTextField displaynameTF;

    private Client client;
    private productIDsave saveID = new productIDsave();


    public Login(Client client) {
        this.client = client;
        productIDsave save = new productIDsave();


        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDTF.getText();
                Message message = new Message(Message.LOAD_LOGININFO, userID, save);
                client.sendMessage(message);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginInfo loginInfo = new LoginInfo();

                loginInfo.setUserID(Integer.parseInt(userIDTF.getText()));
                loginInfo.setUsername(usernameField.getText());
                loginInfo.setPassword(passwordField.getText());
                loginInfo.setDisplayName(displaynameTF.getText());
                loginInfo.setisAdmin(0);


                Gson gson = new Gson();

                String productString = gson.toJson(loginInfo);

                Message message = new Message(Message.SAVE_LOGININFO, productString, save);
                client.sendMessage(message);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateLoginInfo(LoginInfo loginInfo) {
        userIDTF.setText(String.valueOf(loginInfo.getuserID()));
        usernameField.setText(loginInfo.getUsername());
        passwordField.setText(loginInfo.getPassword());
        displaynameTF.setText(loginInfo.getDisplayName());

    }
}
