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
    private  static  Properties properties = new ApplicationProperties().getPropertyObject("config.properties");

    /**
     * Starting Point of the Application
     * @param args Arguments to be passed to main method.
     */
    public static void main(String[] args) {

        try{
            logger.info("Application Starting");

            logger.info("Author : " + properties.getProperty("author"));

            logger.info("Initiating Master Process");
            // Slope - > Angle
            System.out.println(Math.toDegrees(Math.atan(50)));
            double rad = Math.toRadians(40);
            System.out.println(Math.atan(0.83901));

            //Master m = new Master();

            logger.info("Initiating Slave Processes");

            //m.startSlave();
        } catch (Exception ex) {
            logger.error(ex.toString());

        }
    }

}
