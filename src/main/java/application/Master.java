package application;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by nitigyas on 17/9/17.
 *
 */
public class Master {
    // List of Stocks to Analyse
    private static final int NTHREDS = 10;

    final  static String[] symbolList = new String[]{"TCS"};

    // Methods start slaves for each stock
    public void startSlave(){

        List<Future<Void>> list = new ArrayList<>();

        // Maximum thread to run at a time.
        ExecutorService executorPool = Executors.newFixedThreadPool(NTHREDS);

        // Create Slaves Threads
        for (int id =0; id<symbolList.length ; id++){

            Future<Void> future = executorPool.submit(new Slave(id));
            list.add(future);
        }

        shutdownExecutor(executorPool);


        //Monitor Threads? and refork them
        // Wait for Threads to complete


    }

    private void shutdownExecutor(ExecutorService executorService){
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

}
