package observerpattern;

/**
 * Observer class for Observer-Subject pattern.
 */
public interface Observer {
    /**
     * Executed when notified.
     */
    void update(Subject s);
}
