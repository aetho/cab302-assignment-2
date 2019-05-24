package observerpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject class for the Observer-Subject pattern
 */
public abstract class Subject {
    /**
     * List of observers
     */
    private List<Observer> observers = new ArrayList<>();

    /**
     * Attaches an observer to this subject by adding it to the observers list
     * @param observer The observer that will be attached.
     */
    public void attach(Observer observer){
        observers.add(observer);
    }

    /**
     * Notifies observers of an update.
     */
    public void notifyObservers(){
        for(Observer observer : observers){
            observer.update(this);
        }
    }
}
