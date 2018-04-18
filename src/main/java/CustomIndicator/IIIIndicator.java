package CustomIndicator;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.helpers.*;

/**
 * Intraday Intensity Index
 * https://www.investopedia.com/terms/i/intradayintensityindex.asp
 */

public class IIIIndicator extends CachedIndicator<Decimal> {


    private CloseLocationValueIndicator clvIndicator;

    private MaxPriceIndicator maxPriceIndicator;

    private MinPriceIndicator minPriceIndicator;

    private VolumeIndicator volumeIndicator;

    public IIIIndicator(TimeSeries series) {

        super(series);

        clvIndicator = new CloseLocationValueIndicator(series);

        maxPriceIndicator = new MaxPriceIndicator(series);

        minPriceIndicator = new MinPriceIndicator(series);

        volumeIndicator = new VolumeIndicator(series);

    }

    @Override

    protected Decimal calculate(int index) {

        if (index == 0) {

            return Decimal.ZERO;

        }

        Decimal doubleClosePrice =  Decimal.valueOf(2).multipliedBy(clvIndicator.getValue(index));

        Decimal highmlow = maxPriceIndicator.getValue(index).minus(minPriceIndicator.getValue(index));

        Decimal highplow = maxPriceIndicator.getValue(index).plus(minPriceIndicator.getValue(index));

        return (doubleClosePrice.minus(highplow)).dividedBy(highmlow.multipliedBy(volumeIndicator.getValue(index)));

    }

}
