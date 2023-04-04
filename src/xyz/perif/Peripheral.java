package xyz.perif;

import xyz.proc.IOCall;

public class Peripheral {
    private static int COUNT = 0;

    public Peripheral () { fid = COUNT++; }
    private int fid;
    private boolean occupado;
    private IOCall currentCall;

    public boolean isOccupied() { return occupado; }
    public void setAsOccupied(IOCall ioc) {
        occupado = true;
        currentCall = ioc;
    }
    public boolean checkIfIsCurrentCall(IOCall ioc) {
        return currentCall == ioc;
    }
    public void releaseOccupation() {
        occupado = false;
        currentCall = null;
    }

    public String toString() {
        return "P" + fid;
    }
    public String toStringVerbose() {
        return "[P" + fid + '\t' + (occupado ? "BUSY" : "FREE") + ']';
    }
}

