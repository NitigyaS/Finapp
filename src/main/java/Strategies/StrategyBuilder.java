/**
 * Name : Nitigya Sharma
 * Strategy Interface
 */

package Strategies;

/**
 * Created by nitigyas on 10/12/17.
 * https://github.com/team172011
 */

import org.ta4j.core.*;
import ta.IndicatortoChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StrategyBuilder{

    /**
     * initialize the strategy with default parameters
     * @param series the time serie to initilize the strategy with
     */
    void initStrategy(TimeSeries series);

    /**
     * Create and get a long or short strategy.Also need to create private methods for Long/Short Strategy.
     * @return a strategy (long) corresponding to the order type, null if order type is not supported
     */
    Strategy buildStrategy();

    /**
     * Runs the strategy on the time series it is initialized with
     * Useful for Historical Analysis
     * @return results as a trading record
     */
    TradingRecord getTradingRecord();
    /**
     * Get the current time series the strategy is initilized with
     * @return current time series the strategy is initilized with
     */
    TimeSeries getTimeSeries();

    /**
     * Get the name of the strategy
     * @return name of the strategy
     */
    String getName();

    /**
     * Get the parameters of the strategy
     * @return a list of parameters of the strategy
     *
     */
    List<String> getParamters();

    /**
     * Display Strategy on Chart
     *      In this Method you define following
     *          What to get the Data.
     *          Create Indicators on that Data.
     *          Which Indicator to plot.
     *          What Type chart to create
     *              Candles
     *              LineGraph
     */
    void displayOnChart();

    /**
     * Returns Strategy with different paramenters . for thw walk forward class.
     * @param series Series on which the strategy would be analyesed
     * @param numberOfStrategy Number Of Strategy to test.
     * @return
     */


}

