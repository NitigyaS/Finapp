package application;

import Strategies.StrategyBRAD;
import Strategies.StrategyBuilder;
import Strategies.StrategyOne;
import org.ta4j.core.*;
import ta.CustomTick;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */
public class Slave implements Callable<Void> {

    int stock_id;
    String stock_name;
    int maximum_bar_count = 50;
    public static boolean proposalList[] = new boolean[Master.symbolList.length];   //Should Trade or not.

    /**
     *
     * @param stock_id "Passs the Stock Number to be monitored."
     */
    public Slave(int stock_id) {

        this.stock_id = stock_id;
        this.stock_name = Master.symbolList[stock_id];

    }

    @Override
    public Void call() throws Exception {

//Create an Empty Time Series.
        TimeSeries series = new BaseTimeSeries(Master.symbolList[stock_id]);
        series.setMaximumBarCount(maximum_bar_count);

        int i = 1;
        for ( ; series.getBarCount() < maximum_bar_count; i++){
            series.addBar(CustomTick.getBar(stock_name,i));
        }

        TradingRecord tradingRecord = new BaseTradingRecord();
        Strategy strategy = new StrategyBRAD(series,Integer.valueOf(stock_id).toString()).buildStrategy();

        for ( ; i < 200; i++){

            // Add next bar in the series.
            Bar newBar = CustomTick.getBar(stock_name,i);
            series.addBar(newBar);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // Check if strategy should enter this point
            int endIndex = series.getEndIndex();
            if (strategy.shouldEnter(endIndex)) {
                // Our strategy should enter
                System.out.println("Strategy should ENTER on " + endIndex);
                boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(), Decimal.TEN);
                if (entered) {
                    Order entry = tradingRecord.getLastEntry();
                    System.out.println("Entered on " + entry.getIndex()
                            + " (price=" + entry.getPrice().doubleValue()
                            + ", amount=" + entry.getAmount().doubleValue() + ")");
                }
            } else if (strategy.shouldExit(endIndex)) {
                // Our strategy should exit
                System.out.println("Strategy should EXIT on " + endIndex);
                boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(), Decimal.TEN);
                if (exited) {
                    Order exit = tradingRecord.getLastExit();
                    System.out.println("Exited on " + exit.getIndex()
                            + " (price=" + exit.getPrice().doubleValue()
                            + ", amount=" + exit.getAmount().doubleValue() + ")");
                }
            }

            // remove stock from the list if false.

        }
        return null;

    }


}
