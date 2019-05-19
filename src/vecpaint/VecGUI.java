package vecpaint;

import observerpattern.Observer;
import observerpattern.Subject;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VecGUI extends JFrame implements Observer {

    private JPanel tPanel = new JPanel();                       // toolbar container panel
    private Map<String, JButton> btnTools = new HashMap<>();    // Tool buttons (plot, line, etc...)

    private JPanel colorPanel = new JPanel();                   // Colour palette container
    private Map<Color, JButton> btnColors = new HashMap<>();    // Colour palette buttons

    private JTabbedPane tabs = new JTabbedPane();   // Opened files tabbed pane
    private JPanel cPanel = new JPanel();           // canvas container panel
    private JPanel canvas = new JPanel();           // canvas panel

    private JPanel pickPanel = new JPanel();    // Colour picker container panel
    private JButton btnPickPen, btnPickFill;    // Button for picking pen/fill colour
    private JLabel lblPen, lblFill;             // Labels for Pen/Fill


    public Map<String, JButton> getToolButtons(){
        return btnTools;
    }

    public Map<Color, JButton> getPaletteButtons(){
        return btnColors;
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
        // [Todo] Attach event handlers to buttons
        tPanel.setPreferredSize(new Dimension(124,-1));
        tPanel.setBackground(Utility.GREY900);

        // Adding tool buttons
        String btnToolsText[] = {"Plot", "Line", "Rectangle", "Ellipse", "Polygon"};
        for(String text : btnToolsText){
            JButton btn = new JButton(text);
            btn.setPreferredSize(new Dimension(96, 24));
            btn.setBorderPainted(false);
            btn.setBackground(Utility.GREY600);
            btn.setForeground(Color.WHITE);

            tPanel.add(btn);
            btnTools.put(text, btn);
        }

        add(tPanel, BorderLayout.WEST);
    }

    /**
     * Adds colour palette to toolbar
     */
    public void addColorPalette(){
        // [Todo] Attach event handlers to buttons
        colorPanel.setLayout(new GridLayout(4,4));
        colorPanel.setPreferredSize(new Dimension(96,96));

        Color colors[] = {
                Utility.RED,
                Utility.PINK,
                Utility.PURPLE,
                Utility.DEEPPURPLE,
                Utility.INDIGO,
                Utility.BLUE,
                Utility.LIGHTBLUE,
                Utility.CYAN,
                Utility.TEAL,
                Utility.GREEN,
                Utility.LIGHTGREEN,
                Utility.LIME,
                Utility.YELLOW,
                Utility.AMBER,
                Utility.ORANGE,
                Utility.DEEPORANGE
        };

        // Creating and adding colour buttons to colorPanel
        for(Color c : colors){
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(24, 24));
            btn.setBorderPainted(false);
            btn.setBackground(c);

            colorPanel.add(btn);
            btnColors.put(c, btn);
        }

        // Add colour panel to toolbar
        tPanel.add(colorPanel);
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

        tPanel.add(pickPanel);
    }

    /**
     * Adds the content panel to the main frame.
     */
    public void addContentPanel(){
        // Remove Tabbed pane insets
        UIManager.getInsets("TabbedPane.contentBorderInsets").set(-1,-1,-1,-1);

        // set layout to Grid bag to center components inside
        cPanel.setLayout(new GridBagLayout());
        cPanel.setBackground(Utility.GREY800);

        canvas.setPreferredSize(new Dimension(640, 640));

        cPanel.add(canvas);
        tabs.add("Untitled.vec",cPanel);
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

    }
}
