package customcomponent;

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

    public Canvas(VecFile file, VecModel model){
        setOpaque(false);
        this.file = file;
        this.model = model;

        addLineListener();
    }

    private void addLineListener(){
        MouseAdapter lineMA = new MouseAdapter(){
            private Point pressedPoint;
            private Point releasedPoint;

            public void mousePressed(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.LINE){
                    pressedPoint = e.getPoint();
                }
            }

            public void mouseDragged(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.LINE) {
                    Point draggedPoint = e.getPoint();

                    int x1 = pressedPoint.x;
                    int y1 = pressedPoint.y;

                    int x2 = draggedPoint.x;
                    int y2 = draggedPoint.y;

                    getGraphics().drawLine(x1, y1, x2, y2);
                    repaint();
                }
            }

            public void mouseReleased(MouseEvent e){
                Tool cTool = model.getCurrentTool();
                if(cTool == Tool.LINE) {
                    releasedPoint = e.getPoint();
                    int x1 = pressedPoint.x;
                    int y1 = pressedPoint.y;

                    int x2 = releasedPoint.x;
                    int y2 = releasedPoint.y;

                    int fileIndex = model.getOpenedFiles().indexOf(file);

                    String line = String.format("LINE %f %f %f %f", x1/640.0, y1/640.0, x2/640.0, y2/640.0);
                    if(model.getPenColor() != null){
                        model.updateFile(fileIndex, "PEN " + model.getPenColorHexStr());
                    }
                    if(model.getFillColor() != null){
                        model.updateFile(fileIndex, "FILL " + model.getFillColorHexStr());
                    }
                    model.updateFile(fileIndex, line);
                }
            }
        };

        addMouseListener(lineMA);
        addMouseMotionListener(lineMA);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int h = getHeight();
        int w = getWidth();

        // Paint background
        if(file.isIndicatingTransparency()){
            drawTransparencyGrid(g, 8);
        }else{
            g.setColor(Color.WHITE);
            g.fillRect(0,0, w, h);
        }

        System.out.println(file.getFileName());
        System.out.println(file.getContent());


        // Paint file
        Color pen = null;
        Color fill = null;
        if(file.getContent() != null){
            String[] content = file.getContent().split("\n");
            for(String line : content){
                String[] lineArray = line.split(" ");
                String cmd = lineArray[0].toUpperCase();

                String[] strArgs = new String[lineArray.length-1];
                for(int i = 0; i < lineArray.length-1; i++){
                    strArgs[i] = lineArray[i+1];
                }

                if(cmd.equals("PEN")) pen = vecParseColor(strArgs);
                if(cmd.equals("FILL")) fill = vecParseColor(strArgs);

                if(cmd.equals("LINE")){
                    System.out.println("Inside cmd line");
                    vecDrawLine(g, strArgs, pen);
                }
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
//        if(args.length > 2) return null; // [todo] write exeception
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

    private void vecDrawLine(Graphics g, String[] strArgs, Color pen){
        g.setColor(pen);
        Integer[] intArgs = new Integer[strArgs.length];

        for(int i = 0; i < strArgs.length; i++){
            // Scaling up args
            intArgs[i] = (int)Math.round(Double.parseDouble(strArgs[i]) * 640);
        }

        g.drawLine(intArgs[0], intArgs[1], intArgs[2], intArgs[3]);
    }

}
