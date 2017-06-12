package mypackage;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by DNS on 31.03.2017.
 */
public class HTMLFileFilter extends FileFilter {

    @Override
    public boolean accept(File file){
        String name = file.getName();
        return file.isDirectory() || name.substring(name.lastIndexOf(".")).equalsIgnoreCase(".htm") || name.substring(name.lastIndexOf(".")).equalsIgnoreCase(".html");
    }

    @Override
    public String getDescription(){
        return "HTML и HTM файлы";
    }

    
}
