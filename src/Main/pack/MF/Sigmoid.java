package Main.pack.MF;

import java.util.TreeMap;

public class Sigmoid implements Function{
    private double a1;
    private double a2;
    private double c1;
    private double c2;

    private double l_border;
    private double r_border;

    public Sigmoid(double a1, double a2, double c1, double c2) {
        this.a1 = a1;
        this.a2 = a2;
        this.c1 = c1;
        this.c2 = c2;
    }

    private double sigmoid (double x) {
        return  (1 / (1 + Math.exp( -a1 * (x - c1)))) * (1 / (1 + Math.exp( -a2 * (x - c2))));
    }

    @Override
    public TreeMap<Double, Double> pointsList(double step) {
        TreeMap<Double, Double> map = new TreeMap<>();
        for (double i =  l_border; i <= r_border; i+=step) {
            map.put(i, sigmoid(i));
            //System.out.println("i = " + i + " m(x) = " + sigmoid(i));
        }

        return map;
    }

    @Override
    public double findLeftBorder() {
        double left_border = c2;  // x
        double pick_point = sigmoid(left_border); // m(x)

        while ( pick_point > 0.01) {
            left_border -= 0.5;
            pick_point = sigmoid(left_border);
        }

        System.out.println("left border = " + left_border);
        l_border = left_border;
        return left_border;
    }

    @Override
    public double findRightBorder() {
        double right_border = c2;  // x
        double pick_point = sigmoid(right_border); // m(x)

        while ( pick_point <= 0.9999) {
            right_border += 0.5;
            pick_point = sigmoid(right_border);
        }

        System.out.println("right border = " + right_border);
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
