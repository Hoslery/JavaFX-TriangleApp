package org.example;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
public class ViewTriangle {
    private Triangle triangle;
    private GridPane pane;
    private Text type;
    private Text perimetr;
    private Text square;
    private void createPane(){
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(0,3,0,3));
        Rectangle rect = new Rectangle(160, 90);
        rect.setFill(Color.AQUA);
        rect.setStroke(Color.DARKBLUE);
        rect.setStrokeWidth(2);
        pane.getChildren().add(rect);
        Label typeTr = new Label("Type of triangle:");
        typeTr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        pane.getChildren().add(typeTr);
        GridPane.setMargin(typeTr,new Insets(0,2,0,10));
        type = new Text();
        type.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        pane.getChildren().add(type);
        GridPane.setMargin(type,new Insets(0,0,0,86));
        Label per = new Label("Perimetr:");
        per.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        pane.getChildren().add(per);
        GridPane.setMargin(per,new Insets(30,2,0,10));
        perimetr = new Text();
        perimetr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        pane.getChildren().add(perimetr);
        GridPane.setMargin(perimetr,new Insets(30,2,0,56));
        Label sq = new Label("Square:");
        sq.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        pane.getChildren().add(sq);
        GridPane.setMargin(sq,new Insets(60,2,0,10));
        square = new Text();
        square.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        pane.getChildren().add(square);
        GridPane.setMargin(square,new Insets(60,2,0,56));
    }
    private void addTriangle(){
        type.textProperty().bind(triangle.typeOfTriangle());
        perimetr.textProperty().bind(triangle.perimeter().asString());
        square.textProperty().bind(triangle.square().asString());
    }
    public GridPane getPane(){
        return pane;
    }
    public void setTriangle (Triangle triangle) {
        this.triangle = triangle;
        addTriangle();
    }
    public ViewTriangle(Triangle triangle) {
        createPane();
        setTriangle(triangle);
    }
}