package xyz.settings;

public class GeneratorSettings {
    public GeneratorSettings () {}
    public GeneratorSettings(boolean isOn) { RANDOM_INPUT = isOn; }
    public GeneratorSettings(
            boolean isOn,
            int seed,
            int stimulusDuration,
            int maxArrive,
            int maxService,
            int numProcesses
    ) {
        RANDOM_INPUT = isOn;
        RANDOM_SEED = seed;
        STIMULUS_DURATION = stimulusDuration;
        MAX_ARRIVAL_TIME = maxArrive;
        MAX_SERVICE_TIME = maxService;
        NUM_PROCESSES = numProcesses;
    }
    public boolean RANDOM_INPUT = false;
    public int RANDOM_SEED = (int)java.time.Instant.now().toEpochMilli();
    public int MAX_ARRIVAL_TIME = 0;
    public int MAX_SERVICE_TIME = 0;
    public int STIMULUS_DURATION = 0;
    public int NUM_PROCESSES = 0;
}
