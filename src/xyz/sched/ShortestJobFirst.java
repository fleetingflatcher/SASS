package xyz.sched;

import xyz.Simulation;
import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.ques.ActiveJob;
import xyz.sched.ques.BlockedQueue;
import xyz.sched.ques.ReadyQueue;

public class ShortestJobFirst extends Scheduler {
    public ShortestJobFirst(Simulation parent, int batchSize) {
        super(parent, batchSize);
        activeJob = new ActiveJob(this);
        blockedQueue = new BlockedQueue(this);
        readyQueue = new ReadyQueue(this, 0);
    }
    public int getTimeSlice() {
        return 0;
    }

    @Override
    public void activateProcess() {
        if (activeJob.status() != Status.NULL) return;
        Process p = readyQueue.getShortestJob();
        if (activeJob.offer(p))
            parent.scoreboard.reachedCPU(p);
    }
    @Override
    public void blockProcess(Process p) {
        assert p.hasIO;
        assert p.currentIOC != null;
        assert p.currentIOC.start == p.runTime;
        blockedQueue.push(p);
    }
    public void readyProcess(Process p) {
        readyQueue.push(p);
    }
}
