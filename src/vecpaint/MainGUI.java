package vecpaint;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainGUI extends JFrame implements Runnable {
    /**
     * Creates the main GUI of the program
     */
    public void createMainGUI(){
        // Basic initialization of frame
        setTitle("vecPaint - A graphical VEC file editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,740);
        setMinimumSize(new Dimension(800,740));
        setLayout(new BorderLayout());

        // Set look and feel to operating system's look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            // [Todo] Display dialog with exception message.
        }

        addMenu();
        addToolBar();
        addContentPanel();

        // Show frame
        setVisible(true);
    }

    /**
     * Adds the toolbar main GUI.
     */
    public void addToolBar(){
        // [Todo] Attach event handlers to buttons
        // [Todo] Add colour palette
        // [Todo] Add colour picker
        // [Todo] Add colour preview
        JPanel tPanel = new JPanel(); // toolbar container panel
        tPanel.setPreferredSize(new Dimension(124,-1));
        tPanel.setBackground(Utility.GREY900);

        // Adding tool buttons
        String btnToolsText[] = {"Plot", "Line", "Rectangle", "Ellipse", "Polygon"};
        Map<String, JButton> btnTools = new HashMap<>();
        for(String text : btnToolsText){
            JButton btn = new JButton(text);
            btn.setPreferredSize(new Dimension(96, 24));
            btn.setBackground(Utility.GREY900);

            tPanel.add(btn);
            btnTools.put(text, btn);
        }

        JPanel colorPanel = new JPanel(); // Colour palette container
        colorPanel.setLayout(new GridLayout(4,4));
        colorPanel.setPreferredSize(new Dimension(96,96));

        tPanel.add(colorPanel);
        add(tPanel, BorderLayout.WEST);
    }

    /**
     * Adds the content panel to the main frame.
     */
    public void addContentPanel(){
        JTabbedPane tabs = new JTabbedPane();
        // Remove Tabbed pane insets
        UIManager.getInsets("TabbedPane.contentBorderInsets").set(-1,-1,-1,-1);

        JPanel cPanel = new JPanel(); // canvas container panel
        cPanel.setLayout(new GridBagLayout()); // set layout to Grid bag to centre components inside

        JPanel canvas = new JPanel(); // canvas
        canvas.setPreferredSize(new Dimension(640, 640));

        cPanel.add(canvas);
        cPanel.setBackground(Utility.GREY800);

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

    public static void main(String[] args){
        SwingUtilities.invokeLater(new MainGUI());
    }

    public void run(){
        createMainGUI();
    }
}
