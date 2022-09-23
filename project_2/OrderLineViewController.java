import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderLineViewController {
    private JPanel mainPanel;
    private JTextField orderIDTF;
    private JTextField productIDTF;
    private JTextField quantityTF;
    private JTextField costTF;
    private JButton savebutton;
    private JButton loadbutton;

    private Client client;
    private productIDsave saveID = new productIDsave();

    public OrderLineViewController(Client client) {
        this.client = client;

        loadbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderID = orderIDTF.getText();
                saveID.setProductID(Integer.parseInt(productIDTF.getText()));
                Message message = new Message(Message.LOAD_ORDERLINE, orderID, saveID);
                client.sendMessage(message);
            }
        });

        savebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderLine orderline = new OrderLine();
                Product product = new Product();
                productIDsave save = new productIDsave();
                save.setProductID(Integer.parseInt(productIDTF.getText()));
                product.setProductID(Integer.parseInt(productIDTF.getText()));
                product.setName("unknown");
                product.setPrice(Double.parseDouble(costTF.getText()));
                product.setQuantity(Double.parseDouble(quantityTF.getText()));



                orderline.setOrderID(Integer.parseInt(orderIDTF.getText()));
                orderline.setProductID(Integer.parseInt(productIDTF.getText()));
                orderline.setQuantity(Double.parseDouble(quantityTF.getText()));
                orderline.setCost(Double.parseDouble(costTF.getText()));

                Gson gson = new Gson();
                Gson gson2 = new Gson();

                String orderlineString = gson.toJson(orderline);
                String productString = gson2.toJson(product);

                Message message = new Message(Message.SAVE_ORDERLINE, orderlineString, save);
                Message message2 = new Message(Message.SAVE_PRODUCT, productString, save);
                client.sendMessage(message);
                client.sendMessage((message2));
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateOrderLineInfo(OrderLine orderline) {
        orderIDTF.setText(String.valueOf(orderline.getOrderID()));
        productIDTF.setText(String.valueOf(orderline.getProductID()));
        costTF.setText(String.valueOf(orderline.getCost()));
        quantityTF.setText(String.valueOf(orderline.getQuantity()));
    }







}
