package xyz.sched.ques;

import xyz.proc.IOCall;
import xyz.proc.Process;
import xyz.proc.Status;
import xyz.sched.Scheduler;

import java.util.ArrayList;
import java.util.Iterator;

public class BlockedQueue extends ProcessQueue {
    public BlockedQueue (Scheduler parent) { super(parent); }

    public boolean step () {
        if (queue.isEmpty()) return false;
        for (Process p : queue) {
            if (p.currentIOC.done) {
                unblock(p);
                return true;
            }
        }
        return false;
    }
    public void incrementWait () {
        for (Process p : queue)
        {
            p.timeInBlockedQueue ++ ;
            if (p.currentIOC.active)
                p.timeSpentInActiveIO ++ ;
        }
    }
    public ArrayList<Process> ejectUnblocked() {
        ArrayList<Process> arr = new ArrayList<>();
        Iterator<Process> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Process p = iterator.next();
            if (p.getStatus() != Status.BLOCKED) {
                arr.add(p);
                p.setStatus(Status.READY);
                iterator.remove();
            }
        }
        return arr;
    }
    public ArrayList<IOCall> inactiveIOCalls () {
        ArrayList<IOCall> res = new ArrayList<>();
        for (Process p : queue) {
            if (!p.currentIOC.active) res.add(p.currentIOC);
        }
        return res;
    }

    private void unblock(Process p) {
        p.currentIOC = null;
        p.setStatus(Status.READY);
    }
}
