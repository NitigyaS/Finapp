package custom;

/**
 * Created by niitgyas on 17/9/17.
 * https://github.com/team172011
 */


import org.slf4j.LoggerFactory;


import java.util.Properties;


public class ApplicationProperties {
    private Properties properties;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    public  Properties getPropertyObject(String propertyName){
        try
        {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(propertyName));
            return properties;
        }catch (Exception ex){
            logger.error(ex.toString());
            return null;
        }
    }
}
