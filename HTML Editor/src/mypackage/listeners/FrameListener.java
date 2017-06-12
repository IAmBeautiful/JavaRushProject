package mypackage.listeners;

import mypackage.View;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by DNS on 29.03.2017.
 */
public class FrameListener extends WindowAdapter {
    private View view;

    public FrameListener(View view){
        this.view = view;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent){
        view.exit();
    }
}
