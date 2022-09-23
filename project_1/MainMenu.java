import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public JButton btnOrder = new JButton("Order");
    public JButton btnProduct = new JButton("Product");
    public JButton btnCustomer = new JButton("Customer");


    public MainMenu() {

        this.setTitle("Main Menu");
        this.setSize(new Dimension(600, 300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));    // make this window with box layout

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnOrder);
        buttonPanel.add(btnProduct);
        buttonPanel.add(btnCustomer);

        this.getContentPane().add(buttonPanel);

    }

}
