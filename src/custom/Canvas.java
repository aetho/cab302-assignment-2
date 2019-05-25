package custom;

import vecpaint.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of JPanel that allows for mouse directed painting
 */
public class Canvas extends JPanel {
    /**
     * VecFile associated to this canvas
     */
    private VecFile file;

    /**
     * Model this canvas is associated with
     */
    private VecModel model;

    /**
     * Height of canvas
     */
    private int h;

    /**
     * Width of canvas
     */
    private int w;

    /**
     * Temporary variable used for line preview drawing
     */
    private int[] tempLine = new int[4];

    /**
     * Temporary variable used for rectangle preview drawing
     */
    private Rectangle tempRect;

    /**
     * Temporary variable used for ellipse preview drawing
     */
    private int[] tempOval = new int[4];

    /**
     * Temporary variable used for polygon preview drawing
     */
    private List<Point> tempPoly = new ArrayList<>();

    /**
     * Creates a Canvas object
     * @param file The file this canvas is associated to. This canvas will draw the contents in the file
     * @param model The model that contains the VecFile of this canvas. This allows the canvas to update the file
     */
    public Canvas(VecFile file, VecModel model){
        setOpaque(false);
        this.file = file;
        this.model = model;

        // Add listeners
        addPlotListener();
        addLineListener();
        addRectListener();
        addEllipseListener();
        addPolyListener();
    }

    /**
     * Adds the mouse and mouse motion listener to the canvas to allow for line drawing
     */
    private void addLineListener(){
        MouseAdapter lineMA = new MouseAdapter(){
            private Point pressedPoint;
            private Point releasedPoint;

            public void mousePressed(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.LINE && SwingUtilities.isLeftMouseButton(e)) {
                    pressedPoint = e.getPoint();
                }
            }

            public void mouseDragged(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.LINE && SwingUtilities.isLeftMouseButton(e)) {
                    Point draggedPoint = e.getPoint();

                    // Set points of line preview
                    tempLine[0] = pressedPoint.x; // X1
                    tempLine[1] = pressedPoint.y; // Y1
                    tempLine[2] = draggedPoint.x; // X2
                    tempLine[3] = draggedPoint.y; // Y2

                    repaint();
                }
            }

