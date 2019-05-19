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

        view.addColorListeners(new ColorListener());
        view.addToolListeners(new ToolListener());
        model.attach(gui);

        // Initial notification to GUI to update necessary components
        model.notifyObservers();
    }

    private class ColorListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            Object src = e.getSource();
            if(src instanceof JButton){
                JButton btn = (JButton)src;
                if(SwingUtilities.isLeftMouseButton(e)){
                    model.setPenColor(btn.getBackground());
                }else if(SwingUtilities.isRightMouseButton(e)){
                    model.setFillColor(btn.getBackground());
                }
            }
        }
    }

    private class ToolListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            Object src = e.getSource();
            if(src instanceof JButton){
                JButton btn = (JButton)src;
                if(SwingUtilities.isLeftMouseButton(e)){
                    String btnString = btn.getText().toUpperCase();
                    model.setCurrentTool(Tool.valueOf(btnString));
                }
            }
        }
    }

}
