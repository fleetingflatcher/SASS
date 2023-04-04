package xyz.scrb;

public class Figures implements Cloneable {
    public Figures () {}

    public int totalNumberOfProcesses;          // Total number of processes in the simulation.
    public int numberOfFinishedProcesses;       // Number of processes which have finished so far.
    public float finishTime;                 // Simulation finish time (seconds)

    public float averageResponseTime;        // Response time, averaged among all processes which have recieved response.
    public float averageTat;                 // Turnaround time, averaged among all complete processes.
    public float averageServiceTime;         // Service time, averaged among complete processes.
    public float throughput;                 // Average throughput so far, in processes per second.
    public float tatToServiceTimeRatio;      // Ratio of avg. TAT to avg. service time so far.

    public HighestResponseTime highestResponseTime;
    public HighestTurnaroundTime highestTurnaroundTime;
    public HighestTatServiceRatio highestTatServiceRatio;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
