package vecpaint;

import custom.Tool;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

/**
 * The controller of MVC architecture
 */
public class VecController {
    /**
     * The associated model
     */
    private VecModel model;

    /**
     * The associated view
     */
    private VecGUI view;

    /**
     * Creates a controller with the provided model and view/gui
     * @param model the model of MVC architecture
     * @param gui the view of MVC architecture
     */
    public VecController(VecModel model, VecGUI gui){
        this.model = model;
        this.view = gui;

        // Attach gui to model and send initial notification to GUI to update necessary components
        model.attach(gui);
        model.notifyObservers();

        // Add listeners
        addFileMenuListener();
        addEditMenuListener();
        addColorListener();
        addToolListener();
        addPickPenListener();
        addPickFillListener();
        addTransparencyListener();
    }

    /**
     * Add listeners to the color palette buttons
     */
    private void addColorListener(){
        Map<Color, JButton> paletteButtons = view.getPaletteButtons();
        for(Color c : paletteButtons.keySet()){
            paletteButtons.get(c).addMouseListener(new ColorListener());
        }
    }

    /**
     * Add listeners to the tool buttons
     */
    private void addToolListener(){
        Map<String, JButton> toolButtons = view.getToolButtons();
        for(String tool : toolButtons.keySet()){
            toolButtons.get(tool).addActionListener(new ToolListener());
        }
    }

    /**
     * Add listener for pen color picker
     */
    private void addPickPenListener(){
        JButton pickPenButton = view.getPickPenButton();
        pickPenButton.addMouseListener(new PickPenListener());
    }

    /**
     * Add listener for fill color picker
     */
    private void addPickFillListener(){
        JButton pickFillButton = view.getPickFillButton();
        pickFillButton.addMouseListener(new PickFillListener());
    }

    /**
     * Add listener to file menu items
     */
    private void addFileMenuListener(){
        JMenuBar menuBar = view.getJMenuBar();
        JMenu fileMenu = menuBar.getMenu(0);

        for(int i = 0; i < fileMenu.getItemCount(); i++){
            fileMenu.getItem(i).addActionListener(new FileMenuListener());
        }
    }

    /**
     * Add listener to the transparency indicator button
     */
    private void addTransparencyListener(){
        JButton trans = view.getTransparencyButton();
        trans.addActionListener(new TransparencyListener());
    }

    /**
     * Add listener to edit menu item
     */
    private void addEditMenuListener(){
        JMenuBar menuBar = view.getJMenuBar();
        JMenu editMenu = menuBar.getMenu(1);
        editMenu.getItem(0).addActionListener(new EditMenuListener());
    }

    /**
     * Listener for palette buttons
     */
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

    /**
     * Listener for tool buttons
     */
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

    /**
     * Listener for pen color picker
     */
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

    /**
     * Listener for fill color picker
     */
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

    /**
     * Listener for file menu items
     */
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
                        int tabCount = view.getTabs().getTabCount();
                        view.getTabs().setSelectedIndex(tabCount-1);
                    }

//                    JOptionPane.showMessageDialog(null, "TEST");
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

    /**
     * Listener for transparency indicator button
     */
    private class TransparencyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JTabbedPane tabs = view.getTabs();
            model.toggleTransparency(tabs.getSelectedIndex());
        }
    }

    /**
     * Listener for edit menu items
     */
    private class EditMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if(obj instanceof JMenuItem){
                model.undo(view.getTabs().getSelectedIndex());
            }
        }
    }
}
