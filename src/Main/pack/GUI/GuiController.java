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
import java.util.concurrent.atomic.AtomicReference;
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
    private List<String> operations_list = Arrays.asList("Adding", "Subtracting", "Multiplying",
            "Dividing", "Maximum", "Minimum", "Hemming and Euclidean distances", "Fuzzy Indexes");
    private ObservableList<String> o_list = FXCollections.observableArrayList(operations_list);

    @FXML ChoiceBox<String> func_choice;
    private List<String> func_list = Arrays.asList("Triangular","Bilateral Gaussian MF", "Generalized bell MF",
            "The difference between the two sigmoid MFs");
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
    @FXML TextField steps_distance;
    @FXML Label hem_distance;
    @FXML Label evc_distance;
    @FXML Label hem_distance1;
    @FXML Label evc_distance1;

    // for lab 8
    @FXML Label linear_index;
    @FXML Label quadratic_index;
    @FXML Label linear_index1;
    @FXML Label quadratic_index1;
    @FXML RadioButton a_radio_choice;
    @FXML RadioButton b_radio_choice;
    @FXML RadioButton both_radio_choice;

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

        x_column.setCellValueFactory(new PropertyValueFactory<>("x"));
        mx_column.setCellValueFactory(new PropertyValueFactory<>("mx"));

        a_radio_choice.selectedProperty().set(true);
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
        buildGraphics();
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
                A.forEach((x1, y1) -> B.forEach((x2, y2) -> result.add(new Pair<>(x1 + x2, Math.min(y1, y2)))));
                break;

            case "Subtracting":
                A.forEach((x1, y1) -> B.forEach((x2, y2) -> result.add(new Pair<>(x1 - x2, Math.min(y1, y2)))));
                break;

            case "Multiplying":
                A.forEach((x1, y1) -> B.forEach((x2, y2) -> result.add(new Pair<>(x1 * x2, Math.min(y1, y2)))));
                break;

            case "Dividing":
                A.forEach((x1, y1) -> B.forEach((x2, y2) -> result.add(new Pair<>(x1 / x2, Math.min(y1, y2)))));
                break;

            case "Maximum":
                A.forEach((x1, y1) -> B.forEach((x2, y2) -> result.add(new Pair<>(Math.max(x1, x2), Math.min(y1, y2)))));
                break;

            case "Minimum":
                A.forEach((x1, y1) -> B.forEach((x2, y2) -> result.add(new Pair<>(Math.min(x1, x2), Math.min(y1, y2)))));
                break;

            default:
                dialogRegMessage("Warning!","The empty list!",
                        "No items were added to the list. It seems that you did not select the desired operation!");
                break;
        }

        result_size.setText(""+result.size());

        //System.out.println("------- final_list --------");
        result.sort(Comparator.comparing(Pair::getKey));
        //result.forEach((x) -> System.out.println("x = " + x.getKey() + " y = " + x.getValue()));

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

        //System.out.println("------- rounded --------");
        //rounded.forEach((x, y) -> System.out.println("x = " + x + " y = " + y));

        final_size.setText(""+rounded.size());

        return rounded;
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
        map1.forEach((x, y) -> series.getData().add(new XYChart.Data<>(x, y)));

        setColor(series, picker);

        return map1;
    }

    private TreeMap<Double, Double> initResultFunc (TreeMap<Double, Double> map1, TreeMap<Double, Double> map2, ColorPicker picker) {
        TreeMap<Double, Double> C_result = createResultedMap(map1, map2);
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        graph.getData().add(series);
        series.setName("C function");
        C_result.forEach((x, y) -> series.getData().add(new XYChart.Data<>(x, y)));

        setColor(series, picker);

        return C_result;
    }

    //############################################  METHODS FOR 7 LAB ##################################################

    // create hemming collection
    private TreeMap<Double, Double> hemmingBuild (Function first, Function second, LinkedList<Double> axisList) {
        TreeMap<Double, Double> map = new TreeMap<>();

        axisList.forEach((x) -> map.put(x, Math.abs(first.function(x) - second.function(x))));

        return map;
    }

    // create euclidean collection
    private TreeMap<Double, Double> euclideanBuild (Function first, Function second, LinkedList<Double> axisList) {
        TreeMap<Double, Double> map = new TreeMap<>();

        axisList.forEach((x) -> map.put(x, Math.pow(first.function(x) - second.function(x), 2)));

        return map;
    }

    // calculate hemming distance
    private double hemmingDistance (TreeMap<Double, Double> map, Label hem_show, Label index_hide) {
        AtomicReference<Double> distance = new AtomicReference<>(0d);
        map.forEach((x, y) -> distance.updateAndGet(v -> (v + y)));

        double hem = round(distance.get(), 4);
        showHemming(hem, hem_show, index_hide);

        return hem;
    }

    private void showHemming (double hem, Label hem_show, Label index_hide) {
        hem_show.setText("" + hem);
        index_hide.setText("");
    }

    // calculate euclidean distance
    private double euclideanDistance (TreeMap<Double, Double> map, Label euc_show, Label index_hide) {
        AtomicReference<Double> distance = new AtomicReference<>(0d);

        map.forEach((x, y) -> distance.updateAndGet(v -> (v + y)));
        distance.updateAndGet( v -> (Math.sqrt(distance.get())));

        double euc = round(distance.get(), 4);
        showEuclid(euc, euc_show, index_hide);

        return euc;
    }

    private void showEuclid (double euc, Label euc_show, Label index_hide) {
        euc_show.setText("" + euc);
        index_hide.setText("");
    }

    // create list of axis X
    private LinkedList<Double> createAxisList (Pair<Double, Double> borders, double step) {
        LinkedList<Double> axisList = new LinkedList<>();
        double length = round(( borders.getValue() - borders.getKey() )/ step, 2);

        for (double i = borders.getKey(); i <= borders.getValue(); i+=length) {
            axisList.add(i);
        }

        return axisList;
    }

    // find left and right x
    private Pair<Double, Double> findBorders (Function first, Function second) {
        return new Pair<> (Math.min(first.getLBorder(), second.getLBorder()), Math.max(first.getRBorder(), second.getRBorder()));
    }

    private void initHemmingDistance (Function first, Function second, LinkedList<Double> axisList, Label hem_show, Label index_hide) {
        TreeMap<Double, Double> hemming_map = hemmingBuild(first, second, axisList);
        XYChart.Series<Double, Double> series1 = new XYChart.Series<>();
        graph.getData().add(series1);
        series1.setName("Hem distance");
        hemming_map.forEach((x, y) -> series1.getData().add(new XYChart.Data<>(x, y)));
        setColor(series1, color_Hem);

        hemmingDistance(hemming_map, hem_show, index_hide);
    }

    private void initEuclideanDistance (Function first, Function second, LinkedList<Double> axisList, Label euc_show, Label index_hide) {
        TreeMap<Double, Double> euclidean_map = euclideanBuild(first, second, axisList);
        XYChart.Series<Double, Double> series2 = new XYChart.Series<>();
        graph.getData().add(series2);
        series2.setName("Euc distance");
        euclidean_map.forEach((x, y) -> series2.getData().add(new XYChart.Data<>(x, y)));
        setColor(series2, color_Evc);

        euclideanDistance(euclidean_map, euc_show, index_hide);
    }

    // build distances graphics
    private void initDistanceFunc (Function first, Function second, double step, Label hem_show, Label index_hide1, Label euc_show, Label index_hide2) {
        Pair<Double, Double> borders = findBorders(first, second);
        LinkedList<Double> axisList = createAxisList(borders,step);

        refreshLabels();

        initHemmingDistance(first, second, axisList, hem_show, index_hide1);
        initEuclideanDistance(first, second, axisList, euc_show, index_hide2);
    }

    //############################################  METHODS FOR 8 LAB ##################################################

    private TreeMap<Double, Double> hemmingCrispBuild (Function fuzzy, CrispSet crisp, LinkedList<Double> axisList) {
        TreeMap<Double, Double> map = new TreeMap<>();

        axisList.forEach((x) -> map.put(x, Math.abs(fuzzy.function(x) - crisp.crispFunction(fuzzy.function(x)))));

        return map;
    }

    private TreeMap<Double, Double> euclideanCrispBuild (Function fuzzy, CrispSet crisp, LinkedList<Double> axisList) {
        TreeMap<Double, Double> map = new TreeMap<>();

        axisList.forEach((x) -> map.put(x, Math.pow(fuzzy.function(x) - crisp.crispFunction(fuzzy.function(x)), 2)));

        return map;
    }

    // hemming
    private double linearIndex (double hem, double step) {
        double index = (2/step) * hem;
        return round(index,4);
    }

    // euclidean
    private double quadraticIndex (double euc, double step) {
        double index = (2/Math.sqrt(step)) * euc;
        return round(index,4);
    }

    private void initCrisp (TreeMap<Double, Double> map, String func_name, ColorPicker picker) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        graph.getData().add(series);
        series.setName(func_name);

        map.forEach((x, y) -> series.getData().add(new XYChart.Data<>(x, y)));

        setColor(series, picker);
    }

    private void initIndexes (Function first, double step, String func_name, String crisp_name, Label linear, Label quadratic, ColorPicker fuzzy_color, ColorPicker crisp_color,
                              Label hem_show, Label index_hide1, Label euc_show, Label index_hide2) {

        double first_step = findLength(first) / step;

        CrispSet crisp = new CrispSet();
        TreeMap<Double, Double> crispList = crisp.getList(first.pointsList(first_step));
        initFunc(first, first_step, func_name, fuzzy_color);
        initCrisp(crispList, crisp_name, crisp_color);

        double linearIndex = linearIndex(hemmingDistance(hemmingCrispBuild(first, crisp, createAxisList(new Pair<>(first.getLBorder(), first.getRBorder()), step)), hem_show, index_hide1), step);
        double quadraticIndex = quadraticIndex(euclideanDistance(euclideanCrispBuild(first, crisp, createAxisList(new Pair<>(first.getLBorder(), first.getRBorder()), step)), euc_show, index_hide2), step);

        showIndexes(linear, quadratic, linearIndex, quadraticIndex);
    }

    private void showIndexes (Label linear, Label quadratic, double linearIndex, double quadraticIndex) {
        linear.setText("" + linearIndex);
        quadratic.setText("" + quadraticIndex);
    }

    private void initIndexesChooser (Function first, Function second, double step) {
        if (a_radio_choice.isSelected()) {
            refreshLabels();
            initIndexes(first, step, "A func", "A crisp", linear_index, quadratic_index, color_A, color_Hem, hem_distance, linear_index, evc_distance, quadratic_index);
        }
        else if (b_radio_choice.isSelected()) {
            refreshLabels();
            initIndexes(second, step, "B func", "B crisp", linear_index1, quadratic_index1, color_B, color_Evc, hem_distance1, linear_index1, evc_distance1, quadratic_index1);
        }
        else {
            refreshLabels();
            initIndexes(first, step, "A func", "A crisp", linear_index, quadratic_index, color_A, color_Hem, hem_distance, linear_index, evc_distance, quadratic_index);
            initIndexes(second, step, "B func", "B crisp", linear_index1, quadratic_index1, color_B, color_Evc, hem_distance1, linear_index1, evc_distance1, quadratic_index1);
        }
    }

    private void refreshLabels () {
        linear_index.setText(""); linear_index1.setText("");
        quadratic_index.setText(""); quadratic_index1.setText("");

        hem_distance.setText(""); hem_distance1.setText("");
        evc_distance.setText(""); evc_distance1.setText("");
    }

    @FXML
    private void activateARadio () {
        b_radio_choice.setSelected(false);
        both_radio_choice.setSelected(false);
    }

    @FXML
    private void activateBRadio () {
        a_radio_choice.setSelected(false);
        both_radio_choice.setSelected(false);
    }

    @FXML
    private void activateBothRadio () {
        a_radio_choice.setSelected(false);
        b_radio_choice.setSelected(false);
    }

    //############################################  MAIN MANAGER METHOD ################################################

    private void buildGraphics () {
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
            double step_distance = Integer.parseInt(steps_distance.getText());
            TreeMap<Double, Double> C_result = new TreeMap<>();

            graph.setCreateSymbols(false);
            switch (func_choice.getValue()) {
                case "Triangular":
                    Triangular tri1 = new Triangular(c2, a2, b);
                    Triangular tri2 = new Triangular(c2_, a2_, b_);

                    if (!operations_choice.getValue().equals("Fuzzy Indexes")) {
                        double this_step = findLengthStep(tri1, tri2, step);
                        TreeMap<Double, Double> A = initFunc(tri1, this_step, "A function", color_A);
                        TreeMap<Double, Double> B = initFunc(tri2, this_step, "B function", color_B);

                        if(operations_choice.getValue().equals("Hemming and Euclidean distances")) {
                            initDistanceFunc(tri1, tri2, step_distance, hem_distance, linear_index, evc_distance, quadratic_index);
                        }
                        else C_result = initResultFunc(A, B, color_C);
                    }
                    else initIndexesChooser(tri1, tri2, step_distance);

                    break;

                case "Bilateral Gaussian MF":
                    BilGauss bil1 = new BilGauss(a1, a2, c1, c2);
                    BilGauss bil2 = new BilGauss(a1_, a2_, c1_, c2_);

                    if (!operations_choice.getValue().equals("Fuzzy Indexes")) {
                        double this_step1 = findLengthStep(bil1, bil2, step);
                        TreeMap<Double, Double> A1 = initFunc(bil1, this_step1, "A function", color_A);
                        TreeMap<Double, Double> B1 = initFunc(bil2, this_step1, "B function", color_B);

                        if(operations_choice.getValue().equals("Hemming and Euclidean distances")) {
                            initDistanceFunc(bil1, bil2, step_distance, hem_distance, linear_index, evc_distance, quadratic_index);
                        }
                        else C_result = initResultFunc(A1, B1, color_C);
                    }
                    else initIndexesChooser(bil1, bil2, step_distance);

                    break;

                case "Generalized bell MF":
                    BellMF bell = new BellMF(a1, b, c1);
                    BellMF bell1 = new BellMF(a1_, b_, c1_);

                    if (!operations_choice.getValue().equals("Fuzzy Indexes")) {
                        double this_step2 = findLengthStep(bell, bell1, step);
                        TreeMap<Double, Double> A2 = initFunc(bell, this_step2, "A function", color_A);
                        TreeMap<Double, Double> B2 = initFunc(bell1, this_step2, "B function", color_B);

                        if(operations_choice.getValue().equals("Hemming and Euclidean distances")) {
                            initDistanceFunc(bell, bell1, step_distance, hem_distance, linear_index, evc_distance, quadratic_index);
                        }
                        else C_result = initResultFunc(A2, B2, color_C);
                    }
                    else initIndexesChooser(bell, bell1, step_distance);

                    break;

                case "The difference between the two sigmoid MFs":
                    Sigmoid sigm = new Sigmoid(a1, a2, c1, c2);
                    Sigmoid sigm1 = new Sigmoid(a1_, a2_, c1_, c2_);

                    if (!operations_choice.getValue().equals("Fuzzy Indexes")) {
                        double this_step3 = findLengthStep(sigm, sigm1, step);
                        TreeMap<Double, Double> A3 = initFunc(sigm, this_step3, "A function", color_A);
                        TreeMap<Double, Double> B3 = initFunc(sigm1, this_step3, "B function", color_B);

                        if(operations_choice.getValue().equals("Hemming and Euclidean distances")) {
                            initDistanceFunc(sigm, sigm1, step_distance, hem_distance, linear_index, evc_distance, quadratic_index);
                        }
                        else C_result = initResultFunc(A3, B3, color_C);
                    }
                    else initIndexesChooser(sigm, sigm1, step_distance);

                    break;

                default:
                    dialogRegMessage("Warning!", "Invalid function type!",
                            "It looks like you did not select a function or entered a wrong name! Please check the data.");
                    break;
            }

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