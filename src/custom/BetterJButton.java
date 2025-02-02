package custom;

import javax.swing.*;
import java.awt.*;

/**
 * Supports translucent backgrounds
 */
public class BetterJButton extends JButton {

    /**
     * Makes a BetterJButton
     * @param opaque Specifies whether the button will have a translucent background.
     */
    public BetterJButton(boolean opaque){
        setOpaque(opaque);
    }

    /**
     * Paints the BetterJButton at the specified color respecting transparency
     */
    @Override
    protected void paintComponent(Graphics g){
        if(!isOpaque()){
            Color bg = getBackground();

            drawTransparencyGrid(g, 4);

            g.setColor(bg);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }

    /**
     * Draws a checker board pattern as the background
     * @param g Graphics of this button
     * @param gridSize Size of each square in the checker board pattern
     */
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
