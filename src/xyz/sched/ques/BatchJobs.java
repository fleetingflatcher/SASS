package xyz.sched.ques;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import xyz.proc.Process;
import xyz.sched.Scheduler;

public class BatchJobs extends ProcessQueue {
    public BatchJobs (Scheduler parent, int num) {
        super(parent);
        this.num = num;
        queue = new LinkedList<>();
    }
    private int num;

    public boolean step() {
        if (queue.isEmpty()) return false;
        if (num != 0) {
            return true;
        }
        return false;
    }
    public Process getJob() {
        assert num != 0;
        if (num > 0) num --;
        return queue.pop();
    }
    public void releaseJob() {
        if (num >= 0) num ++;
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
