package xyz.exporter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import com.opencsv.CSVWriter;
import xyz.proc.Process;
import xyz.scrb.Figures;

public class Exporter {
    public Exporter() {
        exportList = new ArrayList<>();
    }
    private ArrayList<Table> exportList;

    public Table addFiguresToExport (int time, boolean isFinished, Figures figures) {

        ArrayList<Object> genDataHeadersArr = new ArrayList<>();
        ArrayList<Object> genDataArr = new ArrayList<>();

        genDataHeadersArr.add("Timestamp:");
        genDataArr.add(java.time.LocalDateTime.now().toString());

        genDataHeadersArr.add("Simulation Time (s):");
        genDataArr.add(((float)time)/1000);

        genDataHeadersArr.add("Finished:");
        genDataArr.add(isFinished);

        genDataHeadersArr.add("# of Processes Finished:");
        genDataArr.add(figures.numberOfFinishedProcesses);

        genDataHeadersArr.add("Throughput (proc/s):");
        genDataArr.add(figures.throughput);

        genDataHeadersArr.add("Average Responsiveness (s):");
        genDataArr.add(figures.averageResponseTime);

        genDataHeadersArr.add("Average Turnaround (s):");
        genDataArr.add(figures.averageTat);

        genDataHeadersArr.add("Average TAT-to-Service Ratio:");
        genDataArr.add(figures.tatToServiceTimeRatio);

        Column genDataHeadersCol = new Column ("Info", genDataHeadersArr);
        Column genDataCol = new Column("", genDataArr);

        Table t = new Table();
        t.addColumn(genDataHeadersCol);
        t.addColumn(genDataCol);
        exportList.add(t);
        return t;
    }
    public Table addTableToExport (int time, boolean isFinished, Figures figures, ArrayList<Process> finishedProcessList) {
        ArrayList<Object> genDataHeadersArr = new ArrayList<>();
        ArrayList<Object> genDataArr = new ArrayList<>();

        genDataHeadersArr.add("Timestamp:");
        genDataArr.add(java.time.LocalDateTime.now().toString());

        genDataHeadersArr.add("Simulation Time (s):");
        genDataArr.add(((float)time)/1000);

        genDataHeadersArr.add("Finished:");
        genDataArr.add(isFinished);

        genDataHeadersArr.add("# of Processes Finished:");
        genDataArr.add(finishedProcessList.size());

        genDataHeadersArr.add("Throughput (proc/s):");
        genDataArr.add(figures.throughput);

        genDataHeadersArr.add("Average Responsiveness (s):");
        genDataArr.add(figures.averageResponseTime);

        genDataHeadersArr.add("Average Turnaround (s):");
        genDataArr.add(figures.averageTat);

        genDataHeadersArr.add("Average TAT-to-Service Ratio:");
        genDataArr.add(figures.tatToServiceTimeRatio);

        ArrayList<Object> pids = new ArrayList<>();
        ArrayList<Object> statuses = new ArrayList<>();
        ArrayList<Object> arrivalTimes = new ArrayList<>();
        ArrayList<Object> runTimes = new ArrayList<>();
        ArrayList<Object> waitTimes = new ArrayList<>();

        ArrayList<Object> batchTimes = new ArrayList<>();
        ArrayList<Object> readyTimes = new ArrayList<>();
        ArrayList<Object> blockedTimes = new ArrayList<>();
        ArrayList<Object> activeIOTimes = new ArrayList<>();

        ArrayList<Object> expectedTimes = new ArrayList<>();
        ArrayList<Object> finishTimes = new ArrayList<>();
        ArrayList<Object> responseTimes = new ArrayList<>();
        ArrayList<Object> turnaroundTimes = new ArrayList<>();
        ArrayList<Object> serviceTimes = new ArrayList<>();
        ArrayList<Object> tatServiceRatios = new ArrayList<>();
        LinkedList<Object> ioCalls = new LinkedList<>();

        for (Process p : finishedProcessList) {
            pids.add (p.pid);
            statuses.add (p.getStatus());
            arrivalTimes.add(       ((float) p.arrivalTime)/1000);
            runTimes.add(           ((float) p.runTime)/1000);
            waitTimes.add(          ((float) p.waitTime())/1000);

            batchTimes.add(((float) p.timeInBatchQueue)/1000);
            readyTimes.add(((float) p.timeInReadyQueue)/1000);
            blockedTimes.add(((float)p.timeInBlockedQueue)/1000);
            activeIOTimes.add(         ((float) p.timeSpentInActiveIO)/1000);

            expectedTimes.add(      ((float) p.expectedServiceTime)/1000);
            finishTimes.add(        ((float) p.finishTime)/1000);
            responseTimes.add(      ((float) p.responseTime)/1000);
            turnaroundTimes.add(    ((float) p.turnaroundTime)/1000);
            serviceTimes.add(       ((float) p.serviceTime)/1000);
            tatServiceRatios.add(           p.tatServiceRatio);

            ioCalls.add(p.originalIOCQueue);
        }

        Column genDataHeaders = new Column("Info", genDataHeadersArr);
        Column genData = new Column("", genDataArr);
        Column pidsCol = new Column("Process ID", pids);
        Column statusesCol = new Column ("Status", statuses);
        Column arrivalTimesCol = new Column("Arrival Times (s)", arrivalTimes);
        Column runTimesCol = new Column("Run Times (s)", runTimes);
        Column waitTimesCol = new Column("Wait Times (s)", waitTimes);

        Column batchTimesCol = new Column("Batch Queue Time (s)", batchTimes);
        Column readyTimesCol = new Column("Ready Queue Time (s)", readyTimes);
        Column blockedTimesCol = new Column("Blocked Queue Time (s)", blockedTimes);
        Column activeIOTimesCol = new Column("Active IO Time (s)", activeIOTimes);

        Column expectedTimesCol = new Column("Expected Service Times (s)", expectedTimes);
        Column finishTimesCol = new Column("Finish Times (s)", finishTimes);
        Column responseTimesCol = new Column("Response Times (s)", responseTimes);
        Column turnaroundTimeCol = new Column("Turnaround Times (s)", turnaroundTimes);
        Column serviceTimeCol = new Column("Service Times (s)", serviceTimes);
        Column tatServiceRatiosCol = new Column("TAT Service Ratios", tatServiceRatios);

        Column ioCallsCol = new Column("IO Calls (ms)", ioCalls);

        Table table = new Table();
        table.addColumn(genDataHeaders);
        table.addColumn(genData);
        table.addColumn(new Column("", new ArrayList<>()));
        table.addColumn(pidsCol);
        table.addColumn(statusesCol);
        table.addColumn(arrivalTimesCol);
        table.addColumn(serviceTimeCol);
        //table.addColumn(runTimesCol);
        table.addColumn(waitTimesCol);

        table.addColumn(batchTimesCol);
        table.addColumn(readyTimesCol);
        table.addColumn(blockedTimesCol);
        table.addColumn(activeIOTimesCol);

        table.addColumn(expectedTimesCol);
        table.addColumn(finishTimesCol);
        table.addColumn(responseTimesCol);
        table.addColumn(turnaroundTimeCol);

        table.addColumn(tatServiceRatiosCol);

        table.addColumn(ioCallsCol);
        exportList.add(table);
        return table;
    }

