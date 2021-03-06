package mypackage.actions;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import java.awt.event.ActionEvent;

/**
 * Created by DNS on 31.03.2017.
 */
public class SuperscriptAction extends StyledEditorKit.StyledTextAction {

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        JEditorPane editor = getEditor(actionEvent);
        if (editor != null){
            MutableAttributeSet mutableAttributeSet = getStyledEditorKit(editor).getInputAttributes();
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            StyleConstants.setSuperscript(simpleAttributeSet, !StyleConstants.isSuperscript(mutableAttributeSet));
            setCharacterAttributes(editor, simpleAttributeSet, false);
        }
    }

    public SuperscriptAction(){
        super(StyleConstants.Superscript.toString());
    }
}
