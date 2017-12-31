/**
 *  Name : Nitigya Sharma
 *  Strategy : 1
 *  Long Strategy Only
 *  Why Momentum Indicator:
 *  Why Volatility Indicator:
 *  Why RSI
 *  Why Boilenger : It also tells the Trend Direction
 *
 *  TimeFrame : 1 Day (Since I am only testing and can not look all the details and its easy to focus on)
 *  RSI:
 *      Period : 15 (To cover 3 weeks)
 *
 *  Bollingers Band:
 *      Period : 20 (To cover 4 Weeks)
 *      Standard Deviation : 2
 *
 *  Stop Loss
 *  Stop Gain
 *  Momentum Indicator + Volatilty Indicator
 *  RSI to check overBought and OverSold
 *  Boillenger Band
 *
 *  Find Support and Resistance
 *
 *
 *  Find OverBought and OverSold Condition
 *
 *
 *  Find Top and Buttom
 *
 *
 *  ExitRule
 */
package Strategies;

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.DirectionalMovementIndicator;
import eu.verdelhan.ta4j.indicators.RSIIndicator;
import eu.verdelhan.ta4j.indicators.SMAIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.bollinger.BollingerBandsUpperIndicator;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.helpers.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.helpers.MinPriceIndicator;
import eu.verdelhan.ta4j.indicators.statistics.StandardDeviationIndicator;
import eu.verdelhan.ta4j.trading.rules.CrossedDownIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.CrossedUpIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.OverIndicatorRule;
import eu.verdelhan.ta4j.trading.rules.UnderIndicatorRule;
import ta.IndicatortoChart;


import java.util.List;

/**
 * Created by nitigyas on 10/12/17.
 * https://github.com/team172011
 */

public class StrategyTwo implements StrategyBuilder {

    private TimeSeries series;

    private ClosePriceIndicator closePrice;
    private Indicator<Decimal> maxPrice;
    private Indicator<Decimal> minPrice;
    private int timeFrame = 14;
    private Decimal takeProfitValue = Decimal.valueOf(111.78);

    // Indicator Used
    BollingerBandsMiddleIndicator bolm;
    BollingerBandsUpperIndicator bolu;
    BollingerBandsLowerIndicator boll;
    RSIIndicator rsi;



    public StrategyTwo(TimeSeries series){
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
        this.minPrice = new MinPriceIndicator(this.series);
        this.closePrice = new ClosePriceIndicator(this.series);
        this.maxPrice = new MaxPriceIndicator(this.series);
    }

    @Override
    public Strategy buildStrategy(Order.OrderType type) {
        if (type.equals(Order.OrderType.SELL))
            return getLongStrategy();
        return getLongStrategy();
    }

    @Override
    public TradingRecord getTradingRecord(Order.OrderType type) {
        TimeSeriesManager seriesManager = new TimeSeriesManager(series);
        Strategy strategyOne = buildStrategy(type);
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

        // Bollinger Band
        SMAIndicator sma = new SMAIndicator(closePrice, 20);
        StandardDeviationIndicator sdi = new StandardDeviationIndicator(closePrice, 20);
        bolm = new BollingerBandsMiddleIndicator(sma);
        bolu = new BollingerBandsUpperIndicator(bolm,sdi);
        boll = new BollingerBandsLowerIndicator(bolm,sdi);




        //
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

    private Strategy getShortStrategy(){
        return null;
    }

    public void displayOnChart(){
        IndicatortoChart inc = new IndicatortoChart(series);
        inc.addData(closePrice , "Close Price");
        inc.addData(bolu, "Upper Bollinger Band");
        inc.addData(boll, "Low Bollinger Band");
        inc.displayChart("Strategy Two");
    }
}
