package xyz.sched.ques;

import xyz.perif.Peripheral;
import xyz.sched.Scheduler;

public class PeripheralQueue extends ProcessQueue {
    public PeripheralQueue (Scheduler parent) { super(parent); }
    Peripheral target;
    public boolean step() { return false;}
}
