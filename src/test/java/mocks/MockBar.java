package mocks;

import org.ta4j.core.BaseBar;
import org.ta4j.core.Decimal;

import java.time.ZonedDateTime;

/**
 * A mock bar with sample data.
 */
public class MockBar extends BaseBar {

    private Decimal amount = Decimal.ZERO;

    private int trades = 0;

    public MockBar(double closePrice) {
        this(ZonedDateTime.now(), closePrice);
    }

    public MockBar(double closePrice, double volume) {
        super(ZonedDateTime.now(), 0, 0, 0, closePrice, volume);
    }

    public MockBar(ZonedDateTime endTime, double closePrice) {
        super(endTime, 0, 0, 0, closePrice, 0);
    }

    public MockBar(double openPrice, double closePrice, double maxPrice, double minPrice) {
        super(ZonedDateTime.now(), openPrice, maxPrice, minPrice, closePrice, 1);
    }

    public MockBar(double openPrice, double closePrice, double maxPrice, double minPrice, double volume) {
        super(ZonedDateTime.now(), openPrice, maxPrice, minPrice, closePrice, volume);
    }

    public MockBar(ZonedDateTime endTime, double openPrice, double closePrice, double maxPrice, double minPrice, double amount, double volume, int trades) {
        super(endTime, openPrice, maxPrice, minPrice, closePrice, volume);
        this.amount = Decimal.valueOf(amount);
        this.trades = trades;
    }

    @Override
    public Decimal getAmount() {
        return amount;
    }

    @Override
    public int getTrades() {
        return trades;
    }
}