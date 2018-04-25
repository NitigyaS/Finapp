package application;

import Strategies.StrategyAnalyser;
import Strategies.StrategyBuilder;
import Strategies.StrategyOne;
import org.ta4j.core.*;
import ta.CustomTick;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyPermission;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */
public class Slave implements Callable<StrategyBuilder> {
    public static StrategyBuilder proposalList[] = new StrategyBuilder[Master.symbolList.length];

    int taskid;

    int call_sequence = 0;

    public Slave(int taskid) {

        this.taskid = taskid;;

    }

    @Override
    public StrategyBuilder call() throws Exception {

        // Analyse the Stock given and returns the result.

        // This can be put in loop if slaves should keep on generating it.

            call_sequence++;

            System.out.format("taskID %s  executed and proposed %s .\n", taskid ,Master.symbolList[taskid]);

            synchronized (this) {

                // Analyse the stock

                StrategyBuilder strategyBuilder = AnlayseOpportunity(Master.symbolList[taskid]);

                // Put the stock in static list So that Master can check them

                proposalList[taskid] = strategyBuilder;

                // Improvise the statement below

                return strategyBuilder;

            }

    }

    private StrategyOne AnlayseOpportunity(String symbol){

        ArrayList<Bar> bar = CustomTick.historic_data(symbol,"1month");

        //Create TimeSeries

        TimeSeries ts = new BaseTimeSeries(symbol,bar);

        //Get StrategyObject

        StrategyOne strategyOne = new StrategyOne(ts);

        return  strategyOne;
    }

}
