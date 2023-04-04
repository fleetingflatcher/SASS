package xyz.sched.ques;

import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.RoundRobin;
import xyz.sched.Scheduler;

import java.util.LinkedList;

public class ActiveJob extends AbstractProcessQueue {
    public ActiveJob(Scheduler parent) {
        queue = new LinkedList<>();
        this.parent = parent;
    }
    private int timeOnCurrentJob = 0;
    private final Scheduler parent;

    /***
     * preempt will stop the currently running process and replace it another
     * process, to be passed as a parameter by the scheduler.
     * @param p The process that is to replace the currently running one.
     * @return The process which was preempted, which will be returned with a SUSPENDED status.
     */
    public boolean offer(Process p) {
        if (status() != Status.NULL) return false;
        p.setStatus(Status.RUNNING);
        timeOnCurrentJob = 0;
        queue.push(p);
        return true;
    }

    /***
     * Step the simulation.
     * @return  The previously running process, in the case that some state change has occurred and it is no longer running.
     */
    public boolean step() {
        if (!queue.isEmpty())
        {
            if (peekProcess().timeOfNextIOC() != -1
                    && peekProcess().timeOfNextIOC() == peekProcess().runTime)
            {
                blockJob();
                return true;
            }
            if (peekProcess().remainingTime == 0)
            {
                jobsDone();
                return true;
            }
            if (parent.getTimeSlice() > 0 && timeOnCurrentJob > parent.getTimeSlice())
            {
                preemptJob();
                return true;
            }
            peekProcess().remainingTime-- ;
            peekProcess().runTime ++ ;
            peekProcess().UNIX_utilization ++ ;
        }
        return false;
    }

    public Status status () { if (queue.isEmpty()) return Status.NULL; else return peekProcess().getStatus(); }
    public Process ejectProcess() { if (queue != null && !queue.isEmpty())  return queue.poll(); throw new NullPointerException("ActiveJob not found."); }


    // PRIVATE METHODS
    private Process peekProcess() { if (queue != null && !queue.isEmpty()) return queue.peek(); throw new NullPointerException("ActiveJob not found."); }

    private void jobsDone() {
        peekProcess().setStatus(Status.TERMINATED);
    }
    private void blockJob() {
        peekProcess().setStatus(Status.BLOCKED);
        assert peekProcess().currentIOC == null;
        peekProcess().currentIOC = peekProcess().IOCQueue.poll();
    }
    private void preemptJob() {
        peekProcess().setStatus(Status.READY);
    }
}
