package vecpaint;

import custom.BetterJButton;
import custom.Canvas;
import custom.CanvasContainer;
import observerpattern.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * The view of MVC Architecture
 */
public class VecGUI extends JFrame implements Observer {
    /**
     * Container panel for tool buttons
     */
    private JPanel toolsPanel = new JPanel();

    /**
     * Map containing String-JButton key-value pairs
     */
    private Map<String, JButton> btnTools = new HashMap<>();

    /**
     * Map containing Color-JButton key-value pairs
     */
    private Map<Color, JButton> btnPalette = new HashMap<>();

    /**
     * JTabbedPane containing opened files as tabs
     */
    private JTabbedPane tabs = new JTabbedPane();

    /**
     * Button for toggling transparency
     */
    private JButton btnTrans;

    /**
     * Container panel for color picker buttons
     */
    private JPanel pickPanel = new JPanel();

    /**
     * Pen color picker button
     */
    private JButton btnPickPen;

    /**
     * Fill color picker button
     */
    private JButton btnPickFill;

    /**
     * New file menu item
     */
    private JMenuItem newFileItem;

    /**
     * open file menu item
     */
    private JMenuItem openFileItem;

    /**
     * save file menu item
     */
    private JMenuItem saveFileItem;

    /**
     * close file menu item
     */
    private JMenuItem closeFileItem;

    /**
     * close all file menu item
     */
    private JMenuItem closeAllFilesItem;

    /**
     * Get tool buttons
     * @return a Map with String-JButton key-value pairs
     */
    public Map<String, JButton> getToolButtons(){
        return btnTools;
    }

    /**
     * Get color palette buttons
     * @return a Map with String-JButton key-value pairs
     */
    public Map<Color, JButton> getPaletteButtons(){
        return btnPalette;
    }

    /**
     * Get pen color picker button
     * @return pen color picker JButton
     */
    public JButton getPickPenButton(){
        return btnPickPen;
    }

    /**
     * Get fill color picker button
     * @return fill color picker JButton
     */
    public JButton getPickFillButton(){
        return btnPickFill;
    }

    /**
     * Get tabs
     * @return the tabs JTabbedPane
     */
    public JTabbedPane getTabs(){
        return tabs;
    }

    /**
     * Get transparency indicator button
     * @return the "Opaque" JButton
     */
    public JButton getTransparencyButton(){
        return btnTrans;
    }

    /**
     * Get new file menu item
     * @return newFileItem JMenuItem
     */
    public JMenuItem getNewFileItem(){
        return newFileItem;
    }

    /**
     * Get open file menu item
     * @return openFileItem JMenuItem
     */
    public JMenuItem getOpenFileItem(){
        return openFileItem;
    }

    /**
     * Get save file menu item
     * @return saveFileItem JMenuItem
     */
    public JMenuItem getSaveFileItem(){
        return saveFileItem;
    }

    /**
     * Get close file menu item
     * @return closeFileItem JMenuItem
     */
    public JMenuItem getCloseFileItem(){
        return closeFileItem;
    }

    /**
     * Get close all files menu item
     * @return closeAllFilesItem JMenuItem
     */
    public JMenuItem getCloseAllFilesItem(){
        return closeAllFilesItem;
    }

    /**
     * Creates the main GUI of the program
     */
    public void createMainGUI(){
        // Basic initialization of frame
        setTitle("vecPaint - A graphical VEC file editor");

        setSize(800,740);
        setMinimumSize(new Dimension(800,740));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(Utility.GREY800);

        addMenu();
        addToolBar();
        addColorPalette();
        addTabs();
        addColorPicker();
        addTransparencyToggle();
    }

    /**
     * Adds the toolbar main GUI.
     */
    public void addToolBar(){
        toolsPanel.setPreferredSize(new Dimension(124,-1));
        toolsPanel.setBackground(Utility.GREY900);

        // Adding tool buttons
        for(Tool tool : Tool.values()){
            JButton btn = new JButton(tool.getName());
            btn.setPreferredSize(new Dimension(96, 24));
            btn.setBorderPainted(false);
            btn.setBackground(Utility.GREY600);
            btn.setForeground(Color.WHITE);

            toolsPanel.add(btn);
            btnTools.put(tool.getName(), btn);
        }

        add(toolsPanel, BorderLayout.WEST);
    }

