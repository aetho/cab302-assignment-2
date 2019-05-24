package vecpaint;

/**
 * Tools enum
 */
public enum Tool {
    /**
     * Plot tool
     */
    PLOT("Plot"),
    /**
     * Line tool
     */
    LINE("Line"),
    /**
     * Rectangle tool
     */
    RECTANGLE("Rectangle"),
    /**
     * Ellipse tool
     */
    ELLIPSE("Ellipse"),
    /**
     * Polygon tool
     */
    POLYGON("Polygon");

    private String name;

    /**
     * Creates a tool corresponding to the provided name
     * @param name tool name
     */
    Tool(String name){
        this.name = name;
    }

    /**
     * Gets the tool name as a string
     * @return name as a string
     */
    public String getName(){
        return name;
    }
}
