package vecpaint;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.awt.*;

public class Tests {
    private VecModel model;
    private VecFile vecFile;

    @BeforeEach
    public void reset(){
        model = new VecModel();
        vecFile = new VecFile(null);
    }

    // ------------------------------- Start testing VecModel class ------------------------------- //
    @Test
    public void testModelConstruction(){
        assertEquals(Tool.PLOT, model.getCurrentTool());
        assertEquals(Color.BLACK, model.getPenColor());
        assertEquals(null, model.getFillColor());
    }

    @Test
    public void testModelGetSetPenColor(){
        model.setPenColor(Utility.GREEN);
        assertEquals(Utility.GREEN, model.getPenColor());
    }

    @Test
    public void testModelGetSetFillColor(){
        model.setFillColor(Utility.BLUE);
        assertEquals(Utility.BLUE, model.getFillColor());
    }

    @Test
    public void testModelGetSetCurrentTool(){
        model.setCurrentTool(Tool.POLYGON);
        assertEquals(Tool.POLYGON, model.getCurrentTool());
    }
    // ------------------------------- End testing VecModel class ------------------------------- //


    // ------------------------------- Start testing VecFile class ------------------------------- //
    @Test
    public void testFileConstruction(){
        assertEquals("untitled.vec*", vecFile.getFileName());
        assertEquals("untitled.vec", vecFile.getFilePath());
        assertTrue(vecFile.getModified());
    }


    // ------------------------------- End testing VecFile class ------------------------------- //
}
