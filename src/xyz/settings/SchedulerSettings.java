package xyz.settings;

import xyz.sched.SchedulerType;

public class SchedulerSettings {
    public SchedulerSettings () {}
    public SchedulerSettings (
            SchedulerType type,
            int batchSize,
            int timeSlice
    ) {
        TYPE = type;
        BATCH_SIZE = batchSize;
        TIME_SLICE = timeSlice;
    }
    public SchedulerType TYPE;
    public int BATCH_SIZE = 0;
    public int TIME_SLICE = 0;
    public int UNIX_PRIORITY_LEVELS = 1;
}
