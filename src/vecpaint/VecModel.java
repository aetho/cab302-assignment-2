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

    public void toggleTransparency(int fileIndex){
        VecFile selectedFile = openedFiles.get(fileIndex);
        selectedFile.setIndicatingTransparency(!selectedFile.isIndicatingTransparency());
        notifyObservers();
    }

    public void newFile(){
        openedFiles.add(new VecFile(null));
        notifyObservers();
    }

    public void openFile(File file){
        openedFiles.add(new VecFile(file));
        notifyObservers();
    }

    public void saveFile(File file, int fileIndex){
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();

        openedFiles.get(fileIndex).setFilePath(filePath);
        openedFiles.get(fileIndex).setFileName(fileName);
        openedFiles.get(fileIndex).saveFile();
        openedFiles.get(fileIndex).setModified(false);

        notifyObservers();
    }

    public void updateFile(int fileIndex, String update){
        openedFiles.get(fileIndex).addContent(update);
        openedFiles.get(fileIndex).setModified(true);
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

    public void undo(int fileIndex){
        List<String> fileContent = openedFiles.get(fileIndex).getContent();

        // Iterate through the content from end to beginning to find first drawing command
        for(int i = fileContent.size()-1; i >= 0; i--){
            String line = fileContent.get(i);
            String cmd = line.split(" ")[0];

            // Drawing operation mask used to detect if a command is a drawing command
            String mask1 = "PLOT LINE RECTANGLE ELLIPSE POLYGON";
            String mask2 = "OFF";

            // If latest line contains "OFF" then remove it before finding the drawing command
            if(line.contains(mask2)) {
                fileContent.remove(i);
                continue;
            }

            if(mask1.contains(cmd)){
                // Remove drawing command and break out of loop
                fileContent.remove(i);
                break;
            }
        }

        // Iterate through the content from end to beginning to remove color settings until the next FILL OFF or drawing
        for(int i = fileContent.size()-1; i >= 0; i--){
            String line = fileContent.get(i);
            String cmd = line.split(" ")[0];

            // Masks used to detect drawing commands and FILL OFF commands
            String mask1 = "PLOT LINE RECTANGLE ELLIPSE POLYGON";
            String mask2 = "OFF";

            if(mask1.contains(cmd) || line.contains(mask2)){
                break;
            }else{
                fileContent.remove(i);
            }
        }

        openedFiles.get(fileIndex).setContent(fileContent);
        openedFiles.get(fileIndex).setModified(true);
        notifyObservers();
    }

    public Color getPenColor(){
        return penColor;
    }
    public String getPenColorHexStr() {
        if(penColor == null) return null;

        int r = penColor.getRed();
        int g = penColor.getGreen();
        int b = penColor.getBlue();
        int a = penColor.getAlpha();

        return String.format("#%02X%02X%02X %02X", r, g, b, a);
    }

    public Color getFillColor(){
        return fillColor;
    }
    public String getFillColorHexStr() {
        if(penColor == null) return null;

        int r = fillColor.getRed();
        int g = fillColor.getGreen();
        int b = fillColor.getBlue();
        int a = fillColor.getAlpha();

        return  String.format("#%02X%02X%02X %02X", r, g, b, a);
    }

    public Tool getCurrentTool(){
        return currentTool;
    }

    public List<VecFile> getOpenedFiles(){
        return openedFiles;
    }
}