            public void mouseReleased(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.LINE && SwingUtilities.isLeftMouseButton(e)) {
                    releasedPoint = e.getPoint();
                    int x1 = pressedPoint.x;
                    int y1 = pressedPoint.y;
                    int x2 = releasedPoint.x;
                    int y2 = releasedPoint.y;


                    // Update file
                    int fileIndex = model.getOpenedFiles().indexOf(file);
                    String line = String.format("LINE %f %f %f %f", (double)x1/w, (double)y1/w, (double)x2/w, (double)y2/w);
                    if(model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);

                    // Remove line preview
                    tempLine[0] = -1;
                    tempLine[1] = -1;
                    tempLine[2] = -1;
                    tempLine[3] = -1;

                    repaint();
                }
            }
        };

        addMouseListener(lineMA);
        addMouseMotionListener(lineMA);
    }

    /**
     * Adds the mouse listener to the canvas to allow for plotting (drawing dots)
     */
    private void addPlotListener(){
        MouseAdapter plotMA = new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.PLOT && SwingUtilities.isLeftMouseButton(e)) {
                    Point click = e.getPoint();

                    // Update file
                    int fileIndex = model.getOpenedFiles().indexOf(file);
                    String line = String.format("PLOT %f %f", (double)click.x/w, (double)click.y/w);
                    if(model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);
                }
            }
        };
        addMouseListener(plotMA);
    }

    /**
     * Adds the mouse and mouse motion listener to the canvas to allow for rectangle drawing
     */
    private void addRectListener(){
        MouseAdapter rectMA = new MouseAdapter() {
            private Point pressedPoint;
            private Point releasedPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.RECTANGLE && SwingUtilities.isLeftMouseButton(e)) {
                    pressedPoint = e.getPoint();
                    tempRect = null;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.RECTANGLE && SwingUtilities.isLeftMouseButton(e)) {
                    Point draggedPoint = e.getPoint();
                    int x1 = Math.min(pressedPoint.x, draggedPoint.x);
                    int y1 = Math.min(pressedPoint.y, draggedPoint.y);

                    int x2 = Math.max(pressedPoint.x, draggedPoint.x);
                    int y2 = Math.max(pressedPoint.y, draggedPoint.y);

                    int width = x2 - x1;
                    int height = y2 - y1;

                    // Create rectangle for preview and repaint canvas
                    if (tempRect == null) tempRect = new Rectangle(x1, y1, width, height);
                    else tempRect.setBounds(x1, y1, width, height);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.RECTANGLE && SwingUtilities.isLeftMouseButton(e)) {
                    releasedPoint = e.getPoint();
                    int x1 = Math.min(pressedPoint.x, releasedPoint.x);
                    int y1 = Math.min(pressedPoint.y, releasedPoint.y);

                    int x2 = Math.max(pressedPoint.x, releasedPoint.x);
                    int y2 = Math.max(pressedPoint.y, releasedPoint.y);

                    // Update file
                    String line = String.format("RECTANGLE %f %f %f %f", (double)x1/w, (double)y1/w, (double)x2/w, (double)y2/w);
                    int fileIndex = model.getOpenedFiles().indexOf(file);
                    if(model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL OFF");

                    // Remove preview and repaint
                    tempRect = null;
                    repaint();
                }
            }
        };
        addMouseListener(rectMA);
        addMouseMotionListener(rectMA);
    }

    /**
     * Adds the mouse and mouse motion listener to the canvas to allow for ellipse drawing
     */
    private void addEllipseListener(){
        MouseAdapter ellipseMA = new MouseAdapter() {
            private Point pressedPoint;
            private Point releasedPoint;

            public void mousePressed(MouseEvent e) {
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.ELLIPSE && SwingUtilities.isLeftMouseButton(e)) {
                    pressedPoint = e.getPoint();
                }
            }

            public void mouseDragged(MouseEvent e) {
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.ELLIPSE && SwingUtilities.isLeftMouseButton(e)) {
                    Point draggedPoint = e.getPoint();

                    // Update ellipse preview coordinates
                    tempOval[0] = Math.min(pressedPoint.x, draggedPoint.x); // X1
                    tempOval[1] = Math.min(pressedPoint.y, draggedPoint.y); // Y1
                    tempOval[2] = Math.max(pressedPoint.x, draggedPoint.x); // X2
                    tempOval[3] = Math.max(pressedPoint.y, draggedPoint.y); // Y2

                    repaint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.ELLIPSE && SwingUtilities.isLeftMouseButton(e)) {
                    releasedPoint = e.getPoint();
                    int x1 = Math.min(pressedPoint.x, releasedPoint.x);
                    int y1 = Math.min(pressedPoint.y, releasedPoint.y);
                    int x2 = Math.max(pressedPoint.x, releasedPoint.x);
                    int y2 = Math.max(pressedPoint.y, releasedPoint.y);

                    // Update file
                    int fileIndex = model.getOpenedFiles().indexOf(file);
                    String line = String.format("ELLIPSE %f %f %f %f", (double)x1/w, (double)y1/w, (double)x2/w, (double)y2/w);
                    if (model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if (model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);
                    if (model.getFillColor() != null) model.updateFile(fileIndex, "FILL OFF");

                    // Remove ellipse preview
                    tempOval[0] = -1;
                    tempOval[1] = -1;
                    tempOval[2] = -1;
                    tempOval[3] = -1;

                    repaint();
                }
            }
        };

        addMouseListener(ellipseMA);
        addMouseMotionListener(ellipseMA);
    }

    /**
     * Add the mouse and mouse motion listener to the canvas to allow for polygon drawing
     */
    private void addPolyListener(){
        MouseAdapter polyMA = new MouseAdapter() {
            boolean drawing = false;

            public void mousePressed(MouseEvent e) {
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.POLYGON){
                    if(SwingUtilities.isLeftMouseButton(e)){
                        tempPoly.add(e.getPoint());
                        drawing = true;
                    }else if(SwingUtilities.isRightMouseButton(e)){
                        // Update file if at least 3 point have been drawn
                        if(tempPoly.size() > 2){
                            String line = "POLYGON";
                            for(Point p : tempPoly){
                                line += String.format(" %f %f",(double)p.x/w, (double)p.y/w);
                            }
                            int fileIndex = model.getOpenedFiles().indexOf(file);
                            if (model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                            if (model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                            model.updateFile(fileIndex, line);
                            if (model.getFillColor() != null) model.updateFile(fileIndex, "FILL OFF");
                        }

                        // Remove preview and finish drawing
                        tempPoly.clear();
                        drawing = false;
                        repaint();
                    }
                }
            }

            public void mouseMoved(MouseEvent e){
                Tool cTool = model.getCurrentTool();

                // Set preview points if currently drawing
                if(cTool == Tool.POLYGON && drawing){
                    int size = tempPoly.size();
                    Point currentPoint = e.getPoint();
                    if(size == 1) tempPoly.add(currentPoint);
                    else if(size > 1) tempPoly.set(size-1, currentPoint);
                    repaint();
                }
            }
        };

        addMouseListener(polyMA);
        addMouseMotionListener(polyMA);
    }


    /**
     * Paints the canvas
     * @param g the Graphics object
     */
    @Override
    protected void paintComponent(Graphics g)  {
        h = getHeight();
        w = getWidth();

        // Paint background
        if(file.isIndicatingTransparency()){
            Utility.drawTransparencyGrid(g, w, h,8);
        }else{
            g.setColor(Color.WHITE);
            g.fillRect(0,0, w, h);
        }

        // Paint file
        Color penTemp = Color.BLACK;    // Temporary color use to draw file contents
        Color fillTemp = null;          // Temporary color to use to draw file contents

        if(file.getContent() != null){
            // Iterate through file content
            for(String line : file.getContent()){
                // Split line at spaces
                String[] lineArray = line.split(" ");

                // Extract command from line
                String cmd = lineArray[0].toUpperCase();

                // Extract arguments from line
                String[] strArgs = new String[lineArray.length-1];
                for(int i = 0; i < lineArray.length-1; i++){
                    strArgs[i] = lineArray[i+1];
                }

                // Handle commands accordingly
                try {
                    switch (cmd) {
                        case "PEN":
                            penTemp = Utility.vecParseColor(strArgs);
                            break;
                        case "FILL":
                            fillTemp = Utility.vecParseColor(strArgs);
                            break;
                        case "PLOT":
                            vecPlot(g, strArgs, penTemp);
                            break;
                        case "LINE":
                            vecDrawLine(g, strArgs, penTemp);
                            break;
                        case "RECTANGLE":
                            vecDrawRect(g, strArgs, penTemp, fillTemp);
                            break;
                        case "ELLIPSE":
                            vecDrawEllipse(g, strArgs, penTemp, fillTemp);
                            break;
                        case "POLYGON":
                            vecDrawPoly(g, strArgs, penTemp, fillTemp);
                            break;
                    }
                }catch (Exception e){
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                            int idx = model.getOpenedFiles().indexOf(file);
                            model.closeFile(idx);
                        }
                    });
                }
            }
        }


        // Set pen colors to be lighter to indicate that it's a preview
        Color prevPen = Utility.getTranslucentColor(model.getPenColor(), 64);      // Preview pen color
        Color prevFill = Utility.getTranslucentColor(model.getFillColor(), 64);    // Preview fill color

        // Line preview
        if(tempLine != null){
            g.setColor(prevPen);
            g.drawLine(tempLine[0], tempLine[1], tempLine[2], tempLine[3]);
        }

        // Rectangle preview
        if(tempRect != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            if(model.getFillColor() != null){
                g2d.setColor(prevFill);
                g2d.fill(tempRect);
            }

            g2d = (Graphics2D) g.create();
            g2d.setColor(prevPen);
            g2d.draw(tempRect);
            g2d.dispose();
        }

        // Ellipse preview
        if(tempOval != null){
            int eWidth = tempOval[2] - tempOval[0];
            int eHeight = tempOval[3] - tempOval[1];
            if(model.getFillColor() != null){
                g.setColor(prevFill);
                g.fillOval(tempOval[0], tempOval[1], eWidth, eHeight);
            }
            g.setColor(prevPen);
            g.drawOval(tempOval[0], tempOval[1], eWidth, eHeight);
        }

        // Polygon preview
        if(tempPoly.size() > 1){
            for(int i = 0; i < tempPoly.size() - 1; i++){
                Point p1 = tempPoly.get(i);
                Point p2 = tempPoly.get(i+1);
                g.setColor(prevPen);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            if(tempPoly.size() > 2){
                int[] x = {tempPoly.get(0).x};
                int[] y = {tempPoly.get(0).y};
                Polygon poly = new Polygon(x, y, 0);
                for(Point p : tempPoly){
                    poly.addPoint(p.x, p.y);
                }

                if(model.getFillColor() != null){
                    g.setColor(prevFill);
                    g.fillPolygon(poly);
                }

                g.setColor(prevPen);
                g.drawPolygon(poly);
            }
        }

        super.paintComponent(g);
        g.dispose();
    }

    /**
     * Draws a dot in the specified Color at the specified location
     * @param g Graphics of this canvas
     * @param strArgs location. E.g. ["0.5","0.5"]
     * @param pen Pen color
     * @throws Exception Thrown when Utility.vecArgsToInt() fails
     */
    private void vecPlot(Graphics g, String[] strArgs, Color pen) throws Exception {
        try {
            g.setColor(pen);
            Integer[] intArgs = Utility.vecArgsToInt(strArgs, getHeight());
            g.drawLine(intArgs[0], intArgs[1], intArgs[0], intArgs[1]);
        }catch (Exception e){throw e;}
    }

    /**
     * Draws a line in the specified Color at the specified location
     * @param g Graphics of this canvas
     * @param strArgs location. E.g. ["0.2","0.2","0.8","0.8"]
     * @param pen Pen color
     * @throws Exception Thrown when Utility.vecArgsToInt() fails
     */
    private void vecDrawLine(Graphics g, String[] strArgs, Color pen) throws Exception {
        try{
            g.setColor(pen);
            Integer[] intArgs = Utility.vecArgsToInt(strArgs, getHeight());
            g.drawLine(intArgs[0], intArgs[1], intArgs[2], intArgs[3]);
        }catch (Exception e){throw e;}
    }

    /**
     * Draws a rectangle at the specified location with the specified pen and fill color
     * @param g Graphics of this canvas
     * @param strArgs location. E.g. ["0.2","0.2","0.8","0.8"]
     * @param pen Pen color
     * @param fill Fill color
     * @throws Exception Thrown when Utility.vecArgsToInt() fails
     */
    private void vecDrawRect(Graphics g, String[] strArgs, Color pen, Color fill) throws Exception {
        try{
            Integer[] intArgs = Utility.vecArgsToInt(strArgs, getHeight());
            int x1 = intArgs[0];
            int y1 = intArgs[1];
            int x2 = intArgs[2];
            int y2 = intArgs[3];
            int w = x2 - x1;
            int h = y2 - y1;

            if(fill != null){
                g.setColor(fill);
                g.fillRect(x1, y1, w, h);
            }

            g.setColor(pen);
            g.drawRect(x1, y1, w, h);
        }catch (Exception e){throw e;}
    }

    /**
     * Draws a ellipse at the specified location with the specified pen and fill color
     * @param g Graphics of this canvas
     * @param strArgs location. E.g. ["0.2","0.2","0.8","0.8"]
     * @param pen Pen color
     * @param fill Fill color
     * @throws Exception Thrown when Utility.vecArgsToInt() fails
     */
    private void vecDrawEllipse(Graphics g, String[] strArgs, Color pen, Color fill) throws  Exception{
        try{
            Integer[] intArgs = Utility.vecArgsToInt(strArgs, getHeight());
            int x1 = intArgs[0];
            int y1 = intArgs[1];
            int x2 = intArgs[2];
            int y2 = intArgs[3];
            int w = x2 - x1;
            int h = y2 - y1;

            if(fill != null){
                g.setColor(fill);
                g.fillOval(x1, y1, w, h);
            }

            g.setColor(pen);
            g.drawOval(x1, y1, w, h);
        }catch (Exception e){throw e;}
    }

    /**
     * Draws a polygon at the specified locations with the specified pen and fill color
     * @param g Graphics of this canvas
     * @param strArgs location. E.g. ["0.2", "0.2", "0.8", "0.2", "0.8", "0.8"]
     * @param pen Pen color
     * @param fill Fill color
     * @throws Exception Thrown when Utility.vecArgsToInt() fails
     */
    private void vecDrawPoly(Graphics g, String[] strArgs, Color pen, Color fill) throws Exception{
        try{
            Integer[] intArgs = Utility.vecArgsToInt(strArgs, getHeight());
            int[] x = {intArgs[0]};
            int[] y = {intArgs[1]};

            Polygon poly = new Polygon(x, y, 0);
            for(int i = 0; i < intArgs.length/2; i++){
                poly.addPoint(intArgs[i*2], intArgs[i*2+1]);
            }

            if(fill != null){
                g.setColor(fill);
                g.fillPolygon(poly);
            }

            g.setColor(pen);
            g.drawPolygon(poly);
        }catch (Exception e){throw e;}
    }
}
