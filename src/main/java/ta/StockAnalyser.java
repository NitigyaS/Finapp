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
 *
 * StockAnalyser has 2 methods
 *      backTest()
 *      In this Method you define following
 *          What to get the Data
 *          What Strategy to Use
 *      It does :
 *          It Passes the result to StrategyAnalyser which dumps the result on scree.
 *
 */

public class StockAnalyser {

    List<Tick> ticks = new ArrayList<Tick>();

    public void backTest() throws InterruptedException {

        ticks = CustomTick.historic_data("TCS","3month");    // Get Historic Data

        TimeSeries ts = new BaseTimeSeries("test_series",ticks);              //Create TimeSeries

        StrategyOne strategyOne = new StrategyOne(ts);                              //Initialize Strategy

        StrategyAnalyser strategyAnalyser = new StrategyAnalyser();                 //Initialize Strategy Analyser

        strategyAnalyser.printAllResults(strategyOne);                              // Pass the Strategy to Analyse

    }

}
