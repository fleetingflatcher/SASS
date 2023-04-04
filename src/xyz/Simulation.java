package xyz;

import xyz.exporter.Exporter;
import xyz.sched.UNIX;
import xyz.seqsrc.Dataset;
import xyz.seqsrc.Generator;
import xyz.seqsrc.Importer;
import xyz.proc.Process;
import xyz.proc.IOCall;
import xyz.sched.HighestResponseRatioNext;
import xyz.sched.Scheduler;
import xyz.scrb.Figures;
import xyz.scrb.Scoreboard;

import java.util.ArrayList;

public class Simulation {
    public SimulationSettings SIM_SETTINGS;
    public Scheduler scheduler;
    public Generator generator;
    public Importer importer;

    public IOHandler ioHandler;
    public Sequencer sequencer;
    public Scoreboard scoreboard;
    public Exporter exporter;
    public int time;

    public Simulation ()
    {
        SIM_SETTINGS = new SimulationSettings();
        this.generator = new Generator(this);
        this.importer = new Importer(this);
        this.sequencer = new Sequencer(this);
        this.ioHandler = new IOHandler(this);
        // INSERT DECISION TREE FOR SCHEDULING ALGORITHM HERE
        this.scheduler = new UNIX(this, SIM_SETTINGS.BATCH_SIZE(), SIM_SETTINGS.TIME_SLICE(), 2);
        this.scoreboard = new Scoreboard(this);
        this.exporter = new Exporter();
        time = 0;
    }
    public void setup () {
        ArrayList<Process> allProcesses = new ArrayList<>();

        if (SIM_SETTINGS.RANDOM_INPUT()) {
            ioHandler.setup(generator.generatePeripherals(SIM_SETTINGS.NUM_PERIFS()));
            allProcesses = generator.generateProcesses(SIM_SETTINGS.NUM_PROCESSES());
        }
        else {
            Dataset d = importer.importDataset();
            ioHandler.setup(d.peripherals);
            allProcesses = d.processes;
        }
        sequencer.setup(allProcesses);
        scoreboard.setup(allProcesses);
    }
    void run ()
    {
        while (!finished()) {

            /*
                Sequencer offers new jobs to scheduler.
             */
            scheduler.batchJobs.addJobs(sequencer.getJobsAtTimeT(time));

            /*
                Scheduler behavior.
             */
            scheduler.step();

            /*
                IO Handler trying to schedule IO Calls to Peripherals
             */
            for (IOCall ioc : scheduler.offerInactiveIOCs()) {
                ioHandler.requestResources(ioc);
            }
            ioHandler.step();

            if (time % 5000 == 0) {
                addFigs();
            }
            time++;
        }
        addTable();
        exporter.exportToCSV(scheduler.getClass().getName());
    }
    private void addFigs() {
        Figures figures = scoreboard.produceFigures();
        exporter.addFiguresToExport(time, finished(), figures);
    }
    private void addTable() {
        Figures figures = scoreboard.produceFigures();
        ArrayList<Process> finishedProcs = scoreboard.finishedProcesses();
        exporter.addTableToExport(time, finished(), figures, finishedProcs);
    }
    private boolean finished() {
        return sequencer.stimuliComplete() && scheduler.simulationComplete();
    }
}
