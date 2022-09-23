import com.google.gson.Gson;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;

public class Client {
    private JTextArea messageTextArea;
    private JButton manageCheckoutButton;
    private JButton manageProductButton;
    private JButton manageCustomerButton;
    private JPanel mainPanel;
    private JTextField usernameTF;
    private JTextField passwordTF;
    private JButton registerButton;
    private JButton loginButton;
    private JTextField userIDTF;
    private JButton yesButton;


    private SecretKey secretKey;

    private byte[] initializationVector;


    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private Gson gson;

    private Worker worker;

    public ProductViewController productViewController;
    public CustomerViewController customerViewController;
    public OrderViewController orderViewController;
    public OrderLineViewController orderLineViewController;
    public Login login;
    public CustomerSelection customerSelection;
    public CustomerProductView customerProductView;
    public productIDsave saveID = new productIDsave();
    public CustomerID currentCustomerID = new CustomerID();




    public Client() {
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 12002);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // send the secret key
            secretKey = KeyService.createAESKey();

            String keyString = KeyService.convertSecretKeyToString(secretKey);

            dataOutputStream.writeUTF(keyString);

            // send the initialization vector

            initializationVector = KeyService.createInitializationVector();

            String vectorString = Base64.getEncoder().encodeToString(initializationVector);

            dataOutputStream.writeUTF(vectorString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }





        gson = new Gson();

        worker = new Worker();
        Thread workerThread = new Thread(worker);
        workerThread.start();

        this.productViewController = new ProductViewController(this);
        this.customerViewController = new CustomerViewController(this);
        this.orderViewController = new OrderViewController(this);
        this.orderLineViewController = new OrderLineViewController(this);
        this.customerSelection = new CustomerSelection(this);
        this.customerProductView = new CustomerProductView(this);
        this.login = new Login(this);

        /*manageProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Manage Product");
                frame.setContentPane(productViewController.getMainPanel());
                frame.setMinimumSize(new Dimension(800, 400));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        manageCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Manage Customer");
                frame.setContentPane(customerViewController.getMainPanel());
                frame.setMinimumSize(new Dimension(800, 400));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        manageCheckoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Manage Orders");
                frame.setContentPane(orderViewController.getMainPanel());
                frame.setMinimumSize(new Dimension(800, 400));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Manage a specific aspect of an order (orderline)");
                //frame.setContentPane(orderLineViewController.getMainPanel());
                frame.setMinimumSize(new Dimension(800, 400));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        }); */

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameTF.getText().equals("admin") && passwordTF.getText().equals("password") ) {
                    JFrame frame = new JFrame("Manage Product");
                    frame.setContentPane(productViewController.getMainPanel());
                    frame.setMinimumSize(new Dimension(800, 400));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);

                } else {
                    currentCustomerID.setCustomerID(Integer.parseInt(userIDTF.getText()));

                    //load customer panel
                    JFrame frame = new JFrame("Customer Panel");
                    frame.setContentPane(customerSelection.getMainPanel());
                    frame.setMinimumSize(new Dimension(800, 400));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);

                }




            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginInfo loginInfo = new LoginInfo();
                Customer customer = new Customer();

                customer.setCustomerID(Integer.parseInt(userIDTF.getText()));
                customer.setPhoneNumber(0);
                customer.setFirstName("unkown");

                loginInfo.setUsername(usernameTF.getText());
                loginInfo.setPassword(passwordTF.getText());
                loginInfo.setisAdmin(0);
                loginInfo.setUserID(Integer.parseInt(userIDTF.getText()));

                Gson gson = new Gson();
                Gson gson1 = new Gson();

                String loginString = gson.toJson(loginInfo);
                String customerString = gson.toJson(customer);

                Message message = new Message(Message.REGISTER_LOGIN, loginString, saveID);
                Message message1 = new Message(Message.SAVE_CUSTOMER, customerString, saveID);

                sendMessage(message);
                sendMessage(message1);
            }
        });
    }

    public void sendMessage(Message message) {

        String str = gson.toJson(message);
        try {

            // Encrypting the message
            // using the symmetric key
            byte[] cipherText
                    = KeyService.do_AESEncryption(
                    str,
                    secretKey,
                    initializationVector);

            String cipherTextString = Base64.getEncoder().encodeToString(cipherText);

            dataOutputStream.writeUTF(cipherTextString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                String replyString = null;
                try {
                    replyString = dataInputStream.readUTF();

                    byte[] decode = Base64.getDecoder().decode(replyString);

                    replyString
                            = KeyService.do_AESDecryption(
                            decode,
                            secretKey,
                            initializationVector);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message message = gson.fromJson(replyString, Message.class);

                processMessage(message);

            }
        }
    }

    private void processMessage(Message message) {
        //messageTextArea.append(gson.toJson(message) + "\n");
        switch (message.getId()) {
            case Message.LOAD_PRODUCT_REPLY: {
                Product product = gson.fromJson(message.getContent(), Product.class);
                productViewController.updateProductInfo(product);
                break;
            }

            case Message.LOAD_CUSTOMERPRODUCT_REPLY: {
                Product product = gson.fromJson(message.getContent(), Product.class);
                customerProductView.updateProductInfo(product);
                break;
            }

            case Message.LOAD_CUSTOMER_REPLY: {
                Customer customer = gson.fromJson(message.getContent(), Customer.class);
                customerViewController.updateCustomerInfo(customer);
                break;
            }

            case Message.LOAD_ORDER_REPLY: {
                Order order = gson.fromJson(message.getContent(), Order.class);
                orderViewController.updateOrderInfo(order);
                break;
            }

            case Message.LOAD_ORDERLINE_REPLY: {
                OrderLine orderline = gson.fromJson(message.getContent(), OrderLine.class);
                orderLineViewController.updateOrderLineInfo(orderline);
                break;
            }

            case Message.LOAD_LOGININFO_REPLY: {
                LoginInfo loginInfo = gson.fromJson(message.getContent(), LoginInfo.class);
                login.updateLoginInfo(loginInfo);
                break;
            }

            default:
                return;
        }

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Client");
        frame.setContentPane(new Client().mainPanel);
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}
