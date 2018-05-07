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

import java.util.List;

public abstract class StrategyBuilder{

    TimeSeries timeSeries;

    /**
     * initialize the strategy with default parameters
     * @param series the time serie to initilize the strategy with
     */
    abstract void initStrategy(TimeSeries series);

    /**
     * Create and get a long or short strategy.Also need to create private methods for Long/Short Strategy.
     * @return a strategy (long) corresponding to the order type, null if order type is not supported
     */
    abstract  Strategy buildStrategy();

    /**
     * Runs the strategy on the time series it is initialized with
     * Useful for Historical Analysis
     * @return results as a trading record
     */
    public TradingRecord getTradingRecord(){
        TimeSeriesManager seriesManager = new TimeSeriesManager(timeSeries);
        return seriesManager.run(buildStrategy());
    }

    /**
     * Get the current time series the strategy is initilized with
     * @return current time series the strategy is initilized with
     */
    public TimeSeries getTimeSeries()
    {
        return this.timeSeries;
    }

    /**
     * Get the name of the strategy
     * @return name of the strategy
     */
    public abstract String getName();

    /**
     * Get the parameters of the strategy
     * @return a list of parameters of the strategy
     *
     */
    public abstract List<String> getParamters();

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
    public void displayOnChart(){
        IndicatortoChart inc = new IndicatortoChart(timeSeries);
        inc.generateCandles(getName(),false);
    }

}

