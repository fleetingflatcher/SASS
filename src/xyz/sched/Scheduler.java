package xyz.sched;


import xyz.Simulation;
import xyz.SimulationElement;
import xyz.proc.IOCall;
import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.ques.ActiveJob;
import xyz.sched.ques.BatchJobs;
import xyz.sched.ques.BlockedQueue;
import xyz.sched.ques.ReadyQueue;

import java.util.ArrayList;

public abstract class Scheduler extends SimulationElement {
    public Scheduler(Simulation parent) {
        super(parent);
        batchJobs = new BatchJobs(this, parent.SIM_SETTINGS.SCH.BATCH_SIZE);
    }
    public BatchJobs batchJobs;
    public ActiveJob activeJob;
    public BlockedQueue blockedQueue;
    public ReadyQueue readyQueue;
    public ArrayList<IOCall> offerInactiveIOCs () {
        return blockedQueue.inactiveIOCalls();
    }
    public abstract void activateProcess();
    public abstract void blockProcess(Process p);
    public abstract void readyProcess(Process p);
    public abstract int getTimeSlice();

    public void finishProcess(Process p) {
        batchJobs.releaseJob();
        assert p.runTime > 0;
        assert p.remainingTime == 0;
        if (p.hasIO) {
            assert p.IOCQueue.isEmpty();
            assert p.currentIOC == null;
        }
        p.finishTime = parent.time;
        parent.scoreboard.postProcessing(p);
    }

    public boolean simulationComplete() {
        return (batchJobs.isEmpty()
                && activeJob.status() == Status.NULL
                && blockedQueue.isEmpty()
                && readyQueue.isEmpty());
    }
    public void step() {
        /*
            Batch Jobs
         */
        if (batchJobs.step())
            readyProcess(batchJobs.getJob());

        /*
            Ready Queue
         */
        if (readyQueue.step()) {
            activateProcess();
        }

        /*
            Active Job
         */
        if(activeJob.step()) {
            assert activeJob.status() != Status.NULL;
            Process p = activeJob.ejectProcess();
            switch(p.getStatus()) {
                case TERMINATED -> {
                    finishProcess(p);
                }
                case BLOCKED -> {
                    blockProcess(p);
                }
                default -> {
                    assert false;
                }
            }
        }

        /*
            Blocked Queue
         */
        if (blockedQueue.step())
            for (Process p : blockedQueue.ejectUnblocked()) {
                readyProcess(p);
            }
        
        incrementWaitTimes();
    }
    public void incrementWaitTimes () {
        readyQueue.incrementWait();
        blockedQueue.incrementWait();
        batchJobs.incrementWait();
    }
}
