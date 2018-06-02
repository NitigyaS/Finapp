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

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import Strategies.StrategyBRAD;
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

    private static Logger logger = LoggerFactory.getLogger(WalkForward.class);
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


    public static void main(String args[]){

        String companyName = "tcs";
        String dateRange    = "24month";

        List<Bar> bar = CustomTick.historic_data(companyName, dateRange);    // Get Historic Data
        logger.info("Stock : " + companyName + "Date Range : " + dateRange);

        TimeSeries series = new BaseTimeSeries("test_series",bar);
        List<TimeSeries> subseries = splitSeries(series, Duration.ofDays(30), Duration.ofDays(90));

        // Building the map of strategies
        Map<Strategy, String> strategies = StrategyBRAD.buildStrategiesMapForRSI(series,10 , 1 ,1);
        List<BestStrategy> bestStrategyList =  analyse(strategies , subseries);

        System.out.println(new Gson().toJson(bestStrategyList));




    }

    public static List<BestStrategy> analyse( Map<Strategy, String> strategies  , List<TimeSeries> subseries ) {
        // Splitting the series into slices
        // TotalProfit if Strategy is selected.
        List<BestStrategy> bestStrategyList = new ArrayList<>();
        Map<String, Double> totalBestProfit = new HashMap<>();
        // Number of Times the Strategy give Maximum Profit.
        Map<String, Integer> bestStrategyCount = new HashMap<>();
        Map<String, Double> totalMaxProfit = new HashMap<>();

        // The analysis criterion
        AnalysisCriterion profitCriterion = new TotalProfitCriterion();

        for (TimeSeries slice : subseries) {

            Map<String, Double> stratagyProfit = new HashMap<>();

            // For each sub-series...
            TimeSeriesManager sliceManager = new TimeSeriesManager(slice);

            for (Map.Entry<Strategy, String> entry : strategies.entrySet()) {

                Strategy strategy = entry.getKey();

                String name = entry.getValue();

                // For each strategy...
                TradingRecord tradingRecord = sliceManager.run(strategy);

                double profit = profitCriterion.calculate(slice, tradingRecord) - 1;

                //logger.info(slice.getSeriesPeriodDescription() + "( name=" + name + ", profit=" + profit + ")");

                stratagyProfit.put(name,profit);

                if ( totalMaxProfit.containsKey(name) == false ) {
                    totalMaxProfit.put(name,Double.valueOf(0));
                }
                totalMaxProfit.put(name,totalMaxProfit.get(name)+profit);


            }

            Strategy bestStrategy = profitCriterion.chooseBest(sliceManager, new ArrayList<>(strategies.keySet()));

            String bestStrategyName=strategies.get(bestStrategy);

            //logger.info("Best strategy: " + bestStrategyName +"\n");

            if ( bestStrategyCount.containsKey(bestStrategyName) == false ) {
                bestStrategyCount.put(bestStrategyName,Integer.valueOf(0));
            }

            bestStrategyCount.put(bestStrategyName,bestStrategyCount.get(bestStrategyName)+1);
            if ( totalBestProfit.containsKey(bestStrategyName) == false ) {
                totalBestProfit.put(bestStrategyName,Double.valueOf(0));
            }
            totalBestProfit.put(bestStrategyName,stratagyProfit.get(bestStrategyName)+totalBestProfit.get(bestStrategyName));

        }

        // Create list of BestStrategy Objects.
        for (String value:totalBestProfit.keySet())
        {
            logger.info(value);
            logger.info("bestStrategyCount "+ bestStrategyCount.get(value));
            logger.info("totalMaxProfit "+totalMaxProfit.get(value) );
            logger.info("totalBestProfit "+totalBestProfit.get(value) + "\n");
            BestStrategy bestStrategy = new BestStrategy();
            bestStrategy.setStrategyName(value);
            bestStrategy.setStrategyBestCount(bestStrategyCount.get(value));

            bestStrategy.setTotalProfitYield(totalMaxProfit.get(value));
            bestStrategy.setBestProfitYield(totalBestProfit.get(value));
            bestStrategyList.add(bestStrategy);
        }
        return bestStrategyList;

    }

}

class BestStrategy{
    String strategyName;
    int strategyBestCount = 0;
    double totalProfitYield = 0;
    double bestProfitYield = 0;

    public String getStrategyName()
    {
        return strategyName;
    }

    public void setStrategyName(String strategyName)
    {
        this.strategyName = strategyName;
    }

    public int getStrategyBestCount()
    {
        return strategyBestCount;
    }

    public void setStrategyBestCount(int strategyBestCount)
    {
        this.strategyBestCount = strategyBestCount;
    }

    public double getTotalProfitYield()
    {
        return totalProfitYield;
    }

    public void setTotalProfitYield(double totalProfitYield)
    {
        this.totalProfitYield = totalProfitYield;
    }


    public double getBestProfitYield()
    {
        return bestProfitYield;
    }

    public void setBestProfitYield(double bestProfitYield)
    {
        this.bestProfitYield = bestProfitYield;
    }
}



