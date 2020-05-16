package Main.pack.MF;

import java.util.TreeMap;

public class Triangular implements Function {
    private double a1;
    private double a2;
    private double a3;

    public Triangular(double a1, double a2, double a3) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
    }

    @Override
    public double function(double x) {
        if (a1 < x && x <= a2) {
            return (x - a1) / (a2 - a1);
        }
        else if (a2 < x && x < a3) {
            return (a3 - x) / (a3 - a2);
        }
        else return 0;
    }

    @Override
    public TreeMap<Double, Double> pointsList(double step) {
        TreeMap<Double, Double> map = new TreeMap<>();
        for (double i = a1 ; i <= a3; i+=step) {
            map.put(i, function(i));
        }

        map.put(a3, 0d);

        //map.forEach((x,y) -> System.out.println("x = " + x + " y = " + y));
        return map;
    }

    @Override
    public double findLeftBorder() {
        return a1;
    }

    @Override
    public double findRightBorder() {
        return a3;
    }

    @Override
    public double getLBorder() {
        return a1;
    }

    @Override
    public double getRBorder() {
        return a3;
    }
}
