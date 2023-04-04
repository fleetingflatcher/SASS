package xyz.seqsrc;

import xyz.Simulation;
import xyz.SimulationElement;
import xyz.sched.SchedulerType;
import xyz.settings.SimulationSettings;
import xyz.perif.Peripheral;
import xyz.proc.IOCall;
import xyz.proc.Process;
import xyz.proc.Status;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Importer extends SimulationElement {
    public Importer(Simulation parent) { super(parent); }

    String defaultFileName = "ex_inputData.ini";
    String settingsFileName = "settings.ini";

    public SimulationSettings importSettings ()
    {
        SimulationSettings settings = new SimulationSettings();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(settingsFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("//") || line.isEmpty()) continue;
                if (line.startsWith("~")) continue;
                String[] tokens = line.split(" ");
                switch (tokens[0]) {
                    case "type" -> {
                        switch (tokens[1]) {
                            case "FirstComeFirstServed": settings.SCH.TYPE = SchedulerType.FirstComeFirstServed; break;
                            case "RoundRobin": settings.SCH.TYPE = SchedulerType.RoundRobin; break;
                            case "ShortestJobFirst": settings.SCH.TYPE = SchedulerType.ShortestJobFirst; break;
                            case "ShortestRemainingTime": settings.SCH.TYPE = SchedulerType.ShortestRemainingTime; break;
                            case "HighestResponseRatioNext": settings.SCH.TYPE = SchedulerType.HighestResponseRatioNext; break;
                            case "UNIX": settings.SCH.TYPE = SchedulerType.UNIX; break;
                        }
                    }
                    case "batchSize" -> { settings.SCH.BATCH_SIZE = Integer.parseInt(tokens[1]); }
                    case "timeSlice" -> { settings.SCH.TIME_SLICE = Integer.parseInt(tokens[1]); }
                    case "unixPriorityLevels" -> { settings.SCH.UNIX_PRIORITY_LEVELS = Integer.parseInt(tokens[1]); }

                    case "random" -> { settings.GEN.RANDOM_INPUT = Boolean.parseBoolean(tokens[1]); }
                    case "seed" -> { if (!tokens[1].equals("off")) settings.GEN.RANDOM_SEED = Integer.parseInt(tokens[1]); }
                    case "stimulusDuration" -> { settings.GEN.STIMULUS_DURATION = Integer.parseInt(tokens[1]); }
                    case "maxArrive" -> { settings.GEN.MAX_ARRIVAL_TIME = Integer.parseInt(tokens[1]); }
                    case "maxService" -> { settings.GEN.MAX_SERVICE_TIME = Integer.parseInt(tokens[1]); }
                    case "numProcesses" -> { settings.GEN.NUM_PROCESSES = Integer.parseInt(tokens[1]); }

                    case "numPerifs" -> { settings.IOH.NUM_PERIFS = Integer.parseInt(tokens[1]); }
                    case "maxIoTime" -> { settings.IOH.MAX_IO_TIME = Integer.parseInt(tokens[1]); }
                    case "maxNumCallsPerProcess" -> { settings.IOH.MAX_NUM_IOC_PER_PROCESS = Integer.parseInt(tokens[1]); }
                    case "proportion" -> { settings.IOH.PROPORTION = Float.parseFloat(tokens[1]); }

                    case "frequency" -> { settings.OUT.FREQUENCY = Integer.parseInt(tokens[1]); }
                    case "processTable" -> { settings.OUT.PROCESS_TABLE = Boolean.parseBoolean(tokens[1]); }
                    }
            }
        }
        catch (Exception e) { }
        return settings;
    }

    public Dataset importDataset() {
        return importDataset(defaultFileName);
    }

    public Dataset importDataset(String fileName)
    {
        ArrayList<Peripheral> peripherals = new ArrayList<>();
        ArrayList<Process> processes = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            Process process = null;
            IOCall ioc = null;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("//") || line.isEmpty()) continue;
                String[] tokens = line.split(" ");
                if (tokens[0].equals("proc")) {
                    if (process != null) throw new ImportIllegibleException();
                    process = new Process();
                    process.setStatus(Status.NULL);
                    for (int i = 1; i < tokens.length; i+=2) {
                        switch(tokens[i]) {
                            case "-a" -> { process.arrivalTime = Integer.parseInt(tokens[i+1]); }
                            case "-s" -> {
                                process.serviceTime = Integer.parseInt(tokens[i+1]);
                            }
                            case "-e" -> { process.expectedServiceTime = Integer.parseInt(tokens[i+1]); }
                            case "-p" -> { process.basePriority = Integer.parseInt(tokens[i+1]); }
                            case "-io" -> { process.hasIO = true; }
                        }
                    }
                    if (!process.hasIO) {
                        processes.add(process);
                        process = null;
                    }
                }
                else if (tokens[0].equals("\tioc")) {
                    if (process == null) throw new ImportIllegibleException();
                    if (process.IOCQueue == null) {
                        process.hasIO = true;
                        process.IOCQueue = new LinkedList<>();
                    }
                    ioc = new IOCall(process);
                    for (int i = 1; i < tokens.length; i += 2) {
                        switch(tokens[i]) {
                            case "-b" -> { ioc.start = Integer.parseInt(tokens[i+1]); }
                            case "-d" -> {
                                ioc.duration = Integer.parseInt(tokens[i+1]);
                                ioc.remaining = ioc.duration;
                            }
                            case "-f" -> {
                                ioc.perifs = new ArrayList<>();
                                int j = 1;
                                while (!tokens[i+j].equals("."))
                                {
                                    ioc.perifs.add(peripherals.get(Integer.parseInt(tokens[i+j])));
                                    ++j;
                                }
                            }
                        }
                    }
                    process.IOCQueue.add(ioc);
                }
                else if (tokens[0].equals("done")) {
                    if (process.hasIO) {
                        ArrayList<IOCall> arr = new ArrayList<>();
                        arr.addAll(process.IOCQueue);
                        process.IOCQueue.clear();

                        Collections.sort(arr);
                        process.IOCQueue.addAll(arr);
                    }
                    processes.add(process);
                    process = null;
                }
                else if (tokens[0].equals("perif")) {
                    int num = Integer.parseInt(tokens[2]);
                    peripherals = new ArrayList<>();
                    for (int i = 0; i < num; ++i) {
                        peripherals.add(new Peripheral());
                    }
                }
                else throw new ImportIllegibleException();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Dataset(processes, peripherals);
    }

}
