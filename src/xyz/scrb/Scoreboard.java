package xyz.scrb;

import xyz.Simulation;
import xyz.SimulationElement;
import xyz.proc.Status;
import xyz.scrb.Figures;

import java.util.ArrayList;
import xyz.proc.Process;

public class Scoreboard extends SimulationElement {
    public Scoreboard (Simulation parent) {
        super(parent);
    }
    public void setup(ArrayList<Process> procs) {
        this.allProcesses = procs;
    }
    ArrayList<Process> allProcesses;


    public void reachedCPU(Process p) {
        if (p.seenByCPU) return;
        p.seenByCPU = true;
        p.responseTime = parent.time - p.arrivalTime;
    }
    public void postProcessing(Process p) {
        p.turnaroundTime = parent.time - p.arrivalTime;
        p.tatServiceRatio = (float) (p.turnaroundTime / p.serviceTime);
    }

    public ArrayList<Process> finishedProcesses() {
        ArrayList<Process> arr = new ArrayList<>();
        for (Process p : allProcesses)
            if (p.getStatus() == Status.TERMINATED) arr.add(p);
        return arr;
    }

    public Figures produceFigures() {
        int time = parent.time;
        Figures figures = new Figures();
        figures.totalNumberOfProcesses = allProcesses.size();

        int response_sum = 0;
        int tat_sum = 0;
        int serv_sum = 0;
        int response_count = 0;
        int finish_count = 0;
        Process high_response   = null;
        Process high_tat        = null;
        Process high_tatratio   = null;
        for (Process p : allProcesses) {
            if (p.responseTime != 0) {
                ++response_count;
                response_sum += p.responseTime;
                if (high_response == null
                        || p.responseTime > high_response.responseTime)
                    high_response = p;
            }
            if (p.getStatus() == Status.TERMINATED) {
                ++finish_count;
                tat_sum += p.turnaroundTime;
                serv_sum += p.serviceTime;
                if (high_tat == null
                        || p.turnaroundTime > high_tat.turnaroundTime)
                    high_tat = p;
                if (high_tatratio == null
                    || p.tatServiceRatio > high_tatratio.tatServiceRatio)
                    high_tatratio = p;
            }
            }
        if (finish_count == allProcesses.size()) figures.finishTime = time;
        else figures.finishTime = -1;
        figures.numberOfFinishedProcesses = finish_count;
        figures.throughput = ((float)finish_count / (time != 0 ? time : 1)) * 1000;
        figures.averageResponseTime = (((float)response_sum)/1000) / (response_count != 0 ? response_count : 1);
        figures.averageServiceTime = (((float)serv_sum)/1000) / (finish_count != 0 ? finish_count : 1);
        figures.averageTat = (((float)tat_sum)/1000) / (finish_count != 0 ? finish_count : 1);
        figures.tatToServiceTimeRatio = figures.averageTat / figures.averageServiceTime;

        return figures;
    }
}
