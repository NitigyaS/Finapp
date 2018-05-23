package dao;

import data.Order;

import java.sql.*;

public class OrderDao {


    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrder_history_id(rs.getInt("order_history_id"));
        order.setExchange(rs.getString("exchange"));
        order.setOrder_history_id(rs.getInt("token"));
        order.setExchange(rs.getString("symbol"));
        order.setExchange(rs.getString("product"));
        order.setExchange(rs.getString("order_type"));
        order.setExchange(rs.getString("duration"));
        order.setAverage_price(rs.getDouble("price"));
        order.setAverage_price(rs.getDouble("trigger_price"));
        order.setOrder_history_id(rs.getInt("quantity"));
        order.setOrder_history_id(rs.getInt("disclosed_quantity"));
        order.setExchange(rs.getString("transaction_type"));
        order.setAverage_price(rs.getDouble("average_price"));
        order.setOrder_history_id(rs.getInt("traded_quantity"));
        order.setExchange(rs.getString("message"));
        order.setExchange(rs.getString("exchange_order_id"));
        order.setExchange(rs.getString("parent_order_id"));
        order.setExchange(rs.getString("order_id"));
        order.setExchange(rs.getString("exchange_time"));
        order.setExchange(rs.getString("time_in_micro"));
        order.setExchange(rs.getString("status"));
        order.setExchange(rs.getString("is_amo"));
        order.setExchange(rs.getString("valid_date"));
        order.setExchange(rs.getString("order_request_id"));


