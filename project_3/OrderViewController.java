import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderViewController {

    private Client client;

    private JTextField orderIDTF;
    private JTextField orderDateTF;
    private JTextField customerIDTF;
    private JTextField totalCostTF;
    private JButton saveButton;
    private JButton loadButton;
    private JTextField totalTaxTF;
    private JPanel mainPanel;
    private JButton cancelOrderButton;

    public OrderViewController(Client client) {
        this.client = client;
        productIDsave save = new productIDsave();

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String orderID = orderIDTF.getText();

                Integer check = Integer.parseInt(customerIDTF.getText());
                Integer check1 = 12;
                if (client.currentCustomerID.getCustomerID() == check1) {
                    Message message = new Message(Message.LOAD_ORDER, orderID, save);
                    client.sendMessage(message);
                }

            }
        });

        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order order = new Order();

                Integer check = Integer.parseInt(customerIDTF.getText());
                Integer check1 = 12;

                if (client.currentCustomerID.getCustomerID() == check1) {

                    order.setOrderID(0);
                    order.setDate("blank");
                    order.setCustomerID(0);
                    order.setTotalCost(0.0);
                    order.setTotalTax(0.0);

                    Gson gson = new Gson();

                    String orderString = gson.toJson(order);

                    Message message = new Message(Message.DELETE_ORDER, orderString, save);
                    client.sendMessage(message);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order order = new Order();
                Customer customer = new Customer();
                OrderLine orderline = new OrderLine();

                Integer check = Integer.parseInt(customerIDTF.getText());
                Integer check1 = 12;

                if (client.currentCustomerID.getCustomerID() == check1) {

                    orderline.setOrderID(Integer.parseInt(orderIDTF.getText()));
                    orderline.setProductID(0);
                    orderline.setQuantity(0);
                    orderline.setCost(0);

                    customer.setCustomerID(Integer.parseInt(customerIDTF.getText()));
                    customer.setPhoneNumber(0);
                    customer.setFirstName("unknown");
                    //customer.setLastName("unknown");


                    order.setOrderID(Integer.parseInt(orderIDTF.getText()));
                    order.setCustomerID(Integer.parseInt(customerIDTF.getText()));
                    order.setDate(orderDateTF.getText());
                    order.setTotalCost(Double.parseDouble(totalCostTF.getText()));
                    order.setTotalTax(Double.parseDouble(totalTaxTF.getText()));

                    Gson gson = new Gson();
                    Gson gson2 = new Gson();
                    Gson gson3 = new Gson();

                    String orderString = gson.toJson(order);
                    String customerString = gson2.toJson(customer);
                    String orderlineString = gson3.toJson(orderline);

                    Message message = new Message(Message.SAVE_ORDER, orderString, save);
                    Message message2 = new Message(Message.SAVE_CUSTOMER, customerString, save);
                    Message message3 = new Message(Message.SAVE_ORDERLINE, orderlineString, save);
                    client.sendMessage(message);
                    client.sendMessage((message2));
                    client.sendMessage(message3);
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateOrderInfo(Order order) {
        orderIDTF.setText(String.valueOf(order.getOrderID()));
        customerIDTF.setText(String.valueOf(order.getCustomerID()));
        orderDateTF.setText(order.getDate());
        totalCostTF.setText(String.valueOf(order.getTotalCost()));
        totalTaxTF.setText(String.valueOf(order.getTotalTax()));
    }
}
