package application;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by nitigyas on 17/9/17.
 * refer: http://www.vogella.com/tutorials/JavaConcurrency/article.html
 * Class Initates multiple Slave Threads to Trade.
 */
public class Master{
    // List of Stocks to Analyse
    private static final int NTHREADS = 10;

    final  static String[] symbolList = new String[]{"TCS"};

    // Get Stocks to analyse from Database.
    // Get Which stocks are in volatile phase.

    /**
     * Methods start the slaves.
     */
    public void startSlave(){

        // Maximum thread to run at a time.
        ExecutorService executorPool = Executors.newFixedThreadPool(NTHREADS);

        // Create Slaves Threads
        for (int id =0; id<symbolList.length ; id++){
            executorPool.execute(new Slave(id));
        }

        shutdownExecutor(executorPool);

    }

    /**
     * It Stops the Service Gracefully.
     * @param executorService Service To be stop Gracefully
     */
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
