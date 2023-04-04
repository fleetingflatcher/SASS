package xyz.sched;

import xyz.Simulation;
import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.ques.ActiveJob;
import xyz.sched.ques.BlockedQueue;
import xyz.sched.ques.ReadyQueue;

import java.util.ArrayList;

public class UNIX extends Scheduler {
    public UNIX (Simulation parent, int batchSize, int timeSlice, int numPriorityLevels) {
        super(parent, batchSize);
        activeJob = new ActiveJob(this);
        blockedQueue = new BlockedQueue(this);
        readyQueue = new ReadyQueue(this, 0);
        this.timeSlice = timeSlice;

        readyQueues = new ArrayList<>();
        readyQueues.add(readyQueue);
        for (int i = 1; i < numPriorityLevels; ++i) {
            readyQueues.add(new ReadyQueue(this, i));
        }
    }
    public void step() {
        super.step();
        if (parent.time % 1000 == 0) halveUtilizations();
    }

    private void halveUtilizations () {
        for (ReadyQueue rq : readyQueues) {
            rq.halveUtilizations();
        }
        blockedQueue.halveUtilizations();
    }
    public ArrayList<ReadyQueue> readyQueues;
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
        p.UNIX_priority = p.basePriority + p.UNIX_utilization / 2;
        int numPriorityLevels = readyQueues.size();
        int dest = (int)(p.UNIX_priority * (numPriorityLevels) / 500);
        readyQueues.get(dest).push(p);
    }
}
