public class Message {

    public static final int FAIL = -1;
    public static final int SUCCESS = 0;

    public static final int LOAD_PRODUCT = 1;
    public static final int LOAD_PRODUCT_REPLY = 2;

    public static final int SAVE_PRODUCT = 3;

    public static final int LOAD_CUSTOMER = 4;
    public static final int LOAD_CUSTOMER_REPLY = 5;

    public static final int SAVE_CUSTOMER = 6;

    public static final int LOAD_ORDER = 8;
    public static final int LOAD_ORDER_REPLY = 10;
    public static final int SAVE_ORDER = 7;

    public static final int LOAD_ORDERLINE = 11;
    public static final int LOAD_ORDERLINE_REPLY = 12;
    public static final int SAVE_ORDERLINE = 13;

    private int id; // the type of the message
    private String content; // the content of the message
    private int id1;

    public Message(int id, String content, productIDsave save) {
        this.id = id;
        this.content = content;
        this.id1 = save.getProductID();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getprodId() {
        return id1;
    }

    public void setprodId(int id) {
        this.id1 = id;
    }

}
