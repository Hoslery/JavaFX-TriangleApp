package org.example;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    private ObservableList<Triangle> data = FXCollections.observableArrayList();
    private TableView<Triangle> dataTableView = new TableView<>();
    private String errorMessage="";


    private void readDataFromFile(File dataFile) {
        try {
            data.clear();
            errorMessage="";
            BufferedReader in = new BufferedReader(new FileReader(dataFile));
            String str;
            while ((str = in.readLine()) != null) {
                try {
                    if(str.isEmpty()) break;
                    String[] dataArray = str.split(" +");
                    if (dataArray.length != 3)
                        throw new Exception("wrong data");
                    double a = Double.parseDouble(dataArray[0]);
                    double b = Double.parseDouble(dataArray[1]);
                    double c = Double.parseDouble(dataArray[2]);
                    if (a <=0 || b<=0 || c<=0){
                        if (errorMessage.equals(""))
                            errorMessage += "The sides must be numbers > 0 \n";
                        continue;
                    }
                    if (a+b<=c || a+c<=b || c+b<=a){
                        if (errorMessage.equals(""))
                            errorMessage += "The sum of the two sides should be greater than the third \n";
                        continue;
                    }
                    String formatA= String.format("%.2f", a);
                    String formatB= String.format("%.2f", b);
                    String formatC= String.format("%.2f", c);
                    Triangle triangle = new Triangle(Double.parseDouble(formatA.replaceAll(",",".")),Double.parseDouble(formatB.replaceAll(",",".")),Double.parseDouble(formatC.replaceAll(",",".")));
                    data.add(triangle);
                }
                catch (Exception e){
                    errorMessage += e.getMessage()+"\n";
                    in.close();
                }
            }
            in.close();
        } catch (IOException e){
            errorMessage+=e.getMessage()+"\n";
        }
    }
    private void saveDataToFile(File dataFile) {
        try {
            FileWriter out = new FileWriter(dataFile);
            for (Triangle tr : data) {
                out.write(tr.getA().toString() + " " + tr.getB().toString() + " " + tr.getC().toString() +  "\n");
            }
            out.close();
        } catch (IOException e){
            showMessage(e.getMessage());
        }
    }
    private void createTableView() {
        TableColumn<Triangle, String> aCol = new TableColumn<>("First  side");
        aCol.setCellValueFactory(new PropertyValueFactory("a"));
        aCol.setMinWidth(120);
        TableColumn<Triangle, String> bCol = new TableColumn<>("Second  side");
        bCol.setCellValueFactory(new PropertyValueFactory("b"));
        bCol.setMinWidth(120);
        TableColumn<Triangle, String> cCol = new TableColumn<>("Third  side");
        cCol.setCellValueFactory(new PropertyValueFactory("c"));
        cCol.setMinWidth(120);
        dataTableView.getColumns().setAll(aCol,bCol,cCol);
    }

    private void handleFileOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        readDataFromFile(file);
        if(!errorMessage.isEmpty()) showMessage(errorMessage);
    }
    private void handleButtonEdit() {
        Triangle triangle = dataTableView.getSelectionModel().getSelectedItem();
        if (triangle != null) {
            TriangleEditDialog triangleEditDialog= new TriangleEditDialog(triangle);
            dataTableView.refresh();
        } else {
            showMessage("No selected item!");
        }
    }
    private void handleButtonAdd() {
        Triangle triangle = new Triangle(1,1,1);
        TriangleEditDialog triangleEditDialog = new TriangleEditDialog(triangle);
        if (triangleEditDialog.getResult() == ButtonType.OK) {
            data.add(triangle);
        }
    }
    private void handleButtonDelete() {
        int selectedIndex = dataTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            dataTableView.getItems().remove(selectedIndex);
        } else {
            showMessage("No deleted item!");
        }
    }

    private void handleButtonAddGraphic() {
        Triangle triangle = new Triangle(1,1,1);
        TriangleGraphic triangleGraphic = new TriangleGraphic(triangle);
        if (triangleGraphic.getResult() == ButtonType.OK) {
            data.add(triangle);
        }
    }

    private void handleButtonEditGraphic(){
        Triangle triangle = dataTableView.getSelectionModel().getSelectedItem();
        if (triangle != null) {
            TriangleEditGraphic triangleEditGraphic= new TriangleEditGraphic(triangle);
            dataTableView.refresh();
        } else {
            showMessage("No selected item!");
        }
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("_File");
        MenuItem open = new MenuItem("Open");
        MenuItem exit = new MenuItem("Exit");
        fileMenu.getItems().addAll(open, new SeparatorMenuItem(), exit);
        open.setOnAction((ActionEvent event) -> handleFileOpen());
        exit.setOnAction((ActionEvent event) ->Platform.exit());
        return fileMenu;
    }
    private Menu createEditMenu() {
        Menu editMenu = new Menu("Edit");
        MenuItem add = new MenuItem("Add Triangle");
        editMenu.getItems().add(add);
        add.setOnAction((ActionEvent event) -> handleButtonAdd());
        MenuItem edit = new MenuItem("Edit Triangle");
        editMenu.getItems().add(edit);
        edit.setOnAction((ActionEvent event) -> handleButtonEdit());
        MenuItem delete = new MenuItem("Delete Triangle");
        editMenu.getItems().add(delete);
        delete.setOnAction((ActionEvent event) -> handleButtonDelete());
        return editMenu;
    }

    private Menu createGraphicMenu() {
        Menu graphicMenu = new Menu("Graphic");
        MenuItem add = new MenuItem("Add triangle with graphic");
        graphicMenu.getItems().add(add);
        add.setOnAction((ActionEvent event) -> handleButtonAddGraphic());
        MenuItem edit = new MenuItem("Edit triangle with graphic");
        graphicMenu.getItems().add(edit);
        edit.setOnAction((ActionEvent event) -> handleButtonEditGraphic());
        return graphicMenu;
    }


    private void showMessage(String message) {
        Alert messageAlert = new Alert(Alert.AlertType.WARNING,message, ButtonType.OK);
        messageAlert.showAndWait();
    }

    @Override
    public void init() {
        readDataFromFile(new File("src/main/resources/simplemodel/data.txt"));
    }
    @Override
    public void start(Stage primaryStage) {
        if(!errorMessage.isEmpty()) showMessage(errorMessage);
        primaryStage.setTitle("List of Triangles");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));
        createTableView();
        dataTableView.setItems(data);
        root.setCenter(dataTableView);
        root.setTop(new MenuBar(createFileMenu(), createEditMenu(), createGraphicMenu()));
        dataTableView.setRowFactory( tv -> {
            TableRow<Triangle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    Triangle rowData = row.getItem();
                    ViewTriangle viewTriangle = new ViewTriangle(rowData);
                    root.setLeft(viewTriangle.getPane());
                }
            });
            return row ;
        });
        Scene scene = new Scene(root, 550, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @Override
    public void stop() {
        saveDataToFile(new File("src/main/resources/simplemodel/out.txt"));
    }
    public static void main(String[] args) {
        launch(args);
    }

}