    private String[][] prepareExportStringMatrix () {
        int rowCount = 0;
        int columnCount = 0;
        for (Table t : exportList) {
            rowCount += t.getRowCount() + 1;
            columnCount = Math.max(columnCount, t.getColumnCount());
        }
        String[][] result = new String[rowCount][columnCount];
        int rowIndex = 0;
        for (Table t : exportList) {
            for (int colIndex = 0; colIndex < t.getColumnCount(); colIndex++) {
                String columnName = t.getColumnName(colIndex);
                result[rowIndex][colIndex] = columnName;
                for (int i = 0; i < t.getRowCount(); i++) {
                    result[rowIndex + i + 1][colIndex] = t.getValueAt(i, colIndex);
                }
            }
            rowIndex += t.getRowCount() + 1;
        }
        return result;
    }
    private String prepareFilename(String tag) {
        LocalDateTime ldt = java.time.LocalDateTime.now();
        int year = ldt.getYear() % 100;
        int month = ldt.getMonth().getValue();
        int day = ldt.getDayOfMonth();
        int hour = ldt.getHour();
        int minute = ldt.getMinute();
        int second = ldt.getSecond();

        StringBuilder sb = new StringBuilder();
        sb.append(tag);
        sb.append('_');
        sb.append(day);
        sb.append(month);
        sb.append(year);
        sb.append('_');
        sb.append(hour);
        sb.append(minute);
        sb.append(second);
        sb.append(".csv");
        return sb.toString();
    }
    public void exportToCSV(String tag) {
        String fileName = prepareFilename(tag);
        String[][] data = prepareExportStringMatrix();
        try
        {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            for (String[] row : data) {
                writer.writeNext(row);
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
