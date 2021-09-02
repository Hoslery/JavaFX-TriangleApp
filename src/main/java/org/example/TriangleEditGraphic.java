package org.example;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.input.MouseEvent;
import java.util.concurrent.atomic.AtomicReference;

public class TriangleEditGraphic {
    private Triangle triangle;
    private Stage dialog;
    private static double a;
    private static double b;
    private static double c;
    private Circle circle;
    private Circle circle1;
    private Circle circle2;
    double orgSceneX, orgSceneY,orgTranslateX, orgTranslateY;
    double newTranslateX=0.0;
    double newTranslateY=0.0;
    AtomicReference<Double> x1 = new AtomicReference<>((double) 0);
    AtomicReference<Double> x2 = new AtomicReference<>((double) 0);
    AtomicReference<Double> x3 = new AtomicReference<>((double) 0);
    AtomicReference<Double> y1 = new AtomicReference<>((double) 0);
    AtomicReference<Double> y2 = new AtomicReference<>((double) 0);
    AtomicReference<Double> y3 = new AtomicReference<>((double) 0);
    Pane pane;
    private Canvas canvas;
    private GraphicsContext gc;
    private GridPane root;
    private ButtonType result = ButtonType.CANCEL;

    EventHandler<MouseEvent> mousePressed =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Node)(t.getSource())).getTranslateX();
                    orgTranslateY = ((Node)(t.getSource())).getTranslateY();
                }};
    EventHandler<MouseEvent> mouseDragged =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    newTranslateX = orgTranslateX + offsetX;
                    newTranslateY = orgTranslateY + offsetY;
                    ((Node)(t.getSource())).setTranslateX(newTranslateX);
                    ((Node)(t.getSource())).setTranslateY(newTranslateY);
                }};


    private void createSideText() {

        pane = new Pane();

        x1.set(300.0);
        y1.set(300.0);
        x2.set(triangle.getA() +300);
        y2.set(300.0);
        x3.set(((x2.get()-300)/2 - ((triangle.getB()* triangle.getB() - triangle.getC()* triangle.getC())/(2*(x2.get()-300))) ));
        y3.set(Math.sqrt(triangle.getC()* triangle.getC() - x3.get()*x3.get()));
        x3.set(x3.get() + 300);
        y3.set(y3.get() +300);

        canvas = new Canvas(600, 600);
        circle = new Circle(x1.get(),y1.get(),4,Color.BLACK);
        circle1 = new Circle(x2.get(),y2.get(),4,Color.BLACK);
        circle2 = new Circle(x3.get(),y3.get(),4,Color.BLACK);

        ObjectProperty<Point2D> mouseLoc = new SimpleObjectProperty<>();
        circle.setOnMousePressed(e -> mouseLoc.set(new Point2D(e.getSceneX(), e.getSceneY())));
        circle.setOnMousePressed(mousePressed);
        circle.setOnMouseDragged(mouseDragged);

        circle1.setOnMousePressed(e -> mouseLoc.set(new Point2D(e.getSceneX(), e.getSceneY())));
        circle1.setOnMousePressed(mousePressed);
        circle1.setOnMouseDragged(mouseDragged);

        circle2.setOnMousePressed(e -> mouseLoc.set(new Point2D(e.getSceneX(), e.getSceneY())));
        circle2.setOnMousePressed(mousePressed);
        circle2.setOnMouseDragged(mouseDragged);

        pane.getChildren().addAll(circle,circle1,circle2);


        //сетка
        gc = canvas.getGraphicsContext2D();
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


        root.getChildren().addAll(canvas,pane);

    }



    private void createButtons() {
        Button btn = new Button("Ok");
        btn.setFont(Font.font(18));
        root.add(btn, 0, 3);
        btn.setOnAction((ActionEvent e) -> {
            if (isInputValid()){
                pane.getChildren().clear();
                handleOk();
            }
            else {
                message();
                pane.getChildren().clear();
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


    public TriangleEditGraphic(Triangle triangle) {
        this.triangle = triangle;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit triangle with graphic");
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
        a = Math.sqrt((circle.getTranslateX() - circle1.getTranslateX()) * (circle.getTranslateX() - circle1.getTranslateX()) + (circle.getTranslateY()*(-1) - circle1.getTranslateY()*(-1)) * (circle.getTranslateY()*(-1) - circle1.getTranslateY()*(-1)));
        b = Math.sqrt((circle.getTranslateX() - circle2.getTranslateX()) * (circle.getTranslateX() - circle2.getTranslateX()) + (circle.getTranslateY()*(-1) - circle2.getTranslateY()*(-1)) * (circle.getTranslateY()*(-1) - circle2.getTranslateY()*(-1)));
        c = Math.sqrt((circle1.getTranslateX() - circle2.getTranslateX()) * (circle1.getTranslateX() - circle2.getTranslateX()) + (circle1.getTranslateY()*(-1) - circle2.getTranslateY()*(-1)) * (circle1.getTranslateY()*(-1) - circle2.getTranslateY()*(-1)));
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
