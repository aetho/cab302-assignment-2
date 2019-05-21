package vecpaint;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class VecController {
    private VecModel model;
    private VecGUI view;

    public VecController(VecModel model, VecGUI gui){
        this.model = model;
        this.view = gui;

        addFileMenuListener();
        addColorListener();
        addToolListener();
        addPickPenListener();
        addPickFillListener();
        addTransparencyListener();

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
            toolButtons.get(tool).addActionListener(new ToolListener());
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

    public void addFileMenuListener(){
        JMenuBar menuBar = view.getJMenuBar();
        JMenu fileMenu = menuBar.getMenu(0);

        for(int i = 0; i < fileMenu.getItemCount(); i++){
            fileMenu.getItem(i).addActionListener(new FileMenuListener());
        }
    }

    public void addTransparencyListener(){
        JButton trans = view.getTransparencyButton();
        trans.addActionListener(new TransparencyListener());
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

    private class ToolListener implements ActionListener {
        public void actionPerformed (ActionEvent e){
            Object src = e.getSource();
            if(src instanceof JButton){
                JButton btn = (JButton)src;
                String btnString = btn.getText().toUpperCase();
                model.setCurrentTool(Tool.valueOf(btnString));
            }
        }
    }

    private class PickPenListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            Object src = e.getSource();
            if(src instanceof JButton){
                if(SwingUtilities.isLeftMouseButton(e)){
                    Color c = JColorChooser.showDialog(null,"Choose pen color", model.getPenColor());
                    if(c == null) c = model.getPenColor(); // Return to previous color if none is chosen.
                    model.setPenColor(c);
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
                    Color c = JColorChooser.showDialog(null,"Choose fill color", model.getFillColor());
                    if(c == null) c = model.getFillColor(); // Return to previous color if none is chosen.
                    model.setFillColor(c);
                }else if(SwingUtilities.isRightMouseButton(e)){
                    model.setFillColor(null); // Return to default color if button is right-clicked.
                }
            }
        }
    }

    private class FileMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if(src instanceof JMenuItem){
                if(src == view.getNewFileItem()){
                    model.newFile();
                }else if(src == view.getOpenFileItem()) {
                    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    jfc.setDialogTitle("Select a VEC file");
                    jfc.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("VEC file", "vec");
                    jfc.addChoosableFileFilter(filter);

                    // Choose file and pass it to the model to open
                    int returnValue = jfc.showOpenDialog(null);
                    if(returnValue == JFileChooser.APPROVE_OPTION){
                        model.openFile(jfc.getSelectedFile());
                    }
                }else if(src == view.getSaveFileItem()){
                    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    jfc.setDialogTitle("Choose save folder");
                    jfc.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("VEC file", "vec");
                    jfc.addChoosableFileFilter(filter);

                    // Choose file and past it to the model to save
                    int returnValue= jfc.showSaveDialog(null);
                    if(returnValue == JFileChooser.APPROVE_OPTION){
                        model.saveFile(jfc.getSelectedFile(), view.getTabs().getSelectedIndex());
                    }
                }else if(src == view.getCloseFileItem()){
                    // Close file
                    JTabbedPane tabs = view.getTabs();
                    int selectedTab = tabs.getSelectedIndex();
                    model.closeFile(selectedTab);

                    // Select the index before the closed file.
                    if(tabs.getTabCount() > 0){
                        tabs.setSelectedIndex((selectedTab > 0) ? selectedTab-1 : 0);
                    }
                }else if(src == view.getCloseAllFilesItem()){
                    // Close all files
                    model.closeAllFiles();
                }
            }
        }
    }

    private class TransparencyListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            JTabbedPane tabs = view.getTabs();
            model.toggleTransparency(tabs.getSelectedIndex());
        }
    }

}
