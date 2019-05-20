package vecpaint;

import java.io.File;

public class VecFile {
    private boolean isModified;
    private String filePath;
    private String fileName;

    public VecFile(File file){
        this.filePath = (file == null) ? "untitled.vec" :  file.getAbsolutePath();
        this.fileName = (file == null) ? "untitled.vec" : file.getName();
        this.isModified = file == null;

    }

    public void setModified(boolean isModified){
        this.isModified = isModified;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
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

}
