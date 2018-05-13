package Analyser;

/*
  The MIT License (MIT)

  Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)

  Permission is hereby granted, free of charge, to any person obtaining a copy of
  this software and associated documentation files (the "Software"), to deal in
  the Software without restriction, including without limitation the rights to
  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  the Software, and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import Strategies.StrategyBuilder;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import Strategies.StrategyBRAD;
import Strategies.StrategyOne;
import ta.CustomTick;

/*import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.CCICorrectionStrategy;
import ta4jexamples.strategies.GlobalExtremaStrategy;
import ta4jexamples.strategies.MovingMomentumStrategy;
import ta4jexamples.strategies.RSI2Strategy;*/

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Walk-forward optimization example.
 * <p></p>
 * @see <a href="http://en.wikipedia.org/wiki/Walk_forward_optimization">
 *     http://en.wikipedia.org/wiki/Walk_forward_optimization</a>
 * @see <a href="http://www.futuresmag.com/2010/04/01/can-your-system-do-the-walk">
 *     http://www.futuresmag.com/2010/04/01/can-your-system-do-the-walk</a>
 */
public class WalkForward {

    /**
     * Builds a list of split indexes from splitDuration.
     * @param series the time series to get split begin indexes of
     * @param splitDuration the duration between 2 splits
     * @return a list of begin indexes after split
     */
    public static List<Integer> getSplitBeginIndexes(TimeSeries series, Duration splitDuration) {
        ArrayList<Integer> beginIndexes = new ArrayList<>();

        int beginIndex = series.getBeginIndex();
        int endIndex = series.getEndIndex();

        // Adding the first begin index
        beginIndexes.add(beginIndex);

        // Building the first interval before next split
        ZonedDateTime beginInterval = series.getFirstBar().getEndTime();
        ZonedDateTime endInterval = beginInterval.plus(splitDuration);

        for (int i = beginIndex; i <= endIndex; i++) {
            // For each bar...
            ZonedDateTime barTime = series.getBar(i).getEndTime();
            if (barTime.isBefore(beginInterval) || !barTime.isBefore(endInterval)) {
                // Bar out of the interval
                if (!endInterval.isAfter(barTime)) {
                    // Bar after the interval
                    // --> Adding a new begin index
                    beginIndexes.add(i);
                }

                // Building the new interval before next split
                beginInterval = endInterval.isBefore(barTime) ? barTime : endInterval;
                endInterval = beginInterval.plus(splitDuration);
            }
        }
        return beginIndexes;
    }

    /**
     * Returns a new time series which is a view of a subset of the current series.
     * <p>
     * The new series has begin and end indexes which correspond to the bounds of the sub-set into the full series.<br>
     * The bar of the series are shared between the original time series and the returned one (i.e. no copy).
     * @param series the time series to get a sub-series of
     * @param beginIndex the begin index (inclusive) of the time series
     * @param duration the duration of the time series
     * @return a constrained {@link TimeSeries time series} which is a sub-set of the current series
     */
    public static TimeSeries subseries(TimeSeries series, int beginIndex, Duration duration) {

        // Calculating the sub-series interval
        ZonedDateTime beginInterval = series.getBar(beginIndex).getEndTime();
        ZonedDateTime endInterval = beginInterval.plus(duration);

        // Checking bars belonging to the sub-series (starting at the provided index)
        int subseriesNbBars = 0;
        int endIndex = series.getEndIndex();
        for (int i = beginIndex; i <= endIndex; i++) {
            // For each bar...
            ZonedDateTime barTime = series.getBar(i).getEndTime();
            if (barTime.isBefore(beginInterval) || !barTime.isBefore(endInterval)) {
                // Bar out of the interval
                break;
            }
            // Bar in the interval
            // --> Incrementing the number of bars in the subseries
            subseriesNbBars++;
        }

        return series.getSubSeries(beginIndex, beginIndex + subseriesNbBars);
    }

    /**
     * Splits the time series into sub-series lasting sliceDuration.<br>
     * The current time series is splitted every splitDuration.<br>
     * The last sub-series may last less than sliceDuration.
     * @param series the time series to split
     * @param splitDuration the duration between 2 splits
     * @param sliceDuration the duration of each sub-series
     * @return a list of sub-series
     */
    public static List<TimeSeries> splitSeries(TimeSeries series, Duration splitDuration, Duration sliceDuration) {
        ArrayList<TimeSeries> subseries = new ArrayList<>();
        if (splitDuration != null && !splitDuration.isZero()
                && sliceDuration != null && !sliceDuration.isZero()) {

            List<Integer> beginIndexes = getSplitBeginIndexes(series, splitDuration);
            for (Integer subseriesBegin : beginIndexes) {
                subseries.add(subseries(series, subseriesBegin, sliceDuration));
            }
        }
        return subseries;
    }


    public static void main(String[] args) {
        // Splitting the series into slices
        Map<String, Double> result = new HashMap<String, Double>();

        List<Bar> bar = new ArrayList<Bar>();
        bar = CustomTick.historic_data("tcs","24month");    // Get Historic Data

        TimeSeries series = new BaseTimeSeries("test_series",bar);
        List<TimeSeries> subseries = splitSeries(series, Duration.ofDays(30), Duration.ofDays(90));

        // Building the map of strategies
        Map<Strategy, String> strategies = StrategyBRAD.buildStrategiesMap(series,10 , 1);

        // The analysis criterion
        AnalysisCriterion profitCriterion = new TotalProfitCriterion();
        System.out.println("Sub-series: " + "\t" + "Name" +"\t"+ "Profit");
        for (TimeSeries slice : subseries) {
            // For each sub-series...
            //System.out.println("Sub-series: " + slice.getSeriesPeriodDescription());
            TimeSeriesManager sliceManager = new TimeSeriesManager(slice);
            for (Map.Entry<Strategy, String> entry : strategies.entrySet()) {
                Strategy strategy = entry.getKey();
                String name = entry.getValue();
                // For each strategy...
                TradingRecord tradingRecord = sliceManager.run(strategy);
                double profit = profitCriterion.calculate(slice, tradingRecord);
                //System.out.println("\tProfit for " + name + ": " + profit);
                System.out.println(slice.getSeriesPeriodDescription() + "\t" + name + "\t" + profit);
                if ( result.containsKey(name) == false ) {
                    result.put(name,Double.valueOf(0));
                }

                result.put(name,result.get(name)+Double.valueOf(profit));

            }
            Strategy bestStrategy = profitCriterion.chooseBest(sliceManager, new ArrayList<Strategy>(strategies.keySet()));
            System.out.println("\t\t--> Best strategy: " + strategies.get(bestStrategy)  +"\n");


        }
        System.out.println("\t\t--> Result: " + result + "\n");
    }

}
