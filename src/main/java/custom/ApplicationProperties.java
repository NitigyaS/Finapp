package custom;




import org.slf4j.LoggerFactory;


import java.util.Properties;

/**
 * Created by niitgyas on 17/9/17.
 * The Class Reads the Prperties File and Returns an instance of it.
 */
public class ApplicationProperties {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    public  Properties getPropertyObject(String propertyName){
        try
        {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(propertyName));
            return properties;

        }catch (Exception ex){
            logger.error(ex.toString());
            return null;
        }
    }
}
