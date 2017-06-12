package mypackage.actions;

import mypackage.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by DNS on 31.03.2017.
 */
public class UndoAction extends AbstractAction {

    private View view;

    public UndoAction(View view){
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        view.undo();
    }
}
