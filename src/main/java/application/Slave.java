package application;

import Strategies.StrategyBRAD;
import dao.TradingRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;
import ta.CustomTick;

import java.util.concurrent.Callable;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */
public class Slave implements Callable<Void> {

    private static Logger logger = LoggerFactory.getLogger(Slave.class);
    int stock_id;
    String stock_name;
    int maximum_bar_count = 50;

    /**
     *
     * @param stock_id "Passs the Stock Number to be monitored."
     */
    public Slave(int stock_id) {


        this.stock_id = stock_id;
        this.stock_name = Master.symbolList[stock_id];

    }

    @Override
    public Void call()
    {

        //Create an Empty Time Series.
        TimeSeries series = new BaseTimeSeries(Master.symbolList[stock_id]);

        series.setMaximumBarCount(maximum_bar_count);

        int i = 1;
        for ( ; series.getBarCount() < maximum_bar_count; i++){
            series.addBar(CustomTick.getBar(stock_name,i));
        }


        TradingRecordDao tradingRecordDao = new TradingRecordDao();

        StrategyBRAD strategyBRAD = new StrategyBRAD(series,Integer.valueOf(stock_id).toString());

        Strategy strategy = strategyBRAD.buildStrategy();

        for ( ; i < 500; i++){

            // Add next bar in the series.
            Bar newBar = CustomTick.getBar(stock_name,i);
            series.addBar(newBar);


            int endIndex = series.getEndIndex();

            // Check if strategy should enter this point
            if (strategy.shouldEnter(endIndex)) {

                logger.debug("Strategy.shouldEnter True at " + endIndex);

                data.Order entryOrder = new data.Order(stock_name , newBar.getClosePrice().doubleValue() ,10);

                entryOrder.setTransaction_type("B");

                int entered = tradingRecordDao.enter( entryOrder , newBar.getClosePrice().doubleValue()*0.95 );

                if (entered != 0) {
                    logger.info("Entered on " + entryOrder.getSymbol()
                            + " (price=" + entryOrder.getPrice().doubleValue()
                            + ", amount=" + entryOrder.getQuantity().doubleValue() + ")");
                }


            } else if (strategy.shouldExit(endIndex)) {

                logger.debug("Strategy.shouldExit True at " + endIndex);

                data.Order exitOrder =new data.Order(stock_name , newBar.getClosePrice().doubleValue() ,10);

                exitOrder.setTransaction_type("S");

                boolean exited = tradingRecordDao.exit(exitOrder);

                if (exited) {

                    logger.info("Exited on " + exitOrder.getSymbol()
                            + " (price=" + exitOrder.getPrice().doubleValue()
                            + ", amount=" + exitOrder.getQuantity().doubleValue() + ")");

                }
            }

        }
        return null;

    }


}
