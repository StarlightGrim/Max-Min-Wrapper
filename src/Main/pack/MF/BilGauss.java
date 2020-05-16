package Main.pack.MF;

import java.util.TreeMap;

public class BilGauss implements Function{
    private double a1;
    private double a2;
    private double c1;
    private double c2;

    private double l_border;
    private double r_border;

    public BilGauss (double a1, double a2, double c1, double c2) {
        this.a1 = a1;
        this.a2 = a2;
        this.c1 = c1;
        this.c2 = c2;
    }

    private double exponenta (double x, double c, double a) {
        return Math.exp( - ( Math.pow(x - c, 2) / (2 * a*a )));
    }

    @Override
    public double function (double x) {
        if (c1 < c2) {
            if (x < c1) return exponenta(x, c1, a1);
            else if (x  > c2) return exponenta(x, c2, a2);
            else return 1;
        }
        else if (c1 > c2) {
            if (x < c1) return exponenta(x, c1, a1);
            else if (x  > c2) return exponenta(x, c2, a2);
            else return exponenta(x, c1, a1) * exponenta(x, c2, a2);
        }
        return 1;
    }

    @Override
    public TreeMap<Double, Double> pointsList (double step) {
        TreeMap<Double, Double> map = new TreeMap<>();
        for (double i =  l_border; i <= r_border; i+=step) {
            map.put(i, function(i));
        }

        return map;
    }

    @Override
    public double findLeftBorder () {
        double left_border = c1;  // x
        double pick_point = function(left_border); // m(x)

        while ( pick_point > 0.05) {
            left_border -= 0.5;
            pick_point = function(left_border);
        }

        l_border = left_border;
        return left_border;
    }

    @Override
    public double findRightBorder () {
        double right_border = c2;  // x
        double pick_point = function(right_border); // m(x)

        while ( pick_point > 0.05) {
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
