package data;

import org.ta4j.core.Decimal;

import java.sql.Date;

/**
 * class maps to rows in order_history table
 */
public class Order {
    // Total 24 variables
    private Integer order_history_id = null;
    private String exchange= null;
    private Integer token= null;
    private String symbol= null;
    private String product= null;
    private String order_type= null;
    private String duration= null;
    private Double price= null;
    private Double trigger_price= null;
    private Integer quantity= null;
    private Integer disclosed_quantity= null;
    private String transaction_type= null;
    private Double average_price= null;
    private Integer traded_quantity= null;
    private String message= null;
    private String exchange_order_id= null;
    private String parent_order_id= null;
    private String order_id= null;
    private Date exchange_time= null;
    private Date time_in_micro= null;
    private String status= null;
    private Boolean is_amo= null;
    private Date valid_date= null;
    private String order_request_id= null;

    public Order(){
    }

    public Order(String symbol, double price , int quantity){
        //this.order_history_id = order_history_id;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    public Order(String exchange, int token, String symbol, String product, String order_type, String duration, double price, double trigger_price, int quantity, int disclosed_quantity, String transaction_type, double average_price, int traded_quantity, String message, String exchange_order_id, String parent_order_id, String order_id, Date exchange_time, Date time_in_micro, String status, boolean is_amo, Date valid_date, String order_request_id) {
        //this.order_history_id = order_history_id;
        this.exchange = exchange;
        this.token = token;
        this.symbol = symbol;
        this.product = product;
        this.order_type = order_type;
        this.duration = duration;
        this.price = price;
        this.trigger_price = trigger_price;
        this.quantity = quantity;
        this.disclosed_quantity = disclosed_quantity;
        this.transaction_type = transaction_type;
        this.average_price = average_price;
        this.traded_quantity = traded_quantity;
        this.message = message;
        this.exchange_order_id = exchange_order_id;
        this.parent_order_id = parent_order_id;
        this.order_id = order_id;
        this.exchange_time = exchange_time;
        this.time_in_micro = time_in_micro;
        this.status = status;
        this.is_amo = is_amo;
        this.valid_date = valid_date;
        this.order_request_id = order_request_id;
    }

    public Integer getOrder_history_id() {
        return order_history_id;
    }

    public void setOrder_history_id(Integer order_history_id) {
        this.order_history_id = order_history_id;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTrigger_price() {
        return trigger_price;
    }

    public void setTrigger_price(Double trigger_price) {
        this.trigger_price = trigger_price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDisclosed_quantity() {
        return disclosed_quantity;
    }

    public void setDisclosed_quantity(Integer disclosed_quantity) {
        this.disclosed_quantity = disclosed_quantity;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public Double getAverage_price() {
        return average_price;
    }

    public void setAverage_price(Double average_price) {
        this.average_price = average_price;
    }

    public Integer getTraded_quantity() {
        return traded_quantity;
    }

    public void setTraded_quantity(Integer traded_quantity) {
        this.traded_quantity = traded_quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExchange_order_id() {
        return exchange_order_id;
    }

    public void setExchange_order_id(String exchange_order_id) {
        this.exchange_order_id = exchange_order_id;
    }

    public String getParent_order_id() {
        return parent_order_id;
    }

    public void setParent_order_id(String parent_order_id) {
        this.parent_order_id = parent_order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Date getExchange_time() {
        return exchange_time;
    }

    public void setExchange_time(Date exchange_time) {
        this.exchange_time = exchange_time;
    }

    public Date getTime_in_micro() {
        return time_in_micro;
    }

    public void setTime_in_micro(Date time_in_micro) {
        this.time_in_micro = time_in_micro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIs_amo() {
        return is_amo;
    }

    public void setIs_amo(Boolean is_amo) {
        this.is_amo = is_amo;
    }

    public Date getValid_date() {
        return valid_date;
    }

    public void setValid_date(Date valid_date) {
        this.valid_date = valid_date;
    }

    public String getOrder_request_id() {
        return order_request_id;
    }

    public void setOrder_request_id(String order_request_id) {
        this.order_request_id = order_request_id;
    }
}
