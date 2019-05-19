package vecpaint;

import javax.swing.*;
import java.awt.*;


public class VecPaint {
    public static void main(String[] args){
        VecModel model = new VecModel();

        VecGUI gui = new VecGUI();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.createMainGUI();
            }
        });

        // exit on close and show GUI
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }

}
