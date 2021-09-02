package org.example;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Triangle {

    private DoubleProperty a;
    private DoubleProperty b;
    private DoubleProperty c;


    public DoubleProperty aProperty() {
        if (a == null) {
            a =new SimpleDoubleProperty();
        }
        return a;
    }

    public DoubleProperty bProperty() {
        if (b == null) {
            b =new SimpleDoubleProperty();
        }
        return b;
    }

    public DoubleProperty cProperty() {
        if (c == null) {
            c =new SimpleDoubleProperty();
        }
        return c;
    }

    public final void setA(Double value) {
        aProperty().set(value);
    }

    public final void setB(Double value) {
        bProperty().set(value);
    }

    public final void setC(Double value) {
        cProperty().set(value);
    }

    public final Double getA() {
        return aProperty().get();
    }

    public final Double getB() {
        return bProperty().get();
    }

    public final Double getC() {
        return cProperty().get();
    }

    public final DoubleProperty perimeter() {
        DoubleProperty perimetr = new SimpleDoubleProperty();
        String formatPer= String.format("%.2f", getA() + getB() +getC());
        perimetr.set(Double.parseDouble(formatPer.replaceAll(",",".")));
        return perimetr;
    }

    public final DoubleProperty square() {
        DoubleBinding p = this.perimeter().divide(2);
        DoubleProperty square = new SimpleDoubleProperty();
        String formaterSq = String.format("%.2f", Math.sqrt((p.multiply((p.subtract(a)).multiply((p.subtract(b)).multiply((p.subtract(c)))))).getValue()));
        square.set(Double.parseDouble(formaterSq.replaceAll(",",".")));
        return square;
    }

    public final StringProperty typeOfTriangle() {
        StringProperty str;
        if (getA().equals(getB()) && getB().equals(getC())){
            str =  new SimpleStringProperty("ravnostoronniy");
            return str;
        }
        if ((getA().equals(getB()) & !getA().equals(getC())) || (getA().equals(getC()) & !getC().equals(getB())) || (getB().equals(getC()) & !getB().equals(getA()))){
            str =  new SimpleStringProperty("ravnobedrinniy");
            return str;
        }
        if ( (getC()*getC() == getA()*getA() + getB()*getB()) || (getA()*getA() == getB()*getB() + getC()*getC()) || (getB()*getB() == getA()*getA() + getC()*getC())){
            str =  new SimpleStringProperty("pryamougolniy");
            return str;
        }
        double max = 0;
        max = Math.max(getA(),Math.max(getB(),getC()));
        if ( (getA() == max && max*max < getB()*getB() + getC()*getC()) || (getB() == max  && max*max < getA()*getA() + getC()*getC()) || (getC() == max && max*max < getB()*getB() + getA()*getA())) {
            str =  new SimpleStringProperty("ostrougolniy");
            return str;
        }
        else
        {
            str =  new SimpleStringProperty("typougolniy");
            return str;
        }
    }

    public Triangle(double a, double b, double c){
        setA(a);
        setB(b);
        setC(c);
    }
}


