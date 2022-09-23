import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerProductView {
    private JTextField prodIDTF;
    private JTextField nameTF;
    private JTextField priceTF;
    private JTextField quantityTF;
    private JButton loadButton;
    private JPanel mainPanel;

    private Client client;

    /*public CustomerProductView(Client client) {
        this.client = client;
        productIDsave save = new productIDsave();


        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productID = prodIDTF.getText();
                Message message = new Message(Message.LOAD_PRODUCT, productID, save);
                client.sendMessage(message);
            }
        });


    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateProductInfo(Product product) {
        prodIDTF.setText(String.valueOf(product.getProductID()));
        nameTF.setText(product.getName());
        priceTF.setText(String.valueOf(product.getPrice()));
        quantityTF.setText(String.valueOf(product.getQuantity()));
    }*/

    public CustomerProductView(Client client) {
        this.client = client;
        productIDsave save = new productIDsave();


        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productID = prodIDTF.getText();
                Message message = new Message(Message.LOAD_CUSTOMERPRODUCT, productID, save);
                client.sendMessage(message);
            }
        });

       /* saveProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product product = new Product();

                product.setProductID(Integer.parseInt(productIDTF.getText()));
                product.setName(productNameTF.getText());
                product.setPrice(Double.parseDouble(productPriceTF.getText()));
                product.setQuantity(Double.parseDouble(productQuantityTF.getText()));

                Gson gson = new Gson();

                String productString = gson.toJson(product);

                Message message = new Message(Message.SAVE_PRODUCT, productString, save);
                client.sendMessage(message);
            }
        });*/
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateProductInfo(Product product) {
        prodIDTF.setText(String.valueOf(product.getProductID()));
        nameTF.setText(product.getName());
        priceTF.setText(String.valueOf(product.getPrice()));
        quantityTF.setText(String.valueOf(product.getQuantity()));
    }
}
