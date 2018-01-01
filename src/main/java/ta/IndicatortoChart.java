package ta;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.helpers.ClosePriceIndicator;
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
 * Created by home on 31/12/17.
 */
public class IndicatortoChart {
    TimeSeriesCollection dataset ;
    JFreeChart chart;
    TimeSeries tickSeries;

    public IndicatortoChart(TimeSeries tickSeries){
         this.tickSeries = tickSeries;
         dataset = new TimeSeriesCollection();
    }



    private org.jfree.data.time.TimeSeries buildChartTimeSeries(Indicator<Decimal> indicator, String name) {
        org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries(name);
        for (int i = 0; i < tickSeries.getTickCount(); i++) {
            Tick tick = tickSeries.getTick(i);
            chartTimeSeries.add(new Day(Date.from(tick.getEndTime().toInstant())), indicator.getValue(i).toDouble());
        }
        return chartTimeSeries;
    }

    /**
     * Add Inidcators on the Graph
     * @param indicator
     * @param name
     */
    public void addData( Indicator<Decimal> indicator , String name){
        dataset.addSeries(buildChartTimeSeries(indicator , name));
    }

    /**
     * Displays a chart in a frame.
\   */
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


        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setBackgroundPaint(Color.white);
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        displayChart();

    }


    // CandleStick Code
    private OHLCDataset createOHLCDataset(TimeSeries series) {
        final int nbTicks = series.getTickCount();

        Date[] dates = new Date[nbTicks];
        double[] opens = new double[nbTicks];
        double[] highs = new double[nbTicks];
        double[] lows = new double[nbTicks];
        double[] closes = new double[nbTicks];
        double[] volumes = new double[nbTicks];

        for (int i = 0; i < nbTicks; i++) {
            Tick tick = series.getTick(i);
            dates[i] = new Date(tick.getEndTime().toEpochSecond() * 1000);
            opens[i] = tick.getOpenPrice().toDouble();
            highs[i] = tick.getMaxPrice().toDouble();
            lows[i] = tick.getMinPrice().toDouble();
            closes[i] = tick.getClosePrice().toDouble();
            volumes[i] = tick.getVolume().toDouble();
        }

        OHLCDataset dataset = new DefaultHighLowDataset("OHLC", dates, highs, lows, opens, closes, volumes);
        return dataset;
    }

    private TimeSeriesCollection createAdditionalDataset(TimeSeries series) {
        ClosePriceIndicator indicator = new ClosePriceIndicator(series);
        //TimeSeriesCollection dataset = new TimeSeriesCollection();
        org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries("CandleStick Chart");
        for (int i = 0; i < series.getTickCount(); i++) {
            Tick tick = series.getTick(i);
            chartTimeSeries.add(new Second(new Date(tick.getEndTime().toEpochSecond() * 1000)), indicator.getValue(i).toDouble());
        }
        dataset.addSeries(chartTimeSeries);
        return dataset;
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



}
