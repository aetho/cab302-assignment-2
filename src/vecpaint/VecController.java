package vecpaint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class VecController {
    private VecModel model;
    private VecGUI view;

    public VecController(VecModel model, VecGUI gui){
        this.model = model;
        this.view = gui;

        addColorListener();
        addToolListener();
        addPickPenListener();
        addPickFillListener();

        // Attach gui to model and send initial notification to GUI to update necessary components
        model.attach(gui);
        model.notifyObservers();
    }

    public void addColorListener(){
        Map<Color, JButton> paletteButtons = view.getPaletteButtons();
        for(Color c : paletteButtons.keySet()){
            paletteButtons.get(c).addMouseListener(new ColorListener());
        }
    }

    public void addToolListener(){
        Map<String, JButton> toolButtons = view.getToolButtons();
        for(String tool : toolButtons.keySet()){
            toolButtons.get(tool).addMouseListener(new ToolListener());
        }
    }

    public void addPickPenListener(){
        JButton pickPenButton = view.getPickPenButton();
        pickPenButton.addMouseListener(new PickPenListener());
    }

    public void addPickFillListener(){
        JButton pickFillButton = view.getPickFillButton();
        pickFillButton.addMouseListener(new PickFillListener());
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

    private class PickPenListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            Object src = e.getSource();
            if(src instanceof JButton){
                if(SwingUtilities.isLeftMouseButton(e)){
                    Color pen = JColorChooser.showDialog(null,"Choose pen color", model.getPenColor());
                    if(pen == null) pen = model.getPenColor(); // Return to previous color if none is chosen.
                    model.setPenColor(pen);
                }else if(SwingUtilities.isRightMouseButton(e)){
                    model.setPenColor(Color.BLACK); // Return to default color if button is right-clicked.
                }
            }
        }
    }

    private class PickFillListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            Object src = e.getSource();
            if(src instanceof JButton){
                if(SwingUtilities.isLeftMouseButton(e)){
                    Color fill = JColorChooser.showDialog(null,"Choose fill color", model.getFillColor());
                    if(fill == null) fill = model.getFillColor(); // Return to previous color if none is chosen.
                    model.setFillColor(fill);
                }else if(SwingUtilities.isRightMouseButton(e)){
                    model.setFillColor(null); // Return to default color if none is chosen.
                }
            }
        }
    }

}
