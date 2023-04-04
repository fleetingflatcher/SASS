package xyz.settings;

public class SimulationSettings {
    public SimulationSettings () {
        GEN = new GeneratorSettings();
        IOH = new IOHandlerSettings();
        SCH = new SchedulerSettings();
        OUT = new OutputSettings();
    }
    public OutputSettings OUT;
    public GeneratorSettings GEN;
    public IOHandlerSettings IOH;
    public SchedulerSettings SCH;
}