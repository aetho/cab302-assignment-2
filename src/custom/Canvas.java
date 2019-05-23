package custom;

import vecpaint.Tool;
import vecpaint.Utility;
import vecpaint.VecFile;
import vecpaint.VecModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    private VecFile file;
    private VecModel model;
    private int h, w;

    private int[] tempLine = new int[4];
    private Rectangle tempRect;
    private int[] tempOval = new int[4];
    private List<Point> tempPoly = new ArrayList<>();

    public Canvas(VecFile file, VecModel model){
        setOpaque(false);
        this.file = file;
        this.model = model;

        addLineListener();
        addPlotListener();
        addRectListener();
        addEllipseListener();
        addPolyListener();
        addRectListener();
    }

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

                    tempLine[0] = pressedPoint.x;
                    tempLine[1] = pressedPoint.y;

                    tempLine[2] = draggedPoint.x;
                    tempLine[3] = draggedPoint.y;

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

                    int fileIndex = model.getOpenedFiles().indexOf(file);

                    String line = String.format("LINE %f %f %f %f", (double)x1/w, (double)y1/w, (double)x2/w, (double)y2/w);
                    if(model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);

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

    private void addPlotListener(){
        MouseAdapter plotMA = new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.PLOT && SwingUtilities.isLeftMouseButton(e)) {
                    Point click = e.getPoint();
                    String line = String.format("PLOT %f %f", (double)click.x/w, (double)click.y/w);
                    int fileIndex = model.getOpenedFiles().indexOf(file);
                    if(model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);
                }
            }
        };
        addMouseListener(plotMA);
    }

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

                    String line = String.format("RECTANGLE %f %f %f %f", (double)x1/w, (double)y1/w, (double)x2/w, (double)y2/w);
                    int fileIndex = model.getOpenedFiles().indexOf(file);
                    if(model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);
                    if(model.getFillColor() != null) model.updateFile(fileIndex, "FILL OFF");

                    tempRect = null;
                    repaint();
                }
            }
        };
        addMouseListener(rectMA);
        addMouseMotionListener(rectMA);
    }

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
                    tempOval[0] = Math.min(pressedPoint.x, draggedPoint.x);
                    tempOval[1] = Math.min(pressedPoint.y, draggedPoint.y);

                    tempOval[2] = Math.max(pressedPoint.x, draggedPoint.x);
                    tempOval[3] = Math.max(pressedPoint.y, draggedPoint.y);

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

                    String line = String.format("ELLIPSE %f %f %f %f", (double)x1/w, (double)y1/w, (double)x2/w, (double)y2/w);
                    int fileIndex = model.getOpenedFiles().indexOf(file);
                    if (model.getPenColor() != null) model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    if (model.getFillColor() != null) model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    model.updateFile(fileIndex, line);
                    if (model.getFillColor() != null) model.updateFile(fileIndex, "FILL OFF");

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
                        tempPoly.clear();
                        drawing = false;
                        repaint();
                    }
                }
            }

            public void mouseMoved(MouseEvent e){
                Tool cTool = model.getCurrentTool();
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


    @Override
    protected void paintComponent(Graphics g) {
        h = getHeight();
        w = getWidth();


        // Paint background
        if(file.isIndicatingTransparency()){
            drawTransparencyGrid(g,8);
        }else{
            g.setColor(Color.WHITE);
            g.fillRect(0,0, w, h);
        }

        // Paint file
        Color penTemp = Color.BLACK;
        Color fillTemp = null;
        if(file.getContent() != null){
            for(String line : file.getContent()){
                String[] lineArray = line.split(" ");
                String cmd = lineArray[0].toUpperCase();

                String[] strArgs = new String[lineArray.length-1];
                for(int i = 0; i < lineArray.length-1; i++){
                    strArgs[i] = lineArray[i+1];
                }

                if(cmd.equals("PEN")) penTemp = vecParseColor(strArgs);
                if(cmd.equals("FILL")) fillTemp = vecParseColor(strArgs);

                if (cmd.equals("PLOT")) vecPlot(g, strArgs, penTemp);
                else if(cmd.equals("LINE")) vecDrawLine(g, strArgs, penTemp);
                else if(cmd.equals("RECTANGLE")) vecDrawRect(g, strArgs, penTemp, fillTemp);
                else if(cmd.equals("ELLIPSE")) vecDrawEllipse(g, strArgs, penTemp, fillTemp);
                else if(cmd.equals("POLYGON")) vecDrawPoly(g, strArgs, penTemp, fillTemp);
            }
        }


        // Set pen colors to be lighter to indicate that it's a preview
        Color prevPen = getTranslucentColor(model.getPenColor(), 64);      // Preview pen color
        Color prevFill = getTranslucentColor(model.getFillColor(), 64);    // Preview fill color

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
    }

    private void drawTransparencyGrid(Graphics g, int gridSize){
        int h = getHeight();
        int w = getWidth();
        int bigger = (h <= w) ? w : h;
        int numSquares = (int)Math.ceil(bigger/gridSize);

        for(int i = 0; i < numSquares; i++){
            for(int j = 0; j < numSquares; j++){
                if(numSquares % 2 == 1){
                    if((i*numSquares + j) % 2 == 0){
                        g.setColor(Utility.GREY600);
                    }else {
                        g.setColor(Color.WHITE);
                    }
                }else{
                    if(i % 2 == 0){
                        if((i*numSquares + j) % 2 == 0) g.setColor(Color.WHITE);
                        else g.setColor(Utility.GREY600);
                    }else{
                        if((i*numSquares + j + 1) % 2 == 0) g.setColor(Color.WHITE);
                        else g.setColor(Utility.GREY600);
                    }
                }
                g.fillRect(j*gridSize, i*gridSize, gridSize, gridSize);
            }
        }
    }

    private Color vecParseColor(String[] args){
        if(args.length > 2) return null; // [todo] write exception
        if(args[0].toUpperCase().equals("OFF")) return null;
        Color c = Color.decode(args[0]);
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int a;
        if(args.length > 1){
            a = Integer.decode("#"+args[1]);
            return new Color(r,g,b,a);
        }else {
            return new Color(r,g,b);
        }
    }

    private Integer[] vecArgsToInt(String[] strArgs, boolean scaleUp){
        Integer[] intArgs = new Integer[strArgs.length];
        int scale = (scaleUp) ? getHeight() : 1;
        for(int i = 0; i < strArgs.length; i++){
            // parsing args to integer and scaling up args
            intArgs[i] = (int)Math.round(Double.parseDouble(strArgs[i]) * scale);
        }
        return intArgs;
    }

    private Color getTranslucentColor(Color c, int alpha){
        if (c == null) return null;
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        int a = (alpha < c.getAlpha()) ? alpha : c.getAlpha();

        return new Color(r, g, b, a);
    }

    private void vecPlot(Graphics g, String[] strArgs, Color pen){
        g.setColor(pen);
        Integer[] intArgs = vecArgsToInt(strArgs, true);
        g.drawLine(intArgs[0], intArgs[1], intArgs[0], intArgs[1]);
    }

    private void vecDrawLine(Graphics g, String[] strArgs, Color pen){
        g.setColor(pen);
        Integer[] intArgs = vecArgsToInt(strArgs, true);
        g.drawLine(intArgs[0], intArgs[1], intArgs[2], intArgs[3]);
    }

    private void vecDrawRect(Graphics g, String[] strArgs, Color pen, Color fill){
        Integer[] intArgs = vecArgsToInt(strArgs, true);
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
    }

    private void vecDrawEllipse(Graphics g, String[] strArgs, Color pen, Color fill){
        Integer[] intArgs = vecArgsToInt(strArgs, true);
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
    }

    private void vecDrawPoly(Graphics g, String[] strArgs, Color pen, Color fill){
        Integer[] intArgs = vecArgsToInt(strArgs, true);
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
    }
}
