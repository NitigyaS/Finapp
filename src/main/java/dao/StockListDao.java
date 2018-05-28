package dao;

import application.Slave;
import data.Order;
import data.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class StockListDao {

    Database database ;

    private static Logger logger = LoggerFactory.getLogger(StockListDao.class);

    public StockListDao(){
        database = new Database();
    }

    private Stock extractStockFromResultSet(ResultSet resultSet) throws SQLException {
        Stock stock = new Stock();
        stock.setId(resultSet.getInt("stock_list_id"));
        stock.setSymbol(resultSet.getString("stock_symbol"));
        return stock;
    }

    /**
     *
     * @param preparedStatement
     * @param stock
     * @return preparedStatement
     * @throws SQLException
     */
    private PreparedStatement createPreparedStatementforOrder(PreparedStatement preparedStatement , Stock stock) throws SQLException{
        preparedStatement.setInt(1, stock.getId());
        preparedStatement.setString(2, stock.getSymbol());
        return preparedStatement;
    }

    /**
     *
     * @param stock_list_id
     * @return Stock
     */
    public Stock getStock(int stock_list_id){
        Connection connection  =  database.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM stock_list WHERE stock_list_id=" + stock_list_id);
            if(resultSet.next())
            {
                return extractStockFromResultSet(resultSet);
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }

        return null;
    }

    /**
     *
     * @param stock
     * @return Boolean
     */
    public Boolean insertStock(Stock stock){
//        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO `finapp`.`stock_list` ( `stock_list_id`,  `stock_symbol`) VALUES ( ?, ?);\n");
            preparedStatement = createPreparedStatementforOrder(preparedStatement , stock);

            int i = preparedStatement.executeUpdate();
            if(i == 1) {
                return true;
            }

        } catch (SQLException ex) {
            logger.error(ex.toString());
        }

        return false;
    }

    /**
     *
     * @param stock_list_id
     * @return Boolean
     */
    public Boolean deleteStock(int stock_list_id){
//        Database database = new Database();
        Connection connection  =  database.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM stock_list WHERE stock_list_id=" + stock_list_id);
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
        return false;
    }


}
