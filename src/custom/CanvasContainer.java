package custom;

import vecpaint.Utility;

import javax.swing.*;

/**
 * Container for the canvas that utilises the custom CanvasContainerLayout to center and resize the canvas
 */
public class CanvasContainer extends JPanel {
    /**
     * The canvas this container holds
     */
    private Canvas canvas;

    /**
     * Creates a JPanel that contains the canvas
     * @param canvas the canvas
     */
    public CanvasContainer(Canvas canvas){
        this.canvas = canvas;
        setLayout(new CanvasContainerLayout());
        setBackground(Utility.GREY700);

        add(this.canvas);
    }
}
