package xyz.sched.ques;

import xyz.proc.Process;
import xyz.sched.Scheduler;

import java.util.LinkedList;

public class ProcessQueue extends AbstractProcessQueue {

    protected ProcessQueue (Scheduler parent) {
        this.parent = parent;
        queue = new LinkedList<>();
    }
    private final Scheduler parent;
    public boolean step() {
        return !queue.isEmpty();
    }
    public void halveUtilizations () {
        for (Process p : queue) {
            p.UNIX_utilization = p.UNIX_utilization / 2;
        }
    }
    public Process pop() { return queue.poll(); }
    public void push(Process p) { queue.add(p); }
    public boolean isEmpty() { return queue.isEmpty(); }
}
