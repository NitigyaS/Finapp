package application;

import custom.ApplicationProperties;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */

public class FinApp {
    private static Logger logger = LoggerFactory.getLogger(FinApp.class);

    /**
     * Starting Point of the Application
     * @param args
     */
    public static void main(String[] args) {
        //Master m = new Master();
        //m.startSlave(); 1
        //CustomTick.historic_data();
        try{
            System.out.println("This is the entry point of Application!!");
            Properties properties = new ApplicationProperties().getPropertyObject("config.properties");
            System.out.println("Config File Properties : " + properties.getProperty("author") + " , " + properties.getProperty("company"));
            //OrderDao orderDao = new OrderDao();
            //orderDao.getOrder(29);
            Master m = new Master();
            m.startSlave();
            //CustomTick.historic_data();
            //new StockAnalyser().backTest();

            //System.out.println("Exce",e);
        }catch (Exception e){
            logger.error(e.toString());

        }
    }


}
