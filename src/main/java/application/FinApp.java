package application;


import constant.ApplicationProperties;
import data.Order;
import dao.OrderDao;

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
        //m.startSlave(); 1
        //CustomTick.historic_data();
        try{
            Order order = new Order("tcs",78.5,100);
            order.setOrder_history_id(2);
            OrderDao od = new OrderDao();
            od.updateOrder(order);

            //Master m = new Master();
            //m.startSlave();
            //CustomTick.historic_data();
            //new StockAnalyser().backTest();

            //System.out.println("Exce",e);
        }catch (Exception e){
            System.out.println(e);

        }
    }


}
