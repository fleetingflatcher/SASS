package xyz.exporter;

import java.util.ArrayList;
import java.util.Collection;

public class Column {
    public Column (String name, Object[] values) {
        this.name = name;
        this.values = values;
        len = values.length;
    }
    public Column (String name, Collection<Object> values) {
        this.name = name;
        this.values = values.toArray();
        len = values.size();
    }

    final String name;
    final Object[] values;
    final int len;

    public String [] toStringArray() {
        ArrayList<String> list = new ArrayList<>();
        list.add(name);
        for (Object o : values) {
            list.add(o.toString());
        }
        String[] arr = new String[list.size()];
        list.toArray(arr);
        return arr;
    }
}
