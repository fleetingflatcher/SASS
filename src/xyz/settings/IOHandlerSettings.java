package xyz.settings;

public class IOHandlerSettings {
    public IOHandlerSettings () {}

    public IOHandlerSettings (
            int numPerifs,
            int maxIOTime,
            int maxNumIOCPerProcess,
            float proportion
    ) {
        NUM_PERIFS = numPerifs;
        MAX_IO_TIME = maxIOTime;
        MAX_NUM_IOC_PER_PROCESS = maxNumIOCPerProcess;
        PROPORTION = proportion;
    }

    public int NUM_PERIFS = 0;
    public int MAX_IO_TIME = 0;
    public int MAX_NUM_IOC_PER_PROCESS = 0;
    public float PROPORTION = 0;
}
