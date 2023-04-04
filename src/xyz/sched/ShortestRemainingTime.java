package xyz.sched;

import xyz.Simulation;
import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.ques.ActiveJob;
import xyz.sched.ques.BlockedQueue;
import xyz.sched.ques.ReadyQueue;

public class ShortestRemainingTime extends Scheduler {
    public ShortestRemainingTime (Simulation parent, int batchSize, int timeSlice) {
        super(parent, batchSize);
        activeJob = new ActiveJob(this);
        blockedQueue = new BlockedQueue(this);
        readyQueue = new ReadyQueue(this, 0);
        this.timeSlice = timeSlice;
    }
    private int timeSlice;
    public int getTimeSlice() {
        return timeSlice;
    }

    @Override
    public void activateProcess() {
        activeJob.offer(readyQueue.getShortestRemaining());
    }

    @Override
    public void blockProcess(Process p) {
        assert p.hasIO;
        assert p.currentIOC != null;
        assert p.currentIOC.start == p.runTime;
        blockedQueue.push(p);
    }

    @Override
    public void readyProcess(Process p) {
        p.setStatus(Status.READY);
        readyQueue.push(p);
    }
}