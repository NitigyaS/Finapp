package dao;

import application.Slave;
import com.sun.tools.corba.se.idl.constExpr.Or;
import data.Order;
import data.Stock;
import data.TradingRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.sql.*;

public class TradingRecordDao {

    Database database;

    private static Logger logger = LoggerFactory.getLogger(TradingRecordDao.class);

    public TradingRecordDao(){
        database = new Database();
    }

    private TradingRecord extractTradingRecordFromResultSet(ResultSet resultSet) throws SQLException {
        TradingRecord tradingRecord = new TradingRecord();
        tradingRecord.setTrading_record_id(resultSet.getInt("trading_record_id"));
        tradingRecord.setOrder_history_id(resultSet.getInt("order_history_id"));
        tradingRecord.setDynamic_stop_loss(resultSet.getDouble("dynamic_stop_loss"));
        return tradingRecord;
    }

    /**
     *
     * @param preparedStatement
     * @param tradingRecord
     * @return preparedStatement
     * @throws SQLException
     */
    private PreparedStatement createPreparedStatementforTradingRecord(PreparedStatement preparedStatement , TradingRecord tradingRecord) throws SQLException{
        //preparedStatement.setInt(1, tradingRecord.getTrading_record_id());
        preparedStatement.setInt(1, tradingRecord.getOrder_history_id());
        preparedStatement.setDouble(2, tradingRecord.getDynamic_stop_loss());
        return preparedStatement;
    }

    /**
     *
     * @param trading_record_id
     * @return TradingRecord
     */
    private TradingRecord getTradingRecordById(int trading_record_id){
//        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT trading_record_id  , o.order_history_id , dynamic_stop_loss FROM order_history AS o JOIN trading_records AS t ON o.order_history_id = t.order_history_id WHERE trading_record_id = " + trading_record_id + ";\n");
            if(resultSet.next())
            {
                return extractTradingRecordFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }

        return null;
    }

    private TradingRecord getTradingRecordBySymbol(String symbol){
//        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT trading_record_id  , o.order_history_id , dynamic_stop_loss FROM order_history AS o JOIN trading_records AS t ON o.order_history_id = t.order_history_id WHERE symbol LIKE '" + symbol + "';\n");
            if(resultSet.next())
            {
                return extractTradingRecordFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }

        return null;
    }

    /**
     *
     * @param order
     * @param dynamic_stop_loss
     * @return
     */
    private int insertTradingRecord(Order order , double dynamic_stop_loss ){

        OrderDao orderDao = new OrderDao();
        int order_history_id = orderDao.insertOrder(order);
        TradingRecord tradingRecord = new TradingRecord(order_history_id , dynamic_stop_loss);

        Connection connection  =  database.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO `finapp`.`trading_records` (   `order_history_id` ,`dynamic_stop_loss`) VALUES (  ? ,?);\n" ,Statement.RETURN_GENERATED_KEYS);
            preparedStatement = createPreparedStatementforTradingRecord(preparedStatement , tradingRecord);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int trading_record_id = resultSet.getInt(1);
                logger.debug("Trading Record Inserted at id " + trading_record_id);
                return trading_record_id;
            }

        } catch (SQLException ex) {
            logger.error(ex.toString());
        }

        return 0;
    }

    /**
     *
     * @param trading_record_id
     * @return Boolean
     */
    private Boolean deleteTradingRecord(int trading_record_id){
 //       Database database = new Database();
        Connection connection  =  database.getConnection();

        try {
            //connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();
            logger.debug("Deleting trading_record " + trading_record_id);
            int i = stmt.executeUpdate("DELETE FROM trading_records WHERE trading_record_id =" + trading_record_id );
            if(i == 1) {
                //onnection.commit();
//                connection.close();
                return true;
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
        return false;
    }

    private Boolean updateDynamicStopLoss(int trading_record_id, double dynamic_stop_loss){
 //       Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement( "UPDATE trading_records SET dynamic_stop_loss = ? WHERE trading_record_id = ?;\n");
            preparedStatement.setDouble(1,dynamic_stop_loss);
            preparedStatement.setInt(2,trading_record_id);

            int i = preparedStatement.executeUpdate();
            if(i == 1) {
                connection.commit();
                return true;
            }

        } catch (SQLException ex) {
            logger.error(ex.toString());
        }

        return false;
    }

    /**
     *
     * @param order
     * @return boolean
     */
    public boolean canEnter(Order order){
        if (getTradingRecordBySymbol(order.getSymbol()) == null){
            if ( order.getTransaction_type() == "S" ){
                logger.debug("Can not Enter :: Transaction Type is " + order.getTransaction_type());
                return false;
            }
            logger.debug("Can Enter : True ");
            return true;    // Can enter because it is there is no trading record for this stock.
        } else {
            logger.debug("Can not Enter :: Record Already exists.  ");
            return  false;  // Can not enter because there is a trading record for this stock.
        }

    }


    public int enter(Order order , double dynamic_stop_loss){
        if (canEnter(order)  ){
            return insertTradingRecord(order ,dynamic_stop_loss);
        } else {

            return  0;  // Can not enter because there is a trading record for this stock.
        }
    }

    /**
     *
     * @param order
     * @return boolean
     */
    public boolean canExit(Order order){
        if (getTradingRecordBySymbol(order.getSymbol()) == null){
            if ( order.getTransaction_type() == "B" ){
                logger.debug("Can not Exit :: Transaction Type is " + order.getTransaction_type());
                return false;
            }
            logger.debug("Can not Exit :: No record with symbol exists.");
            return false;    // Can not Exit because it is there is no trading record for this stock.
        } else {

            return  true;  // Can exit because there is a trading record for this stock.
        }
    }

    public boolean exit(Order order ){
        if (canExit(order) ){
            TradingRecord tradingRecord = getTradingRecordBySymbol(order.getSymbol());
            OrderDao orderDao = new OrderDao();
            orderDao.insertOrder(order);
            return deleteTradingRecord(tradingRecord.getTrading_record_id());

        } else {
            return  false;  // Can not enter because there is a trading record for this stock.
        }
    }

    public Order getEntry(int trading_record_id){
        TradingRecord tradingRecord = getTradingRecordById(trading_record_id);
        OrderDao orderDao = new OrderDao();
        Order lastOrder = orderDao.getOrder(tradingRecord.getOrder_history_id());
        if (lastOrder.getTransaction_type() == "B"){
            return lastOrder;
        }
        return null;
    }

    public Order getExit(int trading_record_id){
        TradingRecord tradingRecord = getTradingRecordById(trading_record_id);
        OrderDao orderDao = new OrderDao();
        Order lastOrder = orderDao.getOrder(tradingRecord.getOrder_history_id());
        if (lastOrder.getTransaction_type() == "S"){
            return lastOrder;
        }
        return null;
    }
}
