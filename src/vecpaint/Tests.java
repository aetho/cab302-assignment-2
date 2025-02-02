package vecpaint;
import static org.junit.jupiter.api.Assertions.*;

import custom.Canvas;
import custom.CanvasContainer;
import custom.Tool;
import custom.Utility;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Tests {
    private File dummy;
    private VecFile vecFile;
    private VecModel model;

    @BeforeEach
    public void reset(){
        dummy = new File("dummy.vec");
        vecFile = new VecFile(dummy);
        model = new VecModel();
    }

    @AfterEach
    public void breakDown(){
        // Reset dummy file after each test
        try {
            FileWriter writer = new FileWriter("dummy.vec");
            writer.write("");
            writer.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // ------------------------------- Start testing Utility class ------------------------------- //
    @Test
    public void testUtilityTranslucent(){
        Color c1 = new Color(63,127,255);
        Color ca1 = Utility.getTranslucentColor(c1, 128);
        assertEquals(63, ca1.getRed());
        assertEquals(127, ca1.getGreen());
        assertEquals(255, ca1.getBlue());
        assertEquals(128, ca1.getAlpha());

        Color c2 = new Color(63,127,255,32);
        Color ca2 = Utility.getTranslucentColor(c2, 64);
        assertEquals(63, ca2.getRed());
        assertEquals(127, ca2.getGreen());
        assertEquals(255, ca2.getBlue());
        assertEquals(32, ca2.getAlpha());
    }

    @Test
    public void testVecParseColor() throws Exception{
        String[] hexStr = {"#00FF00", "FF"};
        Color c = Utility.vecParseColor(hexStr);
        assertEquals(0, c.getRed());
        assertEquals(255, c.getGreen());
        assertEquals(0, c.getBlue());
        assertEquals(255, c.getAlpha());

        String[] hexStr2 = {"#00GG00", "FF"};
        Exception thrown = assertThrows(Exception.class, ()->{
            Color a = Utility.vecParseColor(hexStr2);
        });
        assertEquals("Invalid color format, please check the file. Correct example: \"PEN #000000\" ", thrown.getMessage());
    }

    @Test
    public void testVecArgsToInt() throws Exception{
        String[] str = {"1.0", "0.5", "1.0"};
        Integer[] integers = Utility.vecArgsToInt(str, 10);
        assertArrayEquals(new Integer[]{10, 5, 10}, integers);

        String[] str2 = new String[]{"1.0", "wrong", "#352837"};
        Exception thrown = assertThrows(Exception.class, ()->{
            Integer[] integers2 = Utility.vecArgsToInt(str2, 10);
        });

        assertEquals("Unable to parse arguments. Please check file.", thrown.getMessage());
    }
    // ------------------------------- End testing Utility class ------------------------------- //


    // ------------------------------- Start testing Canvas class ------------------------------- //
    @Test
    public void testCanvasConstruction(){
        Canvas c = new Canvas(null, model);
        assertNotNull(c);
    }
    // ------------------------------- End testing Canvas class ------------------------------- //


    // ------------------------------- Start testing Canvas container class ------------------------------- //
    @Test
    public void testCanvasContainerConstruction(){
        Canvas c = new Canvas(null, model);
        assertNotNull(c);
        CanvasContainer cc = new CanvasContainer(c);
        assertNotNull(cc);
    }
    // ------------------------------- End testing Canvas container class ------------------------------- //


    // ------------------------------- Start testing VecFile class ------------------------------- //
    @Test
    public void testFileConstructionWithParams(){
        assertEquals("dummy.vec", vecFile.getFileName());
        assertEquals(dummy.getAbsolutePath(), vecFile.getFilePath());
        assertFalse(vecFile.getModified());
        assertFalse(vecFile.isIndicatingTransparency());
        assertEquals(0, vecFile.getContent().size());
    }

    @Test
    public void testFileConstructionWithoutParams(){
        vecFile = new VecFile(null);
        assertEquals("untitled.vec*", vecFile.getFileName());
        assertEquals("untitled.vec", vecFile.getFilePath());
        assertTrue(vecFile.getModified());
        assertFalse(vecFile.isIndicatingTransparency());
        assertEquals(0, vecFile.getContent().size());
    }

    @Test
    public void testSetGetTransparency(){
        assertFalse(vecFile.isIndicatingTransparency());
        vecFile.setIndicatingTransparency(true);
        assertTrue(vecFile.isIndicatingTransparency());
    }

    @Test
    public void testSetGetModified(){
        assertFalse(vecFile.getModified());
        vecFile.setModified(true);
        assertTrue(vecFile.getModified());
    }

    @Test
    public void testSetGetFileName(){
        assertEquals("dummy.vec", vecFile.getFileName());
        vecFile.setModified(true);
        assertEquals("dummy.vec*", vecFile.getFileName());
        vecFile.setModified(false);

        vecFile.setFileName("change.vec");
        assertEquals("change.vec", vecFile.getFileName());
        vecFile.setModified(true);
        assertEquals("change.vec*", vecFile.getFileName());
    }

    @Test
    public void testSetGetFilePath(){
        assertEquals(dummy.getAbsolutePath(), vecFile.getFilePath());
        vecFile.setFilePath(" ");
        assertEquals(" .vec", vecFile.getFilePath());
    }

    @Test
    public void testSetGetAddContent(){
        assertEquals(0, vecFile.getContent().size());

        vecFile.addContent("LINE 1");
        assertEquals(1, vecFile.getContent().size());
        assertEquals("LINE 1", vecFile.getContent().get(0));
        vecFile.addContent("LINE 2");
        assertEquals(2, vecFile.getContent().size());
        assertEquals("LINE 2", vecFile.getContent().get(1));

        List<String> temp = new ArrayList<>();
        vecFile.setContent(temp);
        assertEquals(0, vecFile.getContent().size());
        temp.add("LINE 1");
        temp.add("LINE 2");
        temp.add("LINE 3");
        vecFile.setContent(temp);
        assertEquals(3, vecFile.getContent().size());
        assertEquals("LINE 1", vecFile.getContent().get(0));
        assertEquals("LINE 2", vecFile.getContent().get(1));
        assertEquals("LINE 3", vecFile.getContent().get(2));
    }

    @Test
    public void testVecSaveFile(){
        vecFile.addContent("VEC FILE SAVE TEST");
        vecFile.saveFile();
        try {
            FileReader fr = new FileReader("dummy.vec");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            assertEquals("VEC FILE SAVE TEST", line);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // ------------------------------- End testing VecFile class ------------------------------- //


    // ------------------------------- Start testing VecModel class ------------------------------- //
    @Test
    public void testModelConstruction(){
        assertEquals(Tool.PLOT, model.getCurrentTool());
        assertEquals(Color.BLACK, model.getPenColor());
        assertNull(model.getFillColor());
        assertEquals(1, model.getOpenedFiles().size());
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

    @Test
    public void testNewFile(){
        assertEquals(1, model.getOpenedFiles().size());
        model.newFile();
        assertEquals(2, model.getOpenedFiles().size());
    }

    @Test
    public void testOpenFile(){
        assertEquals(1, model.getOpenedFiles().size());
        model.openFile(dummy);
        assertEquals(2, model.getOpenedFiles().size());
    }

    @Test
    public void testUpdateFile(){
        vecFile = new VecFile(dummy);
        assertEquals(0, vecFile.getContent().size());
        model.openFile(dummy);
        model.updateFile(1, "FILE UPDATE TEST");
        String line = model.getOpenedFiles().get(1).getContent().get(0);
        assertEquals("FILE UPDATE TEST", line);
    }

    @Test
    public void testModelSaveFile(){
        vecFile = new VecFile(dummy);
        assertEquals(0, vecFile.getContent().size());
        model.openFile(dummy);
        model.updateFile(1, "MODEL FILE SAVE TEST");
        model.saveFile(dummy, 1);

        try {
            FileReader fr = new FileReader("dummy.vec");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            assertEquals("MODEL FILE SAVE TEST", line);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCloseFile(){
        assertEquals(1, model.getOpenedFiles().size());
        model.closeFile(0);
        assertEquals(0, model.getOpenedFiles().size());
    }

    @Test
    public void testCloseAllFiles(){
        assertEquals(1, model.getOpenedFiles().size());
        model.newFile();
        model.newFile();
        model.newFile();
        assertEquals(4, model.getOpenedFiles().size());
        model.closeAllFiles();
        assertEquals(0, model.getOpenedFiles().size());
    }

    @Test
    public void testUndo(){
        assertEquals(1, model.getOpenedFiles().size());
        String line = String.format("RECTANGLE %f %f %f %f", 0f, 0f, 0.75f, 0.75f);
        model.updateFile(0, "PEN #000000 FF");
        model.updateFile(0, "FILL #FF0000 FF");
        model.updateFile(0, line);
        model.updateFile(0, "FILL OFF");

        line = String.format("RECTANGLE %f %f %f %f", 0.25f, 0.25f, 1f, 1f);
        model.updateFile(0, "PEN #000000 FF");
        model.updateFile(0, "FILL #FF0000 FF");
        model.updateFile(0, line);
        model.updateFile(0, "FILL OFF");

        int size = model.getOpenedFiles().get(0).getContent().size();
        assertEquals(8, size);
        model.undo(0);
        size = model.getOpenedFiles().get(0).getContent().size();
        assertEquals(4, size);
        model.undo(0);
        size = model.getOpenedFiles().get(0).getContent().size();
        assertEquals(0, size);

    }

    @Test
    public void testGetHexFillPenColor(){
        assertEquals("#000000 FF", model.getPenColorHexStr());
        model.setFillColor(new Color(0,255,0,255));
        assertEquals("#00FF00 FF", model.getFillColorHexStr());
    }


    @Test
    public void testToggleTransparency(){
        assertFalse(model.getOpenedFiles().get(0).isIndicatingTransparency());
        model.toggleTransparency(0);
        assertTrue(model.getOpenedFiles().get(0).isIndicatingTransparency());
        model.toggleTransparency(0);
        assertFalse(model.getOpenedFiles().get(0).isIndicatingTransparency());
    }
    // ------------------------------- End testing VecModel class ------------------------------- //
}
