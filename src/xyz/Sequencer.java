package xyz;

import java.util.ArrayList;
import xyz.proc.Process;
import xyz.proc.Status;

public class Sequencer {
    Sequencer (Simulation parent) { this.parent = parent; }

    public void setup(ArrayList<Process> procs) { this.allProcesses = procs; }
    private Simulation parent;
    private ArrayList<Process> allProcesses;

    public boolean stimuliComplete() {
        for (Process p : allProcesses) {
            if (p.getStatus() == Status.NULL)
                return false;
        }
        return true;
    }

    public ArrayList<Process> getJobsAtTimeT (int t) {
        ArrayList<Process> result = new ArrayList<>();
        for (Process p : allProcesses)
            if (p.arrivalTime == t) {
                p.setStatus(Status.NEW);
                result.add(p);
            }
        return result;
    }
}
