package ta;

import org.ta4j.core.Decimal;
import org.ta4j.core.Indicator;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.ColorBlock;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nitigyas on 31/12/17.
 *
 * The Class Converts Indicator to Chart.
 *
 */
public class IndicatortoChart {
    TimeSeriesCollection dataset ;
    JFreeChart chart;
    TimeSeries tickSeries;

    public IndicatortoChart(TimeSeries tickSeries){
         this.tickSeries = tickSeries;
         dataset = new TimeSeriesCollection();
    }

    public void generateChart(String title){
        chart = ChartFactory.createTimeSeriesChart(
                title, // title
                "Date", // x-axis label
                "Price Per Unit", // y-axis label
                dataset, // data
                true, // create legend?
                true, // generate tooltips?
                false // generate URLs?
        );


        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setBackgroundPaint(Color.white);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        displayChart();

    }

    public void generateCandles(String title, Boolean volumeFlag){
        OHLCDataset ohlcDataset = createOHLCDataset(tickSeries);
        TimeSeriesCollection xyDataset = createAdditionalDataset(tickSeries);

        chart = ChartFactory.createCandlestickChart(
                title,
                "Time",
                "Rupee",
                ohlcDataset,
                true);
        // Candlestick rendering

        CandlestickRenderer renderer = new CandlestickRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
        renderer.setDrawVolume(volumeFlag);
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(renderer);
        int index = 1;
        plot.setDataset(index, xyDataset);
        plot.mapDatasetToRangeAxis(index, 0);
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(index, Color.blue);
        plot.setRenderer(index, renderer2);
        // Misc
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setBackgroundPaint(Color.white);
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setAutoRangeIncludesZero(false);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        displayChart();

    }

    private void displayChart() {

        // Chart panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
        panel.setMouseZoomable(true);
        // Application frame
        ApplicationFrame frame = new ApplicationFrame("LineChart");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    private org.jfree.data.time.TimeSeries buildChartTimeSeries(Indicator<Decimal> indicator, String name) {
        org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries(name);
        for (int i = 0; i < tickSeries.getBarCount(); i++) {
            Bar bar = tickSeries.getBar(i);
            chartTimeSeries.add(new Day(Date.from(bar.getEndTime().toInstant())), indicator.getValue(i).toDouble());
        }
        return chartTimeSeries;
    }

    /**
     * Add Inidcators on the Graph
     * @param indicator
     * @param name
     */
    public void addData(   Indicator<Decimal> indicator , String name){
        dataset.addSeries(buildChartTimeSeries(indicator , name));
    }

    // CandleStick Code
    private OHLCDataset createOHLCDataset(TimeSeries series) {
        final int nbBars = series.getBarCount();

        Date[] dates = new Date[nbBars];
        double[] opens = new double[nbBars];
        double[] highs = new double[nbBars];
        double[] lows = new double[nbBars];
        double[] closes = new double[nbBars];
        double[] volumes = new double[nbBars];

        for (int i = 0; i < nbBars; i++) {
            Bar bar = series.getBar(i);
            dates[i] = new Date(bar.getEndTime().toEpochSecond() * 1000);
            opens[i] = bar.getOpenPrice().toDouble();
            highs[i] = bar.getMaxPrice().toDouble();
            lows[i] = bar.getMinPrice().toDouble();
            closes[i] = bar.getClosePrice().toDouble();
            volumes[i] = bar.getVolume().toDouble();
        }

        OHLCDataset dataset = new DefaultHighLowDataset("OHLC", dates, highs, lows, opens, closes, volumes);
        return dataset;
    }

    private TimeSeriesCollection createAdditionalDataset(TimeSeries series) {
        ClosePriceIndicator indicator = new ClosePriceIndicator(series);
        //TimeSeriesCollection dataset = new TimeSeriesCollection();
        org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries("CandleStick Chart");
        for (int i = 0; i < series.getBarCount(); i++) {
            Bar bar = series.getBar(i);
            chartTimeSeries.add(new Second(new Date(bar.getEndTime().toEpochSecond() * 1000)), indicator.getValue(i).toDouble());
        }
        dataset.addSeries(chartTimeSeries);
        return dataset;
    }





}
