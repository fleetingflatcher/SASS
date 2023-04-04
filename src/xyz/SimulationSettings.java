package xyz;

public class SimulationSettings {
    /*
        Override fields. If set to zero, default value used.
        Updated by methods with the same names.
     */
    private boolean OR_RANDOM_INPUT = false;
    private int OR_GENERATOR_RAND_SEED = 0;
    private int OR_NUM_PROCESSES = 0;
    private int OR_NUM_PERIFS = 0;
    private int OR_STIMULUS_DURATION = 0;   //in seconds
    private int OR_MAX_ARRIVAL_TIME = 0;
    private int OR_MAX_SERVICE_TIME = 0;
    private int OR_MAX_PERIF_TIME = 0;
    private int OR_MAX_NUM_IOCS = 0;
    private float OR_IOC_PROPORTION = 0;
    private int OR_BATCH_SIZE = 0;
    private int OR_TIME_SLICE = 0;

    public boolean RANDOM_INPUT() { return OR_RANDOM_INPUT; }
    public int GENERATOR_RAND_SEED() { return OR_GENERATOR_RAND_SEED == 0 ? (int)java.time.Instant.now().getEpochSecond() : OR_GENERATOR_RAND_SEED; }
    public int NUM_PROCESSES() { return OR_NUM_PROCESSES == 0 ? 10 : OR_NUM_PROCESSES; }
    public int NUM_PERIFS() { return OR_NUM_PERIFS == 0 ? 1 : OR_NUM_PERIFS; }
    public int STIMULUS_DURATION() { return OR_STIMULUS_DURATION == 0 ? 20 : OR_STIMULUS_DURATION; }
    public int MAX_ARRIVAL_TIME () { return OR_MAX_ARRIVAL_TIME == 0 ? STIMULUS_DURATION(): OR_MAX_ARRIVAL_TIME; }
    public int MAX_SERVICE_TIME () { return OR_MAX_SERVICE_TIME == 0 ? STIMULUS_DURATION()  : OR_MAX_SERVICE_TIME; }
    public int MAX_PERIF_TIME () { return OR_MAX_PERIF_TIME == 0 ? STIMULUS_DURATION()  : OR_MAX_PERIF_TIME; }
    public int MAX_NUM_IOCS () { return OR_MAX_NUM_IOCS == 0 ? 1 : OR_MAX_NUM_IOCS; }
    public float IOC_PROPORTION() { return OR_IOC_PROPORTION == 0 ? 0.66f : OR_IOC_PROPORTION; }
    public int BATCH_SIZE() { return OR_BATCH_SIZE == 0 ? 10 : OR_BATCH_SIZE; }
    public int TIME_SLICE() { return OR_TIME_SLICE == 0 ? 1000 : OR_TIME_SLICE; }

    /*
        Override methods. Setters for the Override fields above.
     */
    public void OR_RANDOM_INPUT (boolean b) { OR_RANDOM_INPUT = b; }
    public void OR_NUM_PROCESSES (int i) { OR_NUM_PROCESSES = i; }
    public void OR_NUM_PERIFS (int i) { OR_NUM_PERIFS = i; }
    public void OR_GENERATOR_RAND_SEED (int i) { OR_GENERATOR_RAND_SEED = i; }
    public void OR_STIMULUS_DURATION (int i) { OR_STIMULUS_DURATION = i; }
    public void OR_MAX_ARRIVAL_TIME (int i) { OR_MAX_ARRIVAL_TIME = i; }
    public void OR_MAX_SERVICE_TIME (int i) { OR_MAX_SERVICE_TIME = i; }
    public void OR_MAX_PERIF_TIME (int i) { OR_MAX_PERIF_TIME = i; }
    public void OR_MAX_NUM_IOCS (int i) { OR_MAX_NUM_IOCS = i; }
    public void OR_IOC_PROPORTION (float f) { OR_IOC_PROPORTION = f; }
    public void OR_BATCH_SIZE (int i) { OR_BATCH_SIZE = i; }
    public void OR_TIME_SLICE (int i) { OR_TIME_SLICE = i; }

    /*
        Override clear methods. Clears a specific override.
     */
    public void CLR_NUM_PROCESSES() { OR_NUM_PROCESSES = 0; }
    public void CLR_NUM_PERIFS() { OR_NUM_PERIFS = 0; }
    public void CLR_GENERATOR_RAND_SEED () { OR_GENERATOR_RAND_SEED = 0; }
    public void CLR_STIMULUS_DURATION() { OR_STIMULUS_DURATION = 0; }
    public void CLR_MAX_ARRIVAL_TIME () { OR_MAX_ARRIVAL_TIME = 0; }
    public void CLR_MAX_SERVICE_TIME () { OR_MAX_SERVICE_TIME = 0; }
    public void CLR_MAX_PERIF_TIME () { OR_MAX_PERIF_TIME = 0; }
    public void CLR_MAX_NUM_IOCS () { OR_MAX_NUM_IOCS = 0; }
    public void CLR_IOC_PROPORTION () { OR_IOC_PROPORTION = 0; }
    public void CLR_BATCH_SIZE () { OR_BATCH_SIZE = 0; }
    public void CLR_TIME_SLICE () { OR_TIME_SLICE = 0; }
}
