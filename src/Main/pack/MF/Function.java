package Main.pack.MF;

import java.util.TreeMap;

public interface Function {
    TreeMap<Double, Double> pointsList (double step);
    double function (double x);
    double findLeftBorder ();
    double findRightBorder ();
    double getLBorder ();
    double getRBorder ();

}
