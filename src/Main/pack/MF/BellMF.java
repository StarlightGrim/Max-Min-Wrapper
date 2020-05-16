package Main.pack.MF;

import java.util.TreeMap;

public class BellMF implements Function {
    private double a;
    private double b;
    private double c;

    private double l_border;
    private double r_border;

    public BellMF (double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double function (double x) {
        return 1 / ( 1+ Math.pow( Math.abs( (x - c) / a) , 2*b) );
    }

    @Override
    public TreeMap<Double, Double> pointsList(double step) {
        TreeMap<Double, Double> map = new TreeMap<>();
        for (double i =  l_border; i <= r_border; i+=step) {
            map.put(i, function(i));
        }

        return map;
    }

    @Override
    public double findLeftBorder() {
        double left_border = c;  // x
        double pick_point = function(left_border); // m(x)

        while ( pick_point > 0.02) {
            left_border -= 0.5;
            pick_point = function(left_border);
        }

        l_border = left_border;
        return left_border;
    }

    @Override
    public double findRightBorder() {
        double right_border = c;  // x
        double pick_point = function(right_border); // m(x)

        while ( pick_point > 0.02) {
            right_border += 0.5;
            pick_point = function(right_border);
        }

        r_border = right_border;
        return right_border;
    }

    @Override
    public double getLBorder() {
        return l_border;
    }

    @Override
    public double getRBorder() {
        return r_border;
    }
}
