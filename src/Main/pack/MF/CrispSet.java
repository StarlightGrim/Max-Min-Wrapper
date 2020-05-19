package Main.pack.MF;

import java.util.TreeMap;

public class CrispSet implements Function {

    public CrispSet () {

    }

    public double crispFunction(double y) {
        if (y < 0.5 || y == 0.5) {
            return 0.0;
        }
        else return 1.0;
    }

    public TreeMap<Double, Double> getList(TreeMap<Double, Double> map) {
        TreeMap<Double, Double> crispList = new TreeMap<>();

        map.forEach((x, y) -> {
            if (y < 0.5 || y == 0.5) {
                crispList.put(x, 0.0);
            }
            else crispList.put(x, 1.0);
        });

        return crispList;
    }

    @Override
    public TreeMap<Double, Double> pointsList(double step) {
        return null;
    }

    @Override
    public double function(double x) {
        return 0;
    }

    @Override
    public double findLeftBorder() {
        return 0;
    }

    @Override
    public double findRightBorder() {
        return 0;
    }

    @Override
    public double getLBorder() {
        return 0;
    }

    @Override
    public double getRBorder() {
        return 0;
    }
}
