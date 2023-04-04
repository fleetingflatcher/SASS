package xyz.sched.ques;

import xyz.proc.Process;
import xyz.sched.Scheduler;

public class ReadyQueue extends ProcessQueue {

    public ReadyQueue(Scheduler parent, int p) {
        super(parent);
        priority = p;
    }
    public void incrementWait() {
        for (Process p : queue) {
            p.timeInReadyQueue ++ ;
        }
    }
    private final int priority;

    public int getPriority() {
        return priority;
    }
    public Process getShortestJob () {
        int shortestTime = 0;
        Process shortest = null;
        for (Process p : queue) {
            if (shortest == null || shortestTime > p.serviceTime) {
                shortest = p;
                shortestTime = p.serviceTime;
            }
        }
        return shortest;
    }
    public Process getShortestRemaining () {
        int shortestTime = 0;
        Process shortest = null;
        for (Process p : queue) {
            if (shortest == null || shortestTime > p.remainingTime) {
                shortest = p;
                shortestTime = p.remainingTime;
            }
        }
        return shortest;
    }
    public Process getHRR () {
        float hrrValue = 0;
        Process hrr = null;
        for (Process p : queue) {
            if (hrr == null || hrrValue < p.responseRatio()) {
                hrr = p;
                hrrValue = p.responseRatio();
            }
        }
        return hrr;
    }

    public boolean isEmpty() { return queue.isEmpty(); }
}