        return order;
    }

    /**
     *
     * @param order_history_id
     * @return Order
     */
    Order getOrder(int order_history_id){
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM order_history WHERE order_history_id=" + order_history_id);
            if(rs.next())
            {
                return extractOrderFromResultSet(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error in OrderDao.getOrder");
            ex.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param order_history_id
     * @param updatedOrder
     * @return
     */
    public boolean updateOrder(int order_history_id ,Order updatedOrder){
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO `finapp`.`order_history` ( `exchange`, `token`, `symbol`, `product`, `order_type`, `duration`, `price`, `trigger_price`, `quantity`, `disclosed_quantity`, `transaction_type`, `average_price`, `traded_quantity`, `message`, `exchange_order_id`, `parent_order_id`, `order_id`, `exchange_time`, `time_in_micro`, `status`, `is_amo`, `valid_date`, `order_request_id`  ,`order_history_id`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);\n");
            preparedStatement = createPreparedStatementforOrder(preparedStatement , updatedOrder);
            preparedStatement.setInt(24,order_history_id);

            int i = preparedStatement.executeUpdate();
            if(i == 1) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     *
     * @param order
     * @return boolean
     */
    public boolean insertOrder(Order order) {
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO `finapp`.`order_history` ( `exchange`, `token`, `symbol`, `product`, `order_type`, `duration`, `price`, `trigger_price`, `quantity`, `disclosed_quantity`, `transaction_type`, `average_price`, `traded_quantity`, `message`, `exchange_order_id`, `parent_order_id`, `order_id`, `exchange_time`, `time_in_micro`, `status`, `is_amo`, `valid_date`, `order_request_id`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n");
            preparedStatement = createPreparedStatementforOrder(preparedStatement , order);

            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("Error in OrderDao.insertOrder");
            ex.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param order_history_id
     * @return boolean
     */
    public boolean deleteOrder(int order_history_id){
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM order_history WHERE order_history_id=" + order_history_id);
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("Error in OrderDao.deleteOrder");
            ex.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param preparedStatement
     * @param order
     * @return preparedStatement
     * @throws SQLException
     */
    private PreparedStatement createPreparedStatementforOrder(PreparedStatement preparedStatement , Order order) throws SQLException{
        // It inserts null if value is not set in the object
        if (order.getExchange() == null){
            preparedStatement.setNull(1, Types.NULL);
        }else {
            preparedStatement.setString(1,order.getExchange());
        }
        if (order.getToken() == null){
            preparedStatement.setNull(2, Types.NULL);
        }else {
            preparedStatement.setInt(2, order.getToken());
        }
        if (order.getSymbol() == null){
            preparedStatement.setNull(3, Types.NULL);
        }else {
            preparedStatement.setString(3,order.getSymbol());
        }
        if (order.getProduct() == null){
            preparedStatement.setNull(4, Types.NULL);
        }else {
            preparedStatement.setString(4,order.getProduct() );
        }
        if (order.getOrder_type() == null){
            preparedStatement.setNull(5, Types.NULL);
        }else {
            preparedStatement.setString(5,order.getOrder_type());
        }
        if (order.getDuration() == null){
            preparedStatement.setNull(6, Types.NULL);
        }else {
            preparedStatement.setString(6,order.getDuration() );
        }
        if (order.getPrice() == null){
            preparedStatement.setNull(7, Types.NULL);
        }else {
            preparedStatement.setDouble(7,order.getPrice());
        }
        if (order.getTrigger_price() == null){
            preparedStatement.setNull(8, Types.NULL);
        }else {
            preparedStatement.setDouble(8,order.getTrigger_price() );
        }
        if (order.getQuantity() == null){
            preparedStatement.setNull(9, Types.NULL);
        }else {
            preparedStatement.setInt(9,order.getQuantity());
        }
        if (order.getDisclosed_quantity() == null){
            preparedStatement.setNull(10, Types.NULL);
        }else {
            preparedStatement.setInt(10,order.getDisclosed_quantity() );
        }
        if (order.getTransaction_type() == null){
            preparedStatement.setNull(11, Types.NULL);
        }else {
            preparedStatement.setString(11,order.getTransaction_type());
        }
        if (order.getAverage_price() == null){
            preparedStatement.setNull(12, Types.NULL);
        }else {
            preparedStatement.setDouble(12,order.getAverage_price() );
        }
        if (order.getTraded_quantity() == null){
            preparedStatement.setNull(13, Types.NULL);
        }else {
            preparedStatement.setInt(13,order.getTraded_quantity());
        }
        if (order.getMessage() == null){
            preparedStatement.setNull(14, Types.NULL);
        }else {
            preparedStatement.setString(14,order.getMessage() );
        }
        if (order.getExchange_order_id() == null){
            preparedStatement.setNull(15, Types.NULL);
        }else {
            preparedStatement.setString(15,order.getExchange_order_id());
        }
        if (order.getParent_order_id() == null){
            preparedStatement.setNull(16, Types.NULL);
        }else {
            preparedStatement.setString(16,order.getParent_order_id() );
        }

        if (order.getOrder_id() == null){
            preparedStatement.setNull(17, Types.NULL);
        }else {
            preparedStatement.setString(17,order.getOrder_id());
        }
        if (order.getExchange_time() == null){
            preparedStatement.setNull(18, Types.NULL);
        }else {
            preparedStatement.setDate(18,order.getExchange_time() );
        }
        if (order.getTime_in_micro() == null){
            preparedStatement.setNull(19, Types.NULL);
        }else {
            preparedStatement.setDate(19,order.getTime_in_micro());
        }
        if (order.getStatus() == null){
            preparedStatement.setNull(20, Types.NULL);
        }else {
            preparedStatement.setString(20,order.getStatus() );
        }
        if (order.getIs_amo() == null){
            preparedStatement.setNull(21, Types.NULL);
        }else {
            preparedStatement.setBoolean(21,order.getIs_amo());
        }
        if (order.getValid_date() == null){
            preparedStatement.setNull(22, Types.NULL);
        }else {
            preparedStatement.setDate(22,order.getValid_date() );
        }
        if (order.getOrder_request_id() == null){
            preparedStatement.setNull(23, Types.NULL);
        }else {
            preparedStatement.setString(23,order.getOrder_request_id());
        }

        return preparedStatement;

    }
}