    /**
     * Adds color btnPalette to toolbar
     */
    public void addColorPalette(){
        JPanel palettePanel = new JPanel(); // color palette container
        palettePanel.setLayout(new GridLayout(4,4));
        palettePanel.setPreferredSize(new Dimension(96,96));

        // Creating and adding color buttons to palettePanel
        for(Color c : Utility.PALETTECOLORS){
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(24, 24));
            btn.setBorderPainted(false);
            btn.setBackground(c);

            palettePanel.add(btn);
            btnPalette.put(c, btn);
        }

        // Add color panel to toolbar
        toolsPanel.add(palettePanel);
    }

    /**
     * Adds color picker to toolbar
     */
    public void addColorPicker(){
        pickPanel.setLayout(new GridLayout(2,2, 4, 0));
        pickPanel.setPreferredSize(new Dimension(96,48));
        pickPanel.setBackground(Utility.GREY900);

        JLabel lblPen = new JLabel("Pen");
        lblPen.setForeground(Color.WHITE);
        JLabel lblFill = new JLabel("Fill");
        lblFill.setForeground(Color.WHITE);

        pickPanel.add(lblPen);
        pickPanel.add(lblFill);

        btnPickPen = new BetterJButton(false);
        btnPickFill = new BetterJButton(false);

        btnPickPen.setBackground(Color.BLACK);
        btnPickPen.setBorderPainted(false);
        btnPickFill.setBackground(Color.WHITE);
        btnPickFill.setBorderPainted(false);

        pickPanel.add(btnPickPen);
        pickPanel.add(btnPickFill);

        toolsPanel.add(pickPanel);
    }

    /**
     * Adds the content panel to the main frame.
     */
    public void addTabs(){
        // Remove Tabbed pane insets
        UIManager.getInsets("TabbedPane.contentBorderInsets").set(-1,-1,-1,-1);
        add(tabs);
    }

    /**
     * Adds the transparency toggle button
     */
    public void addTransparencyToggle(){
        btnTrans = new JButton("Opaque");
        btnTrans.setPreferredSize(new Dimension(96, 24));
        btnTrans.setBorderPainted(false);
        btnTrans.setBackground(Utility.GREY600);
        btnTrans.setForeground(Color.WHITE);

        toolsPanel.add(btnTrans);
    }

    /**
     * Adds the menu to main frame.
     */
    public void addMenu(){
       JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        newFileItem = new JMenuItem("New");
        openFileItem = new JMenuItem("Open");
        saveFileItem = new JMenuItem("Save");
        closeFileItem = new JMenuItem("Close");
        closeAllFilesItem = new JMenuItem("Close all");

        newFileItem.setMnemonic(KeyEvent.VK_N);
        KeyStroke ks = KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        newFileItem.setAccelerator(ks);

        openFileItem.setMnemonic(KeyEvent.VK_O);
        ks = KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        openFileItem.setAccelerator(ks);

        saveFileItem.setMnemonic(KeyEvent.VK_S);
        ks = KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        saveFileItem.setAccelerator(ks);

        closeFileItem.setMnemonic(KeyEvent.VK_C);
        ks = KeyStroke.getKeyStroke('W', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        closeFileItem.setAccelerator(ks);


        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(closeFileItem);
        fileMenu.add(closeAllFilesItem);
        menuBar.add(fileMenu);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        ks = KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        undoItem.setAccelerator(ks);
        editMenu.add(undoItem);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    @Override
    public void update(Subject s){
        VecModel model = (VecModel)(s);

        // Update pen/fill color preview
        btnPickPen.setBackground(model.getPenColor());
        btnPickFill.setBackground(model.getFillColor());

        // Update tool buttons
        for(String key : btnTools.keySet()){
            if(key == model.getCurrentTool().getName()){
                btnTools.get(key).setBackground(Utility.GREY800);
            }else{
                btnTools.get(key).setBackground(Utility.GREY600);
            }
        }

        // Update panels
        int selected = tabs.getSelectedIndex();

        tabs.removeAll();
        for(VecFile file : model.getOpenedFiles()){
            Canvas canvas = new Canvas(file, model);
            CanvasContainer canvasPanel =  new CanvasContainer(canvas);
            canvasPanel.add(canvas);
            tabs.add(file.getFileName(), canvasPanel);
        }

        int tabCount = tabs.getTabCount();
        if(tabCount > 0 && selected < tabCount && selected >= 0) tabs.setSelectedIndex(selected);
    }
}
