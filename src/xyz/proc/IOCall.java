package xyz.proc;

import java.util.ArrayList;
import xyz.perif.Peripheral;


public class IOCall implements Comparable<IOCall> {
    public IOCall (Process parent) {
        this.parent = parent;
        done = false;
        active = false;
        perifs = new ArrayList<>();
    }
    private Process parent;

    public int start;
    public int remaining;
    public int duration;
    public boolean done;
    public boolean active;
    public ArrayList<Peripheral> perifs;


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("@" + start + " for " + duration + " to ");
        for (int i = 0; i < perifs.size(); ++i) {
            sb.append(perifs.get(i).toString());
            if (i != perifs.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public String toStringVerbose () {
        StringBuilder sb = new StringBuilder();
        sb.append((done ? "Done" : (active ? "Active" : "Inactive")) + " IOC\t @" + start + "ms for " + duration + "ms to peripheral(s): ");
        for (int i = 0; i < perifs.size(); ++i) {
            sb.append(perifs.get(i).toStringVerbose());
            if (i != perifs.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public int compareTo(IOCall ioc) {
        return Integer.compare(this.start, ioc.start);
    }
}
