/**
 * Name : Nitigya Sharma
 * Strategy Analyser
 * Useful to anaylyse Strategy on Dummmy Data or Historic Data
 */
package Analyser;

import Strategies.StrategyBRAD;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import ta.CustomTick;

import java.util.ArrayList;
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
 *          It Passes the result to BackTestAnalyser which dumps the result on scree.
 *
 */

public class StockAnalyser {

    public static void main(String args[])
    {
        List<Bar> bar = new ArrayList<Bar>();

        bar = CustomTick.historic_data("biocon","12month");    // Get Historic Data

        TimeSeries ts = new BaseTimeSeries("test_series",bar);

        StrategyBRAD strategyBRAD = new StrategyBRAD(ts,"Bollinger and RSI");
        strategyBRAD.setStopLossPercentage(8.0);
        strategyBRAD.setRsiSlope(1,0.1);//Initialize Strategy

        BackTestAnalyser strategyAnalyser = new BackTestAnalyser();                 //Initialize Strategy Analyser

        strategyAnalyser.printAllResults(strategyBRAD);                              // Pass the Strategy to Analyse

    }

}
