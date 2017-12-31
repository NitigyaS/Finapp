package application;

import Strategies.StrategyAnalyser;
import Strategies.StrategyBuilder;
import Strategies.StrategyOne;
import eu.verdelhan.ta4j.Strategy;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Created by nitigyas on 17/9/17.
 *
 */
public class Master {
    // List of Stocks to Analyse
    final  static String[] symbolList = new String[]{"TCS","ADANIPORTS","ASIANPAINT"};
    // Methods start slaves for each stock
    public void startSlave(){
        List<Future<StrategyBuilder>> futureList = new ArrayList<Future<StrategyBuilder>>();
        // Maximum thread to run at a time.
        ExecutorService executorPool = Executors.newFixedThreadPool(10);

        // Create Slaves Threads
        for (int id =0; id<symbolList.length ; id++){
            Future<StrategyBuilder> future = executorPool.submit(new Slave(id));
            futureList.add(future);

        }

        executorPool.shutdown();
        // Wait for Threads to complete
        // This code needs to be fixed
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display Result of all calculations // This can be run in loop if slaves keep on changing there ouptut
        for (int j = 0 ; j<symbolList.length ; j++){
                System.out.println("Till Now We Got Following Proposal : ");
                for (StrategyBuilder prp : Slave.proposalList) {
                    //System.out.print(prp.getName() + " , ");
                    new StrategyAnalyser().printAllResults((StrategyOne)prp);
                }
                System.out.println("");

        }

    }

}
