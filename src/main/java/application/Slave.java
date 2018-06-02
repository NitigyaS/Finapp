package application;

import Strategies.StrategyBRAD;
import dao.TradingRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;
import ta.CustomTick;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 * Slave Method Implements Runnable because it do not give any information currently
 * In case any value needs to be returned use Callable and Futures
 * refer http://www.vogella.com/tutorials/JavaConcurrency/article.html
 */
public class Slave implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(Slave.class);
    private int stock_id;
    private String stock_name;
    private static final int maximum_bar_count = 50;

    /**
     * Method initialize the slave with a stock Symbol
     * @param stock_id "Pass the Stock Number to be monitored."
     */

    public Slave(int stock_id) {
        this.stock_id = stock_id;
        this.stock_name = Master.symbolList[stock_id];
    }

    /**
     * It is the run method of Runnable class
     */
    @Override
    public void run()
    {

        //Create an Empty Time Series.
        TimeSeries series = new BaseTimeSeries(Master.symbolList[stock_id]);
        series.setMaximumBarCount(maximum_bar_count);

        // Fill the time series with previous Bars.
        int i = 1;
        for ( ; series.getBarCount() < maximum_bar_count; i++){
            series.addBar(CustomTick.getBar(stock_name,i));
        }

        // Trading Record Dao
        TradingRecordDao tradingRecordDao = new TradingRecordDao();

        // Get the StrategyBuilder
        StrategyBRAD strategyBRAD = new StrategyBRAD(series,Integer.valueOf(stock_id).toString());

        // Get the Strategy.
        Strategy strategy = strategyBRAD.buildStrategy();

        for ( ; i < 500; i++){

            // Add next bar in the series.
            Bar newBar = CustomTick.getBar(stock_name,i);
            series.addBar(newBar);


            int endIndex = series.getEndIndex();

            // Check if strategy should enter this point
            if (strategy.shouldEnter(endIndex)) {

                try
                {

                    logger.debug("Strategy.shouldEnter True at " + endIndex);

                    data.Order entryOrder = new data.Order(stock_name, newBar.getClosePrice().doubleValue(), 10);

                    entryOrder.setTransaction_type("B");

                    int entered = tradingRecordDao.enter(entryOrder, newBar.getClosePrice().doubleValue() * 0.95);

                    if (entered != 0)
                    {
                        logger.info("Entered on " + entryOrder.getSymbol()
                                + " (price=" + entryOrder.getPrice()
                                + ", amount=" + entryOrder.getQuantity().doubleValue() + ")");
                    }
                }catch (NullPointerException ex){
                    logger.error(ex.toString());
                }


            } else if (strategy.shouldExit(endIndex)) {
                try
                {

                    logger.debug("Strategy.shouldExit True at " + endIndex);

                    data.Order exitOrder = new data.Order(stock_name, newBar.getClosePrice().doubleValue(), 10);

                    exitOrder.setTransaction_type("S");

                    boolean exited = tradingRecordDao.exit(exitOrder);

                    if (exited)
                    {

                        logger.info("Exited on " + exitOrder.getSymbol()
                                + " (price=" + exitOrder.getPrice()
                                + ", amount=" + exitOrder.getQuantity().doubleValue() + ")");

                    }
                }catch (Exception ex){
                    logger.error(ex.toString());
                }
            }

        }

    }


}
