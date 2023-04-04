package xyz.exporter;

import java.util.ArrayList;

public class Table {
    public Table () {
        columns = new ArrayList<>();
    }
    private ArrayList<Column> columns;
    private int maxLen = 0;
    public int getRowCount() {
        return maxLen;
    }
    public int getColumnCount() {
        return columns.size();
    }
    public String getValueAt(int row, int column) {
        if (columns.size() <= column) return "";
        if (columns.get(column).len <= row) return "";
        if (columns.get(column).values[row] == null) return "";
        return columns.get(column).values[row].toString();
    }
    public String getColumnName(int index) {
        return columns.get(index).name;
    }
    public void addColumn (Column column) {
        if (column.len > maxLen) maxLen = column.len;
        columns.add(column);
    }

    public String[][] toStringMatrix() {
        int num_cols = columns.size();
        int num_rows = columns.isEmpty() ? 0 : columns.get(0).len;
        String[][] matrix = new String[num_rows + 1][num_cols];
        for (int i = 0; i < num_cols; i++) {
            matrix[0][i] = columns.get(i).name;
            for (int j = 0; j < num_rows; j++) {
                matrix[j + 1][i] = columns.get(i).values[j].toString();
            }
        }
        return matrix;
    }


}
