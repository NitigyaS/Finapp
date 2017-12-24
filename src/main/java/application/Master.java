package application;

import data.Proposal;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */
public class Master {
    public void startSlave(){
        List<Future<Proposal>> futureList = new ArrayList<Future<Proposal>>();
        List<Proposal> proposalList = new ArrayList<Proposal>();
        ExecutorService executorPool = Executors.newFixedThreadPool(10);

        for (int id =1; id<=10 ; id++){
            Future<Proposal> future = executorPool.submit(new Slave(id));
            futureList.add(future);

        }

        executorPool.shutdown();

        while (true){
            try {
                System.out.print("Till Now We Got Following Proposal : ");
                for (int prp : Slave.proposalList) {
                    System.out.print(prp + " , ");
                }
                System.out.println("");
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }

        /*for(Future<Proposal> fut : futureList){
            try{
                Proposal p = fut.get();
                System.out.println("Proposal recieve is : " + p.getI() + "");
                proposalList.add(p);
/*                System.out.print("Till Now We Got Following Proposal : ");
                for ( int prp : Slave.proposalList ) {
                    System.out.print(prp + " , ");
                }
                System.out.println("");

            }catch (Exception ex) {
                System.out.print(ex.toString());
            }
        }
        Proposal bestProposal = compare(proposalList);*/
    }

    private Proposal compare(List<Proposal> pli){
        Proposal bestProposal = null;
        int max = 0;
        for(Proposal p : pli){
            if (p.getI() > max){
                max = p.getI();
                bestProposal = p;
            }
        }
        System.out.println("Best Proposal is : " + bestProposal.getI());
        return bestProposal;
    }


    /*
        public void startSlave(){
        List<Future<Proposal>> futureList = new ArrayList<Future<Proposal>>();
        List<Proposal> proposalList = new ArrayList<Proposal>();
        ExecutorService executorPool = Executors.newFixedThreadPool(10);

        for (int id =0; id<10 ; id++){
            Future<Proposal> future = executorPool.submit(new Slave(id));
            futureList.add(future);

        }

        executorPool.shutdown();



        /*for(Future<Proposal> fut : futureList){
            try{
                Proposal p = fut.get();
                System.out.println("Proposal recieve is : " + p.getI() + "");
                proposalList.add(p);

            }catch (Exception ex) {
                System.out.print(ex.toString());
            }
        }
        Proposal bestProposal = compare(proposalList);
       }
     */


}
