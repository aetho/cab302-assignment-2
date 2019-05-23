package custom;

import java.awt.*;

public class CanvasContainerLayout implements LayoutManager {
    /* required by LayoutManager */
    public void addLayoutComponent(String name, Component comp) {
    }

    /* required by LayoutManager */
    public void removeLayoutComponent(Component comp) {

    }

    /* required by LayoutManager */
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    /* required by LayoutManager */
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    /* required by LayoutManager */
    public void layoutContainer(Container parent) {
        for(int i = 0; i < parent.getComponentCount(); i++){
            Component c = parent.getComponent(i);
            if(c.isVisible()){
                int pWidth = c.getParent().getWidth();
                int pHeight = c.getParent().getHeight();
                int size = Math.min(pWidth, pHeight) - 64;
                int x = pWidth/2 - size/2;
                int y = pHeight/2 - size/2;

                c.setBounds(x, y, size, size);
            }
        }
    }
}
