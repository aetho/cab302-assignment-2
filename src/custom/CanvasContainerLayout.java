package custom;

import java.awt.*;

/**
 * Custom layout for canvas containers
 */
public class CanvasContainerLayout implements LayoutManager {

    /**
     * If the layout manager uses a per-component string,
     * adds the component {@code comp} to the layout,
     * associating it
     * with the string specified by {@code name}.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {}

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp) {}

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the container to be laid out
     * @return the preferred dimension for the container
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the component to be laid out
     * @return the minimum dimension for the container
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    public void layoutContainer(Container parent) {
        // Iterate through the components to resize and place each appropriately
        for(int i = 0; i < parent.getComponentCount(); i++){
            Component c = parent.getComponent(i);

            // If component is visible attempt to resize and place component
            if(c.isVisible()){
                int pWidth = c.getParent().getWidth();
                int pHeight = c.getParent().getHeight();

                // Get smaller of the 2 dimensions
                int size = Math.min(pWidth, pHeight) - 64;

                // place component
                int x = pWidth/2 - size/2;
                int y = pHeight/2 - size/2;
                c.setBounds(x, y, size, size);
            }
        }
    }
}
