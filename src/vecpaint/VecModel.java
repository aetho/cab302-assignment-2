package vecpaint;

import observerpattern.Subject;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The model of MVC architecture
 */
public class VecModel extends Subject {
    /**
     * Current pen color
     */
    private Color penColor;

    /**
     * Current fill color
     */
    private Color fillColor;

    /**
     * Current tool
     */
    private Tool currentTool;

    /**
     * List of currently opened files
     */
    private List<VecFile> openedFiles = new ArrayList<>();

    /**
     * Creates a VecModel
     */
    public VecModel(){
        penColor = Color.BLACK;
        fillColor = null;
        currentTool = Tool.PLOT;
        openedFiles.add(new VecFile(null));
    }

    /**
     * Set pen color field
     * @param color desired color
     */
    public void setPenColor(Color color){
        penColor = color;
        notifyObservers();
    }

    /**
     * Set fill color field
     * @param color desired color
     */
    public void setFillColor(Color color){
        fillColor = color;
        notifyObservers();
    }

    /**
     * Set current tool field
     * @param tool desired tool
     */
    public void setCurrentTool(Tool tool){
        currentTool = tool;
        notifyObservers();
    }

    /**
     * Toggle transparency of file at fileIndex
     * @param fileIndex desired index
     */
    public void toggleTransparency(int fileIndex){
        VecFile selectedFile = openedFiles.get(fileIndex);
        selectedFile.setIndicatingTransparency(!selectedFile.isIndicatingTransparency());
        notifyObservers();
    }

    /**
     * Open a new file
     */
    public void newFile(){
        openedFiles.add(new VecFile(null));
        notifyObservers();
    }

    /**
     * Open file
     * @param file desired file
     */
    public void openFile(File file){
        openedFiles.add(new VecFile(file));
        notifyObservers();
    }

    /**
     * Save file
     * @param file desired file
     * @param fileIndex index of the file you wish to save
     */
    public void saveFile(File file, int fileIndex){
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();

        openedFiles.get(fileIndex).setFilePath(filePath);
        openedFiles.get(fileIndex).setFileName(fileName);
        openedFiles.get(fileIndex).saveFile();
        openedFiles.get(fileIndex).setModified(false);

        notifyObservers();
    }

    /**
     * Update file
     * @param fileIndex index of the file you wish to update
     * @param update update content
     */
    public void updateFile(int fileIndex, String update){
        openedFiles.get(fileIndex).addContent(update);
        openedFiles.get(fileIndex).setModified(true);
        notifyObservers();
    }

    /**
     * Close file
     * @param fileIndex index of file you wish to close
     */
    public void closeFile(int fileIndex){
        openedFiles.remove(fileIndex);
        notifyObservers();
    }

    /**
     * Close all files
     */
    public void closeAllFiles(){
        openedFiles.clear();
        notifyObservers();
    }

    /**
     * Undo the last draw operation at specified index
     * @param fileIndex index of file you wish to perform undo on
     */
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

    /**
     * Get pen color
     * @return color of pen
     */
    public Color getPenColor(){
        return penColor;
    }

    /**
     * Get pen color as a hex string
     * @return color of pen as a hex string in the format "RRGGBB AA". E.g. "0000FF FF"
     */
    public String getPenColorHexStr() {
        if(penColor == null) return null;

        int r = penColor.getRed();
        int g = penColor.getGreen();
        int b = penColor.getBlue();
        int a = penColor.getAlpha();

        return String.format("#%02X%02X%02X %02X", r, g, b, a);
    }

    /**
     * Get fill color
     * @return color of fill
     */
    public Color getFillColor(){
        return fillColor;
    }

    /**
     * Get fill color as a hex string
     * @return color of fill as a hex string in the format "RRGGBB AA". E.g. "0000FF FF"
     */
    public String getFillColorHexStr() {
        if(fillColor == null) return null;

        int r = fillColor.getRed();
        int g = fillColor.getGreen();
        int b = fillColor.getBlue();
        int a = fillColor.getAlpha();

        return  String.format("#%02X%02X%02X %02X", r, g, b, a);
    }

    /**
     * Get current tool
     * @return current tool
     */
    public Tool getCurrentTool(){
        return currentTool;
    }

    /**
     * Get list of opened files
     * @return ArrayList containing currently opened files
     */
    public List<VecFile> getOpenedFiles(){
        return openedFiles;
    }
}
