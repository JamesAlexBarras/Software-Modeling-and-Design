import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuController implements ActionListener {

    //void connect();

    MainMenu myView;
    //DataAccess myDAO;

    public MainMenuController(MainMenu view/*, DataAccess dao*/) {
      //  myDAO = dao;
        myView = view;
        myView.btnCustomer.addActionListener(this);
        myView.btnProduct.addActionListener(this);
        myView.btnOrder.addActionListener(this);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myView.btnProduct) {      // button Load is clicked
            myView.dispose();
            StoreManager.getInstance().getProductView().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            StoreManager.getInstance().getProductView().setVisible(true); // Show the ProductView!
        }

        if (e.getSource() == myView.btnOrder) {      // button Save is clicked
            myView.dispose();
            StoreManager.getInstance().getOrderView().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            StoreManager.getInstance().getOrderView().setVisible(true); // Show the ProductView!
        }

        if(e.getSource() == myView.btnCustomer) {
            myView.dispose();
            StoreManager.getInstance().getCustomerView().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            StoreManager.getInstance().getCustomerView().setVisible(true); // Show the ProductView!
        }

    }
}
