package vecpaint;

import javax.swing.*;

public class VecPaint {
    public static void main(String[] args){
        VecModel model = new VecModel();

        VecGUI gui = new VecGUI();
        try{
            // Using invokeAndWait to make sure GUI is created before the controller adds listeners
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    gui.createMainGUI();
                }
            });
        }catch(Exception e){}


        VecController controller = new VecController(model, gui);

        // exit on close and show GUI
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }

}
