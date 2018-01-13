/**
 * Name : Nitigya Sharma
 * Strategy Analyser
 * Useful to anaylyse Strategy on Dummmy Data or Historic Data
 */
package ta;

import CustomIndicator.IIIIndicator;
import Strategies.StrategyAnalyser;
import Strategies.StrategyBuilder;
import Strategies.StrategyOne;
import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.RSIIndicator;
import eu.verdelhan.ta4j.indicators.SMAIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsUpperIndicator;
import eu.verdelhan.ta4j.indicators.candles.BearishEngulfingIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.helpers.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.helpers.MinPriceIndicator;
import eu.verdelhan.ta4j.indicators.statistics.SimpleLinearRegressionIndicator;
import eu.verdelhan.ta4j.indicators.statistics.StandardDeviationIndicator;
import eu.verdelhan.ta4j.trading.rules.*;

import java.sql.Time;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nitigyas on 21/9/17.
 * https://github.com/team172011
 */

public class StockAnalyser {
    List<Tick> ticks = new ArrayList<Tick>();
    TradingRecord tradingRecord = new BaseTradingRecord();;

    public StockAnalyser(){
        Tick newTick  = CustomTick.generateRandomTick();
        ticks.add(newTick);
        //tradingRecord = new BaseTradingRecord();

    }


    private Strategy buildStrategy(TimeSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        int tickLength = series.getTickCount();
        int timeFrame = 5;
        MinPriceIndicator minPrice = new MinPriceIndicator(series);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        MaxPriceIndicator maxPrice = new MaxPriceIndicator(series);
        Decimal takeProfitValue = Decimal.valueOf(111.78);


        // Indicator
        SMAIndicator sma = new SMAIndicator(closePrice, timeFrame);
        SimpleLinearRegressionIndicator longlinear = new SimpleLinearRegressionIndicator(closePrice, tickLength);
        SimpleLinearRegressionIndicator shortlinear = new SimpleLinearRegressionIndicator(closePrice, timeFrame);
        RSIIndicator rsi = new RSIIndicator(closePrice,timeFrame);

        // Entry Rules
        Rule entrySignal1 = new CrossedDownIndicatorRule(rsi,Decimal.valueOf(50));


        // Exit Rules
        Rule exitSignal2 = new CrossedUpIndicatorRule(rsi, Decimal.valueOf(52));

        Strategy strategy = new BaseStrategy(entrySignal1,exitSignal2   );
        return strategy;

    }


    public void test() throws InterruptedException {

        Tick newTick;
        // Dummy code to fill the TimeSeries
        for (int i=1;i<20;i++){
            newTick = CustomTick.generateRandomTick();
            System.out.println(newTick.toString());
            ticks.add(newTick);
        }

        //Create TimeSeries
        TimeSeries ts = new BaseTimeSeries("test_series",ticks);
        // Create TradingRecord

        //Execute the Strategy
        Strategy strategy = buildStrategy(ts);

        ts.setMaximumTickCount(20);
        for (int i=0;i<50;i++){
            Thread.sleep(30);
            // Get the new tick value
            newTick = CustomTick.generateRandomTick();
            // Assign the tick value to TimeSeries
            ts.addTick(newTick);
            System.out.println("Time Series Length : " + ts.getTickCount());
            // Get the index of last element in TimeSeries
            int endIndex = ts.getEndIndex();
            //If Buy Signal recieved from strtategy
            if (strategy.shouldEnter(endIndex)) {
                // Our strategy should enter
                System.out.println("Strategy should ENTER on " + endIndex);
                boolean entered = tradingRecord.enter(endIndex, newTick.getClosePrice(), Decimal.TEN);
                if (entered) {
                    Order entry = tradingRecord.getLastEntry();
                    System.out.println("Entered on " + entry.getIndex()
                            + " (price=" + entry.getPrice().toDouble()
                            + ", amount=" + entry.getAmount().toDouble() + ")");
                }
             //If Sell Signal received from strategy
            } else if (strategy.shouldExit(endIndex)) {
                // Our st rategy should exit
                System.out.println("Strategy should EXIT on " + endIndex);
                //exit only on even
                boolean exited = false;
                if (i%2 == 0) {
                     exited = tradingRecord.exit(endIndex, newTick.getClosePrice(), Decimal.TEN);
                }

                System.out.println("itteration = " + i + "Exited = " + exited);
                if (exited) {
                    Order exit = tradingRecord.getLastExit();
                    System.out.println("Exited on " + exit.getIndex()
                            + " (price=" + exit.getPrice().toDouble()
                            + ", amount=" + exit.getAmount().toDouble() + ")");
                }
            }


        }

    }


    public void backtest() throws InterruptedException {
        // Get Historic Data
        ticks = CustomTick.historic_data("TCS","3month");

        //Create TimeSeries
        TimeSeries ts = new BaseTimeSeries("test_series",ticks);
        System.out.println(ts.getTickCount());
        //Get Strategy
        StrategyOne strategyOne = new StrategyOne(ts);
        StrategyAnalyser strategyAnalyser = new StrategyAnalyser();
        strategyAnalyser.printAllResults(strategyOne);


    }

    public void getChart(){
        // Get Historic Data
        ticks = CustomTick.historic_data("tcs","3month");

        //Create TimeSeries
        TimeSeries ts = new BaseTimeSeries("test_series",ticks);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(ts);

        RSIIndicator rsi = new RSIIndicator(closePrice,14);
        BearishEngulfingIndicator bei = new BearishEngulfingIndicator(ts);

        // Bollinger Band
        SMAIndicator sma = new SMAIndicator(closePrice, 20);
        StandardDeviationIndicator sdi = new StandardDeviationIndicator(closePrice, 20);
        BollingerBandsMiddleIndicator bolm = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsUpperIndicator bolu = new BollingerBandsUpperIndicator(bolm,sdi);
        BollingerBandsLowerIndicator boll = new BollingerBandsLowerIndicator(bolm,sdi);
        System.out.println("___________________");

        IIIIndicator iiiIndicator = new IIIIndicator(ts);

        System.out.println(iiiIndicator.getValue(3));

        IndicatortoChart inc = new IndicatortoChart(ts);
        inc.addData(closePrice,"Stock");
        //inc.addData(boll,"Low Bollinger Band");
        //inc.addData(bolu,"Upper Bollinger Band");
        //inc.addData(rsi,"RSI");
        inc.addData(iiiIndicator, "Intraday Intensity");

        //inc.generateChart("StrategyTwo");

        inc.generateCandles("TCS 3Months",false);



    }
}
