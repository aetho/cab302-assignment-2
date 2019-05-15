package vecpaint;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame implements Runnable {
    /**
     * Creates the main GUI of the program
     */
    public void createMainGUI(){
        // Basic initialization of frame
        setTitle("vecPaint - A graphical VEC file editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960,540);
        setLayout(new BorderLayout());

        // Set look and feel to operating system's look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            // [Todo] Display dialog with exception message.
        }

        // Show frame
        setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new MainGUI());
    }

    public void run(){
        createMainGUI();
    }
}
