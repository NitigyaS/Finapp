package application;

import data.Proposal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;

/**
 * Created by nitigyas on 17/9/17.
 * https://github.com/team172011
 */
public class Slave implements Callable<Proposal> {
    public static int proposalList[] = new int[10];
    Proposal proposal;
    int taskid;
    int call_sequence = 0;

    public Slave(int taskid) {
        this.taskid = taskid;;
        proposal = new Proposal();
    }

    @Override
    public Proposal call() throws Exception {
        while(true) {
            int delay = (int) (taskid * 1000 * Math.random());
            call_sequence++;
            Thread.sleep(delay);
            int propsalInt = (int) (Math.random() * 2000);
            System.out.format("taskID %s took %d microseconds to execute and proposed %d.\n", taskid, delay, propsalInt);

            proposal.setI(propsalInt);

            synchronized (this) {
                proposalList[taskid] = propsalInt;
            }
        }
    }

    /*

        public Proposal call() throws Exception {
        int delay = (int) (taskid *1000* Math.random());
        call_sequence++;
        Thread.sleep(delay);
        int propsalInt = (int)(Math.random()*2000);
        System.out.format("taskID %s took %d microseconds to execute and proposed %d.\n", taskid, delay ,propsalInt);

        proposal.setI(propsalInt);

        synchronized (this){
            proposalList[taskid]= propsalInt;
        }
        return proposal;
    }
     */
}
