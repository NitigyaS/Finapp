/**
 * Name : Nitigya Sharma
 * Strategy Analyser
 * Useful to anaylyse Strategy on Dummmy Data or Historic Data
 */
package Analyser;

import Analyser.BackTestAnalyser;
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

    List<Bar> bar = new ArrayList<Bar>();

    public void backTest() throws InterruptedException {

        bar = CustomTick.historic_data("BIOCON","24month");    // Get Historic Data

        TimeSeries ts = new BaseTimeSeries("test_series",bar);
        //ts.setMaximumBarCount(100);//Create TimeSeries

//        StrategyOne strategy = new StrategyOne(ts);                              //Initialize Strategy
        StrategyBRAD strategy = new StrategyBRAD(ts,"Bollinger and RSI");                              //Initialize Strategy

        BackTestAnalyser strategyAnalyser = new BackTestAnalyser();                 //Initialize Strategy Analyser

        strategyAnalyser.printAllResults(strategy);                              // Pass the Strategy to Analyse

    }

}
