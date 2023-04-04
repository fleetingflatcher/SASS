package xyz.proc;

import java.util.LinkedList;


public class Process implements Comparable<Process> {
    public Process () {
        pid = COUNT++;
        currentIOC = null;
        runTime = 0;
        timeSpentInActiveIO = 0;
        timeInReadyQueue = 0;
        timeInBlockedQueue = 0;
        timeInBatchQueue = 0;
        seenByCPU = false;
        UNIX_utilization = 0;
        UNIX_priority = basePriority;
    }
    private static int COUNT = 0;

    public int pid;
    public boolean hasIO;
    public int basePriority;

    /*
       Runtime
    */
    public boolean seenByCPU;
    public int arrivalTime;
    public int serviceTime;
    public int runTime;
    public int remainingTime;
    public int expectedServiceTime;
    public float UNIX_utilization;
    public float UNIX_priority;

    public int waitTime() { return timeInBatchQueue + timeInReadyQueue + timeInBlockedQueue; }
    public float responseRatio() { return ((float)(waitTime() + expectedServiceTime)) / expectedServiceTime; }


    /*
        Post-Sim Statistics
     */
    public int timeSpentInActiveIO;
    public int timeInReadyQueue;
    public int timeInBlockedQueue;
    public int timeInBatchQueue;
    public int finishTime;
    public int responseTime;
    public int turnaroundTime;
    public float tatServiceRatio;

    /***
     * IOCALL-RELATED VARIABLES & METHODS
     */
    public int timeOfNextIOC () { return (IOCQueue != null && !IOCQueue.isEmpty()) ? IOCQueue.peek().start : -1; }
    public IOCall nextIOC () { if (IOCQueue != null && !IOCQueue.isEmpty()) return IOCQueue.poll(); throw new NullPointerException("No IOC found."); }
    public LinkedList<IOCall> IOCQueue;
    public LinkedList<IOCall> originalIOCQueue;
    public IOCall currentIOC;

    /***
     * PROCESS STATUS VARIABLES, GETTERS AND SETTER
     */
    private Status status;

    public Status getStatus () { return status; }
    public void setStatus (Status s) { status = s; }

    public static boolean isAlive (Process p) {
        return !(p == null || p.getStatus() == Status.TERMINATED);
    }
    public static boolean isRunning (Process p) {
        return p.getStatus() == Status.RUNNING;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder() ;
        sb.append(
                "PID: " + pid +
                "\tRuntime: " + runTime +
                "ms\tRequires: " + remainingTime + "ms");
        sb.append("\tStatus: " + status);
        if (hasIO && currentIOC != null) {
            sb.append('\t');
            sb.append(currentIOC);
        }
        return sb.toString();
    }
    public String toStringWithIOCQueue() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.toString());
        if (hasIO) {
            sb.append("\nI/O Call List:\n");
            for (int i = 0; i < IOCQueue.size(); ++i) {
                sb.append(i+1);
                sb.append(")\t");
                sb.append(IOCQueue.get(i));
                sb.append('\n');
            }
            sb.append('\t');
        }
        return sb.toString();
    }

    public int compareTo(Process p) {
        return Integer.compare(this.arrivalTime, p.arrivalTime);
    }
}
