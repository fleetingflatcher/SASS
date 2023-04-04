package xyz.seqsrc;

import xyz.Simulation;
import xyz.SimulationElement;
import xyz.perif.Peripheral;
import xyz.proc.IOCall;
import xyz.proc.Process;
import xyz.proc.Status;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;

public class Generator extends SimulationElement {
    public Generator (Simulation parent) {
        super(parent);
        rand = new Random(0);
    }
    Random rand;
    public ArrayList<Peripheral> generatePeripherals (int num) {
        ArrayList<Peripheral> result = new ArrayList<>();
        for (int i = 0; i < num; ++i)
        {
            result.add(newPeripheral());
        }
        return result;
    }
    public ArrayList<Process> generateProcesses (int num) {
        ArrayList<Process> result = new ArrayList<>();
        for (int i = 0; i < num; ++i)
        {
            result.add(newProcess());
        }
        return result;
    }

    private Process newProcess () {
        Process p = new Process();
        p.arrivalTime = Math.abs(rand.nextInt()) % parent.SIM_SETTINGS.GEN.MAX_ARRIVAL_TIME;
        p.serviceTime = 1 + Math.abs(rand.nextInt()) % parent.SIM_SETTINGS.GEN.MAX_SERVICE_TIME;
        float factor = (rand.nextBoolean() ? 1 : -1) * (rand.nextFloat() / 2f);
        p.expectedServiceTime = (int)((1 + ( rand.nextBoolean() ? factor : factor * -1 )) * p.remainingTime);
        p.hasIO = false;
        if (rand.nextFloat() < parent.SIM_SETTINGS.IOH.PROPORTION) {

            int numIOCs = 1 + Math.abs(rand.nextInt()) % parent.SIM_SETTINGS.IOH.MAX_NUM_IOC_PER_PROCESS;
            ArrayList<IOCall> arr = new ArrayList<>();
            for (int i = 0; i < numIOCs; ++i) {
                arr.add(newIOCall(p));
            }
            Collections.sort(arr);
            p.IOCQueue = new LinkedList<>();
            p.originalIOCQueue = new LinkedList<>();
            p.IOCQueue.addAll(arr);
            p.originalIOCQueue.addAll(arr);
            p.hasIO = true;
        }
        else {
            p.IOCQueue = null;
        }
        p.setStatus(Status.NULL);
        return p;
    }

    private IOCall newIOCall (Process p) {
        IOCall ioc = new IOCall(p) ;

        ioc.start = Math.abs(rand.nextInt()) % p.serviceTime;
        ioc.duration = 1 + Math.abs(rand.nextInt()) % (parent.SIM_SETTINGS.IOH.MAX_IO_TIME - 1);
        ioc.remaining = ioc.duration;
        int numPerifs = 1 + Math.abs(rand.nextInt()) % parent.ioHandler.perifs.size()/2;
        ioc.perifs = new ArrayList<>();

        for (int i = 0; i < numPerifs; ++i) {
            int x;
            boolean duplicate = false;
            do {
                duplicate = false;
                x = Math.abs(rand.nextInt()) % parent.ioHandler.perifs.size();
                for (int j = 0; j < i; ++j)
                    if (ioc.perifs.get(j) == parent.ioHandler.perifs.get(x)) {
                        duplicate = true;
                        break;
                    }
            } while (duplicate);
            ioc.perifs.add(parent.ioHandler.perifs.get(x));
        }
        return ioc;
    }
    private Peripheral newPeripheral () {
        return new Peripheral();
    }
}
