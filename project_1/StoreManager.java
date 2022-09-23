public class StoreManager {

    private static StoreManager instance = null;

    private SQLiteDataAdapter dao;

    private ProductView productView = null;
    public ProductView getProductView() {
        return productView;
    }
    private ProductController productController = null;

    //order
    private OrderView orderView = null;
    public OrderView getOrderView() { return orderView; }
    private OrderController orderController = null;

    //Customer
    private CustomerView customerView = null;
    public CustomerView getCustomerView() {
        return customerView;
    }
    private CustomerController customerController = null;

    //main menu
    private MainMenu mainView = null;
    public MainMenu getMainManu() {return mainView;}
    private MainMenuController mainController = null;



    public static StoreManager getInstance() {
        if (instance == null)
            instance = new StoreManager("SQLite");
        return instance;
    }

    public SQLiteDataAdapter getDataAccess() {
        return dao;
    }

    private StoreManager(String db) {
        // do some initialization here!!!
        if (db.equals("SQLite"))
            dao = new SQLiteDataAdapter();

        dao.connect();
        //order
        orderView = new OrderView();
        orderController = new OrderController(orderView, dao);

        productView = new ProductView();
        productController = new ProductController(productView, dao);

        customerView = new CustomerView();
        customerController = new CustomerController(customerView, dao);

        mainView = new MainMenu();
        //mainController = new MainMenuController(mainView, dao);
        mainController = new MainMenuController(mainView);

    }






}
