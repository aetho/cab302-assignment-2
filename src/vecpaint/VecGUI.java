package vecpaint;

import observerpattern.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VecGUI extends JFrame implements Observer {

    private JPanel toolsPanel = new JPanel();                       // toolbar container panel
    private Map<String, JButton> btnTools = new HashMap<>();        // Tool buttons (plot, line, etc...)

    private JPanel palettePanel = new JPanel();                     // Colour palette container
    private Map<Color, JButton> btnPalette = new HashMap<>();       // Colour palette buttons

    private JTabbedPane tabs = new JTabbedPane();   // Opened files tabbed pane
    private JPanel canvasPanel = new JPanel();      // canvas container panel
    private JPanel canvas = new JPanel();           // canvas panel

    private JPanel pickPanel = new JPanel();    // Colour picker container panel
    private JButton btnPickPen, btnPickFill;    // Button for picking pen/fill colour
    private JLabel lblPen, lblFill;             // Labels for Pen/Fill


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

    public JPanel getCanvas(){
        return canvas;
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

        addMenu();
        addToolBar();
        addColorPalette();
        addContentPanel();
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
        // [Todo] Attach event handlers to buttons
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
    public void addContentPanel(){
        // Remove Tabbed pane insets
        UIManager.getInsets("TabbedPane.contentBorderInsets").set(-1,-1,-1,-1);

        // set layout to Grid bag to center components inside
        canvasPanel.setLayout(new GridBagLayout());
        canvasPanel.setBackground(Utility.GREY800);

        canvas.setPreferredSize(new Dimension(640, 640));

        canvasPanel.add(canvas);
        tabs.add("Untitled.vec", canvasPanel);
        add(tabs);
    }

    /**
     * Adds the menu to main frame.
     */
    public void addMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem closeFile = new JMenuItem("Close");
        JMenuItem closeAllFiles = new JMenuItem("Close all");

        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(closeFile);
        fileMenu.add(closeAllFiles);

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
    }
}
