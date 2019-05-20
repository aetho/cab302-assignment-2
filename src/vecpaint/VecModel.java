package vecpaint;

import observerpattern.Subject;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VecModel extends Subject {
    private Color penColor;
    private Color fillColor;
    private Tool currentTool;
    private List<VecFile> openedFiles = new ArrayList<>();

    public VecModel(){
        penColor = Color.BLACK;
        fillColor = null;
        currentTool = Tool.PLOT;
        openedFiles.add(new VecFile(null));
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

    public void openFile(File file){
        openedFiles.add(new VecFile(file));
        notifyObservers();
    }

    public void closeFile(int fileIndex){
        openedFiles.remove(fileIndex);
        notifyObservers();
    }

    public void closeAllFiles(){
        openedFiles.clear();
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

    public List<VecFile> getOpenedFiles(){
        return openedFiles;
    }
}
