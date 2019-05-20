package vecpaint;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.awt.*;

public class Tests {
    private VecModel model;

    @BeforeEach
    public void reset(){
        model = new VecModel();
    }

    @Test
    public void testModelConstruction(){
        assertEquals(Tool.PLOT, model.getCurrentTool());
        assertEquals(Color.BLACK, model.getPenColor());
        assertEquals(null, model.getFillColor());
    }

    @Test
    public void testGetSetPenColor(){
        model.setPenColor(Utility.GREEN);
        assertEquals(Utility.GREEN, model.getPenColor());
    }

    @Test
    public void testGetSetFillColor(){
        model.setFillColor(Utility.BLUE);
        assertEquals(Utility.BLUE, model.getFillColor());
    }

    @Test
    public void testGetSetCurrentTool(){
        model.setCurrentTool(Tool.POLYGON);
        assertEquals(Tool.POLYGON, model.getCurrentTool());
    }
}
