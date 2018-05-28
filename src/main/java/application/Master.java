package application;

import Analyser.BackTestAnalyser;
import Strategies.StrategyBuilder;
import Strategies.StrategyOne;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by nitigyas on 17/9/17.
 *
 */
public class Master {
    // List of Stocks to Analyse
    final  static String[] symbolList = new String[]{"TCS"};
    // Methods start slaves for each stock
    public void startSlave(){
        //List<Future<Void>> futureList = new ArrayList<Future<Void>>();
        // Maximum thread to run at a time.
        ExecutorService executorPool = Executors.newFixedThreadPool(10);

        // Create Slaves Threads
        for (int id =0; id<symbolList.length ; id++){
            Future<Void> future = executorPool.submit(new Slave(id));
            //futureList.add(future);

        }

        executorPool.shutdown();
        // Wait for Threads to complete
        // This code needs to be fixed
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
