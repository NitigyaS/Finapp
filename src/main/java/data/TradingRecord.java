package data;

public class TradingRecord {
    private Integer trading_record_id;
    private Integer order_history_id;
    private Double dynamic_stop_loss;

    public TradingRecord() {
    }

    public TradingRecord(Integer trading_record_id, Integer order_history_id) {
        this.trading_record_id = trading_record_id;
        this.order_history_id = order_history_id;
    }


    public TradingRecord(Integer trading_record_id, Integer order_history_id, Double dynamic_stop_loss) {

        this.trading_record_id = trading_record_id;
        this.order_history_id = order_history_id;
        this.dynamic_stop_loss = dynamic_stop_loss;
    }

    public Integer getTrading_record_id() {
        return trading_record_id;
    }

    public void setTrading_record_id(Integer trading_record_id) {
        this.trading_record_id = trading_record_id;
    }

    public Integer getOrder_history_id() {
        return order_history_id;
    }

    public void setOrder_history_id(Integer order_history_id) {
        this.order_history_id = order_history_id;
    }

    public Double getDynamic_stop_loss() {
        return dynamic_stop_loss;
    }

    public void setDynamic_stop_loss(Double dynamic_stop_loss) {
        this.dynamic_stop_loss = dynamic_stop_loss;
    }

}
