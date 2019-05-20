package vecpaint;

import observerpattern.Subject;

import java.awt.*;

public class VecModel extends Subject {
    private Color penColor;
    private Color fillColor;
    private Tool currentTool;

    public VecModel(){
        penColor = Color.BLACK;
        fillColor = null;
        currentTool = Tool.PLOT;
    }

    public void setPenColor(Color color){
        penColor = color;
        notifyObservers();
    }

    public void setFillColor(Color color){
        fillColor = color;
        notifyObservers();
    }

    public void setCurrentTool(Tool tool){
        currentTool = tool;
        notifyObservers();
    }

    public Color getPenColor(){
        return penColor;
    }

    public Color getFillColor(){
        return fillColor;
    }

    public Tool getCurrentTool(){
        return currentTool;
    }
}
