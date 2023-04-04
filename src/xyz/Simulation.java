package xyz;

import xyz.exporter.Exporter;
import xyz.sched.*;
import xyz.seqsrc.Dataset;
import xyz.seqsrc.Generator;
import xyz.seqsrc.Importer;
import xyz.proc.Process;
import xyz.proc.IOCall;
import xyz.scrb.Figures;
import xyz.scrb.Scoreboard;
import xyz.settings.SimulationSettings;

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
        this.importer = new Importer(this);
        SIM_SETTINGS = importer.importSettings();

        this.generator = new Generator(this);
        this.sequencer = new Sequencer(this);
        this.ioHandler = new IOHandler(this);
        switch (SIM_SETTINGS.SCH.TYPE) {
            case FirstComeFirstServed:
                this.scheduler = new FirstComeFirstServed(this);
                break;
            case ShortestJobFirst:
                this.scheduler = new ShortestJobFirst(this);
                break;
            case ShortestRemainingTime:
                this.scheduler = new ShortestRemainingTime(this, SIM_SETTINGS.SCH.TIME_SLICE);
                break;
            case RoundRobin:
                this.scheduler = new RoundRobin(this, SIM_SETTINGS.SCH.TIME_SLICE);
                break;
            case HighestResponseRatioNext:
                this.scheduler = new HighestResponseRatioNext(this);
            case UNIX:
                this.scheduler = new UNIX(this, SIM_SETTINGS.SCH.TIME_SLICE, SIM_SETTINGS.SCH.UNIX_PRIORITY_LEVELS);
                break;
        }

        this.scoreboard = new Scoreboard(this);
        this.exporter = new Exporter(this);

        ArrayList<Process> allProcesses;
        if (SIM_SETTINGS.GEN.RANDOM_INPUT) {
            ioHandler.setup(generator.generatePeripherals(SIM_SETTINGS.IOH.NUM_PERIFS));
            allProcesses = generator.generateProcesses(SIM_SETTINGS.GEN.NUM_PROCESSES);
        }
        else {
            Dataset d = importer.importDataset();
            ioHandler.setup(d.peripherals);
            allProcesses = d.processes;
        }
        sequencer.setup(allProcesses);
        scoreboard.setup(allProcesses);
        time = 0;

        exporter.addSettingsToExport();
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

            if (time % SIM_SETTINGS.OUT.FREQUENCY == 0 && time > 0) {
                if (SIM_SETTINGS.OUT.PROCESS_TABLE) addTable();
                else addFigs();
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
