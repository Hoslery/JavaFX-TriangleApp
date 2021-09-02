package org.example;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TriangleGraphic {
    private Triangle triangle;
    private Stage dialog;
    private double a;
    private double b;
    private double c;
    private Canvas canvas;
    private GraphicsContext gc;
    private GridPane root;
    private ButtonType result = ButtonType.CANCEL;
    private void createSideText() {
        canvas = new Canvas(600, 600);
        gc = canvas.getGraphicsContext2D();
        // сетка
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(.5);
        gc.setFontSmoothingType(null);
        gc.setLineDashes(5, 2);
        for (int i = 0; i < 10; i++) {
            gc.strokeLine(i * 60, 0, i * 60, 600);
            gc.strokeLine(0, i * 60, 600, i * 60);
        }
        gc.setStroke(Color.BLUE);
        gc.setLineDashes(null);
        gc.setLineWidth(1);

        // числа

        gc.setTextAlign(TextAlignment.CENTER);
        for (int i = 0; i < 11; i++) {
            if (i == 5) continue;
            gc.strokeText(String.valueOf(i * 60 - 300), i * 60, 315);
        }
        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.LEFT);
        for (int i = 0; i < 11; i++) {
            if (i == 5) continue;
            gc.strokeText(String.valueOf(300 - i * 60), 310, i * 60);
        }

        gc.setStroke(Color.BLACK);
        gc.strokeLine(300, 0, 300, 600);    // zero line
        gc.strokeLine(0, 300, 600, 300);    // zero line

        AtomicReference<Double> x1 = new AtomicReference<>((double) 0);
        AtomicReference<Double> x2 = new AtomicReference<>((double) 0);
        AtomicReference<Double> x3 = new AtomicReference<>((double) 0);
        AtomicReference<Double> y1 = new AtomicReference<>((double) 0);
        AtomicReference<Double> y2 = new AtomicReference<>((double) 0);
        AtomicReference<Double> y3 = new AtomicReference<>((double) 0);

        AtomicInteger count = new AtomicInteger();
        canvas.setOnMouseClicked(event -> {
            count.set(event.getClickCount() + count.get());
            if (count.get() == 1){
                x1.set(event.getSceneX()-300);
                y1.set((event.getSceneY() - 300)*(-1));
            }
            if (count.get() == 2){
                x2.set(event.getSceneX()-300);
                y2.set((event.getSceneY() - 300)*(-1));
            }
            if (count.get() == 3){
                x3.set(event.getSceneX()-300);
                y3.set((event.getSceneY() - 300)*(-1));
                a = Math.sqrt((x1.get() - x2.get()) * (x1.get() - x2.get()) + (y1.get() - y2.get()) * (y1.get() - y2.get()));
                b = Math.sqrt((x1.get() - x3.get()) * (x1.get() - x3.get()) + (y1.get() - y3.get()) * (y1.get() - y3.get()));
                c = Math.sqrt((x2.get() - x3.get()) * (x2.get() - x3.get()) + (y2.get() - y3.get()) * (y2.get() - y3.get()));
                canvas.setOnMouseClicked(null);
            }
            gc.fillOval(event.getX() - 2, event.getY() - 2, 6, 6);
        });
        root.getChildren().add(canvas);

    }

    private void createButtons() {
        Button btn = new Button("Ok");
        btn.setFont(Font.font(18));
        root.add(btn, 0, 3);
        btn.setOnAction((ActionEvent e) -> {
            if (isInputValid())
                handleOk();
            else {
                message();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                createSideText();
            }
        });
        Button btn1 = new Button("Cancel");
        btn1.setFont(Font.font(18));
        root.add(btn1, 1, 3);
        btn1.setOnAction((ActionEvent e) -> {
            handleCancel();
        });
    }

    private void message(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Triangle entry error");
        alert.setHeaderText("Side entry error");
        if (!isInputValid()){
            alert.setContentText("The sum of the two sides should be greater than the third \n");
            alert.showAndWait();
            return;
        }
    }


    public TriangleGraphic(Triangle triangle) {
        this.triangle = triangle;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add triangle with graphic");
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        createSideText();
        createButtons();
        Scene scene = new Scene(root, 750, 750);
        dialog.setScene(scene);
        dialog.showAndWait();
    }


    //проверка на то, что сумма 2 сторон треугольника > 3-ей стороны
    private boolean isInputValid() {
        String formatA= String.format("%.2f", a );
        String formatB= String.format("%.2f", b );
        String formatC= String.format("%.2f", c );
        double A = Double.parseDouble(formatA.replaceAll(",","."));
        double B = Double.parseDouble(formatB.replaceAll(",","."));
        double C = Double.parseDouble(formatC.replaceAll(",","."));
        if ((A + B > C) && (A + C > B) && (C + B > A))
            return true;
        return false;
    }


    private void handleOk() {
        String formatA= String.format("%.2f", a );
        String formatB= String.format("%.2f", b );
        String formatC= String.format("%.2f", c );
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
