package vecpaint;

import observerpattern.Subject;

import java.awt.*;

public class VecModel extends Subject {
    private Color penColor;
    private Color fillColor;

    public VecModel(){
        penColor = Color.BLACK;
        fillColor = null;
    }

    public void setPenColor(Color color){
        this.penColor = color;
        notifyObservers();
    }

    public void setFillColor(Color color){
        this.fillColor = color;
        notifyObservers();
    }

    public Color getPenColor(){
        return penColor;
    }

    public Color getFillColor(){
        return fillColor;
    }
}
