package vecpaint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class VecFile {
    private boolean indicatingTransparency;
    private boolean isModified;
    private String filePath;
    private String fileName;
    private String content;


    public VecFile(File file){
        indicatingTransparency = false;
        this.isModified = file == null;

        this.filePath = (file == null) ? "untitled.vec" : file.getAbsolutePath();
        this.fileName = (file == null) ? "untitled.vec" : file.getName();

        if(!filePath.toLowerCase().endsWith(".vec")) filePath += ".vec";
        if(!fileName.toLowerCase().endsWith(".vec")) fileName += ".vec";

        BufferedReader br = getBuffer();
        try {
            String line;
            while((line = br.readLine()) != null){
                addContent(line);
            }
            br.close();
        } catch (Exception e){}
    }

    public void setIndicatingTransparency(boolean indicateTransparency){
        indicatingTransparency = indicateTransparency;
    }

    public boolean isIndicatingTransparency(){
        return indicatingTransparency;
    }

    public void setModified(boolean isModified){
        this.isModified = isModified;
    }

    public void setFilePath(String filePath){
        if(!filePath.toLowerCase().endsWith(".vec")) filePath += ".vec";
        this.filePath = filePath;
    }

    public void setFileName(String fileName){
        if(!fileName.toLowerCase().endsWith(".vec")) fileName += ".vec";
        this.fileName = fileName;
    }

    public void addContent(String line){
        if(content == null){
            content = line + "\n";
        }else{
            content += line + "\n";
        }
    }

    public void saveFile(){
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
        } catch (Exception e){}
    }

    public boolean getModified(){
        return isModified;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getFileName(){
        return (isModified) ? fileName+"*" : fileName;
    }

    public String getContent(){
        return content;
    }

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
