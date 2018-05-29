package application;


import custom.ApplicationProperties;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */
public class FinApp {
    public static void main(String args[]){
        System.out.println("This is the entry point of Application!!");
        ApplicationProperties applicationProperties = new ApplicationProperties();
        System.out.println("Config File Properties : " + ApplicationProperties.AUTHOR + " , " + ApplicationProperties.COMPANY);
        //Master m = new Master();
        //m.startSlave(); 1
        //CustomTick.historic_data();
        try{
            //OrderDao orderDao = new OrderDao();
            //orderDao.getOrder(29);
            Master m = new Master();
            m.startSlave();
            //CustomTick.historic_data();
            //new StockAnalyser().backTest();

            //System.out.println("Exce",e);
        }catch (Exception e){
            System.out.println(e);

        }
    }


}
