package xyz.sched;

import xyz.Simulation;
import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.ques.*;


public class FirstComeFirstServed extends Scheduler {
    public FirstComeFirstServed(Simulation parent) {
        super(parent);
        activeJob = new ActiveJob(this);
        blockedQueue = new BlockedQueue(this);
        readyQueue = new ReadyQueue(this, 0);
    }

    @Override
    public void activateProcess() {
        if (activeJob.status() != Status.NULL) return;
        Process p = readyQueue.pop();
        if (activeJob.offer(p))
            parent.scoreboard.reachedCPU(p);
    }

    public int getTimeSlice() {
        return 0;
    }
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