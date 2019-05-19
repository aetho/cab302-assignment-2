package vecpaint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VecController {
    private VecModel model;
    private VecGUI view;

    public VecController(VecModel model, VecGUI gui){
        this.model = model;
        this.view = gui;

        gui.addColorListeners(new ColorListener());
        model.attach(gui);

        // Initial notification to GUI to update necessary components
        model.notifyObservers();
    }


    private class ColorListener extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            Component src = (Component)(e.getSource());
            if(SwingUtilities.isLeftMouseButton(e)){
                model.setPenColor(src.getBackground());
            }else if(SwingUtilities.isRightMouseButton(e)){
                model.setFillColor(src.getBackground());
            }
        }
    }
}
