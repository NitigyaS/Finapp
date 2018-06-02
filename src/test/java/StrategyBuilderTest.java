import Strategies.StrategyBRAD;
import mocks.MockTimeSeries;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.ta4j.core.Strategy;
import org.ta4j.core.TimeSeries;

import java.util.HashMap;
import java.util.Map;

public class StrategyBuilderTest
{
    private TimeSeries series;
    @Before
    public void setUp() {
        series = new MockTimeSeries(
                50.45, 50.30, 50.20,
                50.15, 50.05, 50.06,
                50.10, 50.08, 50.03,
                50.07, 50.01, 50.14,
                50.22, 50.43, 50.50,
                50.56, 50.52, 50.70,
                50.55, 50.62, 50.90,
                50.82, 50.86, 51.20, 51.30, 51.10);

    }

    @Test
    public void ShouldNotEnterAtBegin(){
        StrategyBRAD strategyBRAD = new StrategyBRAD(series,"Test");
        Strategy strategy = strategyBRAD.buildStrategy();
        boolean shouldEnterAtBegin = strategy.shouldEnter(series.getBeginIndex());
        Assert.assertFalse(shouldEnterAtBegin);
    }

    @Test
    public void CheckNumberOfStrategyReturn(){
        int numberOfStrategy = 5;
        Map<Strategy , String > strategyStringHashMap = StrategyBRAD.buildStrategiesMapForRSI(series , numberOfStrategy ,5,2);
        Assert.assertEquals(strategyStringHashMap.size(),numberOfStrategy*(numberOfStrategy-1));
    }


}
