package customcomponent;

import vecpaint.Utility;
import vecpaint.VecFile;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    private VecFile file;

    public Canvas(VecFile file){
        setOpaque(false);

        this.file = file;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int h = getHeight();
        int w = getWidth();

        if(file.isIndicatingTransparency()){
            drawTransparencyGrid(g, 8);
        }else{
            g.setColor(Color.WHITE);
            g.fillRect(0,0, w, h);
        }

        super.paintComponent(g);
    }

    private void drawTransparencyGrid(Graphics g, int gridSize){
        int h = getHeight();
        int w = getWidth();
        int bigger = (h <= w) ? w : h;
        int squareSize = gridSize;
        int numSquares = (int)Math.ceil(bigger/squareSize);

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
                g.fillRect(j*squareSize, i*squareSize, squareSize, squareSize);
            }
        }
    }
}
