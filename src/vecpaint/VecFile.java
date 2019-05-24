package vecpaint;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Object representation of a .vec file
 */
public class VecFile {
    /**
     * Specifies whether file is currently indicating transparency
     */
    private boolean indicatingTransparency;

    /**
     * Specifies whether file has been modified since the last time the file was saved
     */
    private boolean isModified;

    /**
     * Absolute path to the file
     */
    private String filePath;

    /**
     * The name of the file
     */
    private String fileName;

    /**
     * Content of the file
     */
    private List<String> content;


    /**
     * Creates a VecFile with the specified file or an untitled file if file is null
     * @param file a system file object containing the path of this VecFile
     */
    public VecFile(File file){
        this.content = new ArrayList<>();
        this.indicatingTransparency = false;
        this.isModified = file == null;

        this.filePath = (file == null) ? "untitled.vec" : file.getAbsolutePath();
        this.fileName = (file == null) ? "untitled.vec" : file.getName();

        if(!filePath.toLowerCase().endsWith(".vec")) filePath += ".vec";
        if(!fileName.toLowerCase().endsWith(".vec")) fileName += ".vec";

        BufferedReader br = getBuffer();
        if(br != null){
            try {
                String line;
                while((line = br.readLine()) != null){
                    addContent(line);
                }
                br.close();
            } catch (Exception e){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Unable to read file");
                    }
                });
            }
        }

    }

    /**
     * Set the indicatingTransparency field
     * @param indicateTransparency true or false
     */
    public void setIndicatingTransparency(boolean indicateTransparency){
        indicatingTransparency = indicateTransparency;
    }

    /**
     * Set the isModified field
     * @param isModified true or false
     */
    public void setModified(boolean isModified){
        this.isModified = isModified;
    }

    /**
     * Set the filePath field
     * @param filePath a string of the file path
     */
    public void setFilePath(String filePath){
        if(!filePath.toLowerCase().endsWith(".vec")) filePath += ".vec";
        this.filePath = filePath;
    }

    /**
     * Set the fileName field
     * @param fileName a string of the file name
     */
    public void setFileName(String fileName){
        if(!fileName.toLowerCase().endsWith(".vec")) fileName += ".vec";
        this.fileName = fileName;
    }

    /**
     * Set the content field
     * @param content a string ArrayList that specifies the content
     */
    public void setContent(List<String> content){
        this.content = content;
    }

    /**
     * Adds an item to the content field
     * @param line a string that is to be added to the content
     */
    public void addContent(String line){
        content.add(line);
    }

    /**
     * Saves content field to a file on the system at this file's filePath
     */
    public void saveFile(){
        String strContent = "";
        for(String line : content){
            strContent += line + "\n";
        }
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(strContent);
            writer.close();
        } catch (Exception e){}
    }

    /**
     * Gets the indicatingTransparency field
     * @return true or false
     */
    public boolean isIndicatingTransparency(){
        return indicatingTransparency;
    }

    /**
     * Gets the isModified field
     * @return true or false
     */
    public boolean getModified(){
        return isModified;
    }

    /**
     * Gets the filePath field
     * @return a string containing the absolute path of the file
     */
    public String getFilePath(){
        return filePath;
    }

    /**
     * Gets the fileName field
     * @return a string containing the file name with the extension or asterisk(*) if isModified is true
     */
    public String getFileName(){
        return (isModified) ? fileName+"*" : fileName;
    }

    /**
     * Gets the content field
     * @return a string list containing the contents of the file
     */
    public List<String> getContent(){
        return content;
    }

    /**
     * Gets a buffered reader of the file
     * @return a BufferedReader object of the file
     */
    private BufferedReader getBuffer(){
        if(filePath == null) return null;
        BufferedReader buffer = null;
        try {
            FileReader reader = new FileReader(filePath);
            buffer = new BufferedReader(reader);
        } catch (Exception e){}
        return buffer;
    }

}
