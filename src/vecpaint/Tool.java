package vecpaint;

public enum Tool {
    PLOT("Plot"),
    LINE("Line"),
    RECTANGLE("Rectangle"),
    ELLIPSE("Ellipse"),
    POLYGON("Polygon");

    private String name;

    Tool(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
