package Main.pack.MF;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TablePair {
    private double x;
    private double mx;

    public TablePair () {};

    private TablePair(double x, double mx) {
        this.x = x;
        this.mx = mx;
    }

    public ObservableList<TablePair> createList (TreeMap<Double, Double> map) {
        List<TablePair> table_list = new ArrayList<>();
        map.forEach((x,y) -> table_list.add(new TablePair(x, y)));

        return FXCollections.observableArrayList(table_list);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getMx() {
        return mx;
    }

    public void setMx(double mx) {
        this.mx = mx;
    }
}
