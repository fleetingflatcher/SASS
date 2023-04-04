package xyz;

import xyz.seqsrc.Dataset;
import xyz.seqsrc.Importer;

public class ImportTest {
    public static void main(String[] args) {
        Importer importer = new Importer(new Simulation());

        Dataset d = importer.importDataset("given_inputData.ini");
        int i = 0;
    }
}
