import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerViewController {
    private JPanel mainPanel;
    private JTextField customerIDTF;
    private JTextField firstNameTF;
    private JTextField lastNameTF;
    private JTextField phoneNumberTF;
    private JButton loadCustomerButton;
    private JButton saveCustomerButton;


    private Client client;

    public CustomerViewController(Client client) {
        this.client = client;
        productIDsave save = new productIDsave();

        loadCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerID = customerIDTF.getText();
                Message message = new Message(Message.LOAD_CUSTOMER, customerID, save);
                client.sendMessage(message);
            }
        });

        saveCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer customer = new Customer();

                customer.setCustomerID(Integer.parseInt(customerIDTF.getText()));
                customer.setFirstName(firstNameTF.getText());
                customer.setLastName(lastNameTF.getText());
                customer.setPhoneNumber(Integer.parseInt(phoneNumberTF.getText()));

                Gson gson = new Gson();

                String customerString = gson.toJson(customer);

                Message message = new Message(Message.SAVE_CUSTOMER, customerString, save);
                client.sendMessage(message);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateCustomerInfo(Customer customer) {
        customerIDTF.setText(String.valueOf(customer.getCustomerID()));
        firstNameTF.setText(customer.getFirstName());
        lastNameTF.setText(customer.getLastName());
        phoneNumberTF.setText(String.valueOf(customer.getPhoneNumber()));
    }




}
