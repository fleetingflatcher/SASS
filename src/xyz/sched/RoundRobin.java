package xyz.sched;

import xyz.Simulation;
import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.ques.ActiveJob;
import xyz.sched.ques.BlockedQueue;
import xyz.sched.ques.ReadyQueue;

public class RoundRobin extends Scheduler {

    public RoundRobin (Simulation parent, int timeSlice) {
        super(parent);
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
        if (activeJob.status() == Status.NULL) {
            Process p = readyQueue.pop();
            if (activeJob.offer(p))
                parent.scoreboard.reachedCPU(p);
        }
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
