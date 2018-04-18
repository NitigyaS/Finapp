/**
 *  Name : Nitigya Sharma
 *  Strategy : 1
 *  Long Strategy Only
 *  EntryRule:
 *
 *
 *  ExitRule
 */
package Strategies;

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.RSIIndicator;
import eu.verdelhan.ta4j.indicators.SMAIndicator;
import eu.verdelhan.ta4j.indicators.SmoothedRSIIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandWidthIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsUpperIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.helpers.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.helpers.MinPriceIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;
import ta.IndicatortoChart;


import javax.management.relation.Relation;
import java.util.List;

/**
 * Created by nitigyas on 10/12/17.
     * https://github.com/team172011
 */

public class StrategyOne implements StrategyBuilder {

    private TimeSeries series;

    private ClosePriceIndicator closePrice;
    private Indicator<Decimal> maxPrice;
    private Indicator<Decimal> minPrice;
    private int timeFrame = 14;
    private Decimal takeProfitValue = Decimal.valueOf(111.78);


    // indicators
    RSIIndicator rsi;
    SMAIndicator sma;
    SmoothedRSIIndicator smrsi;
    BollingerBandsMiddleIndicator bolm;
    BollingerBandsUpperIndicator bolu;
    BollingerBandsLowerIndicator boll;
    BollingerBandWidthIndicator bolbw;



    public StrategyOne(TimeSeries series){
        initStrategy(series);
    }

    public void setTimeFrame(int timeFrame){
        this.timeFrame = timeFrame;
    }

    public void setTakeProfitValue(double takeProfitValue){
        this.takeProfitValue = Decimal.valueOf(takeProfitValue);
    }

    @Override
    public void initStrategy(TimeSeries series) {

        this.series = series;
        this.closePrice = new ClosePriceIndicator(this.series);

    }

    @Override
    public Strategy buildStrategy() {
        return getLongStrategy();
    }

    @Override
    public TradingRecord getTradingRecord() {
        TimeSeriesManager seriesManager = new TimeSeriesManager(series);
        Strategy strategyOne = buildStrategy();
        TradingRecord tradingRecord = seriesManager.run(strategyOne);
        return  tradingRecord;

    }

    @Override
    public TimeSeries getTimeSeries() {
        return this.series;
    }

    @Override
    public String getName() {
        return "StrategyOne";
    }

    @Override
    public List<String> getParamters() {
        return null;
    }

    private Strategy getLongStrategy(){
        // Initialise Indicators
        rsi = new RSIIndicator(closePrice,timeFrame);
        smrsi = new SmoothedRSIIndicator(closePrice,timeFrame);
        sma = new SMAIndicator(closePrice, timeFrame);

        // Entry Rules
        Rule entrySignal1 = new OverIndicatorRule(sma,closePrice);
        Rule entrySignal2 = new UnderIndicatorRule(rsi, Decimal.valueOf(45));
        // Exit Rules
        Rule exitSignal1 = new UnderIndicatorRule(sma, closePrice);
        Rule exitSignal2 = new OverIndicatorRule(rsi, Decimal.valueOf(55));
        //Create Strategy
        Strategy strategy = new BaseStrategy(entrySignal2,exitSignal2 );
        return strategy;
    }

    public void displayOnChart(){
        IndicatortoChart inc = new IndicatortoChart(series);

        inc.addData(rsi, "Relative Strength Index");    // Add Indicator to plot
        inc.addData(smrsi, "Smoothed Relative Strength Index");

        inc.generateChart("Strategy Two");
    }
}
