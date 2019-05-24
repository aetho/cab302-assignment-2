package observerpattern;

/**
 * Observer class for Observer-Subject pattern.
 */
public interface Observer {
    /**
     * Executed when notified.
     * @param s the subject
     */
    void update(Subject s);
}
