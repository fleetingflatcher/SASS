package xyz;

import xyz.log.*;


public class Test {
    static StdLog std = new StdLog("Main Log", LogPriority.HIGH);



    public static void main (String[] args) {

        Simulation sim = new Simulation();
        sim.SIM_SETTINGS.OR_RANDOM_INPUT(true);
        sim.SIM_SETTINGS.OR_GENERATOR_RAND_SEED(3);
        sim.SIM_SETTINGS.OR_STIMULUS_DURATION(2500);
        sim.SIM_SETTINGS.OR_NUM_PROCESSES(1000);
        sim.SIM_SETTINGS.OR_MAX_SERVICE_TIME(250);
        sim.SIM_SETTINGS.OR_MAX_NUM_IOCS(3);
        sim.SIM_SETTINGS.OR_NUM_PERIFS(7);
        sim.SIM_SETTINGS.OR_MAX_PERIF_TIME(100);
        sim.SIM_SETTINGS.OR_IOC_PROPORTION(0.9f);

        sim.setup();
        sim.run();
    }

}
