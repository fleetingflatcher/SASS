package xyz.sched.ques;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import xyz.proc.Process;
import xyz.sched.Scheduler;

public class BatchJobs extends ProcessQueue {
    public BatchJobs (Scheduler parent, int num) {
        super(parent);
        this.batchCtr = num;
        queue = new LinkedList<>();
    }
    private int batchCtr;

    public boolean step() {
        if (queue.isEmpty()) return false;
        if (batchCtr != 0) {
            return true;
        }
        return false;
    }
    public Process getJob() {
        assert batchCtr!= 0;
        if (batchCtr > 0) batchCtr -- ;
        Process p = queue.pop();
        p.remainingTime = p.serviceTime;
        return p;
    }
    public void releaseJob() {
        if (batchCtr>= 0) batchCtr ++;
    }
    public void incrementWait() {
        for (Process p : queue)
            p.timeInBatchQueue ++ ;
    }
    public void addJobs(ArrayList<Process> jobs)
    {
        if (!queue.isEmpty()) {
            jobs.addAll(queue);
            queue.clear();
        }
        Collections.sort(jobs);
        queue.addAll(jobs);
    }
}
