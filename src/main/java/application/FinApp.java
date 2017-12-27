package application;


import constant.ApplicationProperties;
import ta.CustomTick;
import ta.StockAnalyser;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */
public class FinApp {
    public static void main(String args[]){
        System.out.println("This is the entry point of Application!!");
        ApplicationProperties applicationProperties = new ApplicationProperties();
        System.out.println("Config File Properties : " + applicationProperties.AUTHOR + " , " + applicationProperties.COMPANY);
        //Master m = new Master();
        //m.startSlave();
        //CustomTick.historic_data();
        try{
            Master m = new Master();
            m.startSlave();
            //CustomTick.historic_data();
            //new StockAnalyser().backtest();

            //System.out.println("Exce",e);
        }catch (Exception e){

        }
    }


}
