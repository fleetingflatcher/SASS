package xyz;

public abstract class SimulationElement {
    protected SimulationElement (Simulation parent) {
        this.parent = parent;
    }
    protected Simulation parent;
}
