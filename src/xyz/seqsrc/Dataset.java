package xyz.seqsrc;

import xyz.proc.Process;
import xyz.perif.Peripheral;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Dataset {
    public Dataset(ArrayList<Process> processes, ArrayList<Peripheral> peripherals) {
        this.processes = processes;
        this.peripherals = peripherals;
    }
    public ArrayList<Process> processes;
    public ArrayList<Peripheral> peripherals;
}
