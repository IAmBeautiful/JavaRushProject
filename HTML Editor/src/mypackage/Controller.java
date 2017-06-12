package mypackage;

import mypackage.listeners.UndoListener;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

/**
 * Created by DNS on 29.03.2017.
 */
public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view){
        this.view = view;
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }

    public void init(){
        createNewDocument();
    }

    public void exit(){
        System.exit(0);
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void resetDocument(){

        UndoListener listener = view.getUndoListener();

        if (document != null){
            document.removeUndoableEditListener(listener);
        }

        HTMLEditorKit kit = new HTMLEditorKit();
        document = (HTMLDocument)kit.createDefaultDocument();

        document.addUndoableEditListener(listener);

        view.update();
    }

    public void setPlainText(String text){
        resetDocument();
       StringReader reader = new StringReader(text);
        try {
            HTMLEditorKit kit = new HTMLEditorKit();
            kit.read(reader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText(){
        StringWriter writer = new StringWriter();
        try {
            HTMLEditorKit kit = new HTMLEditorKit();
            kit.write(writer, document, 0, document.getLength());
            return writer.toString();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return writer.toString();
    }

    public void createNewDocument(){
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML Editor");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument(){
        try {
            view.selectHtmlTab();
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new HTMLFileFilter());
            if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                resetDocument();
                view.setTitle(currentFile.getName());
                try (FileReader fr = new FileReader(currentFile)) {
                    new HTMLEditorKit().read(fr, document, 0);
                    view.resetUndo();
                }
            }
        }
        catch (Exception e){
            ExceptionHandler.log(e);
        }
    }

    public void saveDocument(){
        try {
            if (currentFile == null) {
                saveDocumentAs();
            } else {
                view.selectHtmlTab();
                try (FileWriter fw = new FileWriter(currentFile)) {
                    new HTMLEditorKit().write(fw, document, 0, document.getLength());
                }
            }
        }
        catch (Exception e){
            ExceptionHandler.log(e);
        }

    }

    public void saveDocumentAs(){
        try {
            view.selectHtmlTab();
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new HTMLFileFilter());

            chooser.setDialogTitle("Save File");
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                view.setTitle(currentFile.getName());
                try(FileWriter fw = new FileWriter(currentFile)){
                    HTMLEditorKit kit = new HTMLEditorKit();
                    kit.write(fw, document, 0, document.getLength());
                }
            }
        }
        catch (Exception e){
            ExceptionHandler.log(e);
        }
    }
}
