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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.sql.Time;
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

    public void addData( Indicator<Decimal> indicator , String name){
        dataset.addSeries(buildChartTimeSeries(indicator , name));
    }

    /**
     * Displays a chart in a frame.
\   */
    public void displayChart(String title) {
        generateChart(title);
        // Chart panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
        // Application frame
        ApplicationFrame frame = new ApplicationFrame("FinApp - Charts");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    private void generateChart(String title){


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
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
    }





}
