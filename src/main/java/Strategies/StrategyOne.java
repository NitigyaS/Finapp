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

import org.ta4j.core.*;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandWidthIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MaxPriceIndicator;
import org.ta4j.core.indicators.helpers.MinPriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;
import ta.IndicatortoChart;


import javax.management.relation.Relation;
import java.util.List;

/**
 * Created by nitigyas on 10/12/17.
     * https://github.com/team172011
 */

public class StrategyOne extends StrategyBuilder {

    private TimeSeries series;

    private ClosePriceIndicator closePrice;
    private Indicator<Decimal> maxPrice;
    private Indicator<Decimal> minPrice;
    private int timeFrame = 14;
    private Decimal takeProfitValue = Decimal.valueOf(111.78);


    // indicators
    RSIIndicator rsi;
    SMAIndicator sma;
    BollingerBandsMiddleIndicator bolm;
    BollingerBandsUpperIndicator bolu;
    BollingerBandsLowerIndicator boll;
    BollingerBandWidthIndicator bolbw;
    private String name;


    public StrategyOne(TimeSeries series , String name){
        this.name = name;
        initStrategy(series);
    }

    public void setTimeFrame(int timeFrame){
        this.timeFrame = timeFrame;
    }

    public void setTakeProfitValue(double takeProfitValue){
        this.takeProfitValue = Decimal.valueOf(takeProfitValue);
    }


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
        return name;
    }

    @Override
    public List<String> getParamters() {
        return null;
    }

    private Strategy getLongStrategy(){
        // Initialise Indicators
        rsi = new RSIIndicator(closePrice,timeFrame);
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
        //inc.addData(smrsi, "Smoothed Relative Strength Index");

        inc.generateChart("Strategy Two");
    }
}
