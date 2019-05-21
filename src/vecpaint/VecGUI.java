package vecpaint;

import observerpattern.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class VecGUI extends JFrame implements Observer {
    private JPanel toolsPanel = new JPanel();                   // toolbar container panel
    private Map<String, JButton> btnTools = new HashMap<>();    // Tool buttons (plot, line, etc...)
    private Map<Color, JButton> btnPalette = new HashMap<>();   // Colour palette buttons

    private JTabbedPane tabs = new JTabbedPane();   // Opened files tabbed pane

    private JPanel pickPanel = new JPanel();    // Colour picker container panel
    private JButton btnPickPen, btnPickFill;    // Button for picking pen/fill colour
    private JLabel lblPen, lblFill;             // Labels for Pen/Fill

    private JMenuItem newFileItem;
    private JMenuItem openFileItem;
    private JMenuItem saveFileItem;
    private JMenuItem closeFileItem;
    private JMenuItem closeAllFilesItem;

    public Map<String, JButton> getToolButtons(){
        return btnTools;
    }

    public Map<Color, JButton> getPaletteButtons(){
        return btnPalette;
    }

    public JButton getPickPenButton(){
        return btnPickPen;
    }

    public JButton getPickFillButton(){
        return btnPickFill;
    }

    public JTabbedPane getTabs(){
        return tabs;
    }

    public JMenuItem getNewFileItem(){
        return newFileItem;
    }

    public JMenuItem getOpenFileItem(){
        return openFileItem;
    }

    public JMenuItem getSaveFileItem(){
        return saveFileItem;
    }

    public JMenuItem getCloseFileItem(){
        return closeFileItem;
    }

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
            btn.setOpaque(true);
            btn.setBackground(Utility.GREY600);
            btn.setForeground(Color.WHITE);

            toolsPanel.add(btn);
            btnTools.put(tool.getName(), btn);
        }

        add(toolsPanel, BorderLayout.WEST);
    }

    /**
     * Adds colour btnPalette to toolbar
     */
    public void addColorPalette(){
        JPanel palettePanel = new JPanel(); // Colour palette container
        palettePanel.setLayout(new GridLayout(4,4));
        palettePanel.setPreferredSize(new Dimension(96,96));

        // Creating and adding colour buttons to palettePanel
        for(Color c : Utility.PALETTECOLORS){
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(24, 24));
            btn.setBorderPainted(false);
            btn.setBackground(c);

            palettePanel.add(btn);
            btnPalette.put(c, btn);
        }

        // Add colour panel to toolbar
        toolsPanel.add(palettePanel);
    }

    /**
     * Adds colour picker to toolbar
     */
    public void addColorPicker(){
        pickPanel.setLayout(new GridLayout(2,2, 4, 0));
        pickPanel.setPreferredSize(new Dimension(96,48));
        pickPanel.setBackground(Utility.GREY900);

        lblPen = new JLabel("Pen");
        lblPen.setForeground(Color.WHITE);
        lblFill = new JLabel("Fill");
        lblFill.setForeground(Color.WHITE);

        pickPanel.add(lblPen);
        pickPanel.add(lblFill);

        btnPickPen = new JButton();
        btnPickFill = new JButton();

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

        closeFileItem.setMnemonic(KeyEvent.VK_W);
        ks = KeyStroke.getKeyStroke('W', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        closeFileItem.setAccelerator(ks);


        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(closeFileItem);
        fileMenu.add(closeAllFilesItem);

        menuBar.add(fileMenu);
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
        tabs.removeAll();
        for(VecFile file : model.getOpenedFiles()){
            JPanel canvasPanel =  new JPanel();
            canvasPanel.setLayout(new GridBagLayout());
            canvasPanel.setBackground(Utility.GREY700);

            JPanel canvas = new JPanel();
            canvas.setPreferredSize(new Dimension(640, 640));

            canvasPanel.add(canvas);
            tabs.add(file.getFileName(), canvasPanel);
        }
    }
}
