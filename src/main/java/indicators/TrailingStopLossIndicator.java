package indicators;

import org.ta4j.core.Decimal;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import sun.util.cldr.CLDRLocaleDataMetaInfo;

import javax.xml.bind.annotation.XmlType;

public class TrailingStopLossIndicator extends CachedIndicator<Decimal>
{

    private ClosePriceIndicator closePriceIndicator;

    private RSIIndicator rsiIndicator;

    private Decimal stopLossLimit;

    private Decimal stopLossDistance;


    /**
     * Constructor.
     * @param closePriceIndicator an indicator
     */
    public TrailingStopLossIndicator(ClosePriceIndicator closePriceIndicator , int timeFrame) {
        super(closePriceIndicator);
        this.closePriceIndicator = closePriceIndicator;
        this.rsiIndicator = new RSIIndicator(closePriceIndicator ,timeFrame);
    }

    /**
     * Implementation of the trailing stop-loss derived from RSI.
     * Logic:
     * StopLossDistance is inversly propotional to RSI . Range Between [0% - 10%]
     * IF RSI Increases ->  Stock is OverBought -> Tighten the StopLoss Distance
     * IF RSI Decreases ->  Stock is OverSold -> Loosen the StopLoss Distance.
     * IF CurrentPrice - StopLossDistance > StopLossLimit THEN StopLossLimit = CurrentPrice - StopLossDistance
     * Sell if StopLoss Limit is Breached
     * @param index
     * @return Decimal
     */
    @Override
    protected Decimal calculate(int index) {
        Decimal currentValue = closePriceIndicator.getValue(index);

        System.out.println("Current Value " + currentValue);
        System.out.println("RSI " + rsiIndicator.getValue(index));
        // stopLossPercentage = (100 - rsi)/1000
        Decimal stopLossPercentage = (Decimal.HUNDRED.minus(rsiIndicator.getValue(index))).dividedBy(Decimal.THOUSAND);
        stopLossPercentage = stopLossPercentage.dividedBy(Decimal.TWO);
        System.out.println("stopLossPercentage " + stopLossPercentage);
        // stopLossDistance = stopLossPercentage X closePrice
        stopLossDistance = stopLossPercentage.multipliedBy(currentValue);
        System.out.println("stopLossDistance " + stopLossDistance);


        if (stopLossLimit == null) {
            // Case without initial stop-loss limit value
            stopLossLimit = closePriceIndicator.getValue(0).minus(stopLossDistance);
        }
        System.out.println("stopLossLimit " + stopLossLimit);

        Decimal referenceValue = stopLossLimit.plus(stopLossDistance);

        //System.out.println("referenceValue " + referenceValue);

        if (currentValue.isGreaterThan(referenceValue)) {

           // System.out.println(currentValue + " >> " + referenceValue);
            stopLossLimit = currentValue.minus(stopLossDistance);
            System.out.println("stopLossLimit " + stopLossLimit);
        }
        return stopLossLimit;
    }


}
