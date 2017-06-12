package mypackage.listeners;

import mypackage.View;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by DNS on 30.03.2017.
 */
public class TabbedPaneChangeListener implements ChangeListener {
    private View view;

    public TabbedPaneChangeListener(View view){
        this.view = view;
    }

    @Override
    public void stateChanged(ChangeEvent e){
        view.selectedTabChanged();
    }
}
