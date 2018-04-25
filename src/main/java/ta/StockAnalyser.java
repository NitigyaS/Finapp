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
import org.ta4j.*;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.candles.BearishEngulfingIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MaxPriceIndicator;
import org.ta4j.core.indicators.helpers.MinPriceIndicator;
import org.ta4j.core.indicators.statistics.SimpleLinearRegressionIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.trading.rules.*;

import java.sql.Time;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nitigyas on 21/9/17.
 * https://github.com/team172011
 *
 * StockAnalyser has 1 methods
 *      backTest()
 *      In this Method you define following
 *          What to get the Data
 *          What Strategy to Use
 *      It does :
 *          It Passes the result to StrategyAnalyser which dumps the result on scree.
 *
 */

public class StockAnalyser {

    List<Bar> bar = new ArrayList<Bar>();

    public void backTest() throws InterruptedException {

        bar = CustomTick.historic_data("TCS","3month");    // Get Historic Data

        TimeSeries ts = new BaseTimeSeries("test_series",bar);              //Create TimeSeries

        StrategyOne strategyOne = new StrategyOne(ts);                              //Initialize Strategy

        StrategyAnalyser strategyAnalyser = new StrategyAnalyser();                 //Initialize Strategy Analyser

        strategyAnalyser.printAllResults(strategyOne);                              // Pass the Strategy to Analyse

    }

}
