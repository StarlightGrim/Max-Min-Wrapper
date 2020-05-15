package Main.pack.GUI;

import Main.pack.MF.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class GuiController {
    private final static double EPSILON = 0.0001;
    @FXML Label result_size;
    @FXML Label final_size;

    @FXML TableView<TablePair> data_table;
    @FXML TableColumn<TablePair, Double> x_column;
    @FXML TableColumn<TablePair, Double> mx_column;

    @FXML LineChart<Double, Double> graph;
    @FXML NumberAxis chartX;
    @FXML NumberAxis chartY;

    @FXML TextField c1_inputA;
    @FXML TextField c2_inputA;
    @FXML TextField a1_inputA;
    @FXML TextField a2_inputA;
    @FXML TextField b_inputA;

    @FXML TextField c1_inputB;
    @FXML TextField c2_inputB;
    @FXML TextField a1_inputB;
    @FXML TextField a2_inputB;
    @FXML TextField b_inputB;

    @FXML TextField steps_input;
    @FXML Label mA_x;
    @FXML Label mB_x;
    @FXML Label mC_x;

    @FXML ChoiceBox<String> operations_choice;
    private List<String> operations_list = Arrays.asList("Adding", "Subtracting",
            "Multiplying", "Dividing", "Maximum", "Minimum");
    private ObservableList<String> o_list = FXCollections.observableArrayList(operations_list);

    @FXML ChoiceBox<String> func_choice;
    private List<String> func_list = Arrays.asList("Triangular","Bilateral Gaussian MF", "Generalized bell MF",
            "The difference between the two sigmoid MFs", "Hemming and Euclidean distances");
    private ObservableList<String> list = FXCollections.observableArrayList(func_list);

    @FXML Button build_button;
    @FXML RadioButton full_radio;
    @FXML RadioButton envelope_radio;
    @FXML ColorPicker color_A;
    @FXML ColorPicker color_B;
    @FXML ColorPicker color_C;

    // for lab 7
    @FXML ColorPicker color_Hem;
    @FXML ColorPicker color_Evc;
    @FXML Button build_distance;
    @FXML TextField steps_distance;
    @FXML Label hem_distance;
    @FXML Label evc_distance;

    @FXML
    private void initialize() {
        func_choice.setItems(list);
        operations_choice.setItems(o_list);

        func_choice.setValue(list.get(0));
        operations_choice.setValue(o_list.get(0));

        color_A.setValue(Color.web("#34eb40"));
        color_B.setValue(Color.web("#eb3434"));
        color_C.setValue(Color.web("#3480eb"));
        color_Hem.setValue(Color.web("#e5eb4b"));
        color_Evc.setValue(Color.web("#ac47b5"));

        c1_inputA.setText("0"); c2_inputA.setText("0"); a1_inputA.setText("0"); a2_inputA.setText("0"); b_inputA.setText("0");
        c1_inputB.setText("0"); c2_inputB.setText("0"); a1_inputB.setText("0"); a2_inputB.setText("0"); b_inputB.setText("0");

        x_column.setCellValueFactory(new PropertyValueFactory<TablePair, Double>("x"));
        mx_column.setCellValueFactory(new PropertyValueFactory<TablePair, Double>("mx"));
    }

// #################################  UTILS METHODS  ########################################

    private void dialogRegMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    @FXML
    private void startBuild() {
        test();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static boolean equals(double a, double b){
        return a == b || Math.abs(a - b) < EPSILON;
    }

    private List<Pair<Double, Double>> calculateResult (TreeMap<Double, Double> A, TreeMap<Double, Double> B, String operations) {
        List<Pair<Double, Double>> result = new LinkedList<>();

        switch ( operations ) {
            case "Adding":
                A.forEach((x1, y1) -> {
                    B.forEach((x2, y2) -> {
                        result.add(new Pair<>(x1 + x2, Math.min(y1, y2)));
                    });
                });
                break;

            case "Subtracting":
                A.forEach((x1, y1) -> {
                    B.forEach((x2, y2) -> {
                        result.add(new Pair<>(x1 - x2, Math.min(y1, y2)));
                    });
                });
                break;

            case "Multiplying":
                A.forEach((x1, y1) -> {
                    B.forEach((x2, y2) -> {
                        result.add(new Pair<>(x1 * x2, Math.min(y1, y2)));
                    });
                });
                break;

            case "Dividing":
                A.forEach((x1, y1) -> {
                    B.forEach((x2, y2) -> {
                        result.add(new Pair<>(x1 / x2, Math.min(y1, y2)));
                    });
                });
                break;

            case "Maximum":
                A.forEach((x1, y1) -> {
                    B.forEach((x2, y2) -> {
                        result.add(new Pair<>(Math.max(x1, x2), Math.min(y1, y2)));
                    });
                });
                break;

            case "Minimum":
                A.forEach((x1, y1) -> {
                    B.forEach((x2, y2) -> {
                        result.add(new Pair<>(Math.min(x1, x2), Math.min(y1, y2)));
                    });
                });
                break;

            default:
                dialogRegMessage("Warning!","The empty list!",
                        "No items were added to the list. It seems that you did not select the desired operation!");
                break;
        }

        result_size.setText(""+result.size());

        System.out.println("------- final_list --------");
        result.sort(Comparator.comparing(Pair::getKey));
        result.forEach((x) -> System.out.println("x = " + x.getKey() + " y = " + x.getValue()));

        return result;
    }

    private TreeMap<Double, Double> createResultedMap (TreeMap<Double, Double> A, TreeMap<Double, Double> B) {
        List<Pair<Double, Double>> result = calculateResult(A, B, operations_choice.getValue()); // collection after math operations

        List<Double> key_list = new ArrayList<>(); // list with keys
        TreeMap<Double, Double> rounded = new TreeMap<>(); // final collection

        // pull keys in list
        result.forEach((x) -> key_list.add(round(x.getKey(),2)));

        // list with unique keys
        List<Double> uniqueKeys = key_list.stream().distinct().collect(Collectors.toList());
        //System.out.println(uniqueKeys);

        // create unique, sorted and rounded collection
        int iterator = 0;
        double X, Y;
        for (Pair<Double, Double> entry : result) {
            X = round(entry.getKey(), 2);
            Y = round(entry.getValue(), 4);

            if (X == uniqueKeys.get(iterator)) {
                if (rounded.containsKey(uniqueKeys.get(iterator)) ) {
                    if(Y > rounded.get(X)) {
                        rounded.remove(X);

                        X =  round(X, 2);
                        Y = round(Y, 4);

                        rounded.put(X, Y);
                    }
                }
                else rounded.put(X, Y);
            }
            else {
                iterator++;
                if (iterator != uniqueKeys.size()) {
                    if (rounded.containsKey(uniqueKeys.get(iterator)) ) {
                        if(Y > rounded.get(X)) {
                            rounded.remove(X);

                            X =  round(X, 2);
                            Y = round(Y, 4);

                            rounded.put(X, Y);
                        }
                    }
                    else rounded.put(X, Y);
                } else break;
            }
        }

        System.out.println("------- rounded --------");
        rounded.forEach((x, y) -> System.out.println("x = " + x + " y = " + y));

        final_size.setText(""+rounded.size());

        return rounded;
    }

    // m
    private int findCountSteps(Function main_func, Function func, double step) {
        return (int) (findLength(main_func) / findLengthStep(main_func, func, step));
    }

    // d
    private double findLength (Function func) {
        return func.findRightBorder() - func.findLeftBorder();
    }

    // delX
    private double findLengthStep(Function func1, Function func2, double step) {
        return round(Math.min(findLength(func1), findLength(func2) ) / step, 2);
    }

    private TreeMap<Double, Double> initFunc (Function func, double step, String func_name, ColorPicker picker) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        graph.getData().add(series);
        series.setName(func_name);

        TreeMap<Double, Double> map1 = func.pointsList(step);
        map1.forEach((x, y) -> {
            series.getData().add(new XYChart.Data<>(x, y));
        });

        setColor(series, picker);

        return map1;
    }

    private TreeMap<Double, Double> initResultFunc (TreeMap<Double, Double> map1, TreeMap<Double, Double> map2, ColorPicker picker) {
        TreeMap<Double, Double> C_result = createResultedMap(map1, map2);
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        graph.getData().add(series);
        series.setName("C function");
        C_result.forEach((x, y) -> {
            series.getData().add(new XYChart.Data<>(x, y));
        });

        setColor(series, picker);

        return C_result;
    }

    private void test () {
        graph.getData().clear();

        try {
            double a1 = Double.parseDouble(a1_inputA.getText());
            double a2 = Double.parseDouble(a2_inputA.getText());
            double c1 = Double.parseDouble(c1_inputA.getText());
            double c2 = Double.parseDouble(c2_inputA.getText());
            double b = Double.parseDouble(b_inputA.getText());

            double a1_ = Double.parseDouble(a1_inputB.getText());
            double a2_ = Double.parseDouble(a2_inputB.getText());
            double c1_ = Double.parseDouble(c1_inputB.getText());
            double c2_ = Double.parseDouble(c2_inputB.getText());
            double b_ = Double.parseDouble(b_inputB.getText());

            double step = Double.parseDouble(steps_input.getText());
            TreeMap<Double, Double> C_result = new TreeMap<>();

            switch (func_choice.getValue()) {
                case "Triangular":
                    Triangular tri1 = new Triangular(c2, a2, b);
                    Triangular tri2 = new Triangular(c2_, a2_, b_);
                    double this_step = findLengthStep(tri1, tri2, step);
                    TreeMap<Double, Double> A = initFunc(tri1, this_step, "A function", color_A);
                    TreeMap<Double, Double> B = initFunc(tri2, this_step, "B function", color_B);

                    C_result = initResultFunc(A, B, color_C);
                    break;

                case "Bilateral Gaussian MF":
                    BilGauss bil1 = new BilGauss(a1, a2, c1, c2);
                    BilGauss bil2 = new BilGauss(a1_, a2_, c1_, c2_);
                    double this_step1 = findLengthStep(bil1, bil2, step);
                    TreeMap<Double, Double> A1 = initFunc(bil1, this_step1, "A function", color_A);
                    TreeMap<Double, Double> B1 = initFunc(bil2, this_step1, "B function", color_B);

                    C_result = initResultFunc(A1, B1, color_C);
                    break;

                case "Generalized bell MF":
                    BellMF bell = new BellMF(a1, b, c1);
                    BellMF bell1 = new BellMF(a1_, b_, c1_);
                    double this_step2 = findLengthStep(bell, bell1, step);
                    TreeMap<Double, Double> A2 = initFunc(bell, this_step2, "A function", color_A);
                    TreeMap<Double, Double> B2 = initFunc(bell1, this_step2, "B function", color_B);

                    C_result = initResultFunc(A2, B2, color_C);
                    break;

                case "The difference between the two sigmoid MFs":
                    Sigmoid sigm = new Sigmoid(a1, a2, c1, c2);
                    Sigmoid sigm1 = new Sigmoid(a1_, a2_, c1_, c2_);
                    double this_step3 = findLengthStep(sigm, sigm1, step);
                    TreeMap<Double, Double> A3 = initFunc(sigm, this_step3, "A function", color_A);
                    TreeMap<Double, Double> B3 = initFunc(sigm1, this_step3, "B function", color_B);

                    C_result = initResultFunc(A3, B3, color_C);
                    break;

                default:
                    dialogRegMessage("Warning!", "Invalid function type!",
                            "It looks like you did not select a function or entered a wrong name! Please check the data.");
                    break;
            }

            graph.setCreateSymbols(false);

            data_table.setItems( new TablePair().createList(C_result));

        }
        catch (NumberFormatException e){
            dialogRegMessage("Warning!", "Something went wrong :(", "Please check the data you entered.");
        }

    }

    private void setColor(XYChart.Series series, ColorPicker picker) {
        String hex = "#" + Integer.toHexString(picker.getValue().hashCode());
        series.getNode().setStyle("-fx-stroke:" + hex + ";");
    }

}