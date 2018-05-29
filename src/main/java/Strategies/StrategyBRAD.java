package Strategies;

import org.ta4j.core.*;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandWidthIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MinusDMIndicator;
import org.ta4j.core.indicators.helpers.PlusDMIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.trading.rules.InSlopeRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import ta.IndicatortoChart;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyBRAD implements StrategyBuilder {

    private TimeSeries timeSeries;
    private ClosePriceIndicator closePrice;
    private BollingerBandWidthIndicator bandWidth;
    private RSIIndicator rsi;
    private MinusDMIndicator minusDMI;
    private PlusDMIndicator plusDMI;

    private String name = "StrategyBRAD";

    SMAIndicator smaIndicator;
    BollingerBandsMiddleIndicator bbm;
    BollingerBandsUpperIndicator bbu;
    BollingerBandsLowerIndicator bbl;

    private int timeFrame20 = 20;
    private int timeFrame14 = 14;
    private int volatilityThreshold = 5;

    private int rsiSlopeLength = 5;
    private double minRSISLope = 0.1 ; // 6 Degree


    public StrategyBRAD(TimeSeries series , String name){
        initStrategy(series);
        this.name = name;
    }


    @Override
    public void initStrategy(TimeSeries series) {
        this.timeSeries = series;
        this.plusDMI = new PlusDMIndicator(series);
        this.minusDMI = new MinusDMIndicator(series);
        this.closePrice = new ClosePriceIndicator(series);
        this.bandWidth = calculateBollingerBandwidth(series);
        this.rsi = new RSIIndicator(this.closePrice ,this.timeFrame14);

    }

    @Override
    public Strategy buildStrategy() {
        return getLongStrategy();
    }

    @Override
    public TradingRecord getTradingRecord() {
        TimeSeriesManager seriesManager = new TimeSeriesManager(this.timeSeries);
        return seriesManager.run(buildStrategy());
    }

    @Override
    public TimeSeries getTimeSeries() {
        return this.timeSeries;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getParamters() {
        return null;
    }


    /**
     * @param volatilityThreshold Threshold for low volatility and high volatility
     */
    public void setVolatilityThreshold(int volatilityThreshold){
        this.volatilityThreshold = volatilityThreshold;
    }


    /**
     * @param rsiSlopeLength previous nth value to take slope of.
     * @param minSlope minSlope Value. Tan(0) = 0 <-> Tan(90) = Infinity
     */
    public void setRsiSlope(int rsiSlopeLength , double minSlope){
        this.rsiSlopeLength = rsiSlopeLength;
        this.minRSISLope = minSlope;

    }

    @Override
    public void displayOnChart(){
        IndicatortoChart inc = new IndicatortoChart(this.timeSeries);

        inc.addData(this.bbm, "BollingerMiddle");
        inc.addData(this.bbu, "BollingerUpper");
        inc.addData(this.bbl, "BollingerLower");

        inc.generateCandles(getName(),false);
    }

    private BollingerBandWidthIndicator calculateBollingerBandwidth(TimeSeries series){

        this.smaIndicator = new SMAIndicator(this.closePrice,this.timeFrame20);

        StandardDeviationIndicator standardDeviationIndicator = new StandardDeviationIndicator(this.closePrice,this.timeFrame20);

        this.bbm = new BollingerBandsMiddleIndicator(this.smaIndicator);

        this.bbu = new BollingerBandsUpperIndicator(this.bbm,standardDeviationIndicator);

        this.bbl = new BollingerBandsLowerIndicator(this.bbm,standardDeviationIndicator);

        return new BollingerBandWidthIndicator(this.bbu , this.bbm , this.bbl);
    }


    private Strategy getLongStrategy(){

        Rule nonVolatilityRule = new OverIndicatorRule(this.bandWidth , Decimal.valueOf(this.volatilityThreshold)) ;

        Rule rsiSlopePositive = new InSlopeRule(this.rsi ,this.rsiSlopeLength, Decimal.valueOf(this.minRSISLope), Decimal.valueOf(50)); //Tan(n) 0 <-> Infinity

        Rule pDmiGTmDMI = new OverIndicatorRule(this.plusDMI ,this.minusDMI);

        Rule bandwidthSlopeNegative = new InSlopeRule(this.bandWidth , 5, Decimal.valueOf(-50), Decimal.valueOf(-1));

        Rule entrySignal = nonVolatilityRule.and(rsiSlopePositive).and(pDmiGTmDMI);

        Rule exitSignal = bandwidthSlopeNegative;

        Strategy strategyBRAD = new BaseStrategy(entrySignal,exitSignal);

        return strategyBRAD;
    }

    /**
     *
     * @param series
     * @param numberOfStrategy
     * @param rsiSlopeVariation
     * @return
     */
    public static Map<Strategy, String> buildStrategiesMap(TimeSeries series , int numberOfStrategy ,  int rsiSlopeVariation ) {
        // Create a strategyList
        ArrayList<StrategyBRAD> strategyList = new ArrayList<StrategyBRAD>();
        // Create k Different Strategy
        for (int i =1 ; i<numberOfStrategy ;i++){
            StrategyBRAD strategyBRAD = new StrategyBRAD(series , "Strategy-"+i*rsiSlopeVariation); //Initialize strategy with series.
            strategyBRAD.setRsiSlope(i*rsiSlopeVariation,0.1);                      //Set Parameter
            strategyList.add(strategyBRAD);                                                // Add to Strategy List
        }
        HashMap<Strategy, String> strategies = new HashMap<>();         // Create HashMap for WalkForward Class
        for (StrategyBRAD sb : strategyList) {
            strategies.put(sb.buildStrategy(), sb.getName());           // Build Strategy
        }
        return strategies;                                              // Return Strategy Map
    }


}
