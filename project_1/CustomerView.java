import javax.swing.*;
import java.awt.*;

public class CustomerView extends JFrame {

    public JTextField txtCustomerID = new JTextField(30);
    public JTextField txtName = new JTextField(30);
    public JTextField txtNumber = new JTextField(30);

    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");

    public CustomerView() {

        this.setTitle("Customer View");
        this.setSize(new Dimension(600, 300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));    // make this window with box layout

        JPanel line1 = new JPanel();
        line1.add(new JLabel("Customer ID"));
        line1.add(txtCustomerID);
        this.getContentPane().add(line1);

        JPanel line2 = new JPanel();
        line2.add(new JLabel("Name"));
        line2.add(txtName);
        this.getContentPane().add(line2);

        JPanel line3 = new JPanel();
        line3.add(new JLabel("Number"));
        line3.add(txtNumber);
        this.getContentPane().add(line3);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnSave);

        this.getContentPane().add(buttonPanel);

    }

}
