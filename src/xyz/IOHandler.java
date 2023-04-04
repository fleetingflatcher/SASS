package xyz;

import xyz.perif.Peripheral;
import xyz.perif.PeripheralOccupiedException;
import xyz.proc.IOCall;
import java.util.ArrayList;
import java.util.Iterator;

public class IOHandler {
    public IOHandler (Simulation parent) {
        this.parent = parent;
        this.perifs = null;
        this.runningIOCalls = new ArrayList<>();
    }
    public void setup(ArrayList<Peripheral> allPeripherals) {
        this.perifs = allPeripherals;
    }
    private Simulation parent;
    public ArrayList<Peripheral> perifs;
    public ArrayList<IOCall> runningIOCalls;

    public void requestResources(IOCall ioc) {
        /***
         * This is a critical point. We can try to occupy in the first loop,
         * but this produces the possibility of deadlock, with processes
         * "reserving" resources, even if they aren't running the IOC yet.
         *
         * For now, I opt to lean away from deadlock, towards starvation.
         * I will wait until all resources are available before occupying
         * any of them.
         */
        for (Peripheral f : ioc.perifs) {
            if (f.isOccupied())
                if (!f.checkIfIsCurrentCall(ioc))
                    return;
                //else tryToOccupy();       //  Resource "reservation".

        }
        for (Peripheral f : ioc.perifs) {
            try { tryToOccupy(f, ioc); }
            catch (Exception e) {
                System.err.println("IO Call " + ioc + "\tFailed to reserve resource " + f);
                return;
            }
        }
        ioc.active = true;
        runningIOCalls.add(ioc);
    }

    private void tryToOccupy(Peripheral perif, IOCall ioc) throws PeripheralOccupiedException {
        if (perif.isOccupied()) throw new PeripheralOccupiedException(
                "IO Call " + ioc + " wanted to access peripheral " + this + " but " + this + " is currently occupied by " + ioc);
        perif.setAsOccupied(ioc);
    }
    private void tryToRelease(Peripheral perif, IOCall ioc) throws IllegalAccessException {
        if (!perif.checkIfIsCurrentCall(ioc)) throw new IllegalAccessException(
                "ERROR: IO Call " + ioc + " tried to clear occupation of peripheral " + this + " but is not its current occupant!");
        perif.releaseOccupation();
    }

    public void step() {
        Iterator<IOCall> iterator = runningIOCalls.iterator();
        while (iterator.hasNext()) {
            IOCall ioc = iterator.next();
            if (ioc.remaining == 0) {
                ioc.done = true;
                ioc.active = false;
                for (Peripheral f : ioc.perifs)
                    try {
                        tryToRelease(f, ioc);
                    }
                    catch (IllegalAccessException e) {
                        System.err.println(e.getMessage());
                    }
                iterator.remove();
            }
            else ioc.remaining--;
        }
    }
}
