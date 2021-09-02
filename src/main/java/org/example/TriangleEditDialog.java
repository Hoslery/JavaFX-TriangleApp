package org.example;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
public class TriangleEditDialog {
    private Triangle triangle;
    private Stage dialog;
    private TextField a;
    private TextField b;
    private TextField c;
    private Font font;
    private GridPane root;
    private ButtonType result = ButtonType.CANCEL;
    private void createSideText() {
        Label side1 = new Label("First side:");
        side1.setFont(font);
        root.add(side1, 0, 0);
        a = new TextField();
        a.setFont(Font.font(24));
        a.setText(triangle.getA().toString());
        root.add(a, 1, 0);
        Label side2 = new Label("Second side:");
        side2.setFont(font);
        root.add(side2, 0, 1);
        b = new TextField();
        b.setFont(Font.font(24));
        b.setText(triangle.getB().toString());
        root.add(b, 1, 1);
        Label side3 = new Label("Third side:");
        side3.setFont(font);
        root.add(side3, 0, 2);
        c = new TextField();
        c.setFont(Font.font(24));
        c.setText(triangle.getC().toString());
        root.add(c, 1, 2);

    }

    private void createButtons() {
        Button btn = new Button("Ok");
        btn.setFont(Font.font(24));
        root.add(btn, 0, 3);
        btn.setOnAction((ActionEvent e) -> {
            if (isInputValid() && isInputValid2() && isInputValid3())
                handleOk();
            else message();
        });
        Button btn1 = new Button("Cancel");
        btn1.setFont(Font.font(24));
        root.add(btn1, 1, 3);
        btn1.setOnAction((ActionEvent e) -> {
            handleCancel();
        });
    }
    private void message(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Triangle entry error");
        alert.setHeaderText("Side entry error");
        //Различные сообщения при разных типах ошибок
        if (!isInputValid()){
            alert.setContentText("The sides must be numbers > 0 \n");
            alert.showAndWait();
            return;
        }
        if (!isInputValid2()) {
            alert.setContentText("Sides should be numbers > 0 \n");
            alert.showAndWait();
            return;
        }
        if (!isInputValid3()) {
            alert.setContentText("The sum of the two sides should be greater than the third \n");
            alert.showAndWait();
        }
    }
    public TriangleEditDialog(Triangle triangle) {
        this.triangle = triangle;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add triangle");
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        font = Font.font("Tahoma", FontWeight.NORMAL, 20);
        createSideText();
        createButtons();
        Scene scene = new Scene(root, 600, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }


    //проверка на то, что на вход подаются числа
    private boolean isInputValid() {
        if (((a.getText().matches("^[0-9]+[.]*[0-9]*$")) && (b.getText().matches("^[0-9]+[.]*[0-9]*$")) && (c.getText().matches("^[0-9]+[.]*[0-9]*$"))) )
            return true;
        return false;
    }

    //проверка на то,что числа положительные
    private boolean isInputValid2() {
        try {
            if (Double.parseDouble(a.getText()) > 0 && Double.parseDouble(b.getText()) > 0 && Double.parseDouble(c.getText()) > 0)
                return true;
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }


    //проверка на то, что сумма 2 сторон треугольника > 3-ей стороны
    private boolean isInputValid3() {
        if ((Double.parseDouble(a.getText()) + Double.parseDouble(b.getText()) > Double.parseDouble(c.getText())) && (Double.parseDouble(a.getText()) + Double.parseDouble(c.getText()) > Double.parseDouble(b.getText())) && (Double.parseDouble(c.getText()) + Double.parseDouble(b.getText()) > Double.parseDouble(a.getText())))
            return true;
        return false;
    }


    private void handleOk() {
        String formatA= String.format("%.2f", Double.parseDouble(a.getText()) );
        String formatB= String.format("%.2f", Double.parseDouble(b.getText()) );
        String formatC= String.format("%.2f", Double.parseDouble(c.getText()) );
        triangle.setA(Double.parseDouble(formatA.replaceAll(",",".")));
        triangle.setB(Double.parseDouble(formatB.replaceAll(",",".")));
        triangle.setC(Double.parseDouble(formatC.replaceAll(",",".")));
        result = ButtonType.OK;
        dialog.close();
    }
    private void handleCancel(){
        result = ButtonType.CANCEL;
        dialog.close();
    }
    public ButtonType getResult() {
        return result;
    }
}
