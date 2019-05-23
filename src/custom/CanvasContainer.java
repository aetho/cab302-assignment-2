package custom;

import vecpaint.Utility;

import javax.swing.*;

public class CanvasContainer extends JPanel {
    private Canvas canvas;

    public CanvasContainer(Canvas canvas){
        this.canvas = canvas;
        setLayout(new CanvasContainerLayout());
        setBackground(Utility.GREY700);

        add(this.canvas);
    }

}
