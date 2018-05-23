package data;

/**
 * Class Maps to rows in stock_list table
 */
public class Stock {
    private  Integer id;
    private  String symbol;

    public Stock(Integer id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public Stock() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
