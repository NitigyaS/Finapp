package dao;

import data.TradingRecord;

import java.sql.*;

public class TradingRecordDao {

    private TradingRecord extractTradingRecordFromResultSet(ResultSet rs) throws SQLException {
        TradingRecord tradingRecord = new TradingRecord();
        tradingRecord.setTrading_record_id(rs.getInt("trading_record_id"));
        tradingRecord.setOrder_history_id(rs.getInt("order_history_id"));
        tradingRecord.setDynamic_stop_loss(rs.getDouble("dynamic_stop_loss"));
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
        preparedStatement.setInt(1, tradingRecord.getTrading_record_id());
        preparedStatement.setInt(2, tradingRecord.getOrder_history_id());
        preparedStatement.setDouble(3, tradingRecord.getDynamic_stop_loss());
        return preparedStatement;
    }

    /**
     *
     * @param trading_record_id
     * @return TradingRecord
     */
    public TradingRecord getTradingRecord(int trading_record_id){
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM trading_records WHERE trading_record_id=" + trading_record_id);
            if(rs.next())
            {
                return extractTradingRecordFromResultSet(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error in OrderDao.getOrder");
            ex.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param tradingRecord
     * @return Boolean
     */
    public Boolean insertTradingRecord(TradingRecord tradingRecord){
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO `finapp`.`trading_records` ( `trading_record_id`,  `order_history_id` ,`dynamic_stop_loss`) VALUES ( ?, ? ,?);\n");
            preparedStatement = createPreparedStatementforTradingRecord(preparedStatement , tradingRecord);

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
     * @param trading_record_id
     * @return Boolean
     */
    public Boolean deleteTradingRecord(int trading_record_id){
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM trading_records WHERE trading_record_id=" + trading_record_id);
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("Error in OrderDao.deleteOrder");
            ex.printStackTrace();
        }
        return false;
    }

    public Boolean updateDynamicStopLoss(int  trading_record_id, double dynamic_stop_loss){
        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement( "update trading_records set dynamic_stop_loss = ? where trading_record_id = ?;\n");
            preparedStatement.setDouble(1,dynamic_stop_loss);
            preparedStatement.setInt(2,trading_record_id);

            int i = preparedStatement.executeUpdate();
            if(i == 1) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
