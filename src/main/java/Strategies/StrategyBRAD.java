package Strategies;

import custom.Helper;
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
import org.ta4j.core.trading.rules.*;
import ta.IndicatortoChart;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyBRAD implements StrategyBuilder
{

    private TimeSeries timeSeries;
    private ClosePriceIndicator closePrice;
    private BollingerBandWidthIndicator bandWidth;
    private RSIIndicator rsi;
    private MinusDMIndicator minusDMI;
    private PlusDMIndicator plusDMI;
    private String name;
    private BollingerBandsMiddleIndicator bbm;
    private BollingerBandsUpperIndicator bbu;
    private BollingerBandsLowerIndicator bbl;

    private static final int timeFrame20 = 20;
    private static final int timeFrame14 = 14;
    private int volatilityThreshold = 5;
    private double stopLossPercentage = 3;
    private int rsiSlopeLength = 5;
    private double minRSISLope = Helper.degreeToSlope(6); // 6 Degree
    private double maxRSISlope = Helper.degreeToSlope(89); // 6 Degree


    public StrategyBRAD(TimeSeries series, String name)
    {
        initStrategy(series);
        this.name = name;
    }


    @Override
    public void initStrategy(TimeSeries series)
    {
        this.timeSeries = series;
        this.plusDMI = new PlusDMIndicator(series);
        this.minusDMI = new MinusDMIndicator(series);
        this.closePrice = new ClosePriceIndicator(series);
        this.bandWidth = calculateBollingerBandwidth();
        this.rsi = new RSIIndicator(this.closePrice, timeFrame14);

    }

    @Override
    public Strategy buildStrategy()
    {
        return getLongStrategy();
    }

    @Override
    public TradingRecord getTradingRecord()
    {
        TimeSeriesManager seriesManager = new TimeSeriesManager(this.timeSeries);
        return seriesManager.run(buildStrategy());
    }

    @Override
    public TimeSeries getTimeSeries()
    {
        return this.timeSeries;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public List<String> getParamters()
    {
        return null;
    }


    /**
     * @param volatilityThreshold Threshold for low volatility and high volatility
     */
    public void setVolatilityThreshold(int volatilityThreshold)
    {

        this.volatilityThreshold = volatilityThreshold;
    }


    /**
     * @param rsiSlopeLength previous nth value to take slope of.
     * @param minSlopeDegree       minSlope Value. Tan(0) = 0 <-> Tan(90) = Infinity
     */
    public void setRsiSlope(int rsiSlopeLength, double minSlopeDegree)
    {
        this.rsiSlopeLength = rsiSlopeLength;
        this.minRSISLope = Helper.degreeToSlope(minSlopeDegree);

    }

    /**
     *
     * @param rsiSlopeLength
     * @param minSlopeDegree
     * @param maxSlopeDegree
     */
    public void setRsiSlope(int rsiSlopeLength, double minSlopeDegree , double maxSlopeDegree)
    {
        this.rsiSlopeLength = rsiSlopeLength;
        this.minRSISLope = Helper.degreeToSlope(minSlopeDegree);
        this.maxRSISlope = Helper.degreeToSlope(maxSlopeDegree);

    }

    public void setStopLossPercentage(Double stopLossPercentage){
        this.stopLossPercentage = stopLossPercentage;
    }

    @Override
    public void displayOnChart()
    {
        IndicatortoChart inc = new IndicatortoChart(this.timeSeries);

        inc.addData(this.bbm, "BollingerMiddle");
        inc.addData(this.bbu, "BollingerUpper");
        inc.addData(this.bbl, "BollingerLower");

        inc.generateCandles(getName(), false);
    }

    private BollingerBandWidthIndicator calculateBollingerBandwidth()
    {

        SMAIndicator smaIndicator = new SMAIndicator(this.closePrice, timeFrame20);

        StandardDeviationIndicator standardDeviationIndicator = new StandardDeviationIndicator(this.closePrice, timeFrame20);

        this.bbm = new BollingerBandsMiddleIndicator(smaIndicator);

        this.bbu = new BollingerBandsUpperIndicator(this.bbm, standardDeviationIndicator);

        this.bbl = new BollingerBandsLowerIndicator(this.bbm, standardDeviationIndicator);

        return new BollingerBandWidthIndicator(this.bbu, this.bbm, this.bbl);
    }


    private Strategy getLongStrategy()
    {

        Rule nonVolatility = new OverIndicatorRule(this.bandWidth, Decimal.valueOf(this.volatilityThreshold));

        Rule rsiSlopePositive = new InSlopeRule(this.rsi, this.rsiSlopeLength, Decimal.valueOf(this.minRSISLope), Decimal.valueOf(maxRSISlope)); //Tan(n) 0 <-> Infinity

        Rule pDmiGTmDMI = new OverIndicatorRule(this.plusDMI, this.minusDMI);

        Rule bandwidthSlopeNegative = new InSlopeRule(this.bandWidth, 5, Decimal.valueOf(-50), Decimal.valueOf(-1));

        Rule stopLoss = new StopLossRule(closePrice , Decimal.valueOf(stopLossPercentage));

        Rule entrySignal = nonVolatility.and(rsiSlopePositive).and(pDmiGTmDMI);

        Rule exitSignal = bandwidthSlopeNegative.or(stopLoss);

        return new BaseStrategy(entrySignal, exitSignal);


    }


    /**
     *
     * @param series
     * @param numberOfStrategy
     * @param rsiSlopeVariation
     * @param stopLossVariation
     * @return
     */
    public static Map<Strategy, String> buildStrategiesMapForRSI(TimeSeries series, int numberOfStrategy, int rsiSlopeVariation ,  double stopLossVariation)
    {
        // Create a strategyList
        ArrayList<StrategyBRAD> strategyList = new ArrayList<>();
        // Create k Different Strategy
        for (int i = 1; i < numberOfStrategy; i++)
        {
            for ( int j = 0; j < numberOfStrategy ; j++){
                StrategyBRAD strategyBRAD = new StrategyBRAD(series, "StrategyBRAD : RSI=(" + i * rsiSlopeVariation + "), SL=(" +j*stopLossVariation+")"); //Initialize strategy with series.
                strategyBRAD.setRsiSlope(i * rsiSlopeVariation, 0.1);
                strategyBRAD.setStopLossPercentage(j*stopLossVariation);//Set Parameter
                strategyList.add(strategyBRAD);
            }
        }

        HashMap<Strategy, String> strategies = new HashMap<>();         // Create HashMap for WalkForward Class
        for (StrategyBRAD sb : strategyList)
        {
            strategies.put(sb.buildStrategy(), sb.getName());           // Build Strategy
        }
        return strategies;                                              // Return Strategy Map
    }


}


