package xyz.seqsrc;

import xyz.Simulation;
import xyz.perif.Peripheral;
import xyz.proc.IOCall;
import xyz.proc.Process;
import xyz.proc.Status;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Importer {
    public Importer(Simulation parent) { this.parent = parent; }
    private Simulation parent;

    String defaultFileName = "ex_inputData.ini";
    public Dataset importDataset() {
        return importDataset(defaultFileName);
    }

    public Dataset importDataset(String fileName) {
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
                            case "-s" -> { process.serviceTime = Integer.parseInt(tokens[i+1]); }
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
                            case "-d" -> { ioc.duration = Integer.parseInt(tokens[i+1]); }
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
