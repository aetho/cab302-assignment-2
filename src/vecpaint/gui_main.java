package vecpaint;

import javax.swing.*;

public class gui_main extends JFrame {
    gui_main(){
        super("VECpaint - VEC file editor");
        setSize(960,540);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            // [Todo] Display dialog with exception message.
        }
        new gui_main();
    }
}
