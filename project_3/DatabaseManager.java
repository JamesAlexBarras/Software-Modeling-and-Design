import com.google.gson.Gson;

import javax.annotation.processing.Processor;
import javax.xml.crypto.Data;
import java.sql.*;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    public static DatabaseManager getInstance() {
        if (databaseManager == null) databaseManager = new DatabaseManager();
        return databaseManager;
    }

    private Connection connection;

    private DatabaseManager() {

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:data/store.db");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public Message process(String requestString) {

        Gson gson = new Gson();
        Message message = gson.fromJson(requestString, Message.class);
        productIDsave save = new productIDsave();


        switch (message.getId()) {
            case Message.LOAD_PRODUCT: {
                Product product = loadProduct(Integer.parseInt(message.getContent()));
                Message replyMessage = new Message(Message.LOAD_PRODUCT_REPLY, gson.toJson(product), save);
                return replyMessage;
            }

            case Message.LOAD_CUSTOMERPRODUCT: {
                Product product = loadProduct(Integer.parseInt(message.getContent()));
                Message replyMessage = new Message(Message.LOAD_CUSTOMERPRODUCT_REPLY, gson.toJson(product), save);
                return replyMessage;
            }

            case Message.SAVE_PRODUCT: {
                Product product = gson.fromJson(message.getContent(), Product.class);
                boolean result = saveProduct(product);
                if (result) return new Message(Message.SUCCESS, "Product saved", save);
                else return new Message(Message.FAIL, "Cannot save the product", save);
            }

            case Message.LOAD_CUSTOMER: {
                Customer customer = loadCustomer(Integer.parseInt(message.getContent()));
                Message replyMessage = new Message(Message.LOAD_CUSTOMER_REPLY, gson.toJson(customer), save);
                return replyMessage;
            }

            case Message.SAVE_CUSTOMER: {
                Customer customer = gson.fromJson(message.getContent(), Customer.class);
                boolean result = saveCustomer(customer);
                if (result) return new Message(Message.SUCCESS, "Customer saved", save);
                else return new Message(Message.FAIL, "Cannot save the customer", save);
            }

            case Message.LOAD_ORDER: {
                Order order = loadOrder(Integer.parseInt(message.getContent()));
                Message replyMessage = new Message(Message.LOAD_ORDER_REPLY, gson.toJson(order), save);
                return replyMessage;
            }

            case Message.DELETE_ORDER: {
                Order order = gson.fromJson(message.getContent(), Order.class);
                boolean result = deleteOrder(order);
                if (result) return new Message(Message.SUCCESS, "Order Deleted", save);
                else return new Message(Message.FAIL, "Cannot delete the order", save);
            }

            case Message.SAVE_ORDER: {
                Order order = gson.fromJson(message.getContent(), Order.class);
                boolean result = saveOrder(order);
                if (result) return new Message(Message.SUCCESS, "Order saved", save);
                else return new Message(Message.FAIL, "Cannot save the order", save);
            }

            case Message.LOAD_ORDERLINE:{
                OrderLine orderline = loadOrderLine(Integer.parseInt(message.getContent()), message.getprodId());
                Message replyMessage = new Message(Message.LOAD_ORDERLINE_REPLY, gson.toJson(orderline), save);
                return replyMessage;
            }

            case Message.SAVE_ORDERLINE:{
                OrderLine orderline = gson.fromJson(message.getContent(), OrderLine.class);
                boolean result = saveOrderLine(orderline);
                if (result) return new Message(Message.SUCCESS, "OrderLine saved", save);
                else return new Message(Message.FAIL, "Cannot save the OrderLine", save);
            }

            case Message.REGISTER_LOGIN:{
                LoginInfo logininfo = gson.fromJson(message.getContent(), LoginInfo.class);
                boolean result = saveLoginInfo(logininfo);
                if (result) return new Message(Message.SUCCESS, "LoginInfo saved", save);
                else return new Message(Message.FAIL, "Cannot save the LoginInfo", save);
            }

            case Message.CHECK_LOGIN:{
                LoginInfo loginInfo = loadloginInfo(Integer.parseInt(message.getContent()));
                Message replyMessage = new Message(Message.CHECK_LOGIN_REPLY, gson.toJson(loginInfo), save);
                return replyMessage;
            }

            case Message.LOAD_LOGININFO: {
                LoginInfo loginInfo = loadloginInfo(Integer.parseInt(message.getContent()));
                Message replyMessage = new Message(Message.LOAD_LOGININFO_REPLY, gson.toJson(loginInfo), save);
                return replyMessage;
            }

            default:
                return new Message(Message.FAIL, "Cannot process the message", save);
        }
    }

    public Product loadProduct(int id) {
        try {
            String query = "SELECT * FROM Products WHERE ProductID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getDouble(4));
                resultSet.close();
                statement.close();

                return product;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
            statement.setInt(1, product.getProductID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Products SET Name = ?, Price = ?, Quantity = ? WHERE ProductID = ?");
                statement.setString(1, product.getName());
                statement.setDouble(2, product.getPrice());
                statement.setDouble(3, product.getQuantity());
                statement.setInt(4, product.getProductID());
            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Products VALUES (?, ?, ?, ?)");
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setDouble(4, product.getQuantity());
                statement.setInt(1, product.getProductID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public Customer loadCustomer(int id) {
        try {
            String query = "SELECT * FROM Customers WHERE CustomerID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(resultSet.getInt(1));
                customer.setFirstName(resultSet.getString(2));
                //customer.setLastName(resultSet.getString(3));
                customer.setPhoneNumber(resultSet.getInt(3));
                resultSet.close();
                statement.close();

                return customer;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveCustomer(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customers WHERE CustomerID = ?");
            statement.setInt(1, customer.getCustomerID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Customers SET Name = ?, Phone = ? WHERE CustomerID = ?");
                statement.setString(1, customer.getFirstName());
                statement.setInt(2, customer.getPhoneNumber());
                statement.setInt(3, customer.getCustomerID());

            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Customers VALUES (?, ?, ?)");
                statement.setString(2, customer.getFirstName());
                statement.setInt(3, customer.getPhoneNumber());
                statement.setInt(1, customer.getCustomerID());

            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    /*public boolean saveCustomer(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customers WHERE CustomerID = ?");
            statement.setInt(1, customer.getCustomerID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Customers SET FirstName = ?, LastName = ?, PhoneNumber = ? WHERE CustomerID = ?");
                statement.setString(1, customer.getFirstName());
                //statement.setString(2, customer.getLastName());
                statement.setInt(2, customer.getPhoneNumber());
                statement.setInt(3, customer.getCustomerID());
            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Customers VALUES (?, ?, ?, ?)");
                statement.setString(2, customer.getFirstName());
                //statement.setString(3, customer.getLastName());
                statement.setInt(3, customer.getPhoneNumber());
                statement.setInt(1, customer.getCustomerID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }*/

    public Order loadOrder(int id) {
        try {
            String query = "SELECT * FROM Orders WHERE OrderID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Order order = new Order();
                order.setOrderID(resultSet.getInt(1));
                order.setCustomerID(resultSet.getInt(3));
                order.setTotalCost(resultSet.getDouble(4));
                order.setTotalTax(resultSet.getDouble(5));
                order.setDate(resultSet.getString(2));
                resultSet.close();
                statement.close();

                return order;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrder(Order order) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE OrderID = ?");
            statement.setInt(1, order.getOrderID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Orders SET OrderDate = ?, CustomerID = ?, TotalCost = ?, TotalTax = ? WHERE OrderID = ?");
                statement.setString(1, order.getDate());
                statement.setInt(2, order.getCustomerID());
                statement.setDouble(3, order.getTotalCost());
                statement.setDouble(4, order.getTotalTax());
                statement.setInt(5, order.getOrderID());
            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Orders VALUES (?, ?, ?, ?, ?)");
                statement.setString(2, order.getDate());
                statement.setInt(3, order.getCustomerID());
                statement.setDouble(4, order.getTotalCost());
                statement.setDouble(5, order.getTotalTax());
                statement.setInt(1, order.getOrderID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public boolean deleteOrder(Order order) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE OrderID = ?");
            statement.setInt(1, order.getOrderID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Orders SET OrderDate = ?, CustomerID = ?, TotalCost = ?, TotalTax = ? WHERE OrderID = ?");
                statement.setString(1, order.getDate());
                statement.setInt(2, order.getCustomerID());
                statement.setDouble(3, order.getTotalCost());
                statement.setDouble(4, order.getTotalTax());
                statement.setInt(5, order.getOrderID());
            }

            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public OrderLine loadOrderLine(int id, int id1) {
        try {
            String query = "SELECT * FROM OrderLine WHERE OrderID = " + id + " AND ProductID = " + id1;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                OrderLine orderline = new OrderLine();
                orderline.setOrderID(resultSet.getInt(1));
                orderline.setProductID(resultSet.getInt(2));
                orderline.setQuantity(resultSet.getDouble(3));
                orderline.setCost(resultSet.getDouble(4));
                resultSet.close();
                statement.close();

                return orderline;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrderLine(OrderLine orderline) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM OrderLine WHERE OrderID = ? AND ProductID = ?");
            statement.setInt(1, orderline.getOrderID());
            statement.setInt(2, orderline.getProductID());


            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE OrderLine SET Quantity = ?, Cost = ? WHERE OrderID = ? AND ProductID = ?");
                statement.setInt(3, orderline.getOrderID());
                statement.setInt(4, orderline.getProductID());
                statement.setDouble(1, orderline.getQuantity());
                statement.setDouble(2, orderline.getCost());

            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO OrderLine VALUES (?, ?, ?, ?)");
                statement.setInt(2, orderline.getProductID());
                statement.setDouble(3, orderline.getQuantity());
                statement.setDouble(4, orderline.getCost());
                statement.setInt(1, orderline.getOrderID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public boolean saveLoginInfo(LoginInfo loginInfo) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
            statement.setInt(1, loginInfo.getuserID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Users SET UserName = ?, Password = ?, DisplayName = ?, IsManager = ? WHERE UserID = ?");
                statement.setInt(5, loginInfo.getuserID());
                statement.setString(1, loginInfo.getUsername());
                statement.setString(2, loginInfo.getPassword());
                statement.setString(3, loginInfo.getDisplayName());
                statement.setInt(4, loginInfo.getisAdmin());
            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Users VALUES (?, ?, ?, ?, ?)");
                statement.setInt(1, loginInfo.getuserID());
                statement.setString(2, loginInfo.getUsername());
                statement.setString(3, loginInfo.getPassword());
                statement.setString(4, loginInfo.getDisplayName());
                statement.setInt(5, loginInfo.getisAdmin());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public LoginInfo loadloginInfo(int id) {
        try {
            String query = "SELECT * FROM Users WHERE UserID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.setUserID(resultSet.getInt(1));
                loginInfo.setUsername(resultSet.getString(2));
                loginInfo.setPassword(resultSet.getString(3));
                loginInfo.setDisplayName(resultSet.getString(4));
                loginInfo.setisAdmin(resultSet.getInt(5));

                resultSet.close();
                statement.close();

                return loginInfo;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }
}
